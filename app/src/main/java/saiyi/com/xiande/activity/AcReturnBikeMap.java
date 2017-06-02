package saiyi.com.xiande.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.Circle;
import com.amap.api.maps2d.model.CircleOptions;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.bigkoo.svprogresshud.SVProgressHUD;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import saiyi.com.xiande.R;
import saiyi.com.xiande.base.BaseActivity;
import saiyi.com.xiande.base.BaseApplication;
import saiyi.com.xiande.base.Http;
import saiyi.com.xiande.data.DBike;
import saiyi.com.xiande.data.DOrderInfo;
import saiyi.com.xiande.data.DStopPosition;
import saiyi.com.xiande.data.MyData;
import saiyi.com.xiande.utils.LoginUtil;

/**
 * 还车范围，在地图上显示还车点
 */
public class AcReturnBikeMap extends BaseActivity implements View.OnClickListener{
    private MapView mapView;
    private AMap aMap;
    private Button mBtnReturn;
    private Marker mMyMarker;
    private MyData mMyData;
    private SVProgressHUD svProgressHUD;

    private AMapLocation mLastQueryLoc;//上一次搜索附近停车点时，我的坐标。（为提高效率，距此坐标一定距离之后再刷新附近停车点）

    public static final int StrokeColor = Color.argb(200, 51, 223, 132);//停车点圈圈的边沿颜色
    public static final int FillColor = Color.argb(100, 51, 223, 132);//停车点圈圈里面的颜色
    private List<Circle> mCircleList;//地图上的停车点列表

    private DStopPosition mCurStopPosition;//当前可还车点

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_returnbike_map);
        mMyData = BaseApplication.getInstance().getMyData();

        ((TextView)findViewById(R.id.tvTitle)).setText("还车范围");
        findViewById(R.id.loLeft).setOnClickListener(this);
        findViewById(R.id.ivRecovery).setOnClickListener(this);
        mBtnReturn = (Button)findViewById(R.id.btnReturn);
        mBtnReturn.setOnClickListener(this);
        mBtnReturn.setEnabled(false);

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        aMap = mapView.getMap();

        LatLng mMyCoor = new LatLng(mMyData.mScanBike.mLatitude, mMyData.mScanBike.mLongitude);
//        if(mMyData.mLastLoc != null) {
//            mMyCoor = new LatLng(mMyData.mLastLoc.getLatitude(), mMyData.mLastLoc.getLongitude());
//        }else{
//            mMyCoor = new LatLng(mMyData.mCurBike.mLatitude, mMyData.mCurBike.mLongitude);
//        }
        aMap.moveCamera(CameraUpdateFactory.zoomTo((float) 18.0));
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(mMyCoor));

        mMyMarker = aMap.addMarker(new MarkerOptions()
                .position(mMyCoor)
                .title("车辆的位置")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .draggable(true));
        mMyMarker.showInfoWindow();

        svProgressHUD = new SVProgressHUD(this);
        //svProgressHUD.showWithStatus("正在搜索附近停车点");

