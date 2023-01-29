package com.example.helper.controller;
import com.example.helper.dto.DateMealDto;
import com.example.helper.dto.DateReqDto;
import com.example.helper.dto.Mealdto;
import com.example.helper.entity.Meal;
import com.example.helper.service.DateMealService;
import com.example.helper.service.MealService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.server.ResponseStatusException;


@RestController // @Controller + @ResponseBody. return이 view가 아닌, http body에 직접 쓰여짐.
@RequestMapping(path = "/meals", produces = "application/json;charset=UTF-8")
@Slf4j
public class MealController {

    @Autowired
    private MealService mealService;

    @Autowired
    private DateMealService dateMealService;

    @GetMapping("/all")
    public String hello() {
        return "Hello HELPERs. 초기 세팅 완료.";
    }

    @PostMapping("/test")
    public void test(String testStr) {
        log.info(testStr);
    }

    @PostMapping("/create")
    public String createMeal(@RequestBody Mealdto mealDto) {
        // input  : 식단 json
        // output : None

        // DTO to Entity
        Meal meal = new Meal();
        BeanUtils.copyProperties(mealDto, meal);

        // DB Save
        Long saved = mealService.mealCreate(meal);

        return "saved";
    }

    @PostMapping("/kor")
    public Map<String, Object> readKorMeal() throws JsonProcessingException {
        // input  : None (먼저 서버에서 현재 시간 측정)
        // output : 한국어 식단이 포함된 JSON (단, JSON은 카톡 서버가 받을 수 있는 형식이여야 함.)

        String nowMeal = mealService.getNowKorMeal();
        Map<String, Object> responseBody = mealService.responseMeal(nowMeal);
        // if need to stratify.
        // ObjectMapper objectMapper = new ObjectMapper();
        // String result = objectMapper.writeValueAsString(responseBody);

        return responseBody;
    }

    @PostMapping("/eng")
    public Map<String, Object> readEngMeal() throws JsonProcessingException {
        // input  : None (먼저 서버에서 현재 시간 측정)
        // output : 영어 식단이 포함된 JSON (단, JSON은 카톡 서버가 받을 수 있는 형식이여야 함.)

        String nowMeal = mealService.getNowEngMeal();
        Map<String, Object> responseBody = mealService.responseMeal(nowMeal);

        // if need to stratify.
        // ObjectMapper objectMapper = new ObjectMapper();
        // String result = objectMapper.writeValueAsString(responseBody);

        return responseBody;
    }

    @PostMapping("/speckor")
    public Map<String, Object> readSpecKorMeal(@RequestBody Map<String, Map<String, Map<String, String>>> requestBody) throws JsonProcessingException {
        // input : 날짜요일내일 + 아점저 + 1/2학
        // output : 한국어 식단이 포함된 JSON (단, JSON은 카톡 서버가 받을 수 있는 형식이여야 함.)

        Map<String, Map<String, String>> action = requestBody.get("action");
        Map<String, String> params = action.get("params");
        String dateCustom = params.get("dateCustom");
        String bld = params.get("bld");

        log.info(dateCustom + " " + bld);

        String specMeal = mealService.getSpecKorMeal(dateCustom, bld);
        Map<String, Object> responseBody = mealService.responseMeal(specMeal);

        // if need to stratify.
        // ObjectMapper objectMapper = new ObjectMapper();
        // String result = objectMapper.writeValueAsString(responseBody);

        return responseBody;
    }

    @PostMapping("/speceng")
    public Map<String, Object> readSpecEngMeal(@RequestBody Map<String, Map<String, Map<String, String>>> requestBody) throws JsonProcessingException {
        // input : 날짜요일내일 + 아점저 + 1/2학
        // output : 영어 식단이 포함된 JSON (단, JSON은 카톡 서버가 받을 수 있는 형식이여야 함.)

        Map<String, Map<String, String>> action = requestBody.get("action");
        Map<String, String> params = action.get("params");
        String dateCustom = params.get("dateCustom");
        String bld = params.get("bld");

        log.info(dateCustom + " " + bld);

        String specMeal = mealService.getSpecEngMeal(dateCustom, bld);
        Map<String, Object> responseBody = mealService.responseMeal(specMeal);

        // if need to stratify.
        // ObjectMapper objectMapper = new ObjectMapper();
        // String result = objectMapper.writeValueAsString(responseBody);

        return responseBody;
    }

    // FE쪽에서 query에 담아주면 아래처럼 dto 객체 하나만 req로 받으면 되서 코드 깔끔함.
    // DateMealDto dateMealDtoList = dateMealService.getDateMeal(dateReqDto);
    // 근데 FE에서 보낼때 parameter 일일이 적기 귀찮으니 pathvariable로 받아서 처리
    @GetMapping("/date/{year}/{month}/{day}/{bldgType}/{langType}")
    public DateMealDto DateMealRead(
            @PathVariable("langType") Integer langType,
            @PathVariable("bldgType") Integer bldgType,
            @PathVariable("year") Integer year,
            @PathVariable("month") Integer month,
            @PathVariable("day") Integer  day) {

        DateReqDto dateReqDto = DateReqDto.builder()
                .langType(langType)
                .bldgType(bldgType)
                .year(year.toString())
                .month(month.toString())
                .date(day.toString())
                .build();

        try {
            DateMealDto dateMealDto = dateMealService.getDateMenus(dateReqDto);
            return dateMealDto;
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
    }
}
