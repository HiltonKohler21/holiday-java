package com.jacob.holiday.core;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.jacob.holiday.core.enums.HolidayStatusEnum;
import com.jacob.holiday.core.enums.HolidayTypeEnum;
import io.github.vampireachao.stream.core.optional.Sf;
import io.github.vampireachao.stream.core.stream.Steam;
import lombok.SneakyThrows;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.component.VEvent;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class HolidayUtil {

    @SneakyThrows
    public static List<Holiday> resolveIcsByFileName(String fileName) {
        return resolveIcs(Files.newInputStream(new File(fileName).toPath()));
    }

    @SneakyThrows
    public static List<Holiday> resolveIcs(InputStream is) {
        CalendarBuilder builder = new CalendarBuilder();
        Calendar calendar = builder.build(is);
        return resolveIcs(calendar);
    }

    public static List<Holiday> resolveIcsByUrl(String url) {
        // 国内访问地址: https://www.shuyz.com/githubfiles/china-holiday-calender/master/holidayCal.ics
        String icsContent = httpGet(url);
        Calendar calendar = Sf.ofStr(icsContent)
                .mayLet(StringReader::new)
                .mayLet(new CalendarBuilder()::build)
                .get();
        return resolveIcs(calendar);
    }

    public static List<Holiday> resolveIcs(Calendar calendar) {
        List<Holiday> holidays = Lists.newArrayList();
        for (Iterator i = calendar.getComponents(Component.VEVENT).iterator(); i.hasNext(); ) {
            VEvent event = (VEvent) i.next();
            String[] splitSummary = Steam.split(event.getSummary().getValue(), " ").toArray(String[]::new);

            int[] position = Opt.of(splitSummary).<String>map(a -> ArrayUtil.get(a, 2))
                    .map(v -> Steam.split(v, "/")
                            .mapToInt(j -> Integer.parseInt(j.replace("天", "")
                                    .replace("共", "")
                                    .replace("第", ""))
                            ).toArray())
                    .get();
            String description = event.getDescription().getValue();
            Date date = event.getStartDate().getDate();
            holidays.add(
                    Holiday.builder()
                            .date(LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalDate())
                            .type(EnumUtil.getBy(HolidayTypeEnum::getDesc, ArrayUtil.get(splitSummary, 1)))
                            .festival(ArrayUtil.get(splitSummary, 0))
                            .description(description)
                            .currentDay(ArrayUtil.get(position, 0))
                            .totalDay(ArrayUtil.get(position, 1))
                            .status(EnumUtil.getBy(HolidayStatusEnum::getDesc, event.getStatus().getValue()))
                            .notice(StrUtil.subAfter(description, ":", false))
                            .build()
            );
        }

        return holidays;
    }

    /**
     * 获取 get 请求返回数据
     * @param httpUrl   url 地址
     * @return  读取内容
     */
    public static String httpGet(String httpUrl) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
