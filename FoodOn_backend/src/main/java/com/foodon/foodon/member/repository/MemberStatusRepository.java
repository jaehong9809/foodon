package com.foodon.foodon.member.repository;

import com.foodon.foodon.member.domain.Member;
import com.foodon.foodon.member.domain.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberStatusRepository extends JpaRepository<MemberStatus, Long> {
    Optional<MemberStatus> findByMemberId(Long memberId);

    List<MemberStatus> findByMemberIdAndCreatedAtBetween(Long memberId, LocalDateTime start, LocalDateTime end);

    Optional<MemberStatus> findTopByMemberIdOrderByIdDesc(Long memberId);
}
