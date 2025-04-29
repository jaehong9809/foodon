package com.foodon.foodon.intakeLog.repository;

import com.foodon.foodon.intakeLog.domain.IntakeLog;
import com.foodon.foodon.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IntakeLogRepository extends JpaRepository<IntakeLog, Integer> {
    List<IntakeLog> findByMemberAndDateBetween(
            Member member,
            LocalDate startDate,
            LocalDate endDate
    );
}
