package com.example.helper.constant;

import static com.example.helper.constant.SpecMealInputsKor.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SpecMealInputsKorTest {

    @ParameterizedTest
    @MethodSource("provideStringsForIsTypes")
    void enum_한국어식단_타입변환(String kind, Integer expected) {
        // given
        // when
        Integer result = getTypeByString(kind);
        // then
        assertEquals(expected, result);
    }

    private static Stream<Arguments> provideStringsForIsTypes() {
        return Stream.of(
                Arguments.of(BREAKFAST.getInputs(), BREAKFAST.getInputValue()),
                Arguments.of(LUNCH.getInputs(), LUNCH.getInputValue()),
                Arguments.of(DINNER.getInputs(), DINNER.getInputValue())
        );
    }
}
