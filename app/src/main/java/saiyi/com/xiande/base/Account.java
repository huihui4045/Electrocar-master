package saiyi.com.xiande.base;

import android.content.Context;

/**
 * 账户资料和方法，单例
 */
public class Account {
    private static Account instance;
    private final Context mContext;

    public Account(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static Account getInstance(Context context) {
        if (instance == null) {
            synchronized (Account.class) {
                if (instance == null) {
                    instance = new Account(context);
                }
            }
        }
        return instance;
    }
}
