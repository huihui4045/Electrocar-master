package saiyi.com.xiande.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import saiyi.com.xiande.R;
import saiyi.com.xiande.base.BaseActivity;
import saiyi.com.xiande.base.BaseApplication;
import saiyi.com.xiande.data.MyData;

public class AcRefund extends BaseActivity implements View.OnClickListener {
    private MyData mMyData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refound);
        mMyData = BaseApplication.getInstance().getMyData();

        ((TextView)findViewById(R.id.tvTitle)).setText("我的退款");
        findViewById(R.id.loLeft).setOnClickListener(this);
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
