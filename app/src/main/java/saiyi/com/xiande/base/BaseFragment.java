package saiyi.com.xiande.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

/**
 * fragment的基类
 * @author EC
 */
public class BaseFragment extends Fragment {
    // private static final String TAG = "BaseFragmentActivity";

    protected View rootView;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rootView = view;
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
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
        Toast.makeText(getActivity().getApplicationContext(), pMsg, Toast.LENGTH_SHORT).show();
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
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(getActivity().getApplicationContext(), pMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Toast LENGTH_LONG
     *
     * @param pMsg String
     */
    protected void showLongToast(String pMsg) {
        Toast.makeText(getActivity().getApplicationContext(), pMsg, Toast.LENGTH_LONG).show();
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
        Intent intent = new Intent(getActivity(), pClass);
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

    /**
     *
     */
    protected final View findViewById(int id) {
        return rootView.findViewById(id);
    }

    protected void finish() {
        getActivity().finish();
    }
}
