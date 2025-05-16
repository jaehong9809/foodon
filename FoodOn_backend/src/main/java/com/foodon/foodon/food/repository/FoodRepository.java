package com.foodon.foodon.food.repository;

import com.foodon.foodon.food.domain.Food;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long>, FoodRepositoryCustom {
    boolean existsByMemberIdAndName(Long memberId, String foodName);
    List<Food> findByNameContainingAndSearchableIsTrue(String name, Pageable pageable);
}
