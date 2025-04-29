package com.foodon.foodon.auth.infrastructure;

import com.foodon.foodon.auth.dto.response.KakaoUserInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@RequiredArgsConstructor
@Component
public class KakaoApiClient {

    private final WebClient kakaoApiWebClient;

    public KakaoUserInfoResponse getUserInfo(String kakaoAccessToken) {
        return kakaoApiWebClient.get()
                .uri("/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + kakaoAccessToken)
                .retrieve()
                .bodyToMono(KakaoUserInfoResponse.class)
                .block();
    }
}

