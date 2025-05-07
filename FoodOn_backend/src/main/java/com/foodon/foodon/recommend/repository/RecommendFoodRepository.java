package com.foodon.foodon.recommend.repository;

import com.foodon.foodon.recommend.domain.RecommendFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RecommendFoodRepository extends JpaRepository<RecommendFood, Long>, RecommendFoodRepositoryCustom {
}
