package saiyi.com.xiande.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * 对话框的基类的抽取
 * @author ZLJ
 */
public abstract class BaseDialogView extends BaseView {
    protected final Activity mActivity;

    protected Dialog alertDialog;
    //宽度和高度
    private int mWidth;
    private int mHeight;
    //点击外部是否关闭对话框，默认不关闭
    protected boolean isCancelOnTouchOutside = false;
    //
    protected Integer windowFlag;

    public BaseDialogView(Context mContext) {
        super(mContext);
        this.mActivity = (Activity) mContext;
    }

    protected abstract void initView();

    public Dialog alertView() {

        alertDialog = new Dialog(mContext);
        // 去除对话框 标题
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //必须先setContentView再设置WindowManager.LayoutParams这些，否则无效
        alertDialog.setContentView(rootView);
        //设置点击外面是否关闭对话框
        alertDialog.setCanceledOnTouchOutside(isCancelOnTouchOutside);
        //设置为false，按返回键不能退出。默认为true。
        alertDialog.setCancelable(true);
        //
        Window window = alertDialog.getWindow();

        window.setBackgroundDrawableResource(android.R.color.transparent);
        //
        WindowManager.LayoutParams params = window.getAttributes();
        //
        params.width = mWidth;
        params.height = mHeight;
        if (windowFlag != null) {
            params.flags = windowFlag;
        }

        window.setAttributes(params);
        ///
        alertDialog.show();

        return alertDialog;
    }

    /**
     * 加载布局
     *
     * @param id
     */
    protected void inflateLayout(@LayoutRes int id) {
        ViewGroup containerView = (ViewGroup) View.inflate(mContext, id, new FrameLayout(mContext));
        //addView后，需要找到相应的子View，然后获取getLayoutParams
        View contentView = containerView.getChildAt(0);

        rootView = containerView;
        //获取对话框设置的大小
        ViewGroup.LayoutParams contentViewParams = contentView.getLayoutParams();

        mWidth = contentViewParams.width;
        mHeight = contentViewParams.height;
    }
}
