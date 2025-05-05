package com.foodon.foodon.food.presentation;

import com.foodon.foodon.auth.annotation.AuthMember;
import com.foodon.foodon.common.dto.Response;
import com.foodon.foodon.common.util.ResponseUtil;
import com.foodon.foodon.food.application.FoodService;
import com.foodon.foodon.food.dto.CustomFoodCreateRequest;
import com.foodon.foodon.food.dto.FoodWithNutrientInfo;
import com.foodon.foodon.food.domain.FoodType;
import com.foodon.foodon.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/foods")
public class FoodController {

    private final FoodService foodService;

    @PostMapping("/custom")
    @Operation(summary = "음식 등록하기")
    public ResponseEntity<Response<Void>> saveCustomFood(
            @RequestBody CustomFoodCreateRequest request,
            @Parameter(hidden = true) @AuthMember Member member
    ){
        foodService.saveCustomFood(request, member);
        return ResponseUtil.created();
    }

    @GetMapping("/{foodId}")
    @Operation(summary = "선택한 음식 정보 조회하기")
    public ResponseEntity<Response<FoodWithNutrientInfo>> getFood(
            @PathVariable(name = "foodId") Long foodId,
            @RequestParam(name = "type", required = false, defaultValue = "PUBLIC") FoodType type,
            @Parameter(hidden = true) @AuthMember Member member
    ){
        FoodWithNutrientInfo result = foodService.getFood(foodId, type, member);
        return ResponseUtil.success(result);
    }
}
