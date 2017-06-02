package saiyi.com.xiande.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import saiyi.com.xiande.R;
import saiyi.com.xiande.base.BaseActivity;


public class AcAgreement extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);

        ((TextView)findViewById(R.id.tvTitle)).setText("e骑租车协议");
        findViewById(R.id.loLeft).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loLeft:
                finish();
                break;
        }
    }
}
