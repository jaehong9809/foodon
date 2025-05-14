package com.foodon.foodon.recommend.application.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.foodon.foodon.member.domain.Member;
import com.foodon.foodon.recommend.application.RecommendFoodService;
import com.foodon.foodon.recommend.dto.RecommendedFood;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RecommendFoodScheduler {

	private final RecommendFoodService recommendFoodService;

	@Scheduled(cron = "0 0 0 * * Mon")
	public void generateWeeklyRecommendationsForAllUsers() {
		List<Member> activeMembers = recommendFoodService.getRecentlyActiveMembers();
		List<RecommendedFood> recommendedFoods = recommendFoodService.loadAllRecommendedFoods();
		LocalDateTime today = LocalDateTime.now();

		for (Member member : activeMembers) {
			recommendFoodService.generateWeeklyRecommendations(
				member,
				recommendedFoods,
				today
			);
		}
	}

}
