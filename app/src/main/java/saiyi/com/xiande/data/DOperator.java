package saiyi.com.xiande.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import saiyi.com.xiande.base.MyMethod;

/**
 * 操作员
 */
public class DOperator {
    public String mUserId;//操作员账号
    public String mPwd;//密码
    public String mName;//姓名
    public int mType;//角色类型：0商家、1代理、2财务、3维修、4代理商财务、5代理商维修
    public double mRebatePoint;//回扣点，只有代理商身份才有
    public int mIsEnable;//0不可用、 1可用  默认为1

    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();

        try {jsonObject.put("userId",mUserId);} catch (JSONException ignored) {}
        try {jsonObject.put("pwd",mPwd);} catch (JSONException ignored) {}
        try {jsonObject.put("name",mName);} catch (JSONException ignored) {}
        try {jsonObject.put("type",mType);} catch (JSONException ignored) {}
        try {jsonObject.put("rebatePoint",mRebatePoint);} catch (JSONException ignored) {}
        try {jsonObject.put("isEnable",mIsEnable);} catch (JSONException ignored) {}

        return jsonObject;
    }

    public static DOperator fromJSONObject(JSONObject jsonObject){
        DOperator dOperator = new DOperator();
        if(jsonObject != null){

            try {dOperator.mUserId = jsonObject.getString("userId");} catch (JSONException ignored) {}
            try {dOperator.mPwd = jsonObject.getString("pwd");} catch (JSONException ignored) {}
            try {dOperator.mName = jsonObject.getString("name");} catch (JSONException ignored) {}
            try {dOperator.mType = MyMethod.parseInt(jsonObject.getString("type"));} catch (JSONException ignored) {}
            try {dOperator.mRebatePoint = MyMethod.parseDouble(jsonObject.getString("rebatePoint"));} catch (JSONException ignored) {}
            try {dOperator.mIsEnable = MyMethod.parseInt(jsonObject.getString("isEnable"));} catch (JSONException ignored) {}

        }
        return dOperator;
    }

    public static List<DOperator> fromJSONArray(JSONArray jsonArray){
        List<DOperator> list = new ArrayList<>();
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
