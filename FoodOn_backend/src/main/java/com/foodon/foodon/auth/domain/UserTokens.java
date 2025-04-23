package com.foodon.foodon.auth.domain;

public record UserTokens(
        String refreshToken,
        String accessToken
) {

}