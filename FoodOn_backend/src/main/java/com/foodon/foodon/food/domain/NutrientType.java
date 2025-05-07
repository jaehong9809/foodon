package com.foodon.foodon.food.domain;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum NutrientType {
    KCAL("kcal", "열량"),
    CARBS("carbs", "탄수화물"),
    SUGAR("sugar", "당류"),
    FIBER("fiber", "식이섬유"),
    PROTEIN("protein", "단백질"),
    FAT("fat", "지방"),
    SATURATED_FAT("saturated_fat", "포화지방"),
    TRANS_FAT("trans_fat", "트랜스지방"),
    FATTY_ACID("fatty_acid", "지방산"),
    UNSATURATED_FAT("unsaturated_fat", "불포화지방"),
    CHOLESTEROL("cholesterol", "콜레스테롤"),
    SODIUM("sodium", "나트륨"),
    POTASSIUM("potassium", "칼륨"),
    ALCOHOL("alcohol", "알코올");

    private final String dbType;
    private final String label;

    NutrientType(String dbType, String label) {
        this.dbType = dbType;
        this.label = label;
    }

    public static NutrientType from(String dbType) {
        return Arrays.stream(values())
                .filter(nutrientType -> dbType.equals(nutrientType.getDbType()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 이름의 영양 성분은 존재하지 않습니다. dbType = " + dbType));
    }

}
