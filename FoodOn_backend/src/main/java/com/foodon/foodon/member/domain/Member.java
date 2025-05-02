package com.foodon.foodon.member.domain;

import com.foodon.foodon.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "members")
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = true, length = 100)
    private String email;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(length = 1000)
    private String profileImage;

    @Column(nullable = false)
    private boolean profileUpdated;

    private LocalDate birthday;

    private Gender gender;

    private int height;

    private int goalWeight;

    private Long nutrientPlanId;

    private Long activityLevelId;

    private Member (
            String nickname,
            String email,
            String profileImgUrl
    ){
        this.nickname = nickname;
        this.email = email;
        this.profileImage = profileImgUrl;
    }

    public static Member createMember(
            String nickname,
            String email,
            String profileImgUrl
    ) {
        return new Member(
                nickname,
                email,
                profileImgUrl
        );
    }
}
