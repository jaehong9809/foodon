package com.foodon.foodon.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
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
    private LocalDateTime createdAt;

}
