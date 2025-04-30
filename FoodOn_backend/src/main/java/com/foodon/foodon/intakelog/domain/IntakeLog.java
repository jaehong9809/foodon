package com.foodon.foodon.intakelog.domain;

import com.foodon.foodon.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "intake_logs")
public class IntakeLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intake_log_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private LocalDate date;

    @Column(precision = 7, scale = 2, nullable = false)
    private BigDecimal goalKcal;

    @Column(precision = 7, scale = 2, nullable = false)
    private BigDecimal intakeKcal;

    @Column(precision = 6, scale = 2, nullable = false)
    private BigDecimal intakeCarbs;

    @Column(precision = 6, scale = 2, nullable = false)
    private BigDecimal intakeProtein;

    @Column(precision = 6, scale = 2, nullable = false)
    private BigDecimal intakeFat;

}
