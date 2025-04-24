package com.foodon.foodon.auth.dto.request;

public record KakaoLoginRequest(
        Long kakaoId,
        String email,
        String nickname,
        String profileImgUrl
) {
}