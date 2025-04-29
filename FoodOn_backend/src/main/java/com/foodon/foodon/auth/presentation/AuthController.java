package com.foodon.foodon.auth.presentation;

import com.foodon.foodon.auth.application.KakaoAuthService;
import com.foodon.foodon.auth.dto.request.KakaoLoginRequest;
import com.foodon.foodon.auth.dto.response.KakaoLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final KakaoAuthService kakaoAuthService;

    @PostMapping("/kakao")
    public ResponseEntity<KakaoLoginResponse> loginByKakao(
            @RequestBody KakaoLoginRequest request
    ) {
        KakaoLoginResponse response = kakaoAuthService.loginByKakao(request.accessToken());
        return ResponseEntity.ok(response);
    }
}
