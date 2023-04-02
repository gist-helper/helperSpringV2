package com.example.helper.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// https://dev-jhl.tistory.com/entry/Lombok-올바른-Lombok-사용법-Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DateMealDto {
    private String breakfast;
    private String breakfast_corner;
    private String lunch;
    private String lunch_corner;
    private String lunch_bldg1_2;
    private String dinner;

    @Builder
    public DateMealDto(String breakfast,
                       String breakfast_corner,
                       String lunch,
                       String lunch_corner,
                       String lunch_bldg1_2,
                       String dinner) {
        this.breakfast = breakfast;
        this.breakfast_corner = breakfast_corner;
        this.lunch = lunch;
        this.lunch_corner = lunch_corner;
        this.lunch_bldg1_2 = lunch_bldg1_2;
        this.dinner = dinner;
    }
}
