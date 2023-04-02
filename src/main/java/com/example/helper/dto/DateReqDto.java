package com.example.helper.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DateReqDto {
    private Integer langType;
    private Integer bldgType;
    private String year;
    private String month;
    private String date;

    @Builder
    public DateReqDto(Integer langType, Integer bldgType, String year, String month, String date) {
        this.langType = langType;
        this.bldgType = bldgType;
        this.year = year;
        this.month = month;
        this.date = date;
    }
}
