package com.foodon.foodon.auth.dto;

public record MemberTokens(
        String refreshToken,
        String accessToken
) {

}