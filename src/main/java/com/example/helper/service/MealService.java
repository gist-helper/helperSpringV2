package com.example.helper.service;

import com.example.helper.entity.Meal;
import com.example.helper.repository.MealRepository;
import com.example.helper.repository.SqlMealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
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

    public String getNowKorMeal() {
        //TODO sqlMealRepository.findNowKorMeal
        return "2023-01-27 조식\n\n제2학생회관1층\n\n얼갈이된장국\n";
    }

    public String getNowEngMeal() {
        //TODO sqlMealRepository.findNowEngMeal
        return "2023-01-27 Breakfast\n\nStudent Union Bldg.2 1st floor\n\nSoybean Paste Soup\n";
    }

    public String getSpecKorMeal() {
        //TODO sqlMealRepository.findSpecKorMeal
        return "2023-01-27 조식\n\n제2학생회관1층\n\n흰밥*김가루양념밥\n";
    }

    public String getSpecEngMeal() {
        //TODO sqlMealRepository.findSpecEngMeal
        return "2023-01-27 Breakfast\n\nStudent Union Bldg.2 1st floor\n\nWhite rice*Seasoned rice with seaweed\n";
    }

}
