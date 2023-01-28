package com.example.helper.service;

import com.example.helper.repository.SqlMealRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class MealServiceTest {
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

}
