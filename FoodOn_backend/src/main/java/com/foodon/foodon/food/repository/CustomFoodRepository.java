package com.foodon.foodon.food.repository;

import com.foodon.foodon.food.domain.CustomFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomFoodRepository extends JpaRepository<CustomFood, Long> {
}
