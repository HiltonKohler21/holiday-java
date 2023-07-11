package com.jacob.holiday;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import com.jacob.holiday.core.Holiday;
import com.jacob.holiday.core.HolidayUtil;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class HolidayUtilTest {

    @Test
    public void testReadJson() {
        String fileName = new File("").getAbsolutePath() + "/src/test/resources/2023.json";
        String json = FileUtil.readUtf8String(fileName);
        System.out.println(json);
    }

    @Test
    public void testReadIcs() {
        String fileName = new File("").getAbsolutePath() + "/src/test/resources/holidayCal.ics";
        List<Holiday> holidays = HolidayUtil.resolveIcsByFileName(fileName);
        System.out.println(JSONUtil.toJsonPrettyStr(holidays.get(0)));
    }


}
