package com.jacob.holiday.core;

import com.jacob.holiday.core.enums.HolidayStatusEnum;
import com.jacob.holiday.core.enums.HolidayTypeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.time.LocalDate;

@Data
@Builder
public class Holiday {

    private LocalDate date;
    private HolidayTypeEnum type;
    private String festival;
    private String description;
    private Integer currentDay;
    private Integer totalDay;
    private String notice;
    private HolidayStatusEnum status;

    @Tolerate
    public Holiday() {}

}
