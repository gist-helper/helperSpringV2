//package com.example.helper.controller;
//
//import com.example.helper.dto.DateMealDto;
//import com.example.helper.dto.DateReqDto;
//import com.example.helper.service.DateMealService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.server.ResponseStatusException;
//
//@RestController
//@RequestMapping(path = "/mobile", produces = "application/json;charset=UTF-8")
//@Slf4j
//public class MobileController {
//
//    @Autowired
//    private DateMealService dateMealService;
//
//    @GetMapping("/test")
//    public String test() {
//        return "Response from MobileController";
//    }
//
//    // FE쪽에서 query에 담아주면 아래처럼 dto 객체 하나만 req로 받으면 되서 코드 깔끔함.
//    // DateMealDto dateMealDtoList = dateMealService.getDateMeal(dateReqDto);
//    // 근데 FE에서 보낼때 parameter 일일이 적기 귀찮으니 pathvariable로 받아서 처리
//    @GetMapping("/date/{year}/{month}/{day}/{bldgType}/{langType}")
//    public DateMealDto DateMealRead(
//            @PathVariable("langType") Integer langType,
//            @PathVariable("bldgType") Integer bldgType,
//            @PathVariable("year") Integer year,
//            @PathVariable("month") Integer month,
//            @PathVariable("day") Integer  day) {
//
//        DateReqDto dateReqDto = DateReqDto.builder()
//                .langType(langType)
//                .bldgType(bldgType)
//                .year(year.toString())
//                .month(month.toString())
//                .date(day.toString())
//                .build();
//
//        try {
//            DateMealDto dateMealDto = dateMealService.getDateMenus(dateReqDto);
//            return dateMealDto;
//        } catch (IllegalStateException e) {
//            throw new ResponseStatusException(
//                    HttpStatus.NOT_ACCEPTABLE, e.getMessage());
//        }
//    }

//}
