package saiyi.com.xiande.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;

import org.json.JSONArray;

import saiyi.com.xiande.R;
import saiyi.com.xiande.base.BaseActivity;
import saiyi.com.xiande.base.BaseApplication;
import saiyi.com.xiande.base.Common;
import saiyi.com.xiande.base.Http;
import saiyi.com.xiande.data.DBike;
import saiyi.com.xiande.data.DDict;
import saiyi.com.xiande.data.MyData;
import saiyi.com.xiande.utils.DensityUtil;

/**
 * 电动车界面
 */
public class AcBike extends BaseActivity implements View.OnClickListener {
    private boolean mLayoutState_isOpen = true;//界面的状态，蓝色还是灰色，默认是蓝色。false = gray, true = blue
    private Button mBtnLock;//开锁、锁车按钮
    private Button mBtnReturn;//还车按钮
    private MyData mMyData;
    private View mVBattery;//电量显示
    private TextView mTvBattery;
    private SVProgressHUD svProgressHUD;
    private ImageView mBackIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike);
        mMyData = BaseApplication.getInstance().getMyData();

        mBackIv = (ImageView)findViewById(R.id.ivLeft);
        mBackIv.setVisibility(View.GONE);
        ((TextView)findViewById(R.id.tvTitle)).setText(mMyData.mScanBike.mNo);
        mTvBattery = (TextView)findViewById(R.id.tvBattery);

        if(mMyData.mScanBike.mSurplus < 1000) {
            ((TextView) findViewById(R.id.tvDistance)).setText("" + (int) mMyData.mScanBike.mSurplus);
            ((TextView) findViewById(R.id.tvDistance2)).setText("米");
        }else{
            ((TextView) findViewById(R.id.tvDistance)).setText("" + ((int) mMyData.mScanBike.mSurplus/1000));
        }
        mVBattery = findViewById(R.id.vBattery);
        showBattery((int) mMyData.mScanBike.mBattery);

        svProgressHUD = new SVProgressHUD(this);

        mBtnLock = (Button)findViewById(R.id.btnLock);
        mBtnLock.setOnClickListener(this);
        mBtnReturn = (Button)findViewById(R.id.btnReturn);
        mBtnReturn.setOnClickListener(this);
        if(mMyData.mScanBike.mLockStatus == DBike.Status_LOCK) {
            setLayoutState(false);
        }else{
            setLayoutState(true);
        }

        mIsWarnThreadRun = true;
        mWarnThread.start();
    }

    private void showBattery(int persent){
        int full = 24;
        mTvBattery.setText("" + persent);
        RelativeLayout.LayoutParams Params = (RelativeLayout.LayoutParams) mVBattery.getLayoutParams();
        Params.width = DensityUtil.dip2px(this, (full*persent)/100);
        mVBattery.setLayoutParams(Params);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivLeft://返回
                finish();
                break;
            case R.id.btnLock:
                mBtnLock.setEnabled(false);
                svProgressHUD.show();
                final int opr = (mMyData.mScanBike.mLockStatus+1)%2;
                Log.d("LiSiJun","lockStatus=" + opr);
                Log.d("LiSiJun","onClick - mMyData.mScanBike.mLockStatus=" + mMyData.mScanBike.mLockStatus);
                Http.lockBike(mMyData.mScanBike.mEncode,opr, new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what){
                            case 1://成功
                                mCheckLockStatus.start(opr,mCheckFeedbackHandler);
                                break;
                            case 0://失败
                                oprFail();
                                svProgressHUD.dismiss();
                                break;
                        }
                    }
                });
                break;
            case R.id.btnReturn:
                startActivityForResult(new Intent(this, AcReturnBikeMap.class), 2);
                break;
        }
    }

    private final long WarnTimeSpan = 5*60*1000;
    private boolean mIsWarnThreadRun;
    private Thread mWarnThread = new Thread(){
        @Override
        public void run() {
            while (mIsWarnThreadRun){
                if(mMyData.mDict == null){
                    Http.getDict(mGetDictHandler);
                }
                Http.getBike(mMyData.mUserId, mMyData.mScanBike.mEncode, mGetBikeInfoHandler);
                try {
                    sleep(WarnTimeSpan);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private Handler mGetDictHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1 && msg.obj != null) {
                mMyData.mDict = DDict.fromJSONArray((JSONArray) msg.obj).get(0);
                Warn();
            }
        }
    };
    private Handler mGetBikeInfoHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1 && msg.obj != null) {
                mMyData.mScanBike = DBike.fromJSONArray((JSONArray) msg.obj).get(0);
                showBattery((int) mMyData.mScanBike.mBattery);
                Warn();
            }
        }
    };
    private AlertDialog mBatteryWarn = null;
    private AlertDialog mMoneyWarn = null;
    private void Warn(){
        if(mMyData.mDict != null) {
            if (mMyData.mScanBike.mBattery <= mMyData.mDict.mBatteryAlarm) {
                if (mBatteryWarn == null) {
                    mBatteryWarn = Common.showMsgDialog("电动车当前电量不足请及时充电", this);
                }
            }
            if (mMyData.mScanBike.mPrice <= mMyData.mDict.mBalanceAlarm) {
                if (mMoneyWarn != null) {
                    mMoneyWarn.dismiss();
                }
                mMoneyWarn = Common.showMsgDialog("你的当前余额为" + mMyData.mScanBike.mPrice + "元请及时充值", this);
            }
            if (mMyData.mScanBike.mPrice <= (-mMyData.mDict.mOverrunsAlarm)) {
                if (mMoneyWarn != null) {
                    mMoneyWarn.dismiss();
                }
                mMoneyWarn = Common.showMsgDialog("你已超支" + (-mMyData.mScanBike.mPrice) + "元请及时充值", this);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIsWarnThreadRun = false;
        mCheckLockStatus.cancel();
    }

    private CheckLockStatus mCheckLockStatus = new CheckLockStatus();
    private Handler mCheckFeedbackHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == CheckLockStatus.Opr_Success){
                oprSuccess();
            }else{
                oprFail();
            }
        }
    };
    private void oprFail(){
        svProgressHUD.dismiss();
        if(mLayoutState_isOpen){//锁车失败
            Toast.makeText(AcBike.this, "锁车失败", Toast.LENGTH_SHORT).show();
        }else{//开锁失败
            Toast.makeText(AcBike.this, "开锁失败", Toast.LENGTH_SHORT).show();
        }
        mBtnLock.setEnabled(true);
    }
    private void oprSuccess(){
        svProgressHUD.dismiss();
        setLayoutState(!mLayoutState_isOpen);
        mBtnLock.setEnabled(true);
        if(mLayoutState_isOpen) {
            Toast.makeText(AcBike.this, "开锁成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(AcBike.this, "锁车成功", Toast.LENGTH_SHORT).show();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setLayoutState(boolean isBlue){//false = gray, true = blue
        if(isBlue != mLayoutState_isOpen){
            mLayoutState_isOpen = isBlue;
            if(isBlue){
                ((ImageView)findViewById(R.id.ivBike)).setImageResource(R.drawable.set_bike);
                ((ImageView)findViewById(R.id.ivBattery)).setImageResource(R.drawable.electricity);
                ((ImageView)findViewById(R.id.ivDistance)).setImageResource(R.drawable.distance);

                ((TextView)findViewById(R.id.tvBattery)).setTextColor(getResources().getColor(R.color.FontBlue));
                ((TextView)findViewById(R.id.tvBattery2)).setTextColor(getResources().getColor(R.color.FontBlue));
                ((TextView)findViewById(R.id.tvDistance)).setTextColor(getResources().getColor(R.color.FontBlue));
                ((TextView)findViewById(R.id.tvDistance2)).setTextColor(getResources().getColor(R.color.FontBlue));
                ((View)findViewById(R.id.seprateView)).setBackgroundColor(getResources().getColor(R.color.FillBlue));

                mBtnLock.setBackground(getResources().getDrawable(R.drawable.sl_shortbtn));
                mBtnLock.setText("锁车");
                mBtnLock.setTextColor(getResources().getColor(R.color.FontBlue));
                mBtnReturn.setBackground(getResources().getDrawable(R.drawable.sl_shortbtn));
                mBtnReturn.setTextColor(getResources().getColor(R.color.FontBlue));
                mBtnReturn.setEnabled(true);
                mVBattery.setBackgroundColor(getResources().getColor(R.color.FillBlue));
            }else{
                ((ImageView)findViewById(R.id.ivBike)).setImageResource(R.drawable.set_bike_d);
                ((ImageView)findViewById(R.id.ivBattery)).setImageResource(R.drawable.electricity_d);
                ((ImageView)findViewById(R.id.ivDistance)).setImageResource(R.drawable.distance_d);

                ((TextView)findViewById(R.id.tvBattery)).setTextColor(getResources().getColor(R.color.FontGray));
                ((TextView)findViewById(R.id.tvBattery2)).setTextColor(getResources().getColor(R.color.FontGray));
                ((TextView)findViewById(R.id.tvDistance)).setTextColor(getResources().getColor(R.color.FontGray));
                ((TextView)findViewById(R.id.tvDistance2)).setTextColor(getResources().getColor(R.color.FontGray));
                ((View)findViewById(R.id.seprateView)).setBackgroundColor(getResources().getColor(R.color.FillGray));

                mBtnLock.setBackground(getResources().getDrawable(R.drawable.sl_shortbtn_solid));
                mBtnLock.setText("开锁");
                mBtnLock.setTextColor(getResources().getColor(R.color.FontBlack));
                mBtnReturn.setBackground(getResources().getDrawable(R.drawable.sl_shortbtn_solid));
                mBtnReturn.setEnabled(false);
                mBtnReturn.setTextColor(getResources().getColor(R.color.FontBlack));
                mVBattery.setBackgroundColor(getResources().getColor(R.color.FontGray));
            }
        }
    }

    // 回调方法，从第二个页面回来的时候会执行这个方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//从结果页面回来，表示还车已经完成，就结束本页面了
        if(resultCode == 9){//表示还车已成功，结束本页面并跳到主页面
            startActivity(new Intent(AcBike.this,HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            Common.showLogoutDialog(this);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
