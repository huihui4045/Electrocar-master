package saiyi.com.xiande.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import saiyi.com.xiande.base.MyMethod;

/**
 * 订单信息
 */
public class DOrderInfo {
    public String mOrderId;//订单编号
    public String mUserId;//用户账号
    public String mBikeNo;//车辆编码
    public String mBikeEncode;//车辆二维码
    public Date mStartTime;//开始租车时间
    public Date mCurrentTime;//当前时间
    public Date mEndTime;//结束时间
    public double mSpendTime;//租车花费时间
    public double mCost;//租车费用
    public int mIsOver;//是否还车1为还车、0为未还车,默认为0
    public String mStartPos;//开始地点
    public String mEndPos;//结束地点

    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();

        try {jsonObject.put("orderId",mOrderId);} catch (JSONException ignored) {}
        try {jsonObject.put("userId",mUserId);} catch (JSONException ignored) {}
        try {jsonObject.put("carNum", mBikeNo);} catch (JSONException ignored) {}
        try {jsonObject.put("carEncode", mBikeEncode);} catch (JSONException ignored) {}
        try {jsonObject.put("startTime",mStartTime);} catch (JSONException ignored) {}
        try {jsonObject.put("currentTime",mCurrentTime);} catch (JSONException ignored) {}
        try {jsonObject.put("endTime",mEndTime);} catch (JSONException ignored) {}
        try {jsonObject.put("spendTime",mSpendTime);} catch (JSONException ignored) {}
        try {jsonObject.put("cost",mCost);} catch (JSONException ignored) {}
        try {jsonObject.put("isOver",mIsOver);} catch (JSONException ignored) {}
        try {jsonObject.put("carPoint",mStartPos);} catch (JSONException ignored) {}
        try {jsonObject.put("returnCarPoint",mEndPos);} catch (JSONException ignored) {}

        return jsonObject;
    }

    public static DOrderInfo fromJSONObject(JSONObject jsonObject){
        DOrderInfo dOrderInfo = new DOrderInfo();
        if(jsonObject != null){

            try {dOrderInfo.mOrderId = jsonObject.getString("orderId");} catch (JSONException ignored) {}
            try {dOrderInfo.mUserId = jsonObject.getString("userId");} catch (JSONException ignored) {}
            try {dOrderInfo.mBikeNo = jsonObject.getString("carNum");} catch (JSONException ignored) {}
            try {dOrderInfo.mBikeEncode = jsonObject.getString("carEncode");} catch (JSONException ignored) {}
            try {dOrderInfo.mStartTime = MyMethod.getDateFromString(jsonObject.getString("startTime"));} catch (JSONException ignored) {}
            try {dOrderInfo.mCurrentTime = MyMethod.getDateFromString(jsonObject.getString("currentTime"));} catch (JSONException ignored) {}
            try {dOrderInfo.mEndTime = MyMethod.getDateFromString(jsonObject.getString("endTime"));} catch (JSONException ignored) {}
            try {dOrderInfo.mSpendTime = MyMethod.parseDouble(jsonObject.getString("spendTime"));} catch (JSONException ignored) {}
            try {dOrderInfo.mCost = MyMethod.parseDouble(jsonObject.getString("cost"));} catch (JSONException ignored) {}
            try {dOrderInfo.mIsOver = MyMethod.parseInt(jsonObject.getString("isOver"));} catch (JSONException ignored) {}
            try {dOrderInfo.mStartPos = jsonObject.getString("carPoint");} catch (JSONException ignored) {}
            try {dOrderInfo.mEndPos = jsonObject.getString("returnCarPoint");} catch (JSONException ignored) {}

        }
        return dOrderInfo;
    }

    public static List<DOrderInfo> fromJSONArray(JSONArray jsonArray){
        List<DOrderInfo> list = new ArrayList<>();
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
