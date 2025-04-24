package com.foodon.foodon.auth.application;

import com.foodon.foodon.auth.domain.OauthAccount;
import com.foodon.foodon.auth.domain.OauthProvider;
import com.foodon.foodon.auth.dto.MemberTokens;
import com.foodon.foodon.auth.domain.RefreshToken;
import com.foodon.foodon.auth.dto.request.KakaoLoginRequest;
import com.foodon.foodon.auth.dto.response.KakaoLoginResponse;
import com.foodon.foodon.auth.exception.AuthErrorCode;
import com.foodon.foodon.auth.repository.OauthAccountRepository;
import com.foodon.foodon.auth.repository.RefreshTokenRepository;
import com.foodon.foodon.auth.util.JwtUtil;
import com.foodon.foodon.member.domain.Member;
import com.foodon.foodon.member.dto.MemberDto;
import com.foodon.foodon.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.foodon.foodon.auth.exception.AuthException.AuthUnauthorizedException;


@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    private final MemberRepository memberRepository;
    private final OauthAccountRepository oauthAccountRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public KakaoLoginResponse loginByKakao(KakaoLoginRequest request) {
        Member member = findOrCreateMember(request);
        MemberTokens tokens = issueTokensFor(member);
        return buildLoginResponse(member, tokens);
    }

    private Member findOrCreateMember(KakaoLoginRequest request) {
        return oauthAccountRepository
                .findByProviderIdAndProvider(request.kakaoId().toString(), OauthProvider.KAKAO)
                .map(account -> memberRepository.findById(account.getMemberId())
                        .orElseThrow(() -> {
                            log.warn(
                                    "Oauth 계정이 존재하지만 Member 정보 없음. accountId={}, memberId={}",
                                    account.getId(), account.getMemberId()
                            );
                            return new AuthUnauthorizedException(AuthErrorCode.CORRUPTED_OAUTH_LINK);
                        })
                )
                .orElseGet(() -> createMemberAndLinkOauth(request));
    }

    private Member createMemberAndLinkOauth(KakaoLoginRequest request) {
        Member newMember = memberRepository.save(
                Member.createMember(
                    request.nickname(),
                    request.email(),
                    request.profileImgUrl()
                )
        );

        OauthAccount oauthAccount = OauthAccount.of(
                request.kakaoId().toString(),
                OauthProvider.KAKAO,
                newMember.getId()
        );
        oauthAccountRepository.save(oauthAccount);
        return newMember;
    }

    private MemberTokens issueTokensFor(Member member) {
        MemberTokens tokens = jwtUtil.createMemberToken(String.valueOf(member.getId()));
        refreshTokenRepository.save(new RefreshToken(member.getId(), tokens.refreshToken()));
        return tokens;
    }

    private KakaoLoginResponse buildLoginResponse(Member member, MemberTokens tokens) {
        return KakaoLoginResponse.of(
                tokens.refreshToken(),
                tokens.accessToken(),
                MemberDto.from(member)
        );
    }
}
