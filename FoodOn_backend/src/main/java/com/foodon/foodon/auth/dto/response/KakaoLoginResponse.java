package com.foodon.foodon.auth.dto.response;

import com.foodon.foodon.member.dto.MemberDto;

public record KakaoLoginResponse(
        String refreshToken,
        String accessToken,
        MemberDto member
) {
    public static KakaoLoginResponse of(
            String refreshToken,
            String accessToken,
            MemberDto memberDto
    ) {
        return new KakaoLoginResponse(
                refreshToken,
                accessToken,
                memberDto
        );
    }
}
