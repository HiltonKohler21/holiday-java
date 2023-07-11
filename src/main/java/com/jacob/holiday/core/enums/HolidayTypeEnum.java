package com.jacob.holiday.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HolidayTypeEnum {

    HOLIDAY("假期"),
    WORK("补班")
    ;

    private final String desc;

}
