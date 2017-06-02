package saiyi.com.xiande.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.bigkoo.svprogresshud.SVProgressHUD;

import org.json.JSONArray;
import org.json.JSONObject;

import saiyi.com.xiande.R;
import saiyi.com.xiande.base.BaseActivity;
import saiyi.com.xiande.base.BaseApplication;
import saiyi.com.xiande.base.Http;
import saiyi.com.xiande.base.MyMethod;
import saiyi.com.xiande.data.DAccount;
import saiyi.com.xiande.data.DBike;
import saiyi.com.xiande.data.MyData;
import saiyi.com.xiande.utils.LoginUtil;
import saiyi.com.xiande.utils.ProgressUtils;

/**
 * 主界面-->显示地图上的车辆
 */
public class AcBikeOnMap extends BaseActivity implements View.OnClickListener{
    private ImageView mIvScan;
    private MapView mapView;
    private AMap aMap;
    private Marker mMyMarker;
    private MyData mMyData;
    private SVProgressHUD svProgressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bikeonmap);
        mMyData = BaseApplication.getInstance().getMyData();
        mMyData.mUserId = LoginUtil.getPhone();
//        mMyData.mCurBike.mLongitude = mMyData.mLastLoc.getLongitude() + 0.001;//假数据
//        mMyData.mCurBike.mLatitude = mMyData.mLastLoc.getLatitude() + 0.001;//假数据
//        mMyData.mCurBike.mLongitude = 113.890804;//假数据
//        mMyData.mCurBike.mLatitude = 22.602437;//假数据

        ((TextView)findViewById(R.id.tvTitle)).setText("附近车辆");
        findViewById(R.id.loLeft).setOnClickListener(this);
        findViewById(R.id.ivRecovery).setOnClickListener(this);
        mIvScan = (ImageView)findViewById(R.id.ivRight);
        mIvScan.setVisibility(View.VISIBLE);
        mIvScan.setOnClickListener(this);

        svProgressHUD = new SVProgressHUD(this);

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        if(mapView != null) {
            if(aMap == null) {
                aMap = mapView.getMap();
                aMap.moveCamera(CameraUpdateFactory.zoomTo((float) 18.0));
                //LatLng bikeCoor = new LatLng(22.6437,114.05482);
            }
        }
        LatLng bikeCoor = new LatLng(mMyData.mCurBike.mLatitude,mMyData.mCurBike.mLongitude);
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(bikeCoor));
        String title = "车牌:" + mMyData.mCurBike.mNo;
        String info = "可行驶里程:" + MyMethod.disToString(mMyData.mCurBike.mSurplus) + "\n剩余电量:" + ((int)mMyData.mCurBike.mBattery) + "%";
        Marker marker = aMap.addMarker(new MarkerOptions()
                .position(bikeCoor)
                .title(title)
                .snippet(info)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .draggable(true));
        marker.showInfoWindow();

        if(mMyData.mAccount == null){
            svProgressHUD.show();
            Http.getAccount(mMyData.mUserId, new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    svProgressHUD.dismiss();
                    switch (msg.what) {
                        case 1://成功返回
                            if(msg.obj != null) {
                                mMyData.mAccount = (DAccount.fromJSONArray((JSONArray) msg.obj)).get(0);
                            }
                            break;
                        case 0://失败
                            break;
                    }
                }
            });
        }
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation loc) {
            if (null != loc) {
                mMyData.mLastLoc = loc;
                if(mMyMarker == null){
                    mMyMarker = aMap.addMarker(new MarkerOptions()
                            .position(new LatLng(loc.getLatitude(), loc.getLongitude()))
                            .title("我的位置")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                            .draggable(true));
                }else {
                    //mMyMarker.setPosition(new LatLng(22.643773333333336,114.05482666666667));
                    mMyMarker.setPosition(new LatLng(loc.getLatitude(), loc.getLongitude()));
                }
            } else {
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loLeft://返回
                finish();
                break;
            case R.id.ivRight://扫码
                ProgressUtils.showDialog(this,"正在检查中，……",true,null);
                Http.getAccount(LoginUtil.getPhone(),new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if(msg.what == 1) {
                            final JSONObject jsonObject = ((JSONArray) msg.obj).optJSONObject(0);
                            if(jsonObject != null) {
                                final int result = jsonObject.optInt("deposit");
                                if(result == 1) {
                                    startActivityForResult(new Intent(AcBikeOnMap.this,saiyi.com.xiande.zbar.CaptureActivity.class), 1);
                                } else if(result == 2){
                                    Toast.makeText(AcBikeOnMap.this,"您处于待退款状态，无法租车",Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivity(new Intent(AcBikeOnMap.this,AcRechargeDeposit.class));
                                }
                            } else {
                                Toast.makeText(AcBikeOnMap.this,"检查失败",Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(AcBikeOnMap.this,"检查失败",Toast.LENGTH_SHORT).show();
                        }
                        ProgressUtils.dismissDialog();
                    }
                });
                break;
            case R.id.ivRecovery:
                aMap.moveCamera(CameraUpdateFactory.zoomTo((float) 18.0));
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(mMyMarker.getPosition()));
                break;
        }
    }

    // 回调方法，从第二个页面回来的时候会执行这个方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 1) {//成功返回
            switch (requestCode) {
                case 1://扫码返回
                    final String result = data.getStringExtra("result");
                    if(MyMethod.isBikeEanCode(result)) {//二维码正确
                        svProgressHUD.show();
                        Http.getAccount(mMyData.mUserId, new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                switch (msg.what) {
                                    case 1://成功返回
                                        if(msg.obj != null) {
                                            mMyData.mAccount = (DAccount.fromJSONArray((JSONArray) msg.obj)).get(0);
                                            if (mMyData.mAccount.mBalance < 1) {//余额不足
                                                startActivity(new Intent(AcBikeOnMap.this, AcMoneyLack.class));
                                                svProgressHUD.dismiss();
                                            } else {
                                                Http.rentBike(mMyData.mUserId, result /*mMyData.mCurBike.mEncode*/, new Handler() {
                                                    @Override
                                                    public void handleMessage(Message msg) {
                                                        svProgressHUD.dismiss();
                                                        if (msg.what > 1) {//租车成功,返回的其实是订单编号
                                                            mMyData.mCurOrderId = msg.what;
                                                            mMyData.mUserId = LoginUtil.getPhone();
                                                            Http.getBikeAnyTime(LoginUtil.getPhone(),result,new Handler() {
                                                                @Override
                                                                public void handleMessage(Message msg) {
                                                                    if(msg.what == 1) {
                                                                        try {
                                                                            final DBike bike = DBike.fromJSONObject(((JSONArray)msg.obj).optJSONObject(0));
                                                                            mMyData.mScanBike = bike;
                                                                            startActivity(new Intent(AcBikeOnMap.this, AcBike.class));
                                                                        } catch (Exception e) {
                                                                            Toast.makeText(AcBikeOnMap.this, "获取电动车信息失败", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    } else {
                                                                        Toast.makeText(AcBikeOnMap.this, "获取电动车信息失败", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                        } else if (msg.what == 0) {
                                                            Toast.makeText(AcBikeOnMap.this, "租车失败", Toast.LENGTH_SHORT).show();
                                                        }else if (msg.what == -1) {
                                                            Toast.makeText(AcBikeOnMap.this, "当前车辆不可出租", Toast.LENGTH_SHORT).show();
                                                        }else if (msg.what == -2) {
                                                            Toast.makeText(AcBikeOnMap.this, "当前车辆未还", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        }else{
                                            svProgressHUD.dismiss();
                                        }
                                        break;
                                    case 0://失败
                                        svProgressHUD.dismiss();
                                        break;
                                }
                            }
                        });
                    }else{
                        Toast.makeText(AcBikeOnMap.this, "二维码格式错误", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        BaseApplication.getInstance().mLocationClient.setLocationListener(locationListener);
        BaseApplication.getInstance().mLocationClient.startLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        BaseApplication.getInstance().mLocationClient.stopLocation();
        BaseApplication.getInstance().mLocationClient.unRegisterLocationListener(locationListener);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
