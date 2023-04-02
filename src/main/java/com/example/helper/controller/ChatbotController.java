package com.example.helper.controller;
import com.example.helper.dto.DateMealDto;
import com.example.helper.dto.DateReqDto;
import com.example.helper.dto.Mealdto;
import com.example.helper.entity.Meal;
import com.example.helper.service.DeviceService;
import com.example.helper.service.ChatbotService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import org.springframework.web.server.ResponseStatusException;


@RestController // @Controller + @ResponseBody. return이 view가 아닌, http body에 직접 쓰여짐.
@RequestMapping(path = "/meals", produces = "application/json;charset=UTF-8")
@Slf4j
public class ChatbotController {

    @Autowired
    private ChatbotService chatbotService;

    @Autowired
    private DeviceService deviceService;

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
        try {
            Long saved = chatbotService.mealCreate(meal);
            return "saved";
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
    }

    @PostMapping("/kor")
    public Map<String, Object> readKorMeal() throws JsonProcessingException {
        // input  : None (먼저 서버에서 현재 시간 측정)
        // output : 한국어 식단이 포함된 JSON (단, JSON은 카톡 서버가 받을 수 있는 형식이여야 함.)

        String nowMeal = chatbotService.getNowKorMeal();
        Map<String, Object> responseBody = chatbotService.responseMeal(nowMeal);

        return responseBody;
    }

    @PostMapping("/eng")
    public Map<String, Object> readEngMeal() throws JsonProcessingException {
        // input  : None (먼저 서버에서 현재 시간 측정)
        // output : 영어 식단이 포함된 JSON (단, JSON은 카톡 서버가 받을 수 있는 형식이여야 함.)

        String nowMeal = chatbotService.getNowEngMeal();
        Map<String, Object> responseBody = chatbotService.responseMeal(nowMeal);

        return responseBody;
    }

    @PostMapping("/speckor")
    public Map<String, Object> readSpecKorMeal(@RequestBody Map<String, Object> requestBody) throws JsonProcessingException {
        // input  : 날짜요일내일 + 아점저
        // output : 한국어 식단이 포함된 JSON (단, JSON은 카톡 서버가 받을 수 있는 형식이여야 함.)

        // how to print the request body
        //for (Entry<String, Object> entrySet : requestBody.entrySet()) {
        //    log.info(entrySet.getKey() + " : " + entrySet.getValue());
        //}

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> action = objectMapper.convertValue(requestBody.get("action"), Map.class);
        Map<String, Object> params = objectMapper.convertValue(action.get("params"), Map.class);

        //log.info(params2.get("dateCustom").toString() + " " + params2.get("bld").toString());
        String dateCustom = params.get("dateCustom").toString();
        String bld =  params.get("bld").toString();

        LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        String specMeal = chatbotService.getSpecKorMeal(dateCustom, bld, currentDateTime);
        Map<String, Object> responseBody = chatbotService.responseMeal(specMeal);

        return responseBody;
    }

    @PostMapping("/speceng")
    public Map<String, Object> readSpecEngMeal(@RequestBody Map<String, Object> requestBody) throws JsonProcessingException {
        // input  : 날짜요일내일 + 아점저
        // output : 영어 식단이 포함된 JSON (단, JSON은 카톡 서버가 받을 수 있는 형식이여야 함.)

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> action = objectMapper.convertValue(requestBody.get("action"), Map.class);
        Map<String, Object> params = objectMapper.convertValue(action.get("params"), Map.class);

        //log.info(params2.get("dateCustom").toString() + " " + params2.get("bld").toString());
        String dateCustom = params.get("dateCustom").toString();
        String bld =  params.get("bld").toString();

        String specMeal = chatbotService.getSpecEngMeal(dateCustom, bld);
        Map<String, Object> responseBody = chatbotService.responseMeal(specMeal);

        return responseBody;
    }

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
            DateMealDto dateMealDto = deviceService.getDateMenus(dateReqDto);
            return dateMealDto;
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
    }
}
