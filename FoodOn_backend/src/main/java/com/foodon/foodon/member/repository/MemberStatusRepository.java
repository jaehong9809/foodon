package com.foodon.foodon.member.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodon.foodon.member.domain.MemberStatus;

public interface MemberStatusRepository extends JpaRepository<MemberStatus, Long> {

	List<MemberStatus> findByMemberIdAndCreatedAtBetweenOrderByCreatedAt(
		Long memberId,
		LocalDate createdAtAfter,
		LocalDate createdAtBefore
	);

	Optional<MemberStatus> findTopByMemberIdOrderByCreatedAtDesc(Long memberId);

}
