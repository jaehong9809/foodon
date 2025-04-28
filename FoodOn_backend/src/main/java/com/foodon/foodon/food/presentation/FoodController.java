package com.foodon.foodon.food.presentation;

import com.foodon.foodon.auth.annotation.AuthMember;
import com.foodon.foodon.common.dto.Response;
import com.foodon.foodon.common.util.ResponseUtil;
import com.foodon.foodon.food.application.FoodService;
import com.foodon.foodon.food.dto.CustomFoodCreateRequest;
import com.foodon.foodon.food.dto.FoodInfoResponse;
import com.foodon.foodon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/foods")
public class FoodController {

    private final FoodService foodService;

    @PostMapping("/custom")
    public ResponseEntity<Response<Void>> saveCustomFood(
            @RequestBody CustomFoodCreateRequest request,
            @AuthMember Member member
    ){
        foodService.saveCustomFood(request, member);
        return ResponseUtil.created();
    }

    @GetMapping("/{foodId}")
    public ResponseEntity<Response<FoodInfoResponse>> getFood(
            @PathVariable(name = "foodId") Long foodId,
            @RequestParam(name = "type") String type,
            @AuthMember Member member
    ){

        return null;


    }
}
