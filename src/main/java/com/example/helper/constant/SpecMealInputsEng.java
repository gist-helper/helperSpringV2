package com.example.helper.constant;

import jakarta.persistence.criteria.CriteriaBuilder;

public enum SpecMealInputsEng {
    TODAY("today"),
    TOMORROW("tomorrow"),
    MON("Mon", Types.DATE_MON.getType()),
    TUE("Tue", Types.DATE_TUE.getType()),
    WED("Wed", Types.DATE_WED.getType()),
    THR("Thr", Types.DATE_THR.getType()),
    FRI("Fri", Types.DATE_FRI.getType()),
    SAT("Sat", Types.DATE_SAT.getType()),
    SUN("Sun", Types.DATE_SUN.getType()),
    DAY_1("st"),
    DAY_2("nd"),
    DAY_3("rd"),
    DAY_OTHER("th"),
    BREAKFAST("breakfast", Types.KIND_BREAKFAST.getType()),
    LUNCH("lunch", Types.KIND_LUNCH.getType()),
    DINNER("dinner", Types.KIND_DINNER.getType());
    private String input;
    private Integer inputValue;
    SpecMealInputsEng (String input) {
        this.input = input;
    }

    SpecMealInputsEng (String input, Integer inputValue) {
        this.input = input;
        this.inputValue = inputValue;
    }
    public String getInputs() {
        return input;
    }
    public Integer getInputValue() { return inputValue; }
}
