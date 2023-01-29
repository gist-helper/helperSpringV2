package com.example.helper.entity;

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

    public String generateMenu() {
        String menu = "";
        menu += this.date + " " + this.kind + "\n\n";
        menu += this.bldg + "\n\n";
        menu += this.menu;
        if(kindType == 1) {
            menu += "\n\\코너\\\n";
            menu += this.special;
        }
        return menu;
    }
}
