package saiyi.com.xiande.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import saiyi.com.xiande.base.MyMethod;

/**
 * 故障
 */
public class DProblem {
    public String mEncode;//车辆二维码
    public String mDescribe;//故障描述
    public String mPic;//图片路径
    public String mUserId;//申报故障人
    public String mPhone;//申报人电话
    public Date mOccurTime;//故障发生时间

    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();

        try {jsonObject.put("carEncode",mEncode);} catch (JSONException ignored) {}
        try {jsonObject.put("pdescribe",mDescribe);} catch (JSONException ignored) {}
        try {jsonObject.put("pic",mPic);} catch (JSONException ignored) {}
        try {jsonObject.put("userId",mUserId);} catch (JSONException ignored) {}
        try {jsonObject.put("phone",mPhone);} catch (JSONException ignored) {}
        try {jsonObject.put("occurTime",mOccurTime);} catch (JSONException ignored) {}

        return jsonObject;
    }

    public static DProblem fromJSONObject(JSONObject jsonObject){
        DProblem dProblem = new DProblem();
        if(jsonObject != null){

            try {dProblem.mEncode = jsonObject.getString("carEncode");} catch (JSONException ignored) {}
            try {dProblem.mDescribe = jsonObject.getString("pdescribe");} catch (JSONException ignored) {}
            try {dProblem.mPic = jsonObject.getString("pic");} catch (JSONException ignored) {}
            try {dProblem.mUserId = jsonObject.getString("userId");} catch (JSONException ignored) {}
            try {dProblem.mPhone = jsonObject.getString("phone");} catch (JSONException ignored) {}
            try {dProblem.mOccurTime = MyMethod.getDateFromString(jsonObject.getString("occurTime"));} catch (JSONException ignored) {}

        }
        return dProblem;
    }

    public static List<DProblem> fromJSONArray(JSONArray jsonArray){
        List<DProblem> list = new ArrayList<>();
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
