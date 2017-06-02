package saiyi.com.xiande.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 收款
 */
public class DReceive {
    public String mAlipaySeller;//支付宝商户收款账号
    public String mAlipayPartner;//支付宝Id
    public String mAlipaykey;//支付宝私匙
    public String mWXPartner;//微信商户Id
    public String mWXPrepayId;//微信会话Id
    public String mWXNonceStr;//微信支付随机数
    public String mWXSign;//微信支付签名

    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();

        try {jsonObject.put("alipaySeller",mAlipaySeller);} catch (JSONException ignored) {}
        try {jsonObject.put("alipayPartner",mAlipayPartner);} catch (JSONException ignored) {}
        try {jsonObject.put("alipaykey",mAlipaykey);} catch (JSONException ignored) {}
        try {jsonObject.put("wxPartner",mWXPartner);} catch (JSONException ignored) {}
        try {jsonObject.put("wxPrepayId",mWXPrepayId);} catch (JSONException ignored) {}
        try {jsonObject.put("wxNonceStr",mWXNonceStr);} catch (JSONException ignored) {}
        try {jsonObject.put("wxSign",mWXSign);} catch (JSONException ignored) {}

        return jsonObject;
    }

    public static DReceive fromJSONObject(JSONObject jsonObject){
        DReceive dReceive = new DReceive();
        if(jsonObject != null){

            try {dReceive.mAlipaySeller = jsonObject.getString("alipaySeller");} catch (JSONException ignored) {}
            try {dReceive.mAlipayPartner = jsonObject.getString("alipayPartner");} catch (JSONException ignored) {}
            try {dReceive.mAlipaykey = jsonObject.getString("alipaykey");} catch (JSONException ignored) {}
            try {dReceive.mWXPartner = jsonObject.getString("wxPartner");} catch (JSONException ignored) {}
            try {dReceive.mWXPrepayId = jsonObject.getString("wxPrepayId");} catch (JSONException ignored) {}
            try {dReceive.mWXNonceStr = jsonObject.getString("wxNonceStr");} catch (JSONException ignored) {}
            try {dReceive.mWXSign = jsonObject.getString("wxSign");} catch (JSONException ignored) {}

        }
        return dReceive;
    }

    public static List<DReceive> fromJSONArray(JSONArray jsonArray){
        List<DReceive> list = new ArrayList<>();
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
