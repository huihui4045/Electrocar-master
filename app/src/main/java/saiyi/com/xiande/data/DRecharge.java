package saiyi.com.xiande.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import saiyi.com.xiande.base.MyMethod;

/**
 * 充值信息
 */
public class DRecharge {
    public String mRechargeId;//充值号
    public String mUserId;//学生账号
    public double mRechargeAmount;//充值金额
    public Date mRechargeTime;//充值时间

    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();

        try {jsonObject.put("rechargeId",mRechargeId);} catch (JSONException ignored) {}
        try {jsonObject.put("userId",mUserId);} catch (JSONException ignored) {}
        try {jsonObject.put("rechargeAmount",mRechargeAmount);} catch (JSONException ignored) {}
        try {jsonObject.put("rechargeTime",mRechargeTime);} catch (JSONException ignored) {}

        return jsonObject;
    }

    public static DRecharge fromJSONObject(JSONObject jsonObject){
        DRecharge dRecharge = new DRecharge();
        if(jsonObject != null){

            try {dRecharge.mRechargeId = jsonObject.getString("rechargeId");} catch (JSONException ignored) {}
            try {dRecharge.mUserId = jsonObject.getString("userId");} catch (JSONException ignored) {}
            try {dRecharge.mRechargeAmount = MyMethod.parseDouble(jsonObject.getString("rechargeAmount"));} catch (JSONException ignored) {}
            try {dRecharge.mRechargeTime = MyMethod.getDateFromString(jsonObject.getString("rechargeTime"));} catch (JSONException ignored) {}

        }
        return dRecharge;
    }

    public static List<DRecharge> fromJSONArray(JSONArray jsonArray){
        List<DRecharge> list = new ArrayList<>();
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
