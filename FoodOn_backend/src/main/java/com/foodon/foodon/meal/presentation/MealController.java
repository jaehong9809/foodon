package com.foodon.foodon.meal.presentation;

import com.foodon.foodon.meal.application.MealService;
import com.foodon.foodon.meal.dto.MealInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meals")
public class MealController {

    private final MealService mealService;

    @PostMapping(
            value = "/detect",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<MealInfoResponse> uploadAndDetectMeal(
            @RequestPart("image") MultipartFile multipartFile
    ) {

        MealInfoResponse result = mealService.uploadAndDetect(multipartFile);
        return ResponseEntity.ok(result);
    }
}
