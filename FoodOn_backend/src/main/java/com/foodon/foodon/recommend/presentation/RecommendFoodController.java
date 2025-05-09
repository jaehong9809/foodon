package com.foodon.foodon.recommend.presentation;

import com.foodon.foodon.auth.annotation.AuthMember;
import com.foodon.foodon.common.dto.Response;
import com.foodon.foodon.common.util.ResponseUtil;
import com.foodon.foodon.member.domain.Member;
import com.foodon.foodon.recommend.application.RecommendFoodService;
import com.foodon.foodon.recommend.dto.RecommendFoodResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recommend-foods")
public class RecommendFoodController {

    private final RecommendFoodService recommendFoodService;

    @GetMapping("/{date}")
    public ResponseEntity<Response<List<RecommendFoodResponse>>> getRecommendFoodByWeek(
            @PathVariable(name = "date") LocalDate date,
            @AuthMember Member member
    ){
        List<RecommendFoodResponse> result = recommendFoodService.getRecommendFoodsByWeek(date, member);
        return ResponseUtil.success(result);
    }
}

