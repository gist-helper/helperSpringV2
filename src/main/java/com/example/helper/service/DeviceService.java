package com.example.helper.service;

import com.example.helper.constant.Messages;
import com.example.helper.constant.Types;
import com.example.helper.dto.DateMealDto;
import com.example.helper.dto.DateReqDto;
import com.example.helper.entity.Meal;
import com.example.helper.repository.SqlMealRepository;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class DeviceService {

    @Autowired
    private SqlMealRepository sqlMealRepository;

    public DateMealDto getDateMenus(DateReqDto dateReqDto) {
        DateMealDto result = DateMealDto.builder()
                .breakfast(getMenuFromMeal(dateReqDto, Types.KIND_BREAKFAST.getType()))
                .breakfast_corner(getMenuFromMeal(dateReqDto, Types.KIND_BREAKFAST_CORNER.getType()))
                .lunch(getMenuFromMeal(dateReqDto, Types.KIND_LUNCH.getType()))
                .lunch_corner(getMenuFromMeal(dateReqDto, Types.KIND_LUNCH_CORNER.getType()))
                .lunch_bldg1_2(getMenuFromMeal(dateReqDto, Types.KIND_LUNCH_BLDG1_2.getType()))
                .dinner(getMenuFromMeal(dateReqDto, Types.KIND_DINNER.getType()))
                .build();

        // TODO: exception handling(target data is not in DB)
        if (checkAllMenuEqualToNewLine(result)) {
            throw new IllegalStateException(Messages.MOBILE_ALL_MEAL_EMPTY_ERROR.getMessages());
        }
        return result;
    }

    public String getMenuFromMeal(DateReqDto dateReqDto, Integer kindType) {
//        log.info("getMenuFromMeal() called. dateReqDto: {}, kindType: {}", dateReqDto.toString(), kindType);

        if (dateReqDto.getBldgType().equals(Types.BLDG2_1ST.getType())) {
            return getMenuOfBldg2(dateReqDto, kindType);
        }

        // Mobile에서 1학의 bldgType=1로 요청되니, BLDG1_2ND로 비교
        if (dateReqDto.getBldgType().equals(Types.BLDG1_2ND.getType())) {
            return getMenuOfBldg1(dateReqDto, kindType);
        }

        throw new IllegalStateException(Messages.MOBILE_INVALID_BLDG_REQUEST_ERROR.getMessages());
    }

    public String getMenuOfBldg2(DateReqDto dateReqDto, Integer kindType) {
        String result = "";

        // TODO: 필요없는 2층 메뉴 처리
        if (kindType.equals(Types.KIND_LUNCH_BLDG1_2.getType())) {
            return Messages.EMPTY_MEAL.getMessages();
        }

        // TODO: CORNER 메뉴 처리
        Integer kindTypeForCorner = kindType;
        if (kindType.equals(Types.KIND_LUNCH_CORNER.getType())) {
            kindTypeForCorner = Types.KIND_LUNCH.getType();
        }

        Optional<Meal> meal = sqlMealRepository.findByDate(
                dateReqDto.getBldgType(),
                dateReqDto.getLangType(),
                kindTypeForCorner,
                conv2DateStr(dateReqDto.getYear(), dateReqDto.getMonth(), dateReqDto.getDate()));

        if (meal.isPresent()) {
            result = meal.get().getMenu();
            if (kindType.equals(Types.KIND_LUNCH_CORNER.getType())) {
                result = meal.get().getSpecial();
            }
        }

        // 2학 corner메뉴 없는 주말의경우, meal.isPresent()가 false이니 위의 if문 밖에서 Empty 변환함.
        result = convIfEmptyMenu(result);

        return result;
    }

    public String getMenuOfBldg1(DateReqDto dateReqDto, Integer kindType) {
        String result = "";

        // TODO: CORNER 메뉴 처리
        Integer kindTypeForCornerFloor2 = kindType;
        if (kindType.equals(Types.KIND_LUNCH_CORNER.getType())) {
            kindTypeForCornerFloor2 = Types.KIND_LUNCH.getType();
        }

        // TODO: 2층 메뉴 처리
        Integer bldgTypeForBldg1_2 = Types.BLDG1_1ST.getType(); // DB의 bldg1_1 type인 0으로 변환
        if (kindType.equals(Types.KIND_LUNCH_BLDG1_2.getType())) {
            bldgTypeForBldg1_2 = Types.BLDG1_2ND.getType();
            kindTypeForCornerFloor2 = Types.KIND_LUNCH.getType(); // kindType=4이니까 LUNCH=1로 변환
        }

        // TODO: 조식 CORNER 메뉴 처리
        if (kindType.equals(Types.KIND_BREAKFAST_CORNER.getType())) {
            kindTypeForCornerFloor2 = Types.KIND_BREAKFAST.getType();
        }

        Optional<Meal> meal = sqlMealRepository.findByDate(
                bldgTypeForBldg1_2,
                dateReqDto.getLangType(),
                kindTypeForCornerFloor2,
                conv2DateStr(dateReqDto.getYear(), dateReqDto.getMonth(), dateReqDto.getDate()));

        if (meal.isPresent()) {
            result = meal.get().getMenu();
            if (kindType.equals(Types.KIND_LUNCH_CORNER.getType())) {
                result = meal.get().getSpecial();
            }
            if (kindType.equals((Types.KIND_BREAKFAST_CORNER.getType()))) {
                result = meal.get().getSpecial();
            }
        }

        result = convIfEmptyMenu(result);

        return result;
    }

/*
이 밑에 함수들은 Util.java로 분리할까? 그러면 다른 사람들의 util 함수들이랑 섞여서 더 힘들라나...
근데 차피 IDE 있어서 찾기 쉬울텐데 Util로 다 모을까요?
*/
    public String convIfEmptyMenu(String meal) {
        if (meal.isEmpty()) {
            return Messages.EMPTY_MEAL.getMessages();
        }
        return meal;
    }

    public String conv2DateStr(String year, String month, String date) {
        String result = "";
        result = year + "-" + padZero(month) + "-" + padZero(date);
        return result;
    }

    public String padZero(String str) {
        String result = "";
        if (str.length() == 1) {
            result = "0" + str;
        } else {
            result = str;
        }
        return result;
    }

    public boolean checkAllMenuEqualToNewLine(DateMealDto menus) {
        String[] temp = {menus.getBreakfast(),
                menus.getLunch(),
                menus.getLunch_corner(),
                menus.getLunch_bldg1_2(),
                menus.getDinner()};
        for (String menu : temp)
            if (!menu.equals("\n")) {
                return false;
            }
        return true;
    }
}
