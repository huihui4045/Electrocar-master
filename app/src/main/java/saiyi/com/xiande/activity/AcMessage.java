package saiyi.com.xiande.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import saiyi.com.xiande.R;
import saiyi.com.xiande.base.BaseActivity;
import saiyi.com.xiande.base.BaseApplication;
import saiyi.com.xiande.base.MyMethod;
import saiyi.com.xiande.data.DMessage;
import saiyi.com.xiande.data.MyData;

/**
 * 单条消息
 */
public class AcMessage extends BaseActivity implements View.OnClickListener{
    private MyData mMyData;
    private DMessage mCurMsg;
    private TextView mTv;
    private LinearLayout mLoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        mMyData = BaseApplication.getInstance().getMyData();

        mCurMsg = mMyData.mMsgList.get(getIntent().getIntExtra("index", 0));

        ((TextView)findViewById(R.id.tvTitle)).setText("消息中心");
        findViewById(R.id.loLeft).setOnClickListener(this);
        ((TextView) findViewById(R.id.tvTime)).setText(MyMethod.DateToString_mmddhhmm(mCurMsg.mMsgTime));
        mTv = (TextView)findViewById(R.id.tvText);
        mLoText = (LinearLayout)findViewById(R.id.loText);
        mTv.setText(mCurMsg.mMsgContent);

//        mTv.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                int h = mTv.getHeight();
//                RelativeLayout.LayoutParams Params = (RelativeLayout.LayoutParams) mLoText.getLayoutParams();
//                Params.height = h + 100;
//                mLoText.setLayoutParams(Params);
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loLeft://返回
                finish();
                break;
        }
    }
}
