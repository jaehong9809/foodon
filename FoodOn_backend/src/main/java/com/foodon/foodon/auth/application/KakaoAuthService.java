package com.foodon.foodon.auth.application;

import com.foodon.foodon.auth.client.KakaoOauthSyncClient;
import com.foodon.foodon.auth.dto.KakaoTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    private final KakaoOauthSyncClient kakaoOauthSyncClient;

    public KakaoTokenResponse login(String code, String redirectUri) {
        return kakaoOauthSyncClient.requestToken(code, redirectUri);
    }
}
