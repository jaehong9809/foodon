package com.foodon.foodon.member.presentation;

import java.time.YearMonth;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodon.foodon.auth.annotation.AuthMember;
import com.foodon.foodon.common.dto.Response;
import com.foodon.foodon.common.util.ResponseUtil;
import com.foodon.foodon.member.application.MemberService;
import com.foodon.foodon.member.domain.Member;
import com.foodon.foodon.member.dto.WeightProfileResponse;
import com.foodon.foodon.member.dto.WeightRecordResponse;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

	private final MemberService memberService;

	@GetMapping("/weights/calendar/{yearMonth}")
	@Operation(summary = "특정 달 기록 체중 목록 조회")
	public ResponseEntity<Response<List<WeightRecordResponse>>> getWeightCalendar(
		@PathVariable YearMonth yearMonth,
		@AuthMember Member member
	) {
		List<WeightRecordResponse> result = memberService.getWeightRecordCalendar(yearMonth, member);
		return ResponseUtil.success(result);
	}

	@GetMapping("/profile/weight")
	@Operation(summary = "목표 체중 현재 체중 조회")
	public ResponseEntity<Response<WeightProfileResponse>> getWeightProfile(
		@AuthMember Member member
	) {
		WeightProfileResponse result = memberService.getWeightProfile(member);
		return ResponseUtil.success(result);
	}

}
