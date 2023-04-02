package com.example.helper.constant;


public enum SpecMealInputsKor {
    TODAY("오늘"),
    TOMORROW("내일"),
    MON("월", Types.DATE_MON.getType()),
    TUE("화", Types.DATE_TUE.getType()),
    WED("수", Types.DATE_WED.getType()),
    THR("목", Types.DATE_THR.getType()),
    FRI("금", Types.DATE_FRI.getType()),
    SAT("토", Types.DATE_SAT.getType()),
    SUM("일", Types.DATE_SUN.getType()),
    DAY("일"),
    BREAKFAST("조식", Types.KIND_BREAKFAST.getType()),
    LUNCH("중식", Types.KIND_LUNCH.getType()),
    DINNER("석식", Types.KIND_DINNER.getType());
    private String input;
    private Integer inputValue;
    SpecMealInputsKor(String input) {
        this.input = input;
    }
    SpecMealInputsKor(String input, Integer inputValue) {
        this.input = input;
        this.inputValue = inputValue;
    }
    public String getInputs() {
        return input;
    }
    public Integer getInputValue() { return inputValue; }

    public static Integer getTypeByString(String str){
        for(SpecMealInputsKor each : values())
            if (each.getInputs().equals(str)) {
                return each.getInputValue();
            }
        return -1;
    }
}
