package saiyi.com.xiande.base;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import saiyi.com.xiande.AppHelper;
import saiyi.com.xiande.activity.Login;
import saiyi.com.xiande.utils.LoginUtil;

/**
 * 一些公用的东西
 */
public class Common {

    public static void showLogoutDialog(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("确定要退出吗?");
        builder.setTitle("退出登录");
        builder.setPositiveButton("确认",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        LoginUtil.clearLoginInfo();
                        AppHelper.instance().finishAllActivity();
                        activity.startActivity(new Intent(activity, Login.class));
                        activity.finish();
                    }
                });
        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    public static AlertDialog showMsgDialog(String msg,final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        AlertDialog dialog;
        builder.setMessage(msg);
        builder.setPositiveButton("确定",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog = builder.create();
        dialog.show();
        return dialog;
    }
}
