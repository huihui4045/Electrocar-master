package saiyi.com.xiande.utils;

import android.content.Context;
import android.os.Vibrator;

/**
 * @author ZLJ
 */
public class SystemServiceUtils {
    private static SystemServiceUtils instance;

    private final Context mContext;

    public SystemServiceUtils(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static SystemServiceUtils getInstance(Context context) {
        if (instance == null) {
            synchronized (SystemServiceUtils.class) {
                if (instance == null) {
                    instance = new SystemServiceUtils(context);
                }
            }
        }
        //
        return instance;
    }

    /**
     * 震动一次
     */
    public void vibratorOnce() {
        vibratorOnce(500);
    }

    /**
     * 震动一次
     */
    public void vibratorOnce(int time) {
         /*
         * 想设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
         * */
        Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, time};   // 停止 开启 停止 开启
        vibrator.vibrate(pattern, -1);           //重复两次上面的pattern 如果只想震动一次，index设为-1
    }

}
