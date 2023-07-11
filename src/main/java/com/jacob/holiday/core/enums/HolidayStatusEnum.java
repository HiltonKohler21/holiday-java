package com.jacob.holiday.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HolidayStatusEnum {

    CONFIRMED("已确认"),
    TENTATIVE("暂定")
    ;

    private final String desc;

}
