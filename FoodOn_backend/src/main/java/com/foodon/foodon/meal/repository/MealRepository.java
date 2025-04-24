package com.foodon.foodon.meal.repository;

import com.foodon.foodon.meal.domain.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {
}
