package saiyi.com.xiande.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;

import org.json.JSONArray;

import saiyi.com.xiande.R;
import saiyi.com.xiande.base.AppConstants;
import saiyi.com.xiande.base.BaseActivity;
import saiyi.com.xiande.base.BaseApplication;
import saiyi.com.xiande.base.Http;
import saiyi.com.xiande.data.DAccount;
import saiyi.com.xiande.data.MyData;

public class AcMoney extends BaseActivity implements View.OnClickListener {
    private TextView mTvMoney;
    private MyData mMyData;
    private SVProgressHUD svProgressHUD;
    private TextView mTvDeposit;
    private TextView mTvDepositOpr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);
        mMyData = BaseApplication.getInstance().getMyData();

        ((TextView)findViewById(R.id.tvTitle)).setText("我的钱包");
        findViewById(R.id.loLeft).setOnClickListener(this);
        mTvMoney = (TextView)findViewById(R.id.tvWarn);
        mTvDeposit = (TextView) findViewById(R.id.tvDeposit);
        mTvDepositOpr = (TextView)findViewById(R.id.tvDepositOpr);

        svProgressHUD = new SVProgressHUD(this);

        TextView mTvRight = (TextView) findViewById(R.id.tvRight);
        mTvRight.setText("明细");
        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setOnClickListener(this);

        findViewById(R.id.loRecharge).setOnClickListener(this);
        findViewById(R.id.tvDepositOpr).setOnClickListener(this);

        showAccount(false);
    }

    @Override
    public void onStart(){
        super.onStart();

        svProgressHUD.show();
        Http.getAccount(mMyData.mUserId, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                svProgressHUD.dismiss();
                if (msg.what == 1) {
                    if(msg.obj != null) {
                        mMyData.mAccount = DAccount.fromJSONArray((JSONArray) msg.obj).get(0);
                        showAccount(false);
                    }
                }else{
                    Toast.makeText(AcMoney.this, "账户余额查询失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showAccount(boolean isShowDialog){
        if (mMyData.mAccount != null) {
            mTvMoney.setText(mMyData.mAccount.mBalance + AppConstants.MoneyTag);
            if (mMyData.mAccount.mDeposit <= 0) {
                if(isShowDialog) {
                    showNormalDialog();//押金不够，提示
                }
                mTvDepositOpr.setText("充押金");
                mTvDeposit.setText("押金0元");
            }else{
                mTvDepositOpr.setText("退押金");
                mTvDeposit.setText("押金" + (int) (mMyData.mDict.mDeposit) + "元");
            }
            mTvMoney.setText(mMyData.mAccount.mBalance + AppConstants.MoneyTag);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loLeft://返回
                finish();
                break;
            case R.id.tvRight:
                startActivity(new Intent(this, AcPayRecord.class));
                break;
            case R.id.loRecharge:
                if (mMyData.mAccount.mDeposit <= 0){
                    startActivity(new Intent(this,AcRechargeDeposit.class));
                }else {
                    startActivity(new Intent(this, AcReCharge.class));
                }
                break;
            case R.id.tvDepositOpr:
                if(mMyData.mAccount.mDeposit <= 0) {
                    startActivity(new Intent(this,AcRechargeDeposit.class));
                }else {
                    showRefoundTip();
                }
                break;
        }
    }

    private void showNormalDialog(){
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(AcMoney.this);
        normalDialog.setTitle("提示");
        normalDialog.setMessage("使用闲得智能电动车需要支付199元押金，押金可退还");
        normalDialog.setPositiveButton("充押金", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(AcMoney.this,AcRechargeDeposit.class));
            }
        });
        normalDialog.setNegativeButton("去充值", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(AcMoney.this,AcReCharge.class));
            }
        });
        normalDialog.show();
    }

    private void showRefoundTip(){
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(AcMoney.this);
        normalDialog.setTitle("提示");
        normalDialog.setMessage("押金退还时间为7个工作日内，在此期间您的账号无法用车，是否继续退押金？");
        normalDialog.setPositiveButton("取消", null);
        normalDialog.setNegativeButton("退押金", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Http.applyRefund(mMyData.mUserId, new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case 1:
                                startActivity(new Intent(AcMoney.this, AcRefundOK.class));
                                break;
                            case 0:
                                Toast.makeText(AcMoney.this, "申请退押金失败，请稍后再试", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Toast.makeText(AcMoney.this, "您已申请过，请耐心等待", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
            }
        });
        normalDialog.show();
    }
}
