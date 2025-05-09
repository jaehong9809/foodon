package com.foodon.foodon.recommend.domain.characteristic;

import lombok.Getter;

import java.util.List;

@Getter
public class FoodCharacteristicEvaluator {

    private static final List<FoodCharacteristicRule> rules = List.of(
            new HighProteinRule(),
            new LowSugarRule(),
            new LowSodiumRule(),
            new HighKcalRule(),
            new LowKcalRule()
    );

    public static List<FoodCharacteristic> evaluate(List<NutrientServingInfo> nutrientServingInfos) {
        return rules.stream()
                .filter(rule -> rule.matches(nutrientServingInfos))
                .map(FoodCharacteristic::from)
                .toList();
    }

}

