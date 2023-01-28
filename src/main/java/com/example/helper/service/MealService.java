package com.example.helper.service;

import com.example.helper.entity.Meal;
import com.example.helper.repository.MealRepository;
import com.example.helper.repository.SqlMealRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@Transactional
@Slf4j
public class MealService {

    @Autowired(required = true)
    private SqlMealRepository sqlMealRepository;

    public MealService(SqlMealRepository sqlMealRepository) {
        this.sqlMealRepository = sqlMealRepository;
    }

    public Long mealCreate(Meal meal) {
        validateDuplicateMeal(meal);
        sqlMealRepository.save(meal);
        return meal.getId();
    }

    private void validateDuplicateMeal(Meal meal) {
        // DB 중복 체크
        sqlMealRepository.findByPk(meal.getBldgType(), meal.getLangType(), meal.getDateType(), meal.getKindType(), meal.getDate())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 식단입니다.");
                });
    }

    private String getNowMeal(Integer langType) {
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

        String date = currentDateTime.getYear() + "-";
        date += String.format("%02d", currentDateTime.getMonth().getValue()) + "-";
        date += String.format("%02d", currentDateTime.getDayOfMonth()) + "";

        Optional<Meal> result = sqlMealRepository.findByDate(2, langType, kindType, date);

        if(result.isEmpty()) {
            //throw new IllegalStateException("조건에 맞는 식단이 존재하지 않습니다.");
            if(langType == 0) {
                return "식단 준비중입니다.";
            }
            else {
                return "The meal is being prepared.";
            }
        }
        Meal meal = result.get();
        return meal.generateMenu();
    }

    public String getNowKorMeal() {
        return getNowMeal(0);
    }

    public String getNowEngMeal() {
        return getNowMeal(1);
    }

    public String getSpecKorMeal(String dateCustom, String bld) {
        //TODO sqlMealRepository.findSpecKorMeal
        return "2023-01-27 조식\n\n제2학생회관1층\n\n흰밥*김가루양념밥\n";
    }

    public String getSpecEngMeal(String dateCustom, String bld) {
        //TODO sqlMealRepository.findSpecEngMeal
        return "2023-01-27 Breakfast\n\nStudent Union Bldg.2 1st floor\n\nWhite rice*Seasoned rice with seaweed\n";
    }

    public Map<String, Object> responseMeal(String menu) {
        // Response Body Construct
        // const responseBody = {
        //          version: "2.0",
        //          template: {
        //              outputs: [
        //                          {
        //                              simpleText: {
        //                                      text: nowMeal
        //                                  }
        //                           }
        //                        ]
        //                     }
        //                  };

        Map<String, Object> simpleText =  new HashMap<>();
        simpleText.put("text", menu);

        Map<String, Object> simpleTextWrapper = new HashMap<>();
        simpleTextWrapper.put("simpleText", simpleText);

        List<Object> outputs = new ArrayList(1);
        outputs.add(simpleTextWrapper);

        Map<String, Object> template = new HashMap<>();
        template.put("outputs", outputs);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("version", "2.0");
        responseBody.put("template", template);

        return responseBody;
    }
}
