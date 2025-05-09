package com.foodon.foodon.meal.repository;

import com.foodon.foodon.food.domain.QFoodNutrient;
import com.foodon.foodon.food.domain.QNutrient;
import com.foodon.foodon.meal.domain.QMeal;
import com.foodon.foodon.meal.domain.QMealItem;
import com.foodon.foodon.meal.dto.NutrientIntakeInfo;
import com.foodon.foodon.member.domain.Member;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.foodon.foodon.food.domain.QFoodNutrient.foodNutrient;
import static com.foodon.foodon.food.domain.QNutrient.nutrient;
import static com.foodon.foodon.meal.domain.QMeal.meal;
import static com.foodon.foodon.meal.domain.QMealItem.mealItem;

@RequiredArgsConstructor
public class MealRepositoryCustomImpl implements MealRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<NutrientIntakeInfo> findNutrientIntakeByMemberAndDate(
            Member member,
            LocalDateTime start,
            LocalDateTime end
    ) {
        return queryFactory
                .select(
                        Projections.constructor(
                                NutrientIntakeInfo.class,
                                nutrient.id,
                                nutrient.name,
                                nutrient.code,
                                foodNutrient.value.multiply(mealItem.quantity).sum()
                        )
                )
                .from(meal)
                .join(mealItem).on(mealItem.meal.eq(meal))
                .join(foodNutrient).on(foodNutrient.foodId.eq(mealItem.foodId))
                .join(nutrient).on(nutrient.id.eq(foodNutrient.nutrientId))
                .where(
                        meal.member.eq(member),
                        meal.mealTime.between(start, end),
                        meal.isDeleted.isFalse(),
                        mealItem.isDeleted.isFalse()
                )
                .groupBy(nutrient.id, nutrient.name, nutrient.code)
                .fetch();
    }
}
