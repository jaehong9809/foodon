package com.foodon.foodon.member.application;

import java.time.LocalDateTime;
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

		List<MemberStatus> memberStatuses = memberStatusRepository.findByMemberIdAndCreatedAtBetween(member.getId(), start, end);
		return convertToResponse(memberStatuses);
	}

	private List<WeightRecordResponse> convertToResponse(List<MemberStatus> memberStatuses) {
		return memberStatuses.stream()
			.map(WeightRecordResponse::of)
			.collect(Collectors.toList());
	}

	public WeightProfileResponse getWeightProfile(Member member) {
		MemberStatus memberStatus = getCurrentMemberStatus(member.getId());

		return new WeightProfileResponse(
			memberStatus.getGoalWeight(),
			memberStatus.getWeight()
		);
	}

	@Transactional
	public void updateCurrentWeight(Member member, WeightUpdateRequest weightUpdateRequest) {
		int weight = weightUpdateRequest.weight();
		MemberStatus memberStatus = MemberStatus.copyFrom(getCurrentMemberStatus(member.getId()));
		memberStatus.changeWeight(weight);
		memberStatusRepository.save(memberStatus);
	}

	private MemberStatus getCurrentMemberStatus(Long memberId){
		return memberStatusRepository.findTopByMemberIdOrderByIdDesc(memberId)
				.orElseThrow(() -> new NoSuchElementException("해당 ID의 회원 상태가 존재하지 않습니다. memberId = " + memberId));
	}

}
