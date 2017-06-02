package saiyi.com.xiande.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import saiyi.com.xiande.base.MyMethod;

/**
 * 退款
 */
public class DRefund {
    public String mRefundId;//退款编号
    public String mUserId;//用户账号
    public int mStatus;//退款状态,2拒绝,1同意,默认待退款0
    public Date mApplyTime;//申请日期
    public Date mAgreeTime;//同意退款时间
    public Date mRefundTime;//完成退款时间
    public double mRefundableAmount;//可退金额
    public double mRealAmount;//实际退款金额

    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();

        try {jsonObject.put("refundId",mRefundId);} catch (JSONException ignored) {}
        try {jsonObject.put("userId",mUserId);} catch (JSONException ignored) {}
        try {jsonObject.put("refundStatus",mStatus);} catch (JSONException ignored) {}
        try {jsonObject.put("applyTime",mApplyTime);} catch (JSONException ignored) {}
        try {jsonObject.put("agreeTime",mAgreeTime);} catch (JSONException ignored) {}
        try {jsonObject.put("refundTime",mRefundTime);} catch (JSONException ignored) {}
        try {jsonObject.put("refundableAmount",mRefundableAmount);} catch (JSONException ignored) {}
        try {jsonObject.put("realAmount",mRealAmount);} catch (JSONException ignored) {}

        return jsonObject;
    }

    public static DRefund fromJSONObject(JSONObject jsonObject){
        DRefund dRefund = new DRefund();
        if(jsonObject != null){

            try {dRefund.mRefundId = jsonObject.getString("refundId");} catch (JSONException ignored) {}
            try {dRefund.mUserId = jsonObject.getString("userId");} catch (JSONException ignored) {}
            try {dRefund.mStatus = Integer.parseInt(jsonObject.getString("refundStatus"));} catch (JSONException ignored) {}
            try {dRefund.mApplyTime = MyMethod.getDateFromString(jsonObject.getString("applyTime"));} catch (JSONException ignored) {}
            try {dRefund.mAgreeTime = MyMethod.getDateFromString(jsonObject.getString("agreeTime"));} catch (JSONException ignored) {}
            try {dRefund.mRefundTime = MyMethod.getDateFromString(jsonObject.getString("refundTime"));} catch (JSONException ignored) {}
            try {dRefund.mRefundableAmount = MyMethod.parseDouble(jsonObject.getString("refundableAmount"));} catch (JSONException ignored) {}
            try {dRefund.mRealAmount = MyMethod.parseDouble(jsonObject.getString("realAmount"));} catch (JSONException ignored) {}

        }
        return dRefund;
    }

    public static List<DRefund> fromJSONArray(JSONArray jsonArray){
        List<DRefund> list = new ArrayList<>();
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
