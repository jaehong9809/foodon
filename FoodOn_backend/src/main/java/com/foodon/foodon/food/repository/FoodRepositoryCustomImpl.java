package com.foodon.foodon.food.repository;

import com.foodon.foodon.food.dto.FoodInfo;
import com.foodon.foodon.food.dto.FoodWithNutrientInfo;
import com.foodon.foodon.food.domain.FoodType;
import com.foodon.foodon.food.dto.NutrientInfo;
import com.foodon.foodon.member.domain.Member;
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
import static com.foodon.foodon.food.domain.QNutrient.nutrient;

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
                .select(
                        Projections.fields(
                                FoodInfo.class,
                                food.id,
                                food.name,
                                food.unit
                        )
                )
                .from(food)
                .where(
                        food.id.eq(id),
                        eqMemberId(type, member.getId())
                )
                .fetchOne();

        List<NutrientInfo> nutrients = queryFactory
                .select(
                        Projections.fields(
                                NutrientInfo.class,
                                foodNutrient.foodId,
                                foodNutrient.id.as("foodNutrientId"),
                                nutrient.type.as("nutrientType"),
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
                nutrients
        );
    }

    @Override
    public List<FoodWithNutrientInfo> findFoodInfoWithNutrientByNameIn(Set<String> foodNames) {

        List<FoodInfo> foods = queryFactory
                .select(
                        Projections.fields(
                                FoodInfo.class,
                                food.id,
                                food.name,
                                food.unit
                        )
                )
                .from(food)
                .where(
                        food.memberId.isNull(), // 공공데이터
                        food.name.in(foodNames)
                )
                .fetch();

        List<Long> foodIds = foods.stream().map(FoodInfo::id).toList();

        List<NutrientInfo> nutrientList = queryFactory
                .select(
                        Projections.fields(
                                NutrientInfo.class,
                                foodNutrient.foodId,
                                foodNutrient.id.as("foodNutrientId"),
                                nutrient.type.as("nutrientType"),
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
                .map(food -> new FoodWithNutrientInfo(
                        FoodType.PUBLIC,  // 필요 시 매핑 로직으로 조정
                        food.id(),
                        food.name(),
                        food.unit(),
                        nutrientsByFoodId.getOrDefault(food.id(), List.of())
                ))
                .toList();
    }

    private BooleanExpression eqMemberId(FoodType type, Long memberId) {
        if(Objects.isNull(type) || type.equals(FoodType.PUBLIC)){
            return null;
        }

        return food.memberId.eq(memberId);
    }

}
