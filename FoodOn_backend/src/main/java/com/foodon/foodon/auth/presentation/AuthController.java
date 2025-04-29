package com.foodon.foodon.auth.presentation;

import com.foodon.foodon.auth.application.KakaoAuthService;
import com.foodon.foodon.auth.dto.request.KakaoLoginRequest;
import com.foodon.foodon.auth.dto.response.KakaoLoginResponse;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "카카오 로그인 후 서버 자체 인증 토큰 발급")
    public ResponseEntity<KakaoLoginResponse> loginByKakao(
            @RequestBody KakaoLoginRequest request
    ) {
        KakaoLoginResponse response = kakaoAuthService.loginByKakao(request.accessToken());
        return ResponseEntity.ok(response);
    }
}
