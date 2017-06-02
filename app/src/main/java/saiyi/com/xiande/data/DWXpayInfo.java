package saiyi.com.xiande.data;

import com.tencent.mm.sdk.modelpay.PayReq;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import saiyi.com.xiande.base.AppConstants;
import saiyi.com.xiande.base.MyMethod;

/**
 * 微信订单信息
 */
public class DWXpayInfo {
    public String mAppId;
    public String mTime_Stamp;
    public String mPackageValue;
    public String mPrepayId;
    public String mMch_Id;
    public String mNonce_Str;
    public String mSign;

    public PayReq toPayReq(){
        PayReq req = new PayReq();
        req.appId			= mAppId;
        req.partnerId		= mMch_Id;
        req.prepayId		= mPrepayId;
        req.nonceStr		= mNonce_Str;
        req.timeStamp		= mTime_Stamp;
        req.packageValue	= mPackageValue;
        req.sign			= mSign;
        return req;
    }

    public static DWXpayInfo fromJSONObject(JSONObject jsonObject){
        DWXpayInfo dwXpayInfo = new DWXpayInfo();
        if(jsonObject != null){

            try {dwXpayInfo.mAppId = jsonObject.getString("appid");} catch (JSONException ignored) {}
            try {dwXpayInfo.mTime_Stamp = jsonObject.getString("time_stamp");} catch (JSONException ignored) {}
            try {dwXpayInfo.mPackageValue = jsonObject.getString("packageValue");} catch (JSONException ignored) {}
            try {dwXpayInfo.mPrepayId = jsonObject.getString("prepayid");} catch (JSONException ignored) {}
            try {dwXpayInfo.mMch_Id = jsonObject.getString("mch_id");} catch (JSONException ignored) {}
            try {dwXpayInfo.mNonce_Str = jsonObject.getString("nonce_str");} catch (JSONException ignored) {}
            try {dwXpayInfo.mSign = jsonObject.getString("sign");} catch (JSONException ignored) {}
        }
        return dwXpayInfo;
    }

    public static List<DWXpayInfo> fromJSONArray(JSONArray jsonArray){
        List<DWXpayInfo> list = new ArrayList<>();
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
