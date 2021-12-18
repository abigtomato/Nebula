package org.abigtomato.nebula.common.utils;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.text.ParseException;
import java.util.Date;

/**
 * @author abigtomato
 */
public class DateUtil {

    private static final String LUNA_DATE = "yyyy-MM-dd";
    private static final String LUNA_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    /**
     * Date格式化为yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        if (date == null) return "";
        return DateFormatUtils.format(date, LUNA_DATE);
    }

    /**
     * Date格式化为yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String formatDateTime(Date date) {
        if (date == null) return "";
        return DateFormatUtils.format(date, LUNA_DATE_TIME);
    }

    /**
     * 自定义格式化Date
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String formatCustom(Date date, String pattern) {
        return DateFormatUtils.format(date, pattern);
    }


    /**
     * String(yyyy-MM-dd)格式日期转为Date
     *
     * @param str yyy-MM-dd
     * @return
     * @throws ParseException
     */
    public static Date parseDate(String str) throws ParseException {
        return org.apache.commons.lang3.time.DateUtils.parseDate(str, LUNA_DATE);
    }

    /**
     * String(yyyy-MM-dd HH:mm:ss)格式日期转为Date
     *
     * @param str
     * @return
     * @throws ParseException
     */
    public static Date parseDateTime(String str) throws ParseException {
        return org.apache.commons.lang3.time.DateUtils.parseDate(str, LUNA_DATE_TIME);
    }

    /**
     * 自定义格式日期转为Date
     *
     * @param str
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date parseCustom(String str, String pattern) {
        try {
            return org.apache.commons.lang3.time.DateUtils.parseDate(str, pattern);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取当前年
     *
     * @return
     */
    public static int getYear() {
        return DateTime.now().getYear();
    }

    /**
     * 获取当前月
     *
     * @return
     */
    public static int getMoth() {
        return DateTime.now().getMonthOfYear();
    }

    /**
     * 获取当前日
     *
     * @return
     */
    public static int getDay() {
        return DateTime.now().getDayOfYear();
    }

    /**
     * 获取当前日（月）
     *
     * @param date
     * @return
     */
    public static int getDayOfMonth(Date date) {
        return new DateTime(date).getDayOfMonth();
    }

    /**
     * 获取当前日（年）
     *
     * @param date
     * @return
     */
    public static int getDayOfYear(Date date) {
        return new DateTime(date).getDayOfYear();
    }

    /**
     * 获取当前日（周）
     *
     * @param date
     * @return
     */
    public static int getDayOfWeek(Date date) {
        return new DateTime(date).getDayOfWeek();
    }

    /**
     * 获取当前时间
     *
     * @return Sat Jan 14 10:15:47 CST 2017
     */
    public static Date getDateTime() {
        return DateTime.now().toDate();
    }

    /**
     * 获取当前时间后几天的时间，例如获取后1天
     *
     * @param days 几天
     * @return
     */
    public static Date plusDays(int days) {
        return DateTime.now().plusDays(days).toDate();
    }

    /**
     * 获取指定时间的后几天
     *
     * @param date
     * @param days
     * @return
     */
    public static Date plusDays(Date date, int days) {
        return new DateTime(date).plusDays(days).toDate();
    }

    /**
     * 获取指定时间的后几分
     *
     * @param date
     * @param minute
     * @return
     */
    public static Date plusMinute(Date date, int minute) {
        return new DateTime(date).plusMinutes(minute).toDate();
    }

    /**
     * 获取指定时间的后几秒
     *
     * @param date
     * @param second
     * @return
     */
    public static Date plusSecond(Date date, int second) {
        return new DateTime(date).plusSeconds(second).toDate();
    }

    /**
     * 获取当前日期前几天的时间，例如获取前1天
     *
     * @param days 几天
     * @return
     */
    public static Date minusDays(int days) {
        return DateTime.now().minusDays(days).toDate();
    }

    /**
     * 获取指定时间的前几天
     *
     * @param date
     * @param days
     * @return
     */
    public static Date minusDays(Date date, int days) {
        return new DateTime(date).minusDays(days).toDate();
    }

    /**
     * 获取当前时间对于的星期
     *
     * @param date
     * @return
     */
    public static int getWeek(Date date) {
        return new DateTime(date).getDayOfWeek();
    }

    /**
     * <p>date1 距离 date2 有多长时间</p>
     *
     * @param date1 如 2017-03-21 14:38:20
     * @param date2 如 2017-03-28 14:38:20
     * @param type  需要得到什么类型的返回值，d天,h时,m分 ,s秒，默认是天
     * @return long
     * @author Hubal 2017年3月21日
     */
    public static int getDate1MinusDate2(Date date1, Date date2, String type) {
        DateTime dateTime1 = new DateTime(date1);
        DateTime dateTime2 = new DateTime(date2);
        switch (type) {
            case "h":
                return new Period(dateTime1, dateTime2, PeriodType.hours()).getHours();
            case "m":
                return new Period(dateTime1, dateTime2, PeriodType.minutes()).getMinutes();
            case "s":
                return new Period(dateTime1, dateTime2, PeriodType.seconds()).getSeconds();
            default:
                return new Period(dateTime1, dateTime2, PeriodType.days()).getDays();
        }
    }
}
