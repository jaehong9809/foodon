package com.foodon.foodon.meal.presentation;

import com.foodon.foodon.auth.annotation.AuthMember;
import com.foodon.foodon.common.dto.Response;
import com.foodon.foodon.common.util.ResponseUtil;
import com.foodon.foodon.meal.application.MealService;
import com.foodon.foodon.meal.dto.MealCreateRequest;
import com.foodon.foodon.meal.dto.MealInfoResponse;
import com.foodon.foodon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<Response<MealInfoResponse>> uploadAndDetectMeal(
            @RequestPart("image") MultipartFile multipartFile,
            @AuthMember Member member
    ) {

        MealInfoResponse result = mealService.uploadAndDetect(multipartFile);
        return ResponseUtil.success(result);
    }

    @PostMapping
    public ResponseEntity<Response<Void>> saveMeal(
            @RequestBody MealCreateRequest request,
            @AuthMember Member member
    ) {

        mealService.saveMeal(request, member);
        return ResponseUtil.success();
    }

}
