package saiyi.com.xiande.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.widget.Toast;

import org.json.JSONArray;

import saiyi.com.xiande.R;
import saiyi.com.xiande.base.AppConstants;
import saiyi.com.xiande.base.BaseActivity;
import saiyi.com.xiande.base.BaseApplication;
import saiyi.com.xiande.base.Http;
import saiyi.com.xiande.base.MyMethod;
import saiyi.com.xiande.data.DBike;
import saiyi.com.xiande.data.DOrderInfo;
import saiyi.com.xiande.data.DStudent;
import saiyi.com.xiande.data.MyData;
import saiyi.com.xiande.utils.AES;
import saiyi.com.xiande.utils.LoginUtil;
import saiyi.com.xiande.utils.PreferencesUtils;
import saiyi.com.xiande.utils.StringUtil;

public class AcSplash extends BaseActivity {
    private MyData mMyData;
    private int mLoginType;
    //定时器
    private CountDownTimer mCountDownTimer = new CountDownTimer(3 * 1000,1000) {
        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            loginInfo();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        mMyData = BaseApplication.getInstance().getMyData();
        mCountDownTimer.start();
    }

    /**
     * 登录信息
     */
    private void loginInfo() {
        mLoginType = LoginUtil.getLoginType();
        if(mLoginType != -1) {
            if(mLoginType == 1) {
                mMyData.mUserId = LoginUtil.getPhone();
                Http.getUserInfo(LoginUtil.getPhone(),mLoadUserInfoHandler);
            } else {
                mMyData.mUserId = LoginUtil.getSellerId();
                openActivity(Login.class);
            }
        } else {
            openActivity(Login.class);
        }
        mCountDownTimer.cancel();
        mCountDownTimer = null;
    }

    @Override
    protected void openActivity(Class<?> pClass) {
        super.openActivity(pClass);
        finish();
    }

    private Handler mLoadUserInfoHandler =  new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1://成功
                    if(msg.obj != null) {
                        mMyData.mStudent = (DStudent.fromJSONArray((JSONArray) msg.obj)).get(0);
                        switch (mMyData.mStudent.mIsValid) {
                            default:
                            case 0://初始状态
                                final Intent intent = new Intent(AcSplash.this,AcUserInfo.class);
                                intent.putExtra("phone",mMyData.mStudent.mPhone);
                                startActivity(intent);
                                break;
                            case 1://已提交
                            case 2://审核失败
                                final Intent userInfo = new Intent(AcSplash.this,AcUserInfoSubmit.class);
                                userInfo.putExtra("phone",mMyData.mStudent.mPhone);
                                startActivity(userInfo);
                                break;
                            case 3://审核通过
                                Http.getMyOrder_NoOver(mMyData.mStudent.mPhone, mLoadOrderHandler);
                                break;
                        }
                    } else {
                        openActivity(Login.class);
                    }
                    break;
                case 0://失败
                    openActivity(Login.class);
                    break;
            }
        }
    };

    private Handler mLoadOrderHandler =  new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                LoginUtil.setLogin(LoginUtil.getPhone(),1);
                if (msg.obj != null) {
                    mMyData.mCurOrder = DOrderInfo.fromJSONArray((JSONArray)msg.obj).get(0);
                    mMyData.mCurOrderId = MyMethod.parseInt(mMyData.mCurOrder.mOrderId);
                    mMyData.mUserId = LoginUtil.getPhone();
                    Http.getBike(mMyData.mUserId,mMyData.mCurOrder.mBikeEncode,mLoadBikeHandler);
                }else{
                    openActivity(HomeActivity.class);
                }
            }else {
                openActivity(HomeActivity.class);
            }

        }
    };

    private Handler mLoadBikeHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (msg.obj != null) {
                    mMyData.mScanBike = DBike.fromJSONArray((JSONArray) msg.obj).get(0);
                    openActivity(AcBike.class);
                } else {
                    openActivity(Login.class);
                }
            } else {
                openActivity(Login.class);
            }
        }
    };
}
