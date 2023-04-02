package com.example.helper.service;

import com.example.helper.constant.Messages;
import com.example.helper.constant.SpecMealInputsEng;
import com.example.helper.constant.SpecMealInputsKor;
import com.example.helper.constant.Types;
import com.example.helper.entity.Meal;
import com.example.helper.repository.SqlMealRepository;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@Transactional
@Slf4j
public class ChatbotService {

    @Autowired(required = true)
    private SqlMealRepository sqlMealRepository;

    public ChatbotService(SqlMealRepository sqlMealRepository) {
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
                    throw new IllegalStateException(Messages.EXIST_MEAL_ERROR.getMessages());
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
            currentDateTime = currentDateTime.plusDays(1);
        }
        else { // 0 <= hour && hour < 9
            kindType = 0;
        }

        String date = currentDateTime.getYear() + "-";
        date += String.format("%02d", currentDateTime.getMonth().getValue()) + "-";
        date += String.format("%02d", currentDateTime.getDayOfMonth()) + "";

        Optional<Meal> result0 = sqlMealRepository.findByDate(Types.BLDG1_1ST.getType(), langType, kindType, date);
        Optional<Meal> result1 = sqlMealRepository.findByDate(Types.BLDG1_2ND.getType(), langType, kindType, date);
        Optional<Meal> result2 = sqlMealRepository.findByDate(Types.BLDG2_1ST.getType(), langType, kindType, date);

        // TODO: 함수로 분리
        if(result0.isEmpty() && result1.isEmpty() && result2.isEmpty()) {
            //throw new IllegalStateException(Messages.EXIST_MEAL_ERROR.getMessages());
            if(langType == 0) {
                return Messages.NO_MEAL_KOR.getMessages();
            }
            else {
                return Messages.NO_MEAL_ENG.getMessages();
            }
        }

        String result ="";
        Meal meal0 = new Meal();
        Meal meal1 = new Meal();
        Meal meal2 = new Meal();
        if(!result0.isEmpty()){meal0 = result0.get();}
        if(!result1.isEmpty()){meal1 = result1.get();}
        if(!result2.isEmpty()){meal2 = result2.get();}

