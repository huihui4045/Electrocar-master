package saiyi.com.xiande.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

public class WeekdayUtil {

    //获取今天是星期几，返回（1,2，...，7）对应（周日，...，周六）
    public static int getWeekDayToday(){
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        return c.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * @param beforeDate 前时间
     * @param afterDate  后时间
     * @param deadline   最多相隔时间
     * @return 是的话，返回true，否则返回false
     * @title 判断两个日期是否在指定工作日内
     * @detail (只计算周六和周日)
     * 例如：前时间2008-12-05，后时间2008-12-11
     * @author chanson
     */
    public boolean compareWeekday(String beforeDate, String afterDate, int deadline) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d1 = sdf.parse(beforeDate);
            Date d2 = sdf.parse(afterDate);

            //工作日
            int workDay = 0;
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(d1);
            // 两个日期相差的天数
            long time = d2.getTime() - d1.getTime();
            long day = time / 3600000 / 24 + 1;
            if (day < 0) {
                //如果前日期大于后日期，将返回false
                return false;
            }
            for (int i = 0; i < day; i++) {
                if (isWeekday(gc)) {
                    workDay++;
                    //                  System.out.println(gc.getTime());
                }
                //往后加1天
                gc.add(GregorianCalendar.DATE, 1);
            }
            return workDay <= deadline;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param calendar 日期
     * @return 是工作日返回true，非工作日返回false
     * @title 判断是否为工作日
     * @detail 工作日计算:
     * 1、正常工作日，并且为非假期
     * 2、周末被调整成工作日
     * @author chanson
     */
    public boolean isWeekday(GregorianCalendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (calendar.get(GregorianCalendar.DAY_OF_WEEK) != GregorianCalendar.SATURDAY
                && calendar.get(GregorianCalendar.DAY_OF_WEEK) != GregorianCalendar.SUNDAY) {
            //平时
            return !getWeekdayIsHolidayList().contains(sdf.format(calendar.getTime()));
        } else {
            //周末
            return getWeekendIsWorkDateList().contains(sdf.format(calendar.getTime()));
        }
    }

    /**
     * @return 周末是工作日的列表
     * @title 获取周六和周日是工作日的情况（手工维护）
     * 注意，日期必须写全：
     * 2009-1-4必须写成：2009-01-04
     * @author chanson
     */
    public List getWeekendIsWorkDateList() {
        List list = new ArrayList();
        list.add("2009-01-04");
        list.add("2009-01-24");
        list.add("2009-02-01");
        list.add("2009-05-31");
        list.add("2009-09-27");
        list.add("2009-10-10");
        return list;
    }

    /**
     * @return 平时是假期的列表
     * @title 获取周一到周五是假期的情况（手工维护）
     * 注意，日期必须写全：
     * 2009-1-4必须写成：2009-01-04
     * @author chanson
     */
    public List getWeekdayIsHolidayList() {
        List list = new ArrayList();
        list.add("2009-01-29");
        list.add("2009-01-30");
        list.add("2009-04-06");
        list.add("2009-05-01");
        list.add("2009-05-28");
        list.add("2009-05-29");
        list.add("2009-10-01");
        list.add("2009-10-02");
        list.add("2009-10-05");
        list.add("2009-10-06");
        list.add("2009-10-07");
        list.add("2009-10-08");
        return list;
    }

    public static void main(String[] args) {

        WeekdayUtil dateUtils = new WeekdayUtil();
        boolean ok = dateUtils.compareWeekday("2009-10-1", "2009-10-15", 5);
        System.out.println("是否在五个工作日内：" + ok);
    }
}