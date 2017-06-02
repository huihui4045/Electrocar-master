package saiyi.com.xiande.base;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.WindowManager;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import saiyi.com.xiande.utils.StringUtil;

/**
 * 定义我的一些公用方法
 */
public class MyMethod {
    /**
     * 向Handler中发送消息
     * @param handler 用于接收欲发送的消息的Handler
     * @param what    随消息一起发送的int型数据
     * @return  true:发送成功,false:参数handler为null或发送失败
     */
    public static boolean sendMsgToHandler(Handler handler, int what) {
        if (handler == null) {
            return false;
        } else {
            handler.sendEmptyMessage(what);
            return true;
        }
    }

    /**
     * 向Handler中发送消息
     * @param handler 用于接收欲发送的消息的Handler
     * @param obj     随消息一起发送的Object类的实例
     * @param what    随消息一起发送的int型数据
     * @return  true:发送成功,false:参数handler为null或发送失败
     */
    public static boolean sendMsgToHandler(Handler handler, Object obj, int what) {
        if (handler == null) {
            return false;
        }
        android.os.Message msg = handler.obtainMessage();
        if (msg != null) {
            if (obj != null) {
                msg.obj = obj;
            }
            msg.what = what;
            handler.sendMessage(msg);
            return true;
        }
        return false;
    }

    /**
     * 向Handler中发送消息
     * @param handler 用于接收欲发送的消息的Handler
     * @param what    随消息一起发送的int型数据
     * @return  true:发送成功,false:参数handler为null或发送失败
     */
    public static boolean sendMsgToHandlerDelayed(Handler handler, int what, long delayMillis) {
        if (handler == null) {
            return false;
        } else {
            handler.sendEmptyMessageDelayed(what, delayMillis);
            return true;
        }
    }

    /**
     * 向Handler中发送消息
     * @param handler 用于接收欲发送的消息的Handler
     * @param obj     随消息一起发送的Object类的实例
     * @param what    随消息一起发送的int型数据
     * @return  true:发送成功,false:参数handler为null或发送失败
     */
    public static boolean sendMsgToHandlerDelayed(Handler handler, Object obj, int what, long delayMillis) {
        if (handler == null) {
            return false;
        }
        android.os.Message msg = handler.obtainMessage();
        if (msg != null) {
            if (obj != null) {
                msg.obj = obj;
            }
            msg.what = what;
            handler.sendMessageDelayed(msg, delayMillis);
            return true;
        }
        return false;
    }

    // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
    private final static String DateFormatStringArr[] = {
            "yyyy/MM/dd HH:mm:ss",  "yyyy/MM/dd HH:mm",     "yyyy/MM/dd",
            "yyyy-MM-dd HH:mm:ss",  "yyyy-MM-dd HH:mm",     "yyyy-MM-dd",
            "yyyy年MM月dd日 HH时mm分ss秒",  "yyyy年MM月dd日 HH时mm分",     "yyyy年MM月dd日",
            "yyyyMMdd HH:mm:ss",    "yyyyMMdd HH:mm",       "yyyyMMdd",
            "yy/MM/dd",
    };
    /**
     * 把一个字符串转换成一个Date对象
     * @param str 字符串
     * @return Date对象; null:字符串格式不对
     */
    public static Date getDateFromString(String str) {
        if(str==null || str.isEmpty())
            return null;

        for(String formatString:DateFormatStringArr){
            SimpleDateFormat format = new SimpleDateFormat(formatString);
            format.setLenient(false);// 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            try {
                Date date = format.parse(str);
                if(date != null){
                    return date;
                }
            } catch (ParseException e) {
                //e.printStackTrace();
            }
        }
        try {//毫秒数
            return new Date(Long.parseLong(str));
        }catch (Exception e){
        }

