package com.example.helper.controller;
import com.example.helper.dto.Mealdto;
import com.example.helper.entity.Meal;
import com.example.helper.service.MealService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(path = "/meals", produces = "application/json;charset=UTF-8")
@Slf4j
public class MealController {

    @Autowired
    private MealService mealService;

    @GetMapping("/all")
    public String hello() {
        return "Hello HELPERs. 초기 세팅 완료.";
    }

    @PostMapping("/test")
    public @ResponseBody void test(String testStr) {
        log.info(testStr);
    }
    @PostMapping("/create")
    public @ResponseBody String createMeal(@RequestBody Mealdto mealDto) {
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
    public @ResponseBody String readKorMeal() throws JsonProcessingException {
        // input  : None (먼저 서버에서 현재 시간 측정)
        // output : 한국어 식단이 포함된 JSON (단, JSON은 카톡 서버가 받을 수 있는 형식이여야 함.)

        // TODO 아래 기능 구현해 주세요.
        String nowMeal = mealService.getNowKorMeal();

        // Response Body Construct
        // TODO 더 좋은 방식이 있으면 구현해 주세요. ex) dto 이용.
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
        simpleText.put("text", nowMeal);

        Map<String, Object> simpleTextWrapper = new HashMap<>();
        simpleTextWrapper.put("simpleText", simpleText);

        List<Object> outputs = new ArrayList(1);
        outputs.add(simpleTextWrapper);

        Map<String, Object> template = new HashMap<>();
        template.put("outputs", outputs);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("version", "2.0");
        responseBody.put("template", template);

        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(responseBody);

        return result;
    }

    @PostMapping("/eng")
    public @ResponseBody String readEngMeal() throws JsonProcessingException {
        // input  : None (먼저 서버에서 현재 시간 측정)
        // output : 영어 식단이 포함된 JSON (단, JSON은 카톡 서버가 받을 수 있는 형식이여야 함.)

        // TODO 아래 기능 구현해 주세요.
        String nowMeal = mealService.getNowEngMeal();

        // Response Body Construct
        // TODO 더 좋은 방식이 있으면 구현해 주세요. ex) dto 이용.
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
        simpleText.put("text", nowMeal);

        Map<String, Object> simpleTextWrapper = new HashMap<>();
        simpleTextWrapper.put("simpleText", simpleText);

        List<Object> outputs = new ArrayList(1);
        outputs.add(simpleTextWrapper);

        Map<String, Object> template = new HashMap<>();
        template.put("outputs", outputs);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("version", "2.0");
        responseBody.put("template", template);

        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(responseBody);

        return result;
    }

    @PostMapping("/speckor")
    public @ResponseBody String readSpecKorMeal() throws JsonProcessingException {
        // input : 날짜요일내일 + 아점저 + 1/2학
        // output : 한국어 식단이 포함된 JSON (단, JSON은 카톡 서버가 받을 수 있는 형식이여야 함.)

        // TODO 아래 기능 구현해 주세요.
        String specMeal = mealService.getSpecKorMeal();

        // Response Body Construct
        // TODO 더 좋은 방식이 있으면 구현해 주세요. ex) dto 이용.
        // const responseBody = {
        //          version: "2.0",
        //          template: {
        //              outputs: [
        //                          {
        //                              simpleText: {
        //                                      text: specMeal
        //                                  }
        //                           }
        //                        ]
        //                     }
        //                  };

        Map<String, Object> simpleText =  new HashMap<>();
        simpleText.put("text", specMeal);

        Map<String, Object> simpleTextWrapper = new HashMap<>();
        simpleTextWrapper.put("simpleText", simpleText);

        List<Object> outputs = new ArrayList(1);
        outputs.add(simpleTextWrapper);

        Map<String, Object> template = new HashMap<>();
        template.put("outputs", outputs);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("version", "2.0");
        responseBody.put("template", template);

        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(responseBody);

        return result;
    }

    @PostMapping("/speceng")
    public @ResponseBody String readSpecEngMeal() throws JsonProcessingException {
        // input : 날짜요일내일 + 아점저 + 1/2학
        // output : 영어 식단이 포함된 JSON (단, JSON은 카톡 서버가 받을 수 있는 형식이여야 함.)

        // TODO 아래 기능 구현해 주세요.
        String specMeal = mealService.getSpecEngMeal();

        // Response Body Construct
        // TODO 더 좋은 방식이 있으면 구현해 주세요. ex) dto 이용.
        // const responseBody = {
        //          version: "2.0",
        //          template: {
        //              outputs: [
        //                          {
        //                              simpleText: {
        //                                      text: specMeal
        //                                  }
        //                           }
        //                        ]
        //                     }
        //                  };

        Map<String, Object> simpleText =  new HashMap<>();
        simpleText.put("text", specMeal);

        Map<String, Object> simpleTextWrapper = new HashMap<>();
        simpleTextWrapper.put("simpleText", simpleText);

        List<Object> outputs = new ArrayList(1);
        outputs.add(simpleTextWrapper);

        Map<String, Object> template = new HashMap<>();
        template.put("outputs", outputs);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("version", "2.0");
        responseBody.put("template", template);

        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(responseBody);

        return result;
    }
}
