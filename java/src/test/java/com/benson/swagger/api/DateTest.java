package com.benson.swagger.api;

import cn.hutool.core.thread.ThreadUtil;
import org.junit.jupiter.api.Test;
import sun.rmi.runtime.Log;

import java.text.DateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.util.Date;
import java.util.Locale;
import java.util.stream.IntStream;

public class DateTest {

    @Test
    public void chapter01() {
        Instant start = Instant.now();
        ThreadUtil.safeSleep(2000);
        Instant end = Instant.now();
        Duration between = Duration.between(start, end);
        System.out.println(between.getSeconds());

    }

    @Test
    public void chapter02() {
        LocalDate now = LocalDate.now();
        LocalDate localDate = LocalDate.of(2020, 9, 16);
        System.out.println(now.plusDays(2));
        System.out.println(now);
        System.out.println(localDate);
        System.out.println(Month.from(localDate).getValue());
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println(localDateTime.format(dateTimeFormatter));

        LocalDate with = now.with(TemporalAdjusters.dayOfWeekInMonth(1, DayOfWeek.SUNDAY));
        System.out.println(with);
        System.out.println(with.with(TemporalAdjusters.lastDayOfMonth()));

    }

    @Test
    public void chapter03() {
        LocalTime now = LocalTime.now();
        System.out.println(now);
        System.out.println(now.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    @Test
    public void chapter04() {
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        Instant now = Instant.now();
        ZonedDateTime zonedDateTime = now.atZone(zoneId);
        System.out.println(zonedDateTime);
        System.out.println(now.atZone(ZoneId.of("GMT+7")));
    }

    @Test
    public void chapter05() {
        IntStream.range(0, 10)
                .forEach(item -> System.out.println(item));
    }
}
