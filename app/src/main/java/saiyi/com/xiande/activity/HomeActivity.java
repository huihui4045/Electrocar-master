package saiyi.com.xiande.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import saiyi.com.xiande.AppHelper;
import saiyi.com.xiande.R;
import saiyi.com.xiande.base.BaseActivity;
import saiyi.com.xiande.base.BaseApplication;
import saiyi.com.xiande.base.Common;
import saiyi.com.xiande.base.Http;
import saiyi.com.xiande.base.MyMethod;
import saiyi.com.xiande.base.PopUpView;
import saiyi.com.xiande.data.DAccount;
import saiyi.com.xiande.data.DBike;
import saiyi.com.xiande.data.DDict;
import saiyi.com.xiande.data.MyData;
import saiyi.com.xiande.utils.LoginUtil;
import saiyi.com.xiande.utils.ProgressUtils;

/**
 * 描述：主界面
 * 创建作者：黎丝军
 * 创建时间：2017/1/13 17:46
 */

public class HomeActivity extends BaseActivity implements View.OnClickListener,LocationSource,AMapLocationListener{

    private AMap mAmap;
    //进入附近车辆
    private TextView mNearMenuIv;
    //扫描
    private TextView mScanIv;
    //地图显示
    private MapView mMapView;
    //侧滑菜单
    private TextView mIvMenu;
    private DrawerLayout mDrawerLayout;
    private LinearLayout mLeftDrawer;
    private MyData mMyData;
    private ImageView mIvAvatar;
    private SVProgressHUD svProgressHUD;
    private long mExitTime = 0;
    //定位客户端
    private AMapLocationClient mLocationClient;
    //位置改变监听
    private OnLocationChangedListener mLocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mMyData = BaseApplication.getInstance().getMyData();
        mMyData.mUserId = LoginUtil.getPhone();
        ((TextView)findViewById(R.id.tvTitle)).setText("");
        mIvMenu = (TextView) findViewById(R.id.tv_center);
        mIvMenu.setVisibility(View.VISIBLE);
        (findViewById(R.id.ivLeft)).setVisibility(View.GONE);
        findViewById(R.id.loLeft).setOnClickListener(this);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.Dlo);
        mLeftDrawer = (LinearLayout)findViewById(R.id.left_drawer);
        mIvAvatar = (ImageView) findViewById(R.id.avatar_iv);
        mNearMenuIv = (TextView) findViewById(R.id.iv_home_near);
        mScanIv = (TextView) findViewById(R.id.iv_home_scan);
        mMapView = (MapView) findViewById(R.id.mv_location);
        if(!MyMethod.isEmpty_null(mMyData.mStudent.mHeadPic)) {
            Picasso.with(this).load(Http.UrlDownload + mMyData.mStudent.mHeadPic).resize(80, 80).into(mIvAvatar);
        }

        mScanIv.setOnClickListener(this);
        mNearMenuIv.setOnClickListener(this);

        mMapView.onCreate(savedInstanceState);
        if(mAmap == null) {
            mAmap = mMapView.getMap();
            mAmap.setLocationSource(this);// 设置定位监听
            mAmap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
            mAmap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        }
        svProgressHUD = new SVProgressHUD(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mDrawerLayout.isDrawerOpen(mLeftDrawer)) {
            BaseApplication.getInstance().mLocationClient.startLocation();
        }
        ((TextView)findViewById(R.id.tvName)).setText(mMyData.mUserId);
        ((TextView)findViewById(R.id.tvPhone)).setText(mMyData.mStudent.mPhone);
        if(mMyData.mDict == null){
            Http.getDict(new Handler() {//获取系统定义的一些商家标准数据
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == 1) {
                        if(msg.obj != null) {
                            mMyData.mDict = DDict.fromJSONArray((JSONArray) msg.obj).get(0);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        mMapView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_home_near:
                startActivity(new Intent(HomeActivity.this,AcNearBike.class));
                break;
            case R.id.iv_home_scan://扫码
                ProgressUtils.showDialog(this,"正在检查中，……",true,null);
                Http.getAccount(LoginUtil.getPhone(),new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if(msg.what == 1) {
                            final JSONObject  jsonObject = ((JSONArray) msg.obj).optJSONObject(0);
                            if(jsonObject != null) {
                                final int result = jsonObject.optInt("deposit");
                                if(result == 1) {
                                    startActivityForResult(new Intent(HomeActivity.this,saiyi.com.xiande.zbar.CaptureActivity.class), 1);
                                } else if(result == 2){
                                    Toast.makeText(HomeActivity.this,"您处于待退款状态，无法租车",Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivity(new Intent(HomeActivity.this,AcRechargeDeposit.class));
                                }
                            } else {
                                Toast.makeText(HomeActivity.this,"检查失败",Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(HomeActivity.this,"检查失败",Toast.LENGTH_SHORT).show();
                        }
                        ProgressUtils.dismissDialog();
                    }
                });

                break;
            case R.id.loLeft://菜单
                if(mDrawerLayout.isDrawerOpen(mLeftDrawer)){
                    mDrawerLayout.closeDrawer(mLeftDrawer);
                }else{
                    mDrawerLayout.openDrawer(mLeftDrawer);
                }
                break;
            case R.id.avatar_iv://头像
                PopUpView.showPopwindow(this, new PopUpView.Click() {
                    @Override
                    public void onCamera() {
                        startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"), 3);
                    }
                    @Override
                    public void onAlbum() {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(intent, 2);
                    }
                    @Override
                    public void onCancel() {}
                });
                break;
            case R.id.loPwd://修改密码
                startActivity(new Intent(this, AcChangePwd.class));
                break;
            case R.id.loConnect://修改联系方式
                //startActivity(new Intent(this,AcChangeConnect.class));
                startActivity(new Intent(this,AcChangePhone1.class));
                break;
            case R.id.loMoney://余额
                startActivity(new Intent(this,AcMoney.class));
                break;
            case R.id.loRefound://我的退款
                startActivity(new Intent(this,AcRefund.class));
                break;
            case R.id.loMessage://消息中心
                startActivity(new Intent(this,AcMessageList.class));
                break;
            case R.id.loFault://故障反馈
                startActivity(new Intent(this,AcFault.class));
                break;
            case R.id.loAbout://关于我们
                startActivity(new Intent(this,AcAbout.class));
                break;
            case R.id.loLogout://退出登录
                Common.showLogoutDialog(this);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
        deactivate();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    // 回调方法，从第二个页面回来的时候会执行这个方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 2) {//选择头像图片成功返回
            Uri uri = data.getData();
            Log.e("uri", uri.toString());
            ContentResolver cr = getContentResolver();
            try {
                final Bitmap bitmap = MyMethod.xiandeCompress(BitmapFactory.decodeStream(cr.openInputStream(uri)), this);
                final File file = new File(MyMethod.getRealFilePath(HomeActivity.this, uri));
                final String newFileName;
                newFileName = mMyData.mUserId + Http.CXHeadPic + "." + MyMethod.getSuffix(file);
                Http.uploadPic(MyMethod.getRealFilePath(HomeActivity.this, uri), newFileName, new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case 1://成功
                                mIvAvatar.setImageBitmap(bitmap);
                                mMyData.mStudent.mHeadPic = newFileName;
                                Toast.makeText(HomeActivity.this, "头像上传成功", Toast.LENGTH_SHORT).show();
                                Http.updateUserInfo(mMyData.mUserId, mMyData.mStudent, null);
                                break;
                            case 0://失败
                                Toast.makeText(HomeActivity.this, "头像上传失败", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } else if (resultCode == Activity.RESULT_OK && requestCode == 3) {//头像拍照成功返回
            final Bitmap bitmap = MyMethod.xiandeCompress((Bitmap) data.getExtras().get("data"), this);
            final String newFileName;
            newFileName = mMyData.mUserId + Http.CXHeadPic + ".jpg";
            Http.uploadPic(bitmap, newFileName, new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 1://成功
                            mIvAvatar.setImageBitmap(bitmap);
                            mMyData.mStudent.mHeadPic = newFileName;
                            Toast.makeText(HomeActivity.this, "头像上传成功", Toast.LENGTH_SHORT).show();
                            Http.updateUserInfo(mMyData.mUserId, mMyData.mStudent, null);
                            break;
                        case 0://失败
                            Toast.makeText(HomeActivity.this, "头像上传失败", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
        }
        if (resultCode == 1) {//成功返回
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
                                        startActivity(new Intent(HomeActivity.this, AcMoneyLack.class));
                                        svProgressHUD.dismiss();
                                    } else {
                                        Http.rentBike(mMyData.mUserId, result, new Handler() {
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
                                                                    startActivity(new Intent(HomeActivity.this, AcBike.class));
                                                                } catch (Exception e) {
                                                                    Toast.makeText(HomeActivity.this, "获取电动车信息失败", Toast.LENGTH_SHORT).show();
                                                                }
                                                            } else {
                                                                Toast.makeText(HomeActivity.this, "获取电动车信息失败", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                } else if (msg.what == 0) {
                                                    Toast.makeText(HomeActivity.this, "租车失败", Toast.LENGTH_SHORT).show();
                                                }else if (msg.what == -1) {
                                                    Toast.makeText(HomeActivity.this, "当前车辆不可出租", Toast.LENGTH_SHORT).show();
                                                }else if (msg.what == -2) {
                                                    Toast.makeText(HomeActivity.this, "当前车辆未还", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(HomeActivity.this, "二维码格式错误", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) { //监控/拦截/屏蔽返回键
            long currentTime = System.currentTimeMillis();
            if (currentTime - mExitTime < 2000) {
                AppHelper.instance().exitApp();
            } else {
                mExitTime = currentTime;
                Toast.makeText(this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mLocationListener = onLocationChangedListener;
        if(mLocationClient == null) {
            mLocationClient = new AMapLocationClient(this);
            final AMapLocationClientOption locationOption = new AMapLocationClientOption();
            locationOption.setHttpTimeOut(30 * 1000);
            locationOption.setOnceLocation(true);
            locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationClient.setLocationOption(locationOption);
            mLocationClient.setLocationListener(this);
            mLocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mLocationListener = null;
        if(mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient = null;
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        mMyData.mLastLoc = aMapLocation;
        if(mLocationListener != null) {
            mAmap.moveCamera(CameraUpdateFactory.zoomTo(mAmap.getScalePerPixel()));
            mLocationListener.onLocationChanged(aMapLocation);
        }
    }
}
