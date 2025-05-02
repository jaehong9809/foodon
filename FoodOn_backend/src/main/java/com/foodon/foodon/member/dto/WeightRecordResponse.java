package com.foodon.foodon.member.dto;

import java.time.LocalDate;

import com.foodon.foodon.member.domain.WeightRecord;

public record WeightRecordResponse(
	Long weightRecordId,
	LocalDate date,
	int weight
) {
	public static WeightRecordResponse of(WeightRecord weightRecord) {
		return new WeightRecordResponse(
			weightRecord.getId(),
			weightRecord.getRecordedAt().toLocalDate(),
			weightRecord.getWeight()
		);
	}
}
