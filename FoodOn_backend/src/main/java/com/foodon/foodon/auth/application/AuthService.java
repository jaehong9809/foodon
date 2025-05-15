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
        validateRefreshToken(memberTokens.refreshToken());
        String subject = extractSubjectAllowingExpiration(memberTokens.accessToken());
        validateMemberExists(subject);
        return jwtUtil.createMemberToken(subject);
    }

    private void validateRefreshToken(String refreshToken) {
        jwtUtil.validateRefreshToken(refreshToken);
    }

    private String extractSubjectAllowingExpiration(String accessToken) {
        try {
            return jwtUtil.getSubject(accessToken);
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        }
    }

    private void validateMemberExists(String userId) {
        if (!memberRepository.existsById(Long.valueOf(userId))) {
            throw new MemberNotFoundException(MemberErrorCode.MEMBER_NOT_FOUND);
        }
    }
}
