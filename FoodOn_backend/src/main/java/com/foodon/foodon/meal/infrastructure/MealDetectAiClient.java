package com.foodon.foodon.meal.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class MealDetectAiClient {

    private final WebClient webClient;

    public MealDetectAiResponse detect(String imageUrl) {
        return webClient.post()
                .bodyValue(new MealDetectAiRequest(imageUrl))
                .retrieve()
                .bodyToMono(MealDetectAiResponse.class)
                .block();
    }

}
