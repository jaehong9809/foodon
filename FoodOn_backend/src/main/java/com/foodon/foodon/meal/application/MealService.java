package com.foodon.foodon.meal.application;

import com.foodon.foodon.image.application.S3ImageService;
import com.foodon.foodon.meal.repository.MealItemRepository;
import com.foodon.foodon.meal.repository.MealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MealService {

    private final MealRepository mealRepository;
    private final MealItemRepository mealItemRepository;
    private final S3ImageService s3ImageService;

    public void detect(MultipartFile multipartFile) {
        String imageUrl = s3ImageService.upload(multipartFile);
        detectMealImage(imageUrl);

    }

    private void detectMealImage(String imageUrl) {

    }
}
