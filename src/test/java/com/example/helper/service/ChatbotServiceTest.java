package com.example.helper.service;

import com.example.helper.constant.SpecMealInputsEng;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ChatbotServiceTest {
    @Autowired
    private ChatbotService chatbotService;
    @Test
    void getNowTest() {
        LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        Integer hour = currentDateTime.getHour();
        Integer kindType = 0;
        if(9 <= hour && hour < 13) {
            kindType = 1;
        }
        else if(13 <= hour && hour < 19) {
            kindType = 2;
        }
        else if(19 <= hour && hour < 24) {
            kindType = 0;
        }
        else {
            kindType = 0;
            currentDateTime.plusDays(1);
        }

        Integer year = currentDateTime.getYear();
        Integer month = currentDateTime.getMonth().getValue();
        Integer day = currentDateTime.getDayOfMonth();

        System.out.println(year);
        System.out.println(month);
        System.out.println(day);
        System.out.println(hour);
        System.out.println(kindType);
    }

    @Test
    void specEngTest() {
        List<String> dateCustomTestList = new ArrayList<>();
        for(int i = 1 ; i <= 31 ; i++) {
            String tc = i + "";
            if(i == 1) {
                tc += "st";
            }
            else if(i == 2) {
                tc += "nd";
            }
            else if(i == 3) {
                tc += "rd";
            }
            else {
                tc += "th";
            }
            dateCustomTestList.add(tc);
        }
        dateCustomTestList.add(SpecMealInputsEng.TODAY.getInputs());
        dateCustomTestList.add(SpecMealInputsEng.TOMORROW.getInputs());
        dateCustomTestList.add(SpecMealInputsEng.MON.getInputs());
        dateCustomTestList.add(SpecMealInputsEng.THR.getInputs());
        dateCustomTestList.add(SpecMealInputsEng.WED.getInputs());
        dateCustomTestList.add(SpecMealInputsEng.THR.getInputs());
        dateCustomTestList.add(SpecMealInputsEng.FRI.getInputs());
        dateCustomTestList.add(SpecMealInputsEng.SAT.getInputs());
        dateCustomTestList.add(SpecMealInputsEng.SUN.getInputs());

        List<String> bldTestList = new ArrayList<>();
        bldTestList.add(SpecMealInputsEng.BREAKFAST.getInputs());
        bldTestList.add(SpecMealInputsEng.LUNCH.getInputs());
        bldTestList.add(SpecMealInputsEng.DINNER.getInputs());

        for(String dateCustom : dateCustomTestList) {
            for(String bld : bldTestList) {
                String specMeal = chatbotService.getSpecEngMeal(dateCustom, bld);
                System.out.println(dateCustom + " " + bld);
                System.out.println(specMeal);
            }
        }
    }

}
