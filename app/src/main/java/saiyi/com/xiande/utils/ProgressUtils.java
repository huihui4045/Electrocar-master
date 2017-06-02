package saiyi.com.xiande.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import saiyi.com.xiande.R;
import saiyi.com.xiande.base.ICancelRequestCallBack;

/**
 * 文件描述：任何简单进度弹出框
 * 创建作者：黎丝军
 * 创建时间：16/8/8 PM12:19
 */
public class ProgressUtils {

    //弹出框
    private static Dialog dialog = null;
    //视图
    private static View dialogView = null;

    private ProgressUtils() {
    }

    /**
     * 显示弹出框
     */
    public static void showDialog(Context context) {
        showDialog(context,null,false,0,null);
    }

    /**
     * 显示弹出框
     */
    public static void showDialog(Context context, String info,ICancelRequestCallBack callBack) {
        showDialog(context,info,false,0,callBack);
    }

    /**
     * 显示弹出框
     */
    public static void showDialog(Context context, int resId,ICancelRequestCallBack callBack) {
        showDialog(context,context.getString(resId),false,0,callBack);
    }

    /**
     * 显示弹出框
     * @param context 运行环境
     * @param cancelable 点击返回是否能被取消
     */
    public static void showDialog(Context context, boolean cancelable,ICancelRequestCallBack callBack) {
        showDialog(context,null,cancelable,0,callBack);
    }

    /**
     * 显示弹出框
     * @param message 提示信息
     */
    public static void showDialog(Context context, String message, boolean cancelable,ICancelRequestCallBack callBack) {
        showDialog(context,message,cancelable,0,callBack);
    }

    /**
     * 显示弹出框
     * @param message 提示信息
     */
    public static void showDialog(Context context, String message, final boolean cancelable, int resBgId, final ICancelRequestCallBack callBack) {
        dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_progress, null);
        if(resBgId > 0) dialogView.setBackgroundDrawable(context.getResources().getDrawable(resBgId));
        final TextView tvMessage = (TextView) dialogView.findViewById(R.id.tv_dialog_msg);
        if (!TextUtils.isEmpty(message)) {
            tvMessage.setText(message);
        } else {
        }
        dialog = new AlertDialog.Builder(context).create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(cancelable);
        if(callBack != null) {
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        callBack.onCancel();
                        dismissDialog();
                        return true;
                    }
                    return false;
                }
            });
        }
        dialog.show();
        dialog.setContentView(dialogView);
    }

    /**
     * 取消弹出框
     */
    public static void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
