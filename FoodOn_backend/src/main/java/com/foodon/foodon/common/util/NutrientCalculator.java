package com.foodon.foodon.common.util;

import com.foodon.foodon.meal.dto.MealItemInfo;
import com.foodon.foodon.meal.dto.NutrientInfo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.function.Function;

@Component
public class NutrientCalculator {

    public static BigDecimal multiply(BigDecimal value, BigDecimal quantity) {
        return value.multiply(quantity != null ? quantity : BigDecimal.ONE);
    }

    public static BigDecimal sum(
            List<MealItemInfo> items,
            Function<NutrientInfo, BigDecimal> getter
    ) {

        return items.stream()
                .map(item -> multiply(getter.apply(item.nutrientInfo()), item.quantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static int toRoundedInt(BigDecimal value) {
        return value.setScale(0, RoundingMode.HALF_UP).intValue();
    }

}
