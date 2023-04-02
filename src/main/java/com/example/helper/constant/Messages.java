package com.example.helper.constant;

public enum Messages {
    EXIST_MEAL_ERROR("이미 존재하는 식단입니다."),
    NO_EXIST_MEAL_ERROR("조건에 맞는 식단이 존재하지 않습니다."),
    NO_MEAL_KOR("식단 준비중입니다."),
    NO_MEAL_ENG("The meal is being prepared."),
    INVALID_DATE("유효하지 않은 날짜입니다."),
    DUMMY_MEAL_KOR("2023-01-27 조식\n\n제2학생회관1층\n\n흰밥*김가루양념밥\n"),
    DUMMY_MEAL_ENG("2023-01-27 Breakfast\n\nStudent Union Bldg.2 1st floor\n\nWhite rice*Seasoned rice with seaweed\n"),
    EMPTY_MEAL("\n"),
    MOBILE_ALL_MEAL_EMPTY_ERROR("해당 날짜의 식단이 하나도 존재하지 않습니다."),
    MOBILE_INVALID_BLDG_REQUEST_ERROR("bldgType이 1또는 2가 아닙니다.");
    private String message;
    Messages(String message) {
        this.message = message;
    }
    public String getMessages() {
        return message;
    }
}
