package com.foodon.foodon.nutrientplan.repository;

import com.foodon.foodon.nutrientplan.domain.NutrientPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NutrientPlanRepository extends JpaRepository<NutrientPlan, Long> {
}
