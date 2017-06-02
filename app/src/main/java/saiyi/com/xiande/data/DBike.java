package saiyi.com.xiande.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import saiyi.com.xiande.base.MyMethod;

/**
 * 电动车信息
 */
public class DBike {
    public String mEncode;//车辆二维码
    public String mNo;//车辆编号
    public String mTypeId;//车辆类型
    public int mStatus;//车辆状态,0 未出租、1 已出租、2 故障,默认未出租
    public double mBattery;//电池电量
    public double mLongitude;//车辆当前经度
    public double mLatitude;//车辆当前纬度
    public String mPosition;//车辆当前位置
    public Date mBuyDate;//采购日期
    public String mStopId;//当前所属停车场
    public String mOwnerId;//所属车主
    public double mDistance;//车辆和APP的距离
    public double mSurplus;//车辆还能跑多少米
    public String mStopName;//停车场名字
    public int mLockStatus = 1;//锁的状态，0未锁，1锁着, 默认锁着
    public double mPrice;//可剩金额
    public double mCost;//花费金额
    public String mAddress;//地址
    public String mBikeImgPath;//车辆类型图片

    public static final int Status_LOCK = 1;
    public static final int Status_UNLOCK = 0;

    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();

        try {jsonObject.put("carEncode",mEncode);} catch (JSONException ignored) {}
        try {jsonObject.put("carNum",mNo);} catch (JSONException ignored) {}
        try {jsonObject.put("carTypeId",mTypeId);} catch (JSONException ignored) {}
        try {jsonObject.put("carStatus",mStatus);} catch (JSONException ignored) {}
        try {jsonObject.put("battery",mBattery);} catch (JSONException ignored) {}
        try {jsonObject.put("carLongitude",mLongitude);} catch (JSONException ignored) {}
        try {jsonObject.put("carLatitude",mLatitude);} catch (JSONException ignored) {}
        try {jsonObject.put("carPosition",mPosition);} catch (JSONException ignored) {}
        try {jsonObject.put("buyDate",mBuyDate.getTime());} catch (JSONException ignored) {}
        try {jsonObject.put("stopId",mStopId);} catch (JSONException ignored) {}
        try {jsonObject.put("ownerId",mOwnerId);} catch (JSONException ignored) {}
        try {jsonObject.put("distance",mDistance);} catch (JSONException ignored) {}
        try {jsonObject.put("surplus",mSurplus);} catch (JSONException ignored) {}
        try {jsonObject.put("sname",mStopName);} catch (JSONException ignored) {}
        try {jsonObject.put("lockStatus",mLockStatus);} catch (JSONException ignored) {}
        try {jsonObject.put("price",mPrice);} catch (JSONException ignored) {}
        try {jsonObject.put("cost",mCost);} catch (JSONException ignored) {}
        try {jsonObject.put("carImg",mBikeImgPath);} catch (JSONException ignored) {}
        try {jsonObject.put("address",mAddress);} catch (JSONException ignored) {}
        return jsonObject;
    }

    public static DBike fromJSONObject(JSONObject jsonObject){
        DBike dBike = new DBike();
        if(jsonObject != null){
            try {dBike.mEncode = jsonObject.getString("carEncode");} catch (JSONException ignored) {}
            try {dBike.mNo = jsonObject.getString("carNum");} catch (JSONException ignored) {}
            try {dBike.mTypeId = jsonObject.getString("carTypeId");} catch (JSONException ignored) {}
            try {dBike.mStatus = MyMethod.parseInt(jsonObject.getString("carStatus"));} catch (JSONException ignored) {}
            try {dBike.mBattery = MyMethod.parseDouble(jsonObject.getString("battery"));} catch (JSONException ignored) {}
            try {dBike.mLongitude = MyMethod.parseDouble(jsonObject.getString("carLongitude"));} catch (JSONException ignored) {}
            try {dBike.mLatitude = MyMethod.parseDouble(jsonObject.getString("carLatitude"));} catch (JSONException ignored) {}
            try {dBike.mPosition = jsonObject.getString("carPosition");} catch (JSONException ignored) {}
            try {dBike.mBuyDate = new Date(MyMethod.parseLong(jsonObject.getString("buyDate")));} catch (JSONException ignored) {}
            try {dBike.mStopId = jsonObject.getString("stopId");} catch (JSONException ignored) {}
            try {dBike.mOwnerId = jsonObject.getString("ownerId");} catch (JSONException ignored) {}
            try {dBike.mDistance = MyMethod.parseDouble(jsonObject.getString("distance"));} catch (JSONException ignored) {}
            try {dBike.mSurplus = MyMethod.parseDouble(jsonObject.getString("surplus"));} catch (JSONException ignored) {}
            try {dBike.mStopName = jsonObject.getString("sname");} catch (JSONException ignored) {}
            try {dBike.mLockStatus = Integer.parseInt(jsonObject.getString("lockStatus"));} catch (Exception ignored) {}
            try {dBike.mPrice = MyMethod.parseDouble(jsonObject.getString("price"));} catch (JSONException ignored) {}
            try {dBike.mCost = MyMethod.parseDouble(jsonObject.getString("cost"));} catch (JSONException ignored) {}
            try {dBike.mBikeImgPath = jsonObject.getString("carImg");} catch (JSONException ignored) {}
            try {dBike.mAddress = jsonObject.getString("address");} catch (JSONException ignored) {}

        }
        return dBike;
    }

    public static List<DBike> fromJSONArray(JSONArray jsonArray){
        List<DBike> list = new ArrayList<>();
        for(int i=0; i<jsonArray.length(); ++i){
            try {
                list.add(fromJSONObject(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