//        // 绘制一个圆形
//        Circle circle = aMap.addCircle(new CircleOptions().center(mMyCoor)
//                .radius(100).strokeColor(Color.argb(200, 51, 223, 132))
//                .fillColor(Color.argb(100, 51, 223, 132)).strokeWidth(25));
        if(mMyData.mScanBike != null){
            //getBikeInfoThread = new Thread();
            mIsRunThread = true;
            getBikeInfoThread.start();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loLeft://返回
                finish();
                break;
            case R.id.btnReturn:
                if(mCurStopPosition != null) {
                    svProgressHUD.show();
                    mBtnReturn.setEnabled(false);
                    Http.returnBike(String.valueOf(mMyData.mCurOrderId), mCurStopPosition.mSName, new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            if (msg.what == 1) {//还车成功
                                if (msg.obj != null) {
                                    mMyData.mCurOrder = DOrderInfo.fromJSONArray((JSONArray) msg.obj).get(0);
                                    mCheckLockStatus.start(DBike.Status_LOCK, mCheckFeedbackHandler);
                                }
                            } else {//还车失败
                                svProgressHUD.dismiss();
                                mBtnReturn.setEnabled(true);
                                Toast.makeText(AcReturnBikeMap.this, "还车失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{

                }
                break;
            case R.id.ivRecovery:
                aMap.moveCamera(CameraUpdateFactory.zoomTo((float) 18.0));
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(mMyMarker.getPosition()));
                break;
        }
    }

    private CheckLockStatus mCheckLockStatus = new CheckLockStatus();

    private Handler mCheckFeedbackHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            svProgressHUD.dismiss();
            mBtnReturn.setEnabled(true);
            if(msg.what == CheckLockStatus.Opr_Success){
                startActivityForResult(new Intent(AcReturnBikeMap.this, AcReturnBike.class), 2);
            }else{
                Toast.makeText(AcReturnBikeMap.this, "还车失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation loc) {
            if (null != loc) {
                mMyData.mLastLoc = loc;
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(loc.getLatitude(),loc.getLongitude())));
                mMyMarker.setPosition(new LatLng(loc.getLatitude(),loc.getLongitude()));
                checkReturnBike();
                checkUpdateSPosList();
            } else {
            }
        }
    };

    private void updateBikeLoc(double lat, double lon){
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(lat,lon)));
        mMyMarker.setPosition(new LatLng(lat,lon));
        checkReturnBike();
        checkUpdateSPosList();
    }

    //检查是否可以还车以及可以还车的停车场，先在本地过滤一遍以提高效率
    private void checkReturnBike(){
        boolean isCanReturn = false;
        if(mMyData.mSPosList!=null && !mMyData.mSPosList.isEmpty()){
            for(DStopPosition position:mMyData.mSPosList){
                double dis = AMapUtils.calculateLineDistance(new LatLng(position.mLatitud, position.mLongitude),
                        new LatLng(mMyData.mScanBike.mLatitude, mMyData.mScanBike.mLongitude));//两点间直线距离，单位为米
                if(dis <= position.mRadius){
                    isCanReturn = true;
                    break;
                }
            }
            mBtnReturn.setEnabled(isCanReturn);
        }
    }

    private boolean mIsRunThread;//false时结束获取车辆位置的线程
    private boolean mIsPauseThread;//true时暂停获取车辆位置信息
    private Thread getBikeInfoThread = new Thread(){
        @Override
        public void run() {
            while (mIsRunThread){
                if(!mIsPauseThread){
                    if(mMyData.mScanBike != null) {
                        Http.getBikeAnyTime(LoginUtil.getPhone(), mMyData.mScanBike.mEncode,mGetBikeAnyTimeHandler);
                    }
                }
                try {
                    sleep(5*1000);
                } catch (InterruptedException e) {
                }
            }
        }
    };

    private Handler mGetBikeAnyTimeHandler =  new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1 && msg.obj != null) {
                mMyData.mScanBike = DBike.fromJSONArray((JSONArray) msg.obj).get(0);
                updateBikeLoc(mMyData.mScanBike.mLatitude, mMyData.mScanBike.mLongitude);
            }
        }
    };

    //在地图上把附近停车点列表显示出来
    private void showSPosList(){
        //先把原来显示的清除掉
        if(mCircleList!=null && !mCircleList.isEmpty()){
            for(Circle circle:mCircleList){
                circle.remove();
            }
            mCircleList.clear();
        }

        if(mMyData.mSPosList!=null && !mMyData.mSPosList.isEmpty()){
            mCircleList = new ArrayList<>(mMyData.mSPosList.size());
            for(DStopPosition position:mMyData.mSPosList){
                // 绘制一个圆形
                Circle circle = aMap.addCircle(new CircleOptions().center(new LatLng(position.mLatitud,position.mLongitude))
                        .radius(position.mRadius).strokeColor(StrokeColor)
                        .fillColor(FillColor).strokeWidth(25));
                mCircleList.add(circle);
            }
        }
    }

//    //检查，是否刷新附近停车点列表
//    private void checkUpdateSPosList(){
//        float distance = 1000;
//        if(mLastQueryLoc != null) {
//            distance = AMapUtils.calculateLineDistance(new LatLng(mLastQueryLoc.getLatitude(), mLastQueryLoc.getLongitude()),
//                    new LatLng(mMyData.mLastLoc.getLatitude(), mMyData.mLastLoc.getLongitude()));//两点间直线距离，单位为米
//        }
//        if(distance > 300){//300米刷新一次附近停车点列表
//            mLastQueryLoc = mMyData.mLastLoc;
//            Http.getStopList(mMyData.mLastLoc.getLongitude(),mMyData.mLastLoc.getLatitude(),new Handler(){
//                @Override
//                public void handleMessage(Message msg) {
//                    if(msg.what == 1){//成功
//                        mMyData.mSPosList = DStopPosition.fromJSONArray((JSONArray)msg.obj);
//                        mCurStopPosition = mMyData.mSPosList.get(0);//测试用
//                        showSPosList();
//                    }else{
//                        mLastQueryLoc = null;
//                        Toast.makeText(AcReturnBikeMap.this, "查询附近停车场失败", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        }
//    }

    //检查，是否刷新附近停车点列表
    private void checkUpdateSPosList() {
        Http.getStopList(mMyData.mScanBike.mLatitude, mMyData.mScanBike.mLongitude, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {//成功
                    mMyData.mSPosList = DStopPosition.fromJSONArray((JSONArray) msg.obj);
                    mCurStopPosition = mMyData.mSPosList.get(0);//测试用
                    showSPosList();
                } else {
                    Toast.makeText(AcReturnBikeMap.this, "查询附近停车场失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    // 回调方法，从第二个页面回来的时候会执行这个方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//从结果页面回来，表示还车已经完成，就结束本页面了
        setResult(9, getIntent());
        finish();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
//        BaseApplication.getInstance().mLocationClient.setLocationListener(locationListener);
//        BaseApplication.getInstance().mLocationClient.startLocation();
        mIsPauseThread = false;
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
//        BaseApplication.getInstance().mLocationClient.stopLocation();
//        BaseApplication.getInstance().mLocationClient.unRegisterLocationListener(locationListener);
        mIsPauseThread = false;
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        mIsRunThread = false;
    }
}
