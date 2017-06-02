package saiyi.com.xiande.utils;

import java.util.Calendar;

/**
 * @author EC
 */
public class TimeFormatUtils {

    /**
     * 获取当前是上午还是下午
     *
     * @return
     */
    public static int getAmOrPm() {
        Calendar nowCalendar = Calendar.getInstance();

        int r = nowCalendar.get(Calendar.AM_PM);//询上午还是下午

        return r;
    }

}
