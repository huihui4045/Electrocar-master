package saiyi.com.xiande.base;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import saiyi.com.xiande.R;
import saiyi.com.xiande.activity.AcBike;

/**
 * 描述：时间选择器弹出框
 * 创建作者：黎丝军
 * 创建时间：2017/1/4 17:48
 */

public class PickerWindow {

    //保存选中的时间
    private String mSelectTime;
    //取消按钮
    private TextView mCancelBtn;
    //确认按钮
    private TextView mOkBtn;
    //时间选择器
    private PickerView mTimeSelectPv;
    //弹出窗体
    private PopupWindow mPopupWindow;
    //时间选择器
    private OnClickOkListener mListener;
    //运行环境
    private Context mContext;

    public PickerWindow(Context context) {
        mContext = context;
        final View contentView = LayoutInflater.from(context).inflate(R.layout.window_school_selected_view,null);
        initView(contentView);
    }

    /**
     * 初始化内容视图控件
     * @param contentView 内容视图
     */
    private void initView(View contentView) {
        mCancelBtn = (TextView) contentView.findViewById(R.id.tv_cancel);
        mOkBtn = (TextView) contentView.findViewById(R.id.tv_ok);
        mTimeSelectPv = (PickerView) contentView.findViewById(R.id.pv_time);

        mCancelBtn.setOnClickListener(mCommonListener);
        mOkBtn.setOnClickListener(mCommonListener);
        setPickerViewListener();

        initTimeData();

        mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setFocusable(false);
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    //初始化时间数据
    private void initTimeData() {
        final List<String> mSchools = new ArrayList<>();
        Http.getSchoolList(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 1) {
                    final JSONArray jsonArray = (JSONArray) msg.obj;
                    JSONObject jsonObject;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.optJSONObject(i);
                        mSchools.add(jsonObject.optString("name"));
                    }
                    if(!mSchools.isEmpty()) {
                        mTimeSelectPv.setData(mSchools);
                        mTimeSelectPv.setSelected(0);
                    }
                } else {
                    Log.d("LiSiJun","获取学校数据失败");
                }
            }
        });
    }

    //公用的点击监听
    private View.OnClickListener mCommonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.tv_ok) {
                if(mListener != null) {
                    mListener.onClickOk(mSelectTime);
                }
            }
            mPopupWindow.dismiss();
        }
    };

    /**
     * 设置时间选择器监听
     */
    private void setPickerViewListener() {
        mTimeSelectPv.setOnSelectListener(new PickerView.OnSelectListener() {
            @Override
            public void onSelect(String text) {
                mSelectTime = text;
            }
        });
    }

    /**
     * 弹出window
     * @param dropView 弹出window
     */
    public void popWindow(View dropView) {
        final InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(dropView.getWindowToken(), 0);
        mPopupWindow.showAtLocation(dropView, Gravity.BOTTOM,0,0);
    }

    /**
     * 设置点击完成的监听器
     * @param listener 监听实例
     */
    public void setOnClickOkListener(OnClickOkListener listener) {
        mListener = listener;
    }

    /**
     * OK监听器
     */
    public interface OnClickOkListener{
        /**
         * 点击Ok处理监听方法
         * @param text 文本
         */
        void onClickOk(String text);
    }
}
