package com.foodon.foodon.member.domain;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(
	name = "member_status",
	indexes = {
		@Index(name = "idx_member_created_at", columnList = "memberId, createdAt")
	}
)
public class MemberStatus {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_status_id")
	private Long id;

	@Column(nullable = false)
	private Long memberId;

	@Column(nullable = false)
	private int height;

	@Column(nullable = false)
	private int weight;

	@Column(nullable = false)
	private int goalWeight;

	@Column(nullable = false)
	private Long nutrientPlanId;

	@Column(nullable = false)
	private Long activityLevelId;

	@CreatedDate
	@Column(updatable = false)
	private LocalDate createdAt;

	public static MemberStatus createMemberStatus(
		Long memberId,
		int height,
		int weight,
		int goalWeight,
		Long nutrientPlanId,
		Long activityLevelId
	) {
		return MemberStatus.builder()
			.memberId(memberId)
			.height(height)
			.weight(weight)
			.goalWeight(goalWeight)
			.nutrientPlanId(nutrientPlanId)
			.activityLevelId(activityLevelId)
			.createdAt(LocalDate.now())
			.build();
	}

	public static MemberStatus createFromPrevious(
		MemberStatus previousStatus,
		int currentWeight,
		LocalDate currentDate
	) {
		return MemberStatus.builder()
			.memberId(previousStatus.getMemberId())
			.height(previousStatus.getHeight())
			.weight(currentWeight)
			.goalWeight(previousStatus.getGoalWeight())
			.nutrientPlanId(previousStatus.getNutrientPlanId())
			.activityLevelId(previousStatus.getActivityLevelId())
			.createdAt(currentDate)
			.build();
	}

	public void updateWeight(int currentWeight) {
		this.weight = currentWeight;
	}

}
