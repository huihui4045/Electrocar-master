package saiyi.com.xiande.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import saiyi.com.xiande.R;
import saiyi.com.xiande.base.BaseActivity;
import saiyi.com.xiande.base.BaseApplication;
import saiyi.com.xiande.data.MyData;

/**
 * 退款申请成功
 */
public class AcRefundOK extends BaseActivity implements View.OnClickListener {
    private MyData mMyData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo_submit);
        mMyData = BaseApplication.getInstance().getMyData();

        ((TextView)findViewById(R.id.tvTitle)).setText("退押金");
        findViewById(R.id.loLeft).setOnClickListener(this);
        findViewById(R.id.btnOK).setOnClickListener(this);
        //((TextView)findViewById(R.id.tvTitle)).setText("退押金");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOK:
                finish();
                break;
            case R.id.loLeft:
                finish();
                break;
        }
    }
}