        return null;
    }

    /**
     * 把一个Date对象转换成一个"yyyy/MM/dd HH:mm:ss"格式的字符串
     * @param date 字符串
     * @return "yyyy/MM/dd HH:mm:ss"格式的字符串; null:date为null
     */
    public static String DateToString(Date date)
    {
        if(date == null){
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat(DateFormatStringArr[0]);
        return df.format(date);
    }

    //把一个Date对象转换成一个"yy/MM/dd"格式的字符串
    public static String DateToString_yymmdd(Date date)
    {
        if(date == null){
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat("yy/MM/dd");
        return df.format(date);
    }

    //把一个Date对象转换成一个"MM/dd"格式的字符串
    public static String DateToString_mmdd(Date date)
    {
        if(date == null){
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat("MM/dd");
        return df.format(date);
    }

    //把一个Date对象转换成一个"MM/dd hh:mm"格式的字符串
    public static String DateToString_mmddhhmm(Date date)
    {
        if(date == null){
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat("MM/dd HH:mm");
        return df.format(date);
    }

    /**
     * 把 String 转成小写，主要是为了避免抛出空指针异常
     * @param string String
     * @return 转换后的String
     */
    public static String toLowercase(String string){
        if(string!=null && !string.isEmpty()){
            return string.toLowerCase();
        }else{
            return string;
        }
    }

    /**
     * 判断 str 是否是空的，null 和 空字符串都算空
     */
    public static boolean isEmpty(String str){
        return (str==null || str.isEmpty());
    }

    /**
     * 判断 str 是否是空的，null 和 空字符串都算空 "null"串也算空
     */
    public static boolean isEmpty_null(String str){
        return (str==null || str.isEmpty() || "null".equals(str));
    }

    /**
     * 把 String 列表中的 String 全部转成小写
     * @param list String 列表
     * @return 转换后的列表
     */
    public static List<String> toLowercase(List<String> list){
        if(list!=null && !list.isEmpty()){
            List<String> lowerList = new ArrayList<>(list.size());
            for(String str:list){
                lowerList.add(str.toLowerCase());
            }
            return lowerList;
        }else{
            return list;
        }
    }

    /**
     * 判断一个字符串是否为纯数字
     */
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    /**
     * 判断一个字符串是否为手机号码（中国大陆普通用户手机号）
     */
    public static boolean isCellPhone(String str){
        if(isEmpty(str)){
            return false;
        }else{
            return str.matches("^(13|15|18)\\d{9}$");
        }
    }

    /**
     * 判断一个字符串是否为车辆的二维码
     */
    public static boolean isBikeEanCode(String str){
        if(isEmpty(str)){
            return false;
        }else{
            return str.matches("^\\d{15}$");//15位纯数字
        }
    }

    /**
     * 将字符串按分隔符转换成字符串列表
     * @param source 要分隔的字符串
     * @param divisionChar 分隔符
     * @return  分割好的字符串列表,或空列表
     */
    public static List<String> StringToList(String source,String divisionChar){
        List<String> ret = new ArrayList<>();
        if(source==null || source.isEmpty())
            return ret;
        String[] arr = source.split(divisionChar);
        return java.util.Arrays.asList(arr);
    }

    /**
     * 将字符串列表按分隔符组成一个字符串
     * @param list 字符串列表
     * @param divisionChar 分隔符
     * @return  组成的字符串,或空字符串
     */
    public static String ListToString(List<String> list,String divisionChar){
        String ret = "";
        if(list != null){
            for (String str:list) {
                ret += str + divisionChar;
            }
        }
        return ret;
    }

    /**
     * list 去重，（注意，这个方法会改变输入的 list）
     * @param list 输入的列表
     * @return 去掉重复元素后的列表
     */
    public static List removeDuplicate(List list) {
        HashSet hashSet = new HashSet(list);
        try {
            list.clear();
        }catch (UnsupportedOperationException e){
            return list;
        }
        list.addAll(hashSet);
        return list;
    }

    /**
     * 根据输入的列表，返回一个没有重复元素的列表（输入的列表不会被改变）
     * @param list 输入的列表
     * @return 没有重复元素的列表
     */
    public static List getUniqueList(List list) {
        List uList = new ArrayList();
        for(Object object:list){
            if(!uList.contains(object)){
                uList.add(object);
            }
        }
        return uList;
    }

    public static int parseInt(String string){
        int i = 0;
        try{i = Integer.parseInt(string);}catch (NumberFormatException ignored){}
        return i;
    }

    public static double parseDouble(String string){
        double d = 0;
        try{d = Double.parseDouble(string);}catch (NumberFormatException ignored){}
        return d;
    }

    public static long parseLong(String string){
        long l = 0;
        try{l = Long.parseLong(string);}catch (NumberFormatException ignored){}
        return l;
    }

    public static String sexToString(int sex){
        if(sex == 1){
            return "男";
        }else{
            return "女";
        }
    }

    public static int stringToSex(String str){
        if(!isEmpty(str) && str.equals("男")){
            return 1;
        }else{
            return 0;
        }
    }

    //根据uri获取图片文件路径
    public static String getRealFilePath( final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String filePath = null;
        if ( scheme == null )
            filePath = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            filePath = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        filePath = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return filePath;
    }

    //距离转换为距离，返回如 402米，1.2km ,dis 单位为米
    public static String disToString(double dis){
        int m = (int)dis;
        if(dis < 0) {
            return "0米";
        }else if(dis < 1000){
            return m+"米";
        }else{
            int k = m/1000;
            int h = (m/100)%10;
            return k+"."+h+"公里";
        }
    }

    //取文件名的后缀
    public static String getSuffix(File file){
        String fileName = file.getName();
        return fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
    }

    //取文件名的后缀
    public static String getSuffix(String fileName){
        return fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
    }

    //是否符合密码格式：6~20位字符串
    public static boolean isPwdFormatOK(String pwd){
        if(StringUtil.isEmpty(pwd) || pwd.length()<6 || pwd.length()>20){
            return false;
        }else{
            return true;
        }
    }

    //Character.isLetter 把中文也算做字母，只好自己写一个了
    public static boolean isLetter(char c){
        if((c>='a' && c<='z') || (c>='A' && c<='Z')){
            return true;
        }else{
            return false;
        }
    }

    //是否符合用户名格式：4~20位字符串(只能字母或数字)
    public static boolean isUserNameFormatOK(String user){
        if(StringUtil.isEmpty(user) || user.length()<4 || user.length()>20){
            return false;
        }else{
            for(char c:user.toCharArray()){
                if(!(Character.isDigit(c) || isLetter(c))){
                    return false;
                }
            }
            return true;
        }
    }

    //Bitmap 尺寸压缩
    public static Bitmap scaleBitmap(Bitmap src, float scale) {
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, false);
    }

    //闲得的压缩图片，压到手机屏幕宽高的一半，并把原图释放
    public static Bitmap xiandeCompress(Bitmap src,Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth()/2;
        int height = wm.getDefaultDisplay().getHeight()/2;
        float sx = ((float)width)/(float)(src.getWidth());
        float sy = ((float)height)/(float)(src.getHeight());
        float s = Math.min(sx,sy);

        if(s < 1.0){
            Bitmap bitmap = scaleBitmap(src,s);
            src.recycle();
            return bitmap;
        }else{
            return src;
        }
    }
}
