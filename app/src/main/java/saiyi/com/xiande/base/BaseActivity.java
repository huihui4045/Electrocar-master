package saiyi.com.xiande.base;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.Toast;

import saiyi.com.xiande.AppHelper;

/**
 * 基类Activity
 *
 * @author EC
 */
public class BaseActivity extends AppCompatActivity {
//    private static final String TAG = BaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppHelper.instance().putActivity(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        AppHelper.instance().removeActivity(this);
        super.onDestroy();
    }

    /**
     * Toast LENGTH_SHORT
     *
     * @param pResId R.string.xxx
     */
    protected void showShortToast(int pResId) {
        showShortToast(getString(pResId));
    }

    /**
     * Toast LENGTH_SHORT
     *
     * @param pMsg String
     */
    protected void showShortToast(String pMsg) {
        Toast.makeText(getApplicationContext(), pMsg, Toast.LENGTH_SHORT)
                .show();

    }

    /**
     * Toast LENGTH_SHORT
     *
     * @param pResId R.string.xxx
     */
    protected void showShortToastInThread(int pResId) {
        showShortToastInThread(getString(pResId));
    }

    /**
     * Toast LENGTH_SHORT
     *
     * @param pMsg String
     */
    protected void showShortToastInThread(final String pMsg) {
        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), pMsg,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Toast LENGTH_LONG
     *
     * @param pMsg String
     */
    protected void showLongToast(String pMsg) {
        Toast.makeText(getApplicationContext(), pMsg, Toast.LENGTH_LONG).show();
    }

    /**
     * 跳轉到Activity
     *
     * @param pClass
     */
    protected void openActivity(Class<?> pClass) {
        openActivity(pClass, null);

    }

    /**
     * 跳轉到Activity,可以傳遞Bundle
     *
     * @param pClass
     * @param pBundle
     */
    protected void openActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }

    /**
     * 跳轉到Activity,Action
     *
     * @param pAction
     */
    protected void openActivity(String pAction) {
        openActivity(pAction, null);
    }

    /**
     * 跳轉到Activity,Action,可以傳遞Bundle
     *
     * @param pAction
     * @param pBundle
     */
    protected void openActivity(String pAction, Bundle pBundle) {
        Intent intent = new Intent(pAction);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }

    @SuppressLint("NewApi")
    public void recreateActivity() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    finish();
                    startActivity(getIntent());
                } else {
                    recreate();
                }
            }
        }, 1);
    }

}
