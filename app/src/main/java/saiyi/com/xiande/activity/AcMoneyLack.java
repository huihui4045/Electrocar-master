package saiyi.com.xiande.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import saiyi.com.xiande.R;
import saiyi.com.xiande.base.BaseActivity;
import saiyi.com.xiande.base.BaseApplication;
import saiyi.com.xiande.data.MyData;

/**
 * 余额不足
 */
public class AcMoneyLack extends BaseActivity implements View.OnClickListener {
    private MyData mMyData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lackmoney);
        mMyData = BaseApplication.getInstance().getMyData();

        ((TextView)findViewById(R.id.tvTitle)).setText("余额不足");
        findViewById(R.id.loLeft).setOnClickListener(this);

        findViewById(R.id.loRecharge).setOnClickListener(this);
        findViewById(R.id.tvDepositOpr).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loLeft://返回
                finish();
                break;
            case R.id.loRecharge:
                startActivity(new Intent(this, AcReCharge.class));
                break;
            case R.id.tvDepositOpr:
                showRefoundTip();
                break;
        }
    }

    private void showRefoundTip(){
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(AcMoneyLack.this);
        normalDialog.setTitle("提示");
        normalDialog.setMessage("押金退还时间为7个工作日内，在此期间您的账号无法用车，是否继续退押金？");
        normalDialog.setPositiveButton("取消", null);
        normalDialog.setNegativeButton("退押金", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(AcMoneyLack.this,AcRefundOK.class));
            }
        });
        normalDialog.show();
    }
}