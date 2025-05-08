package com.foodon.foodon.food.repository;

import com.foodon.foodon.food.domain.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long>, FoodRepositoryCustom {
    boolean existsByMemberIdAndName(Long memberId, String foodName);
}
