package saiyi.com.xiande.activity;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;

import saiyi.com.xiande.base.BaseApplication;
import saiyi.com.xiande.base.Http;
import saiyi.com.xiande.data.DBike;
import saiyi.com.xiande.data.MyData;

/**
 * 查询锁车状态的逻辑
 */
public class CheckLockStatus {
    private int mOpr;//动作
    private MyData mMyData;
    private boolean mIsRunThread;//false时结束获取车辆位置的线程
    private Handler mFeedBackHandler;//用来返回结果

    public static final int Opr_Success = 1;
    public static final int Opr_Fail = 0;

    public CheckLockStatus(){
        mMyData = BaseApplication.getInstance().getMyData();
    }

    private long mStartTimeMilsec;
    private final long SpanTime = 5*1000;//查询时间间隔
    private final long FailTime = 60*1000;//超时时间
    public void start(int opr,Handler feedbackHandler){
        mOpr = opr;
        mFeedBackHandler = feedbackHandler;
        mIsRunThread = true;
        mStartTimeMilsec = System.currentTimeMillis();
        new getBikeInfoThread().start();
    }

    public void cancel(){
        mIsRunThread = false;
    }

    private class getBikeInfoThread extends Thread{
        @Override
        public void run() {
            while (mIsRunThread) {
                if (mMyData.mScanBike != null) {
                    Http.getBikeAnyTime(mMyData.mUserId, mMyData.mScanBike.mEncode, mGetBikeHandler);
                    try {
                        checkFail();
                        sleep(SpanTime);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }
    private Handler mGetBikeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1 && msg.obj != null) {
                mMyData.mScanBike = DBike.fromJSONArray((JSONArray) msg.obj).get(0);
                if(mMyData.mScanBike.mLockStatus == mOpr){//操作成功
                    mFeedBackHandler.sendEmptyMessage(Opr_Success);
                    mIsRunThread = false;
                }
            }
        }
    };
    private void checkFail(){
        if(System.currentTimeMillis()-mStartTimeMilsec > FailTime){
            mFeedBackHandler.sendEmptyMessage(Opr_Fail);
            mIsRunThread = false;
        }
    }
}
