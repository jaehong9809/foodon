package com.foodon.foodon.member.presentation;

import java.time.YearMonth;
import java.util.List;

import com.foodon.foodon.member.dto.ProfileRegisterRequest;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import com.foodon.foodon.auth.annotation.AuthMember;
import com.foodon.foodon.common.dto.Response;
import com.foodon.foodon.common.util.ResponseUtil;
import com.foodon.foodon.member.application.MemberService;
import com.foodon.foodon.member.domain.Member;
import com.foodon.foodon.member.dto.WeightProfileResponse;
import com.foodon.foodon.member.dto.WeightRecordResponse;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

	@PostMapping("/profile")
	@Operation(summary = "가입한 사용자 정보 등록 (키, 체중 등)")
	public ResponseEntity<Response<Void>> registerProfile(
			@Valid @RequestBody ProfileRegisterRequest request,
			@Parameter(hidden = true) @AuthMember Member member
	){
		memberService.registerProfile(request, member);
		return ResponseUtil.success();
	}

}
