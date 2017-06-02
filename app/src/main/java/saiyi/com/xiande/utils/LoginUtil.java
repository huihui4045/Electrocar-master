package saiyi.com.xiande.utils;
/**
 * 描述：登录工具类
 * 创建作者：黎丝军
 * 创建时间：2017/1/14 10:25
 */

public class LoginUtil {

    //保存用户登录手机号
    private final static String PHONE = "phone";
    //登录类型
    private final static String LOGIN_TYPE = "loginType";
    //商户Id
    private final static String USER_ID = "userId";

    private LoginUtil() {
    }

    /**
     * 保存登录
     * @param account
     */
    public static void setLogin(String account,int type) {
        if(type == 1) {
            saiyi.com.xiande.PreferencesUtils.putString(PHONE,account);
        } else {
            saiyi.com.xiande.PreferencesUtils.putString(USER_ID,account);
        }
        saiyi.com.xiande.PreferencesUtils.putInt(LOGIN_TYPE,type);
    }

    /**
     * 获取手机用户
     * @return 手机用户
     */
    public static String getPhone() {
        return saiyi.com.xiande.PreferencesUtils.getString(PHONE);
    }

    /**
     * 获取商家登录Id
     * @return id
     */
    public static String getSellerId() {
        return saiyi.com.xiande.PreferencesUtils.getString(USER_ID);
    }

    /**
     * 获取登录类型
     * @return 1为学生登录，2为商家登录
     */
    public static int getLoginType() {
        return saiyi.com.xiande.PreferencesUtils.getInt(LOGIN_TYPE);
    }

    /**
     * 清除登录信息
     */
    public static void clearLoginInfo() {
        saiyi.com.xiande.PreferencesUtils.remove(PHONE);
        saiyi.com.xiande.PreferencesUtils.remove(LOGIN_TYPE);
        saiyi.com.xiande.PreferencesUtils.remove(USER_ID);
    }
}
