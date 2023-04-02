package com.example.helper.constant;

public enum Types {
    LANG_KOR(0),
    LANG_ENG(1),
    BLDG1_1ST(0),
    BLDG1_2ND(1),
    BLDG2_1ST(2),
    KIND_BREAKFAST(0),
    KIND_LUNCH(1),
    KIND_DINNER(2),
    KIND_LUNCH_CORNER(3),
    KIND_LUNCH_BLDG1_2(4),
    KIND_BREAKFAST_CORNER(5),
    DATE_MON(1),
    DATE_TUE(2),
    DATE_WED(3),
    DATE_THR(4),
    DATE_FRI(5),
    DATE_SAT(6),
    DATE_SUN(7);

    private Integer type;

    Types(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}
