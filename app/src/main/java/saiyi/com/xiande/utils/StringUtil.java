package saiyi.com.xiande.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.regex.Pattern;

/**
 * FileUtils 字符串操作
 * create by zhaoqijun
 */

public class StringUtil {

    private final static Pattern emailer = Pattern
            .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

    private final static Pattern phone = Pattern
            .compile("^((13[0-9])|(14[0-9])|17[0-9]|(15[^4,\\D])|(18[0-9]))\\d{8}$");

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    /**
     * 返回当前系统时间
     */
    public static String getDataTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }

    /**
     * 返回当前系统时间
     */
    public static String getDataTime() {
        return getDataTime("HH:mm");
    }

    /**
     * 毫秒值转换为mm:ss
     * @author kymjs
     * @param ms
     */
    public static String timeFormat(int ms) {
        StringBuilder time = new StringBuilder();
        time.delete(0, time.length());
        ms /= 1000;
        int s = ms % 60;
        int min = ms / 60;
        if (min < 10) {
            time.append(0);
        }
        time.append(min).append(":");
        if (s < 10) {
            time.append(0);
        }
        time.append(s);
        return time.toString();
    }

    /**
     * 将字符串转位日期类型
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 判断给定字符串时间是否为今日
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate) {
        boolean b = false;
        Date time = toDate(sdate);
        Date today = new Date();
        if (time != null) {
            String nowDate = dateFormater2.get().format(today);
            String timeDate = dateFormater2.get().format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }

    /**
     * 判断给定字符串是否空白串 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     */
    public static boolean isEmail(String email) {
        if (email == null || email.trim().length() == 0)
            return false;
        return emailer.matcher(email).matches();
    }

    /**
     * 判断是不是一个合法的手机号码
     */
    public static boolean isPhone(String phoneNum) {
        if (phoneNum == null || phoneNum.trim().length() == 0)
            return false;
        return phone.matcher(phoneNum).matches();
    }

    /**
     * 字符串转整数
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 对象转整
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * String转long
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * String转double
     * @param obj
     * @return 转换异常返回 0
     */
    public static double toDouble(String obj) {
        try {
            return Double.parseDouble(obj);
        } catch (Exception e) {
        }
        return 0D;
    }

    /**
     * 字符串转布尔
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 判断一个字符串是不是数字
     */
    public static boolean isNumber(String str) {
        try {
            Integer.parseInt(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 获取AppKey
     */
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {

        }
        return apiKey;
    }

    /**
     * 获取手机IMEI码
     */
    public static String getPhoneIMEI(Activity aty) {
        TelephonyManager tm = (TelephonyManager) aty
                .getSystemService(Activity.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * URL中特殊字符
     */
    public static String escapeSpecialCharForUrlSegments(String src) {
        return src.replace(" ", "%20").replace("[", "%5B").replace("]", "%5D")
                .replace("|", "%7C");
    }

    /**
     * 替换字符串中特殊字符
     */
    public static String encodeString(String strData)

    {
        if (strData == null || strData.equals(""))
        {
            return "";
        }

        return strData.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("'", "&apos;")
                .replaceAll("\"", "&quot;");
    }

    /**
     * 还原字符串中特殊字符
     */
    public static String decodeString(String strData)
    {
        if (strData == null || strData.equals(""))
        {
            return "";
        }

        return strData.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&apos;", "'").replaceAll("&quot;", "\"")
                .replaceAll("&amp;", "&").replaceAll("&ensp;", " ").replaceAll("&emsp;", "  ").replaceAll("&nbsp", " ")
                .replaceAll("&copy", "?").replaceAll("&reg", "?").replaceAll("&times;", "×")
                .replaceAll("&divide;", "÷");
    }


    /**
     * 将字符串str按子字符串separatorChars 分割成数组
     *
     * @param str
     *            要拆分的字符串
     * @param separatorChars
     *            用来拆分的分割字符
     * @return 拆分后的字符串
     */
    public static String[] split2(String str, String separatorChars)
    {
        return splitWorker(str, separatorChars, -1, false);
    }

    /**
     * 拆分字符串
     *
     * @param str
     *            要拆分的字符串
     * @param separatorChars
     *            用来拆分的分割字符
     * @param max
     *            要拆分字符串的最大长度
     * @param preserveAllTokens
     * @return 拆分后的字符串
     */
    private static String[] splitWorker(String str, String separatorChars, int max, boolean preserveAllTokens)
    {
        if (str == null)
        {
            return null;
        }
        int len = str.length();
        if (len == 0)
        {
            return new String[] { "" };
        }
        Vector<String> vector = new Vector<String>();
        int sizePlus1 = 1;
        int i = 0;
        int start = 0;
        boolean match = false;
        boolean lastMatch = false;
        if (separatorChars == null)
        {
            while (i < len)
            {
                if (str.charAt(i) == '\r' || str.charAt(i) == '\n' || str.charAt(i) == '\t')
                {
                    if (match || preserveAllTokens)
                    {
                        lastMatch = true;
                        if (sizePlus1++ == max)
                        {
                            i = len;
                            lastMatch = false;
                        }
                        vector.addElement(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                }
                else
                {
                    lastMatch = false;
                    match = true;
                    i++;
                }
            }
        }
        else if (separatorChars.length() == 1)
        {
            char sep = separatorChars.charAt(0);
            while (i < len)
            {
                if (str.charAt(i) == sep)
                {
                    if (match || preserveAllTokens)
                    {
                        lastMatch = true;
                        if (sizePlus1++ == max)
                        {
                            i = len;
                            lastMatch = false;
                        }
                        vector.addElement(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                }
                else
                {
                    lastMatch = false;
                    match = true;
                    i++;
                }
            }
        }
        else
        {
            while (i < len)
            {
                int id = i + separatorChars.length() < len ? i + separatorChars.length() : len;
                if (separatorChars.indexOf(str.charAt(i)) >= 0 && separatorChars.equals(str.substring(i, id)))
                {
                    if (match || preserveAllTokens)
                    {
                        lastMatch = true;
                        if (sizePlus1++ == max)
                        {
                            i = len;
                            lastMatch = false;
                        }
                        vector.addElement(str.substring(start, i));
                        match = false;
                    }
                    i += separatorChars.length();
                    start = i;
                }
                else
                {
                    lastMatch = false;
                    match = true;
                    i++;
                }
            }
        }

        if (match || preserveAllTokens && lastMatch)
        {
            vector.addElement(str.substring(start, i));
        }
        String[] ret = new String[vector.size()];
        vector.copyInto(ret);
        return ret;
    }

}
