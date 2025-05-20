package com.foodon.foodon.food.repository;

import com.foodon.foodon.food.domain.Food;
import com.foodon.foodon.food.dto.response.FoodLocalDbResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long>, FoodRepositoryCustom {
    boolean existsByMemberIdAndName(Long memberId, String foodName);

    List<Food> findByNameContainingAndSearchableIsTrueAndMemberIdIsNull(String name, Pageable pageable);

    List<Food> findTop10ByMemberIdAndSearchableIsTrueOrderByIdDesc(Long memberId);

    @Query(value = """
    SELECT f.food_id AS foodId,
           f.display_name AS foodName,
           f.unit AS servingUnit,
           CAST(COALESCE(fn.value, 0) AS SIGNED) AS kcal,
           false AS isCustom
    FROM foods f
    LEFT JOIN food_nutrients fn ON f.food_id = fn.food_id AND fn.nutrient_id = 1
    WHERE f.food_id > :lastFoodId
    """, nativeQuery = true)
    List<FoodLocalDbResponse> findSyncFoodsWithKcal(@Param("lastFoodId") Long lastFoodId);
}
