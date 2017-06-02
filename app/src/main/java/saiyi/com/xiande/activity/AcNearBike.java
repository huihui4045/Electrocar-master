package saiyi.com.xiande.activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import saiyi.com.xiande.Map.Utils;
import saiyi.com.xiande.R;
import saiyi.com.xiande.base.BaseActivity;
import saiyi.com.xiande.base.BaseApplication;
import saiyi.com.xiande.base.Http;
import saiyi.com.xiande.base.MyMethod;
import saiyi.com.xiande.base.PullToRefreshListView;
import saiyi.com.xiande.base.UIUtils;
import saiyi.com.xiande.data.DBike;
import saiyi.com.xiande.data.DBikeType;
import saiyi.com.xiande.data.DDict;
import saiyi.com.xiande.data.MyData;
import saiyi.com.xiande.utils.StringUtil;

public class AcNearBike extends BaseActivity implements View.OnClickListener{
    private ImageView mIvMenu;

    private PullToRefreshListView mLvBikeList;
    private SVProgressHUD svProgressHUD;
    private MyData mMyData;
    private ImageView mIvMsg;
    private TextView mTvMsg;
    private View load_more;
    private int mNextPage = 1;//附近车辆列表要加载的下一页数据
    private int mBikeNumOnPage = 10;//一页返回的车辆数量。

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_bike);
        mMyData = BaseApplication.getInstance().getMyData();

        ((TextView)findViewById(R.id.tvTitle)).setText("附近车辆");
        mIvMenu = (ImageView)findViewById(R.id.ivLeft);
        mIvMenu.setImageResource(R.drawable.ic_back);
        findViewById(R.id.loLeft).setOnClickListener(this);
        mLvBikeList = (PullToRefreshListView)findViewById(R.id.lvBikeList);
        mIvMsg = (ImageView)findViewById(R.id.ivMsg);
        mTvMsg = (TextView)findViewById(R.id.tvMsg);

        mLvBikeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mMyData.mCurBike = mMyData.mBikeList.get(position - 1);//添加 headerView 之后，position就会加1
                startActivity(new Intent(AcNearBike.this, AcBikeOnMap.class));
            }
        });

        svProgressHUD = new SVProgressHUD(this);

        LayoutInflater mInflater = LayoutInflater.from(this);
        load_more = mInflater.inflate(R.layout.load_more_footview_layout, null);
        //load_more_tv.setText("上拉加载更多数据...");
        //mLvBikeList.addFooterView(load_more, null, false);

        //listview 开始下拉刷新回调函数
        mLvBikeList.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                svProgressHUD.show();
                BaseApplication.getInstance().mLocationClient.startLocation();//刷新数据，重新定位刷新
            }
        });
        //listview 滑动  进行上拉加载更多
        mLvBikeList.setOnScrollListener(mPullUpListener);
        //初始化 上次下拉刷新的时间
        UIUtils.setPullToRefreshLastUpdated(mLvBikeList, UIUtils.DEMO_PULL_TIME_KEY);
        if(mMyData.mBikeTypeList==null || mMyData.mBikeTypeList.isEmpty()){
            Http.queryAllBikeType(new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 1:
                            if (msg.obj != null) {
                                mMyData.mBikeTypeList = DBikeType.fromJSONArray((JSONArray) msg.obj);
                            }
                            break;
                    }
                }
            });
        }

        mIvMenu.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        svProgressHUD.show();
        BaseApplication.getInstance().mLocationClient.startLocation();
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

    private BikeAdapter mBikeAdapter;
    //上拉刷新监听器
    private AbsListView.OnScrollListener mPullUpListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                UIUtils.savePullToRefreshLastUpdateAt(mLvBikeList, UIUtils.DEMO_PULL_TIME_KEY);
                if (mLvBikeList.getLastVisiblePosition() == (mLvBikeList.getCount() - 1)) {//显示到底了
                    if(mMyData.mBikeList.size() >= (mNextPage-1)*mBikeNumOnPage){//上一页数据是满的（不满说明不会有下一页）
                        load_more.setVisibility(View.VISIBLE);
                        svProgressHUD.show();
                        Http.getBikeList(String.valueOf(mMyData.mLastLoc.getLongitude()),String.valueOf(mMyData.mLastLoc.getLatitude()),mNextPage, new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                mLvBikeList.onRefreshComplete();
                                svProgressHUD.dismiss();
                                load_more.setVisibility(View.GONE);
                                switch (msg.what){
                                    case 1:
                                        if(msg.obj != null){
                                            mNextPage++;
                                            mMyData.mBikeList.addAll(DBike.fromJSONArray((JSONArray) msg.obj));
                                            mBikeAdapter.AddData(toMapList(DBike.fromJSONArray((JSONArray) msg.obj)));
                                        }
                                        break;
                                    case 0:
                                        Toast.makeText(AcNearBike.this, "加载第" + mNextPage + "页数据失败", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        });
                    }
                }
            }
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                //正在滑动中，当前listview正在滑动 可以暂停图片加载器或者其他一些耗时操作
            } else {
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        }
    };

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation loc) {
            if (null != loc) {
                //解析定位结果
                String result = Utils.getLocationStr(loc);
                mMyData.mLastLoc = loc;
                BaseApplication.getInstance().mLocationClient.stopLocation();
                mNextPage = 1;
                Http.getBikeList(String.valueOf(loc.getLongitude()),String.valueOf(loc.getLatitude()),1, new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        svProgressHUD.dismiss();
                        mLvBikeList.onRefreshComplete();
                        load_more.setVisibility(View.GONE);
                        switch (msg.what){
                            case 1:
                                if(msg.obj != null){
                                    mNextPage = 2;
                                    mMyData.mBikeList = DBike.fromJSONArray((JSONArray) msg.obj);
                                    mIvMsg.setVisibility(View.INVISIBLE);
                                    mTvMsg.setVisibility(View.INVISIBLE);
                                    mBikeAdapter = new BikeAdapter(AcNearBike.this, toMapList(mMyData.mBikeList), R.layout.item_bikelist, new String[]{} , new int[]{});
                                    mLvBikeList.setAdapter(mBikeAdapter);
                                }
                                break;
                            case 0:
                                Toast.makeText(AcNearBike.this, "查找附近车辆失败", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        if(mMyData.mBikeList==null || mMyData.mBikeList.isEmpty()){
                            mIvMsg.setVisibility(View.VISIBLE);
                            mTvMsg.setVisibility(View.VISIBLE);
                        }
                    }
                });
            } else {
                svProgressHUD.dismiss();
                Toast.makeText(AcNearBike.this, "定位失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivRight://扫码
//                startActivityForResult(new Intent(this,saiyi.com.xiande.zbar.CaptureActivity.class), 1);
                break;
            case R.id.ivLeft:
                finish();
                break;
        }
    }

    private class BikeAdapter extends SimpleAdapter {
        private List<Map<String,DBike>> mListViewData = new ArrayList<>();//电动车列表（适配后的数据）

        public BikeAdapter(Context context, List<Map<String,DBike>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
            mListViewData = data;
        }
        public void AddData(List<Map<String,DBike>> data){
            mListViewData.addAll(data);
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View v = super.getView(position, convertView, parent);
            DBike bike = mListViewData.get(position).get("index");
            ImageView iconView = (ImageView) v.findViewById(R.id.avatar_iv);

            if(bike!=null) {
                if (!StringUtil.isEmpty(bike.mBikeImgPath)) {
                    Picasso.with(AcNearBike.this).load(Http.UrlDownload + bike.mBikeImgPath).resize(80, 80).into(iconView);
                }
                ((TextView) v.findViewById(R.id.tvBikeName)).setText(bike.mNo);
                ((TextView) v.findViewById(R.id.tvBikeElect)).setText("电量" + String.valueOf(bike.mBattery) + "%");
                ((TextView) v.findViewById(R.id.tvBikePowerDis)).setText(MyMethod.disToString(bike.mSurplus));
                ((TextView) v.findViewById(R.id.tvBuyDate)).setText("采购日期" + MyMethod.DateToString_yymmdd(bike.mBuyDate));
                ((TextView) v.findViewById(R.id.tvBikePos)).setText(bike.mAddress);
                ((TextView) v.findViewById(R.id.tvBikeDis)).setText(MyMethod.disToString(bike.mDistance));
            }
            return v;
        }
    }

    public  List<Map<String,DBike>> toMapList(List<DBike> list){
        if(list == null){
            return null;
        }
        List<Map<String,DBike>> mapList = new ArrayList<>(list.size());
        for(DBike object:list){
            Map<String,DBike> map = new HashMap<>(1);
            map.put("index",object);
            mapList.add(map);
        }
        return mapList;
    }

    @Override
    protected void onResume() {
        super.onResume();
        BaseApplication.getInstance().mLocationClient.setLocationListener(locationListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BaseApplication.getInstance().mLocationClient.unRegisterLocationListener(locationListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    // 回调方法，从第二个页面回来的时候会执行这个方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 1) {//成功返回
            switch (requestCode) {
                case 1://扫码返回
                    String change01 = data.getStringExtra("result");
                    break;
                default:
                    break;
            }
        }
    }
}


