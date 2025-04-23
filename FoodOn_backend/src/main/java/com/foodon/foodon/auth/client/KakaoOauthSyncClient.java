package com.foodon.foodon.auth.client;

import com.foodon.foodon.auth.dto.KakaoTokenResponse;
import com.foodon.foodon.auth.dto.KakaoUserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;


@Component
@RequiredArgsConstructor
public class KakaoOauthSyncClient {

    private final WebClient webClient;

    @Value("${oauth.kakao.client-id}")
    private String clientId;

    @Value("${oauth.kakao.client-secret:}") // optional
    private String clientSecret;

    public KakaoTokenResponse requestToken(String code, String redirectUri) {
        return webClient.post()
                .uri("https://kauth.kakao.com/oauth/token")
                .bodyValue(buildFormData(code, redirectUri))
                .retrieve()
                .bodyToMono(KakaoTokenResponse.class)
                .block();
    }

    public KakaoUserInfoResponse requestUserInfo(String accessToken) {
        return webClient.get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(KakaoUserInfoResponse.class)
                .block();
    }

    private MultiValueMap<String, String> buildFormData(String code, String redirectUri) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", clientId);
        formData.add("redirect_uri", redirectUri);
        formData.add("code", code);
        if (clientSecret != null && !clientSecret.isBlank()) {
            formData.add("client_secret", clientSecret);
        }
        return formData;
    }
}