package com.foodon.foodon.recommend.repository;

import com.foodon.foodon.food.domain.FoodType;
import com.foodon.foodon.member.domain.Member;
import com.foodon.foodon.recommend.domain.RecommendFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecommendFoodRepository extends JpaRepository<RecommendFood, Long> {

    Optional<RecommendFood> findByMemberAndFoodTypeAndFoodId(
            Member member,
            FoodType foodType,
            Long foodId
    );
}
