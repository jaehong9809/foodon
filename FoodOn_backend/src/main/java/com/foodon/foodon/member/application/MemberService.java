package com.foodon.foodon.member.application;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import com.foodon.foodon.member.domain.MemberStatus;
import com.foodon.foodon.member.dto.ProfileRegisterRequest;
import com.foodon.foodon.member.repository.MemberStatusRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foodon.foodon.member.domain.Member;
import com.foodon.foodon.member.dto.WeightProfileResponse;
import com.foodon.foodon.member.dto.WeightRecordResponse;
import com.foodon.foodon.member.dto.WeightUpdateRequest;
import com.foodon.foodon.member.exception.MemberErrorCode;
import com.foodon.foodon.member.exception.MemberException;
import com.foodon.foodon.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
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


	private MemberStatus getLatestStatusOrThrow(Member member) {
		return memberStatusRepository.findTopByMemberIdOrderByCreatedAtDesc(member.getId())
				.orElseThrow(
						() -> new MemberException.MemberBadRequestException(MemberErrorCode.MEMBER_STATUS_NOT_FOUND)
				);
	}

}
