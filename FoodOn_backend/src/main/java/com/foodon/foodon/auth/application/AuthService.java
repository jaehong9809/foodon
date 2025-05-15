package com.foodon.foodon.auth.application;

import com.foodon.foodon.auth.dto.MemberTokens;
import com.foodon.foodon.auth.util.JwtUtil;
import com.foodon.foodon.member.exception.MemberErrorCode;
import com.foodon.foodon.member.repository.MemberRepository;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import static com.foodon.foodon.member.exception.MemberException.MemberNotFoundException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    public MemberTokens createSuperToken(String userId) {
        validateMemberExists(userId);
        return jwtUtil.createSuperToken(userId);
    }

    public MemberTokens validateAndReissueTokens(MemberTokens memberTokens) {
        jwtUtil.validateRefreshToken(memberTokens.refreshToken());

        String subject;
        try {
            subject = jwtUtil.getSubject(memberTokens.accessToken());
        } catch (ExpiredJwtException e) {
            subject = e.getClaims().getSubject();
        }

        validateMemberExists(subject);
        return jwtUtil.createMemberToken(subject);
    }

    private void validateMemberExists(String userId) {
        if (!memberRepository.existsById(Long.valueOf(userId))) {
            throw new MemberNotFoundException(MemberErrorCode.MEMBER_NOT_FOUND);
        }
    }
}
