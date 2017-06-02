package saiyi.com.xiande.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间格式互换
 *
 * @author EC
 */
public class DateUtils {

    /**
     * 日期是否在其他两个日期范围内
     *
     * @param youDate
     * @param beforeDate
     * @param afterDate
     * @return
     */
    public static boolean isRangeDate(Date youDate, Date beforeDate, Date afterDate) {
        Date formatDate = formatDate(youDate, "yyyy-MM-dd");
        if (formatDate.getTime() == beforeDate.getTime() || formatDate.getTime() == afterDate.getTime()) {
            return true;
        }
        //
        if (youDate.after(beforeDate) && youDate.before(afterDate)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 当前日期是否在其他两个日期范围内
     *
     * @param beforeDate
     * @param afterDate
     * @return
     */
    public static boolean isRangeDateByNow(Date beforeDate, Date afterDate) {
        Date youDate = new Date();
        return isRangeDate(youDate, beforeDate, afterDate);
    }

    /**
     * 仅仅比较时间，是否在其他两个时间范围内
     *
     * @param youDate
     * @param beforeTime
     * @param afterTime
     * @return
     */
    public static boolean isRangeTime(Date youDate, Date beforeTime, Date afterTime) {
        Calendar youDateCal = Calendar.getInstance();

        youDateCal.setTime(youDate);
        //
        Calendar beforeTimeCal = Calendar.getInstance();

        beforeTimeCal.setTime(beforeTime);
        beforeTimeCal.set(youDateCal.get(Calendar.YEAR), youDateCal.get(Calendar.MONTH), youDateCal.get(Calendar.DATE));
        //
        Calendar afterTimeCal = Calendar.getInstance();

        afterTimeCal.setTime(afterTime);
        afterTimeCal.set(youDateCal.get(Calendar.YEAR), youDateCal.get(Calendar.MONTH), youDateCal.get(Calendar.DATE));

        Date time = beforeTimeCal.getTime();
        Date time1 = afterTimeCal.getTime();

        if (youDate.after(beforeTimeCal.getTime()) && youDate.before(afterTimeCal.getTime())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取现在的时间 Str
     *
     * @return
     */
    public static String getNowDate2Str() {
        return getNowDate2Str("yyyy-MM-dd HH:mm:ss");
    }

    public static String getNowDate2Str(String parsePattern) {
        Date currentTime = new Date();
        return getDate2Str(currentTime, parsePattern);
    }

    public static String getDate2Str(Date date, String parsePattern) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(parsePattern);
            String dateString = formatter.format(date);
            return dateString;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * @param date
     * @param parsePattern
     * @return
     */
    public static Date formatDate(Date date, String parsePattern) {
        try {
            String dateInputStr = getDate2Str(date, parsePattern);
            //
            SimpleDateFormat dateInputFormat = new SimpleDateFormat(parsePattern);

            Date dateInputNoTime = dateInputFormat.parse(dateInputStr);

            return dateInputNoTime;
        } catch (Exception e) {
            e.printStackTrace();
            return date;
        }
    }

    /**
     * 把一个字符串类型的日期转换成另一种格式表示，比如2015-12-12转成2015年12月12日
     *
     * @param dateStr
     * @param patternSource
     * @param patternTo
     * @return
     */
    public static String formatOne2One(String dateStr, String patternSource, String patternTo) {
        SimpleDateFormat patternSourceSF = new SimpleDateFormat(patternSource);
        try {
            Date patternSourceDate = patternSourceSF.parse(dateStr);

            SimpleDateFormat patternToFormat = new SimpleDateFormat(patternTo);

            String patternToDateStr = patternToFormat.format(patternSourceDate);

            return patternToDateStr;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 识别当前是今天、昨天、前天、以及日期
     *
     * @param dateInput
     * @return
     */
    public static String getDesc(String dateInput) {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat dateInputFormat = new SimpleDateFormat(pattern);

        try {
            Date dateInputNoTime = dateInputFormat.parse(dateInput);
            return getDesc(dateInputNoTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        //

    }

    public static String getDesc(Date dateInput) {
        int descType;
        String pattern = "yyyy-MM-dd";
        String desc = "未知";
        //
        String dateInputStr = getDate2Str(dateInput, pattern);

        SimpleDateFormat dateInputFormat = new SimpleDateFormat(pattern);
        try {
            Date dateInputNoTime = dateInputFormat.parse(dateInputStr);

            Calendar calendarInputNoTime = Calendar.getInstance();

            calendarInputNoTime.setTime(dateInputNoTime);
            ////
            Date dateNow = new Date();

            String ateNowStr = getDate2Str(dateNow, pattern);

            Date dateNowNoTime = dateInputFormat.parse(ateNowStr);

            Calendar calendarNowNoTime = Calendar.getInstance();

            calendarNowNoTime.setTime(dateNowNoTime);
            //
            long timeInputNoTime = calendarInputNoTime.getTimeInMillis();

            long timeNowNoTime = calendarNowNoTime.getTimeInMillis();
            //
            long one = 60 * 60 * 24 * 1000;

            long subtract = timeNowNoTime - timeInputNoTime;

            if (subtract < one) {
                //小于一天，今天
                descType = 1;
            } else if (subtract >= one && subtract < one * 2) {
                //昨天
                descType = 2;
            } else if (subtract >= one * 2 && subtract < one * 3) {
                //前天
                descType = 3;
            } else {
                descType = 4;
            }
            ////
            switch (descType) {
                case 1:
                    desc = "今天";
                    break;
                case 2:
                    desc = "昨天";
                    break;
                case 3:
                    desc = "前天";
                    break;
                case 4:
                    desc = getDate2Str(dateInput, "yyyy-MM-dd");
                    break;
                default:
                    desc = "未知";
                    break;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        //
        return desc;

    }


}
