package com.foodon.foodon.food.repository;

import com.foodon.foodon.food.domain.CustomFood;
import com.foodon.foodon.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomFoodRepository extends JpaRepository<CustomFood, Long> {
    boolean existsByMemberAndName(Member member, String name);
    Optional<CustomFood> findByIdAndMember(Long id, Member member);
}
