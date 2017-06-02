package saiyi.com.xiande.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.location.AMapLocationClient;
import com.pgyersdk.crash.PgyCrashManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import saiyi.com.xiande.Map.Location;
import saiyi.com.xiande.PreferencesUtils;
import saiyi.com.xiande.data.MyData;

import static java.security.spec.MGF1ParameterSpec.SHA1;


/**
 * 基类Application
 */
public class BaseApplication extends Application {

    private static BaseApplication instance = null;
    public AMapLocationClient mLocationClient = null;//高德地图定位
    private MyData mMyData = new MyData();

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onCreate() {
        super.onCreate();
        //
        instance = this;
        Log.d("LiSiJun",sHA1(this));
        Log.d("BaseApplication", "你好吗，");
        PreferencesUtils.initContext(this);
        //蒲光英崩溃日志上传注册
        PgyCrashManager.register(this);
//        // 收集异常
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());
        mLocationClient = new AMapLocationClient(this);
        mLocationClient.setLocationOption(Location.getDefaultOption());

         //zhh
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
                TestActivityManager.getInstance().setCurrentActivity(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
    }

    //获取当前网络状态：0：联网，1：未开启联网，2：开启了但没有网
    public int getNetWorkState() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connMgr.getActiveNetworkInfo();
        if (info == null) {//当前没有已激活的网络连接（表示用户关闭了数据流量服务，也没有开启WiFi等别的数据服务）
            return 1;
        } else {//当前有已激活的网络连接，但是否可用还需判断
            boolean isAlive = info.isAvailable();
            if(isAlive){
                return 0;
            }else{
                return 2;
            }
        }
    }

    public static BaseApplication getInstance() {
        return instance;
    }

    public MyData getMyData(){
        return mMyData;
    }

    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
            }
            return hexString.toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
