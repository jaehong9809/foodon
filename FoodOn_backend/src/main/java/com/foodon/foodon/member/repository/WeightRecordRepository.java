package com.foodon.foodon.member.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.foodon.foodon.member.domain.Member;
import com.foodon.foodon.member.domain.WeightRecord;

@Repository
public interface WeightRecordRepository extends JpaRepository<WeightRecord, Long> {
	List<WeightRecord> findByMemberAndRecordedAtBetween(Member member, LocalDateTime start, LocalDateTime end);

	Optional<WeightRecord> findTopByMemberOrderByIdDesc(Member member);
}
