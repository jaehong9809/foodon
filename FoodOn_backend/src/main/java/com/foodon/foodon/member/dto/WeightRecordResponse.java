package com.foodon.foodon.member.dto;

import com.foodon.foodon.member.domain.MemberStatus;

import java.time.LocalDate;

public record WeightRecordResponse(
	Long weightRecordId,
	LocalDate date,
	int weight
) {
	public static WeightRecordResponse of(MemberStatus memberStatus) {
		return new WeightRecordResponse(
			memberStatus.getId(),
			memberStatus.getCreatedAt().toLocalDate(),
			memberStatus.getWeight()
		);
	}
}
