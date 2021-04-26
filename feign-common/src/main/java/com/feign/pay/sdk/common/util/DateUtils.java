package com.feign.pay.sdk.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * @Description 时间工具类
 * @Author: ph
 * @Date: 2019/11/26 13:55
 */
public class DateUtils {

    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final String FORMAT_ZERO = "yyyy-MM-dd 00:00:00";

    private static final String FORMAT_DATE = "yyyy-MM-dd";

    private static final String FORMAT_TIME = "HH:mm:ss";

    private static final String FORMAT_YEAR = "yyyy";

    private static final String FORMAT_MONTH_DAY = "MM.dd";

    private static final String FORMAT_HOUR_MINUTE = "HH:mm";

    private static final String FORMAT_NO_SECOND = "yyyy-MM-dd HH:mm";

    private static final String FORMAT_DATE_MD = "MM-dd";

    /**HOUR_MILL
     * 获取某个时间的指定时间单元后的日期
     */
    public static Date next(int timeUnit, int amount) {
        Calendar currentTime = Calendar.getInstance();
        currentTime.add(timeUnit, amount);
        Date nextTime = currentTime.getTime();
        return nextTime;
    }

    /**
     * 获取某个时间的指定时间单元前的日期
     */
    public static Date before(int timeUnit, int amount) {
        Calendar currentTime = Calendar.getInstance();
        currentTime.add(timeUnit, -amount);
        Date beforeTime = currentTime.getTime();
        return beforeTime;
    }

    /**
     * 获取某个时间的指定时间单元后的日期
     */
    public static Date next(Date date, int timeUnit, int amount) {
        Calendar currentTime = Calendar.getInstance();
        currentTime.setTime(date);
        currentTime.add(timeUnit, amount);
        Date nextTime = currentTime.getTime();
        return nextTime;
    }

    /**
     * 获取某个时间的指定时间单元前的日期
     */
    public static Date before(Date date, int timeUnit, int amount) {
        Calendar currentTime = Calendar.getInstance();
        currentTime.setTime(date);
        currentTime.add(timeUnit, -amount);
        Date beforeTime = currentTime.getTime();
        return beforeTime;
    }

