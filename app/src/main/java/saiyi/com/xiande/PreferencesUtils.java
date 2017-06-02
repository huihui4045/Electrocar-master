package saiyi.com.xiande;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.ref.WeakReference;
import java.util.Set;

/**
 * 文件描述：该工具是专为Preferences文件的写入和获取的类
 * 创建作者：黎丝军
 * 创建时间：16/7/28
 */
public final class PreferencesUtils {

    //存储文件名
    private static final String PREFERENCE_NAME = "preferences_file";
    //运行环境
    private static WeakReference<Context> mContext = null;

    private  PreferencesUtils() {
    }

    /**
     * 初始化运行环境
     * @param context 运行
     */
    public static void initContext(Context context) {
        mContext = new WeakReference<Context>(context);
    }

    /**
     * 获取运行环境
     * @return Context
     */
    public static Context getContent() {
        return mContext.get();
    }

    /**
     * 写string值
     *
     * @param key     待写入的key
     * @param value   新值
     * @return 写入成功返回true，否则返回false
     */
    public static boolean putString(String key, String value) {
        SharedPreferences settings = getContent().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    /**
     * 读string值
     *
     * @param key     待读值的key
     * @return 存在返回值，否则返回null，如果不是String类型抛出ClassCastException
     */
    public static String getString(String key) {
        return getString(key, null);
    }

    /**
     * 获取string值
     *
     * @param key          待读值的key
     * @param defaultValue 该值不存在，返回返回该值
     * @return 存在返回值，否则返回defaultValue，如果不是String类型抛出ClassCastException
     */
    public static String getString(String key, String defaultValue) {
        SharedPreferences settings = getContent().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getString(key, defaultValue);
    }

    /**
     * 写int值
     *
     * @param key     待写入的key
     * @param value   新值
     * @return 写入成功返回true，否则返回false
     */
    public static boolean putInt(String key, int value) {
        SharedPreferences settings = getContent().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    /**
     * 获取int值
     * @param key     待读取的key
     * @return 存在返回值，否则返回-1，如果不是int类型抛出ClassCastException
     */
    public static int getInt(String key) {
        return getInt(key, -1);
    }

    /**
     * 获取int值
     * @param key          待读值的key
     * @param defaultValue 该值不存在，返回返回该值
     * @return 存在返回值，否则返回defaultValue，如果不是int类型抛出ClassCastException
     */
    public static int getInt(String key, int defaultValue) {
        SharedPreferences settings = getContent().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getInt(key, defaultValue);
    }

    /**
     * 写long值
     * @param key     待写入的key
     * @param value   新值
     * @return 写入成功返回true，否则返回false
     */
    public static boolean putLong(String key, long value) {
        SharedPreferences settings = getContent().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    /**
     * 获取long值
     * @param key     待读取的key
     * @return 存在返回值，否则返回-1，如果不是long类型抛出ClassCastException
     */
    public static long getLong(String key) {
        return getLong(key, -1);
    }

    /**
     * 读long值
     * @param key          待读值的key
     * @param defaultValue 该值不存在，返回返回该值
     * @return 存在返回值，否则返回defaultValue，如果不是long类型抛出ClassCastException
     */
    public static long getLong(String key, long defaultValue) {
        SharedPreferences settings = getContent().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getLong(key, defaultValue);
    }

    /**
     * 写float值
     * @param key     待写入的key
     * @param value   新值
     * @return 写入成功返回true，否则返回false
     */
    public static boolean putFloat( String key, float value) {
        SharedPreferences settings = getContent().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(key, value);
        return editor.commit();
    }

    /**
     * 获取float值
     * @param key     待读取的key
     * @return 存在返回值，否则返回-1，如果不是float类型抛出ClassCastException
     */
    public static float getFloat(String key) {
        return getFloat(key, -1);
    }

    /**
     * 读float值
     * @param key          待读值的key
     * @param defaultValue 该值不存在，返回返回该值
     * @return 存在返回值，否则返回defaultValue，如果不是float类型抛出ClassCastException
     */
    public static float getFloat(String key, float defaultValue) {
        SharedPreferences settings = getContent().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getFloat(key, defaultValue);
    }

    /**
     * 写boolean值
     * @param key     待写入的key
     * @param value   新值
     * @return 写入成功返回true，否则返回false
     */
    public static boolean putBoolean(String key, boolean value) {
        SharedPreferences settings = getContent().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    /**
     * 写Set值
     * @param key     待写入的key
     * @param value   新值
     * @return 写入成功返回true，否则返回false
     */
    public static boolean putStringSet(String key, Set<String> value) {
        SharedPreferences settings = getContent().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putStringSet(key, value);
        return editor.commit();
    }

    /**
     * 获取Set值
     * @param key     待读取的key
     * @return Set<String>
     */
    public static Set<String> getStringSet(String key) {
        SharedPreferences settings = getContent().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getStringSet(key,null);
    }

    /**
     * 向集合里添加元素
     * @param key 需要添加元素的集合
     * @param value 值
     */
    public static void addSetValue(String key,String value) {
        Set<String> set = PreferencesUtils.getStringSet(key);
        if(set != null && !set.contains(key)) {
            set.add(value);
            putStringSet(key,set);
        }
    }

    /**
     * 移除set集合里的元素
     * @param key 集合键值
     * @param value 值
     */
    public static void removeSetValue(String key,String value) {
        Set<String> set = PreferencesUtils.getStringSet(key);
        if(set != null && set.contains(key)) {
            set.remove(value);
            putStringSet(key,set);
        }
    }

    /**
     * 获取boolean值
     * @param key     待读取的key
     * @return 存在返回值，否则返回-1，如果不是boolean类型抛出ClassCastException
     */
    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    /**
     * 读boolean值
     *
     * @param key          待读值的key
     * @param defaultValue 该值不存在，返回返回该值
     * @return 存在返回值，否则返回defaultValue，如果不是boolean类型抛出ClassCastException
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        SharedPreferences settings = getContent().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getBoolean(key, defaultValue);
    }

    /**
     * 移除某个键值
     * @param key 键值
     */
    public static void remove(String key) {
        SharedPreferences settings = getContent().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(key);
        editor.commit();
    }

    /**
     * 清除所有
     */
    public static void clear() {
        SharedPreferences settings = getContent().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();
    }
}
