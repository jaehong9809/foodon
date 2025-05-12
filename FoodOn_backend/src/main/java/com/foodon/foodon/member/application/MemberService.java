package com.foodon.foodon.member.application;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import com.foodon.foodon.member.domain.ActivityLevel;
import com.foodon.foodon.member.domain.MemberStatus;
import com.foodon.foodon.member.domain.NutrientPlan;
import com.foodon.foodon.member.dto.*;
import com.foodon.foodon.member.repository.ActivityLevelRepository;
import com.foodon.foodon.member.repository.NutrientPlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foodon.foodon.member.domain.Member;
import com.foodon.foodon.member.exception.MemberErrorCode;
import com.foodon.foodon.member.exception.MemberException;
import com.foodon.foodon.member.repository.MemberRepository;
import com.foodon.foodon.member.repository.MemberStatusRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final ActivityLevelRepository activityLevelRepository;
	private final NutrientPlanRepository nutrientPlanRepository;
	private final MemberStatusRepository memberStatusRepository;

	@Transactional
	public void registerProfile(
			ProfileRegisterRequest request,
			Member member
	) {
		memberStatusRepository.save(
			MemberStatus.createMemberStatus(
				member.getId(),
				request.height(),
				request.weight(),
				request.goalWeight(),
				request.managementType(),
				request.activityType()
			)
		);

		member.updateProfile(request.gender());
		member.markProfileUpdated();
	}

	public List<WeightRecordResponse> getWeightRecordCalendar(
		YearMonth yearMonth,
		Member member
	) {
		LocalDate start = yearMonth.atDay(1);
		LocalDate end = yearMonth.atEndOfMonth();

		List<MemberStatus> records = memberStatusRepository.findByMemberIdAndCreatedAtBetweenOrderByCreatedAt(
			member.getId(),
			start,
			end
		);

		return convertToResponse(records);
	}

	private List<WeightRecordResponse> convertToResponse(List<MemberStatus> records) {
		return records.stream()
			.map(WeightRecordResponse::of)
			.collect(Collectors.toList());
	}

	public WeightProfileResponse getWeightProfile(Member member) {
		MemberStatus latestStatus = getLatestStatusOrThrow(member);

		return WeightProfileResponse.of(latestStatus);
	}

	private MemberStatus getLatestStatusOrThrow(Member member) {
		return memberStatusRepository.findTopByMemberIdOrderByCreatedAtDesc(member.getId())
			.orElseThrow(
				() -> new MemberException.MemberBadRequestException(MemberErrorCode.MEMBER_STATUS_NOT_FOUND)
			);
	}

	@Transactional
	public void updateCurrentWeight(
		Member member,
		WeightUpdateRequest weightUpdateRequest
	) {
		int currentWeight = weightUpdateRequest.weight();
		LocalDate today = LocalDate.now();

		MemberStatus latestStatus = getLatestStatusOrThrow(member);

		if (latestStatus.getCreatedAt().isEqual(today)) {
			latestStatus.updateWeight(currentWeight);
		}else {
			MemberStatus newStatus = MemberStatus.createFromPrevious(
				latestStatus,
				currentWeight,
				today
			);
			memberStatusRepository.save(newStatus);
		}
	}

	public List<ActivityLevelResponse> getActivityLevels() {
		List<ActivityLevel> activityLevels = activityLevelRepository.findAll();
		return activityLevels.stream()
				.map(ActivityLevelResponse::of)
				.collect(Collectors.toList());
	}

	public List<NutrientPlanResponse> getNutrientPlans() {
		List<NutrientPlan> nutrientPlans = nutrientPlanRepository.findAll();
		return nutrientPlans.stream()
				.map(NutrientPlanResponse::of)
				.collect(Collectors.toList());
	}

}
