package com.example.helper.entity;

import com.example.helper.constant.Types;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
public class Meal {

    @Id
    @GeneratedValue
    private Long id;
    @Column
    private Integer bldgType;
    @Column
    private Integer langType;
    @Column
    private Integer dateType;
    @Column
    private Integer kindType;
    @Column
    private String bldg;
    @Column
    private String date;
    @Column
    private String kind;
    @Column
    private String menu;
    @Column
    private String special;
    //평일 아,저 1학 1층 + 2학
    //평일 점심이면 1학 1층 + 1학 2층 + 2학
    // 주말이면 2학만
    public String generateMenu() {
        String menu = "";
        menu += this.bldg + "\n\n";
        menu += this.menu;
//        if(kindType == Types.KIND_LUNCH.getType()) {
        if(!special.isBlank()) {
            if (langType == Types.LANG_KOR.getType()) {
                menu += "\n\\코너\\\n";
                menu += this.special;
            } else if (langType == Types.LANG_ENG.getType()) {
                menu += "\n\\Coner\\\n";
                menu += this.special;
            }
        }
        return menu;
    }
}

