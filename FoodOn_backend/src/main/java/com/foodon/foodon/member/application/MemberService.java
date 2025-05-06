package com.foodon.foodon.member.application;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import com.foodon.foodon.member.dto.ProfileRegisterRequest;
import org.springframework.stereotype.Service;

import com.foodon.foodon.member.domain.Member;
import com.foodon.foodon.member.domain.WeightRecord;
import com.foodon.foodon.member.dto.WeightProfileResponse;
import com.foodon.foodon.member.dto.WeightRecordResponse;
import com.foodon.foodon.member.repository.MemberRepository;
import com.foodon.foodon.member.repository.WeightRecordRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final WeightRecordRepository weightRecordRepository;

	@Transactional
	public void registerProfile(
			ProfileRegisterRequest request,
			Member member
	) {
		member.updateProfile(
				request.gender(),
				request.height(),
				request.weight(),
				request.managementType(),
				request.activityType()
		);
		member.markProfileUpdated();
	}

	public List<WeightRecordResponse> getWeightRecordCalendar(
		YearMonth yearMonth,
		Member member
	) {
		LocalDateTime start = yearMonth.atDay(1).atStartOfDay();
		LocalDateTime end = yearMonth.atEndOfMonth().atTime(23, 59, 59);

		List<WeightRecord> records = weightRecordRepository.findByMemberAndRecordedAtBetween(member, start, end);
		return convertToResponse(records);
	}

	private List<WeightRecordResponse> convertToResponse(List<WeightRecord> records) {
		return records.stream()
			.map(WeightRecordResponse::of)
			.collect(Collectors.toList());
	}

	public WeightProfileResponse getWeightProfile(Member member) {
		return new WeightProfileResponse(
			member.getGoalWeight(),
			getCurrentWeightOrDefault(member)
		);
	}

	private int getCurrentWeightOrDefault(Member member) {
		return weightRecordRepository.findTopByMemberOrderByIdDesc(member)
			.map(WeightRecord::getWeight)
			.orElse(0);
	}

}