    /**
     * 字符串转datetime
     * @param str
     * @return
     * @throws ParseException
     */
    public static Date parseDateTime(String str) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT);
        Date parse = dateFormat.parse(str);
        return parse;
    }

    /**
     * 字符串转date
     * @param str
     * @return
     * @throws ParseException
     */
    public static Date parseDate(String str) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_DATE);
        Date parse = dateFormat.parse(str);
        return parse;
    }

    /**
     * 字符串转date
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date parseDateNoSecond(Date date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_NO_SECOND);
        Date parse = dateFormat.parse(dateFormat.format(date));
        return parse;
    }

    /**
     * 字符串转date
     * @param str
     * @return
     * @throws ParseException
     */
    public static Date parseYear(String str) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_YEAR);
        Date parse = dateFormat.parse(str);
        return parse;
    }

    /**
     * 时间转日期字符串
     * @param date
     * @return
     */
    public static String formateDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE);
        return sdf.format(date);
    }

    /**
     * 时间转日期字符串
     * @param date
     * @return
     */
    public static String formatMonthDay(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_MONTH_DAY);
        return sdf.format(date);
    }

    /**
     * 时间转日期字符串
     * @param date
     * @return
     */
    public static String formatHourMinute(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_HOUR_MINUTE);
        return sdf.format(date);
    }

    /**
     * 时间转日期字符串
     * @param date
     * @return
     */
    public static String formatMonthDay(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE_MD);
        return sdf.format(DateUtils.parseDateTime(date));
    }

    /**
     * 时间转日期时间字符串
     * @param date
     * @return
     */
    public static String formatDateTime(Date date){
        if(Objects.isNull(date)){
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
        return sdf.format(date);
    }

    /**
     * 获取某个时间的指定时间单元后的零点
     *
     * @param timeUnit
     * @param amount
     * @return
     * @throws ParseException
     */
    public static Date nextZeroTime(int timeUnit, int amount) throws ParseException {
        Date nextTime = next(timeUnit, amount);
        Date nextTimeZero = parseDateTime(new SimpleDateFormat(FORMAT_ZERO).format(nextTime));
        return nextTimeZero;
    }

    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param dateTime  当前时间
     * @param startTime 开始时间 HH:mm:ss格式
     * @param endTime   结束时间 HH:mm:ss格式
     * @return
     */
    public static boolean isEffectiveTime(Date dateTime, String startTime, String endTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_TIME);
        Date st = sdf.parse(startTime);
        Date et = sdf.parse(endTime);

        String dateTimeStr = sdf.format(dateTime);
        Date nowTime = sdf.parse(dateTimeStr);
        if (nowTime.getTime() == st.getTime()
                || nowTime.getTime() == et.getTime()) {
            return true;
        }

        Calendar now = Calendar.getInstance();
        now.setTime(nowTime);
        Calendar begin = Calendar.getInstance();
        begin.setTime(st);
        Calendar end = Calendar.getInstance();
        end.setTime(et);

        return now.after(begin) && now.before(end);
    }


    /**
     *
     * @param dateTime
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public static boolean isEffectiveTime(String dateTime, String startDate, String endDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_TIME);
        return isEffectiveTime(sdf.parse(dateTime), startDate, endDate);
    }

    /**
     * 判断当前时间是否在[startDate, endDate]区间，注意时间格式要一致
     *
     * @param dateTime  当前时间
     * @param startDate 开始时间 yyyy-MM-dd格式
     * @param endDate   结束时间 yyyy-MM-dd格式
     * @return
     */
    public static boolean isEffectiveDate(Date dateTime, String startDate, String endDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE);
        Date st = sdf.parse(startDate);
        Date et = sdf.parse(endDate);

        String dateTimeStr = sdf.format(dateTime);
        Date nowTime = sdf.parse(dateTimeStr);
        if (nowTime.getTime() == st.getTime()
                || nowTime.getTime() == et.getTime()) {
            return true;
        }

        Calendar now = Calendar.getInstance();
        now.setTime(nowTime);
        Calendar begin = Calendar.getInstance();
        begin.setTime(st);
        Calendar end = Calendar.getInstance();
        end.setTime(et);

        return now.after(begin) && now.before(end);
    }

    /**
     * 判断当前时间是否在[startDate, endDate]区间，注意时间格式要一致
     *
     * @param dateTime  当前时间
     * @param startDate 开始时间 yyyy-MM-dd格式
     * @param endDate   结束时间 yyyy-MM-dd格式
     * @return
     */
    public static boolean isEffectiveDateTime(Date dateTime, String startDate, String endDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
        Date st = sdf.parse(startDate);
        Date et = sdf.parse(endDate);
        String dateTimeStr = sdf.format(dateTime);
        System.out.println(dateTimeStr + " " + startDate + " " +endDate);
        Date nowTime = sdf.parse(dateTimeStr);
        if (nowTime.getTime() == st.getTime()
                || nowTime.getTime() == et.getTime()) {
            return true;
        }

        Calendar now = Calendar.getInstance();
        now.setTime(nowTime);
        Calendar begin = Calendar.getInstance();
        begin.setTime(st);
        Calendar end = Calendar.getInstance();
        end.setTime(et);

        return now.after(begin) && now.before(end);
    }

    /**
     * 获取 next年 后的年份
     * @param millis
     * @param next
     * @return
     */
    public static int getNextYear(Long millis,int next){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        calendar.add(Calendar.YEAR, next);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取周几
     * @param date
     * @return
     * @throws ParseException
     */
    public static Integer getWeek(String date) throws ParseException {
        if(Objects.isNull(date)){
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(parseDate(date));
        int dayForWeek = 0;
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            dayForWeek = 7;
        } else {
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }

    /**
     * 获取时间间隔，秒
     * @param startDate
     * @param endDate
     * @return
     */
    public static long diffSeconds(Date startDate, Date endDate){
        long different = endDate.getTime() - startDate.getTime();
        return different / 1000;
    }

    public static void main(String[] args) throws ParseException {
        System.out.println(parseDateNoSecond(new Date()));
    }

}
