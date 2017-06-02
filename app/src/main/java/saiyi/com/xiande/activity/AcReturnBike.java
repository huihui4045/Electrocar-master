package saiyi.com.xiande.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import saiyi.com.xiande.R;
import saiyi.com.xiande.base.BaseActivity;
import saiyi.com.xiande.base.BaseApplication;
import saiyi.com.xiande.base.MyMethod;
import saiyi.com.xiande.data.MyData;

public class AcReturnBike extends BaseActivity implements View.OnClickListener {
    private MyData mMyData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_returnbike);
        mMyData = BaseApplication.getInstance().getMyData();

        ((TextView)findViewById(R.id.tvTitle)).setText("还车");
        findViewById(R.id.loLeft).setOnClickListener(this);

        ((TextView)findViewById(R.id.tvTime)).setText(mMyData.mCurOrder.mSpendTime + "分");
        ((TextView)findViewById(R.id.tvMoney)).setText("¥" + mMyData.mCurOrder.mCost);
        ((TextView)findViewById(R.id.tvStartTime)).setText(MyMethod.DateToString(mMyData.mCurOrder.mStartTime));
        ((TextView)findViewById(R.id.tvEndTime)).setText(MyMethod.DateToString(mMyData.mCurOrder.mEndTime));
        ((TextView)findViewById(R.id.tvStartPos)).setText(mMyData.mCurOrder.mStartPos);
        ((TextView)findViewById(R.id.tvEndPos)).setText(mMyData.mCurOrder.mEndPos);

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
