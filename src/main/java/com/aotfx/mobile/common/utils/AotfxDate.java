package com.aotfx.mobile.common.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * @Description
 * @auther xiutao li
 * @email hipilee@gamil.com leexiutao@foxmail.com
 * @create 2018-11-21 13:34
 */
public class AotfxDate {
    /**
     * date2比date1多的天数
     *
     * @param date
     * @param anotherDate
     * @return
     */
    public static int differentDays(Date date, Date anotherDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        Calendar anotherCalendar = Calendar.getInstance();
        anotherCalendar.setTime(anotherDate);
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        int anotherDay = anotherCalendar.get(Calendar.DAY_OF_YEAR);

        int year = calendar.get(Calendar.YEAR);
        int anotherYear = anotherCalendar.get(Calendar.YEAR);
        if (year != anotherYear)   //Different year
        {
            int dayDistance = 0;
            for (int i = year; i < anotherYear; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0)    //Leap year
                {
                    dayDistance += 366;
                } else    //Not leap year
                {
                    dayDistance += 365;
                }
            }

            return dayDistance + (anotherDay - day);
        } else    //Same year
        {
            return anotherDay - day;
        }
    }


    /**
     * 获取当前日期是星期几<br>
     *
     * @param date
     * @return {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"}
     */
    public static String dayOfWeek(Date date) {
        String[] weekDays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
}
