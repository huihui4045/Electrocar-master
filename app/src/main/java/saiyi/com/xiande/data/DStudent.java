package saiyi.com.xiande.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import saiyi.com.xiande.base.MyMethod;

/**
 * 学生信息
 */
public class DStudent {
    public String mUserId;
    public String mPwd;
    public String mSidNo;//学号
    public String mName;
    public int mSex = -1;//女0，男1
    public int mAge;
    public String mAddress;
    public String mIdNo;//身份证号码
    public String mSchool;//学校
    public String mDepartment;//院系
    public String mMajor;//专业
    public String mClass;//班级
    public String mPhone;//联系电话
    public int mIsValid;//审核通过1,没通过为0  默认为0

    public String mHeadPic;//头像
    public String mIdFace;//身份证正面
    public String mIdBack;//身份证背面
    public String mSidPic;//学生证
    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();

        try {jsonObject.put("userId",mUserId);} catch (JSONException ignored) {}
        try {jsonObject.put("name",mName);} catch (JSONException ignored) {}
        try {jsonObject.put("school",mSchool);} catch (JSONException ignored) {}
        try {jsonObject.put("isvalid",mIsValid);} catch (JSONException ignored) {}

        try {jsonObject.put("headPic",mHeadPic);} catch (JSONException ignored) {}
        try {jsonObject.put("cardPicFace",mIdFace);} catch (JSONException ignored) {}
        try {jsonObject.put("cardPicBack",mIdBack);} catch (JSONException ignored) {}
        try {jsonObject.put("studentPic",mSidPic);} catch (JSONException ignored) {}

        return jsonObject;
    }

    public static DStudent fromJSONObject(JSONObject jsonObject){
        DStudent dStudent = new DStudent();
        if(jsonObject != null){

            try {dStudent.mUserId = jsonObject.getString("userId");} catch (JSONException ignored) {}
            try {dStudent.mPwd = jsonObject.getString("pwd");} catch (JSONException ignored) {}
            try {dStudent.mSidNo = jsonObject.getString("studentId");} catch (JSONException ignored) {}
            try {dStudent.mName = jsonObject.getString("name");} catch (JSONException ignored) {}
            try {dStudent.mSex = MyMethod.parseInt(jsonObject.getString("sex"));} catch (JSONException ignored) {}
            try {dStudent.mAge = MyMethod.parseInt(jsonObject.getString("age"));} catch (JSONException ignored) {}
            try {dStudent.mAddress = jsonObject.getString("address");} catch (JSONException ignored) {}
            try {dStudent.mIdNo = jsonObject.getString("cardId");} catch (JSONException ignored) {}
            try {dStudent.mSchool = jsonObject.getString("school");} catch (JSONException ignored) {}
            try {dStudent.mDepartment = jsonObject.getString("studentDepartment");} catch (JSONException ignored) {}
            try {dStudent.mMajor = jsonObject.getString("professional");} catch (JSONException ignored) {}
            try {dStudent.mClass = jsonObject.getString("clazz");} catch (JSONException ignored) {}
            try {dStudent.mPhone = jsonObject.getString("phone");} catch (JSONException ignored) {}
            try {dStudent.mIsValid = MyMethod.parseInt(jsonObject.getString("isvalid"));} catch (JSONException ignored) {}

            try {dStudent.mHeadPic = jsonObject.getString("headPic");} catch (JSONException ignored) {}
            try {dStudent.mIdFace = jsonObject.getString("cardPicFace");} catch (JSONException ignored) {}
            try {dStudent.mIdBack = jsonObject.getString("cardPicBack");} catch (JSONException ignored) {}
            try {dStudent.mSidPic = jsonObject.getString("studentPic");} catch (JSONException ignored) {}
        }
        return dStudent;
    }

    public static List<DStudent> fromJSONArray(JSONArray jsonArray){
        List<DStudent> list = new ArrayList<>();
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