        result = resultMenu(meal0, meal1, meal2);
        return result;
    }


    public String resultMenu(Meal meal0, Meal meal1, Meal meal2){
        Calendar today = Calendar.getInstance();
        String result = today.get(Calendar.YEAR)+"-"+(today.get(Calendar.MONTH)+1)+ "-"+ today.get(Calendar.DATE);


        if(today.get(Calendar.HOUR_OF_DAY)>=19 || today.get(Calendar.HOUR_OF_DAY)<9){result += " 조식\n\n";}
        else if(today.get(Calendar.HOUR_OF_DAY)>=9 && today.get(Calendar.HOUR_OF_DAY)<13) {result += " 중식\n\n";}
        else if(today.get(Calendar.HOUR_OF_DAY)>=13 && today.get(Calendar.HOUR_OF_DAY)<19) {result += " 석식\n\n";}

        if(today.get(Calendar.DAY_OF_WEEK) == 6 && today.get(Calendar.HOUR_OF_DAY)>=19){
            result += meal2.generateMenu(); //금요일 저녁엔 토요일(주말) 아침을 보여줘야함
            return result;}

        if(today.get(Calendar.DAY_OF_WEEK) == 1 && today.get(Calendar.HOUR_OF_DAY)>=19){
            result += meal0.generateMenu() + "\n-------------------\n\n" + meal2.generateMenu();
            return result;}//일요일 저녁엔 월요일(평일) 아침을 보여줘야함

        if(today.get(Calendar.DAY_OF_WEEK) == 7 || today.get(Calendar.DAY_OF_WEEK) == 1 ){
            result += meal2.generateMenu(); //주말, 2학 메뉴만 7:토요일,  1:일요일
            return result;
        }
        else{
            if(today.get(Calendar.HOUR_OF_DAY)>=9 && today.get(Calendar.HOUR_OF_DAY)<13){ //평일 점심,1학1층 + 1학2층 + 2학
                result += meal0.generateMenu()
                        + "\n-------------------\n\n" + meal1.generateMenu()
                        + "\n-------------------\n\n" + meal2.generateMenu();
                return result;
            }
            else{
                result += meal0.generateMenu() + "\n-------------------\n\n" + meal2.generateMenu();
                return result;
                //평일 아침/저녁, 1학1층+ 2학
            }
        }
    }

    private Boolean specInputValidation(String dateCustom, String bld) {
        // input arguments : null, empty, " "
        if (dateCustom == null || bld == null || dateCustom.isBlank() || bld.isBlank()) {
            return false;
        }
        return true;
    }

    public String getNowKorMeal() {
        return getNowMeal(Types.LANG_KOR.getType());
    }

    public String getNowEngMeal() {
        return getNowMeal(Types.LANG_ENG.getType());
    }

    public String getSpecKorMeal(String dateCustom, String bld, LocalDateTime currentDateTime) {
        // test를 위해 LocalDateTime 객체를 argument로 받도록 변경

        if (!specInputValidation(dateCustom, bld)) {
            return Messages.NO_MEAL_KOR.getMessages();
        }

        try {
            if (dateCustom.equals(SpecMealInputsKor.TOMORROW.getInputs())) {
                // 내일
                currentDateTime = currentDateTime.plusDays(1);
            } else if (dateCustom.length() == 1) {
                // 요일
                Integer dateDiff = getDateDifference(dateCustom, currentDateTime);
                currentDateTime = currentDateTime.plusDays(dateDiff);
            } else if ((dateCustom.charAt(dateCustom.length() - 1) + "").equals(SpecMealInputsKor.DAY.getInputs())) {
                // 특정날짜
                currentDateTime = currentDateTime.withDayOfMonth(
                        Integer.parseInt(dateCustom.substring(0, dateCustom.length() - 1)));
            }
        } catch (Exception e) {
            return Messages.INVALID_DATE.getMessages();
        }

        String date = currentDateTime.getYear() + "-";
        date += String.format("%02d", currentDateTime.getMonth().getValue()) + "-";
        date += String.format("%02d", currentDateTime.getDayOfMonth()) + "";

        Optional<Meal> result = sqlMealRepository.findByDate(
                Types.BLDG2_1ST.getType(),
                Types.LANG_KOR.getType(),
                SpecMealInputsKor.getTypeByString(bld),
                date);

        // TODO : 존재하지 않는 식단이면, error message 반환.
        if(result.isEmpty()) {
            return Messages.NO_MEAL_KOR.getMessages();
        }

        return result.get().generateMenu();
    }

    public Integer getDateDifference(String day, LocalDateTime currentDateTime) {
        return SpecMealInputsKor.getTypeByString(day) - (Integer) currentDateTime.getDayOfWeek().getValue();
    }
    public Boolean dateFormatValidation(String date) {
        Boolean ret = true;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            sdf.parse(date);

        } catch(ParseException e) {
            ret = false;
        }
        return  ret;
    }
    public String getSpecEngMeal(String dateCustom, String bld) {
        if (!specInputValidation(dateCustom, bld)) {
            return Messages.NO_MEAL_ENG.getMessages();
        }
        LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        Integer year = currentDateTime.getYear();
        Integer month = currentDateTime.getMonth().getValue();
        Integer day = currentDateTime.getDayOfMonth();
        Integer currentDateNumber = currentDateTime.getDayOfWeek().getValue();

        Integer dateCustomLen = dateCustom.length();
        if(dateCustom.equals(SpecMealInputsEng.TODAY.getInputs())) {
            // 오늘
        }
        else if(dateCustom.equals(SpecMealInputsEng.TOMORROW.getInputs())) {
            // 내일
            currentDateTime = currentDateTime.plusDays(1);
            year = currentDateTime.getYear();
            month = currentDateTime.getMonth().getValue();
            day = currentDateTime.getDayOfMonth();
        }
        else if(dateCustomLen > 2) {
            String dateCustomPostfix = dateCustom.substring(dateCustomLen - 2, dateCustomLen);
            if( dateCustomPostfix.equals(SpecMealInputsEng.DAY_1.getInputs()) ||
                    dateCustomPostfix.equals(SpecMealInputsEng.DAY_2.getInputs()) ||
                    dateCustomPostfix.equals(SpecMealInputsEng.DAY_3.getInputs()) ||
                    dateCustomPostfix.equals(SpecMealInputsEng.DAY_OTHER.getInputs())) {
                // 특정날짜
                String dateCustomPrefix = (dateCustomLen == 3) ? dateCustom.substring(0, 1) : dateCustom.substring(0, 2);

                try{
                    day = Integer.valueOf(dateCustomPrefix);
                }
                catch (NumberFormatException ex){
                    day = 0;
                }
            }
            else {
                //요일
                Integer dateNumber = 0;
                if(dateCustom.equals(SpecMealInputsEng.MON.getInputs())) {
                    dateNumber = SpecMealInputsEng.MON.getInputValue();
                }
                else if(dateCustom.equals(SpecMealInputsEng.TUE.getInputs())) {
                    dateNumber = SpecMealInputsEng.TUE.getInputValue();
                }
                else if(dateCustom.equals(SpecMealInputsEng.WED.getInputs())) {
                    dateNumber = SpecMealInputsEng.WED.getInputValue();
                }
                else if(dateCustom.equals(SpecMealInputsEng.THR.getInputs())) {
                    dateNumber = SpecMealInputsEng.THR.getInputValue();
                }
                else if(dateCustom.equals(SpecMealInputsEng.FRI.getInputs())) {
                    dateNumber = SpecMealInputsEng.FRI.getInputValue();
                }
                else if(dateCustom.equals(SpecMealInputsEng.SAT.getInputs())) {
                    dateNumber = SpecMealInputsEng.SAT.getInputValue();
                }
                else if(dateCustom.equals(SpecMealInputsEng.SUN.getInputs())) {
                    dateNumber = SpecMealInputsEng.SUN.getInputValue();
                }

                if(dateNumber == 0) {
                    day = 0;
                }
                else {
                    Integer dateDiff = dateNumber - currentDateNumber;
                    Integer dateDiffAbs = (dateDiff > 0) ? dateDiff : dateDiff * -1;
                    currentDateTime = (dateDiff > 0) ? currentDateTime.plusDays(dateDiffAbs) : currentDateTime.minusDays(dateDiffAbs);
                    year = currentDateTime.getYear();
                    month = currentDateTime.getMonth().getValue();
                    day = currentDateTime.getDayOfMonth();
                }

            }
        }

        Integer kindType = -1;
        if(bld.equals(SpecMealInputsEng.BREAKFAST.getInputs())) {
            kindType = SpecMealInputsEng.BREAKFAST.getInputValue();
        }
        else if(bld.equals(SpecMealInputsEng.LUNCH.getInputs())) {
            kindType = SpecMealInputsEng.LUNCH.getInputValue();
        }
        else if(bld.equals(SpecMealInputsEng.DINNER.getInputs())) {
            kindType = SpecMealInputsEng.DINNER.getInputValue();
        }

        if( (day < 1 && day > 31) || (kindType < 0 || kindType > 2)) {
            return Messages.NO_MEAL_ENG.getMessages();
        }

        String date = year + "-";
        date += String.format("%02d", month) + "-";
        date += String.format("%02d", day) + "";

        //형식이 잘못되면 Query 에서 NULL 을 반환한다.
        //if(!dateFormatValidation(date)) {
        //    return Messages.NO_MEAL_ENG.getMessages();
        //}

        Optional<Meal> result = sqlMealRepository.findByDate(Types.BLDG2_1ST.getType(), Types.LANG_ENG.getType(), kindType, date);

        if(result.isEmpty()) {
            //throw new IllegalStateException(Messages.EXIST_MEAL_ERROR.getMessages());
            return Messages.NO_MEAL_ENG.getMessages();
        }
        Meal meal = result.get();
        return meal.generateMenu();
    }

    public Map<String, Object> responseMeal(String menu) {
        // Response Body Construct
        // responseBody: {
        //      version: "2.0",
        //          template: {
        //              outputs: [
        //                  {
        //                      simpleText:
        //                          {
        //                              text: meal
        //                           }
        //                   }
        //              ]
        //       }
        // };

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

        // stratify json to string
        // ObjectMapper objectMapper = new ObjectMapper();
        // String result = objectMapper.writeValueAsString(responseBody);

        return responseBody;
    }
}
