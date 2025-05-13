package com.foodon.foodon.food.repository;

import com.foodon.foodon.food.dto.*;
import com.foodon.foodon.food.domain.FoodType;
import com.foodon.foodon.member.domain.Member;
import com.foodon.foodon.recommend.dto.RecommendedFood;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.foodon.foodon.food.domain.QFood.food;
import static com.foodon.foodon.food.domain.QFoodNutrient.foodNutrient;
import static com.foodon.foodon.food.domain.QFoodNutrientClaim.foodNutrientClaim;
import static com.foodon.foodon.food.domain.QNutrient.nutrient;
import static com.foodon.foodon.food.domain.QNutrientClaim.nutrientClaim;

@RequiredArgsConstructor
public class FoodRepositoryCustomImpl implements FoodRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public FoodWithNutrientInfo findFoodInfoWithNutrientByIdAndType(
            Long id,
            FoodType type,
            Member member
    ) {
        FoodInfo foodInfo = queryFactory
                .select(Projections.constructor(
                                FoodInfo.class,
                                food.id,
                                food.memberId,
                                food.name,
                                food.unit,
                                food.servingSize
                        )
                )
                .from(food)
                .where(
                        food.id.eq(id),
                        eqMemberId(type, member.getId())
                )
                .fetchOne();

        List<NutrientInfo> nutrients = queryFactory
                .select(Projections.constructor(
                                NutrientInfo.class,
                                foodNutrient.foodId,
                                foodNutrient.id,
                                nutrient.name,
                                nutrient.code,
                                nutrient.nutrientUnit,
                                foodNutrient.value
                ))
                .from(foodNutrient)
                .join(nutrient).on(foodNutrient.nutrientId.eq(nutrient.id))
                .where(
                        foodNutrient.foodId.eq(id)
                )
                .fetch();


        return new FoodWithNutrientInfo(
                type,
                foodInfo.id(),
                foodInfo.name(),
                foodInfo.unit(),
                foodInfo.servingSize(),
                nutrients
        );
    }

    @Override
    public List<FoodWithNutrientInfo> findFoodInfoWithNutrientByNameIn(Set<String> foodNames) {
        List<FoodInfo> foods = queryFactory
                .select(Projections.constructor(
                                FoodInfo.class,
                                food.id,
                                food.memberId,
                                food.name,
                                food.unit,
                                food.servingSize
                        )
                )
                .from(food)
                .where(
                        food.memberId.isNull(), // 공공데이터
                        food.name.in(foodNames)
                )
                .fetch();

        List<Long> foodIds = foods.stream().map(FoodInfo::id).toList();

        List<NutrientInfo> nutrients = queryFactory
                .select(Projections.constructor(
                                NutrientInfo.class,
                                foodNutrient.foodId,
                                foodNutrient.id,
                                nutrient.name,
                                nutrient.code,
                                nutrient.nutrientUnit,
                                foodNutrient.value
                ))
                .from(foodNutrient)
                .join(nutrient).on(foodNutrient.nutrientId.eq(nutrient.id))
                .where(foodNutrient.foodId.in(foodIds))
                .fetch();

        Map<Long, List<NutrientInfo>> nutrientsByFoodId = groupByFoodId(nutrients);

        return foods.stream()
                .map(food -> new FoodWithNutrientInfo(
                        FoodType.PUBLIC,
                        food.id(),
                        food.name(),
                        food.unit(),
                        food.servingSize(),
                        nutrientsByFoodId.getOrDefault(food.id(), List.of())
                ))
                .toList();
    }

	@Override
	public List<RecommendedFood> findRecommendedFoodsWithNutrientInfo() {
		List<FoodInfo> foods = queryFactory
			.select(Projections.constructor(
					FoodInfo.class,
					food.id,
					food.memberId,
					food.name,
					food.unit,
					food.servingSize
				)
			)
			.from(food)
			.where(
				food.memberId.isNull(),
				food.isRecommended.isTrue()
			)
			.fetch();

		List<Long> foodIds = foods.stream().map(FoodInfo::id).toList();

		List<NutrientInfo> nutrientList = queryFactory
			.select(Projections.constructor(
				NutrientInfo.class,
				foodNutrient.foodId,
				foodNutrient.id,
				nutrient.name,
				nutrient.code,
				nutrient.nutrientUnit,
				foodNutrient.value
			))
			.from(foodNutrient)
			.join(nutrient).on(foodNutrient.nutrientId.eq(nutrient.id))
			.where(foodNutrient.foodId.in(foodIds))
			.fetch();

		Map<Long, List<NutrientInfo>> nutrientsByFoodId = nutrientList.stream()
			.collect(Collectors.groupingBy(NutrientInfo::foodId));

		return foods.stream()
			.map(food -> RecommendedFood.from(
				food.id(),
				food.name(),
				food.servingSize(),
				food.unit(),
				nutrientsByFoodId.getOrDefault(food.id(), List.of())
			))
			.toList();
	}

    @Override
    public List<FoodWithNutrientInfo> findAllFoodInfo() {
        List<FoodInfo> foods = queryFactory
                .select(Projections.constructor(
                                FoodInfo.class,
                                food.id,
                                food.memberId,
                                food.name,
                                food.unit,
                                food.servingSize
                        )
                )
                .from(food)
                .where(food.memberId.isNull())
                .fetch();

        List<NutrientInfo> nutrients = queryFactory
                .select(Projections.constructor(
                        NutrientInfo.class,
                        foodNutrient.foodId,
                        foodNutrient.id,
                        nutrient.name,
                        nutrient.code,
                        nutrient.nutrientUnit,
                        foodNutrient.value
                ))
                .from(foodNutrient)
                .join(nutrient).on(foodNutrient.nutrientId.eq(nutrient.id))
                .fetch();

        Map<Long, List<NutrientInfo>> nutrientsByFoodId = groupByFoodId(nutrients);

        return foods.stream()
                .map(food -> new FoodWithNutrientInfo(
                        FoodType.PUBLIC,
                        food.id(),
                        food.name(),
                        food.unit(),
                        food.servingSize(),
                        nutrientsByFoodId.getOrDefault(food.id(), List.of())
                ))
                .toList();
    }

    @Override
    public List<NutrientClaimInfo> findNutrientClaimsByFoodIds(List<Long> foodIds) {
        return queryFactory
                .select(Projections.constructor(
                        NutrientClaimInfo.class,
                        foodNutrientClaim.foodId,
                        nutrientClaim.id,
                        nutrientClaim.name,
                        nutrientClaim.type
                ))
                .from(foodNutrientClaim)
                .join(nutrientClaim).on(nutrientClaim.id.eq(foodNutrientClaim.nutrientClaimId))
                .where(foodNutrientClaim.id.in(foodIds))
                .fetch();
    }

    public static Map<Long, List<NutrientInfo>> groupByFoodId(List<NutrientInfo> nutrients) {
        return nutrients.stream().collect(Collectors.groupingBy(NutrientInfo::foodId));
    }

    private BooleanExpression eqMemberId(FoodType type, Long memberId) {
        if(Objects.isNull(type) || type.equals(FoodType.PUBLIC)){
            return null;
        }

        return food.memberId.eq(memberId);
    }

}
