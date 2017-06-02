package saiyi.com.xiande.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * @author ZLJ
 */
public abstract class BaseView {
    protected Context mContext;

    protected View rootView;

    public BaseView(Context mContext) {
        this.mContext = mContext;
        //如果不是Activity的Context，抛异常
        if (!(mContext instanceof Activity)) {
            throw new RuntimeException("Context 不是 Activity!");
        }

    }

    /**
     * 加载布局
     *
     * @param layoutResourseId
     */
    protected void inflateView(int layoutResourseId) {
        rootView = View.inflate(mContext, layoutResourseId, null);
    }

    protected View findViewById(int id) {
        return rootView.findViewById(id);
    }

    protected Context getApplicationContext() {
        return mContext.getApplicationContext();
    }

    public View getRootView() {
        return rootView;
    }


    /**
     * Toast LENGTH_SHORT
     *
     * @param pResId R.string.xxx
     */
    protected void showShortToast(int pResId) {
        showShortToast(mContext.getString(pResId));
    }

    /**
     * Toast LENGTH_SHORT
     *
     * @param pMsg String
     */
    protected void showShortToast(String pMsg) {
        Toast.makeText(mContext.getApplicationContext(), pMsg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Toast LENGTH_SHORT
     *
     * @param pResId R.string.xxx
     */
    protected void showShortToastInThread(int pResId) {
        showShortToastInThread(mContext.getString(pResId));
    }

    /**
     * Toast LENGTH_SHORT
     *
     * @param pMsg String
     */
    protected void showShortToastInThread(final String pMsg) {
        ((Activity) mContext).runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(mContext.getApplicationContext(), pMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Toast LENGTH_LONG
     *
     * @param pMsg String
     */
    protected void showLongToast(String pMsg) {
        Toast.makeText(mContext.getApplicationContext(), pMsg, Toast.LENGTH_LONG).show();
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
        Intent intent = new Intent(mContext, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        mContext.startActivity(intent);
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
        mContext.startActivity(intent);
    }
}
