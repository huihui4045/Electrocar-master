package saiyi.com.xiande.base;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import saiyi.com.xiande.data.DBike;
import saiyi.com.xiande.data.DStudent;

/**
 * 封装HTTP方法
 */
public class Http {
    public static String UrlRoot = "http://117.34.112.134:8080/xiande/";//外网
    //public static String UrlRoot = "http://172.16.10.103:8086/xiande/";//内网
    //public static String UrlRoot = "http://172.16.3.59:8086/xiande/";
    public static String UrlUpload = UrlRoot + "file/fileupload";//上传文件
    public static String UrlDownload = UrlRoot + "resources/image/";//下载图片
    public static String UrlAuthCode = UrlRoot + "authcode/send";//获取验证码
    public static String UrlRegister = UrlRoot + "student/add";//注册
    public static String UrlLogin = UrlRoot + "student/login";//登录
    //public static String UrlSellerLogin = UrlRoot + "operator/querymerchant";//商户登录
    public static String UrlSellerLogin = UrlRoot + "operator/queryobj";//商户登录
    public static String UrlEditPwd = UrlRoot + "student/update";//修改密码，忘记密码
    public static String UrlUpdateUserInfo = UrlRoot + "student/updateone";//修改用户信息
    public static String UrlQueryUserInfo = UrlRoot + "student/queryone";//查询用户信息
    public static String UrlQueryBikeList = UrlRoot + "stopposition/querylist";//查询附近车辆信息
    public static String UrlQueryStopList = UrlRoot + "stopposition/querylistss";//查询附近停车场信息
    public static String UrlRentBike = UrlRoot + "orderinfo/add";//扫码租车
    public static String UrlLockBike = UrlRoot + "car/lock";//锁车/开锁
    public static String UrlCheckReturn = UrlRoot + "stopposition/check";//检查当前位置是否可还车
    public static String UrlReturnBike = UrlRoot + "orderinfo/update";//还车
    public static String UrlQueryBikeType = UrlRoot + "cartype/querylist";//查询车辆类型
    public static String UrlAddBike = UrlRoot + "car/add";//车辆录入
    public static String UrlGetBike = UrlRoot + "car/querylistApp";//查询车辆信息（还了车就查不到了）
    public static String UrlGetBikeAnyTime = UrlRoot + "car/querycar";//查询车辆信息(任何时候都能查到,但实时余额是不准的)
    public static String UrlQueryAccount = UrlRoot + "account/queryOne";//查询账户信息
    public static String UrlRecharge = UrlRoot + "recharge/add";//充值
    public static String UrlQueryCostList = UrlRoot + "cost/querylist";//查询收支明细
    public static String UrlQueryCost = UrlRoot + "cost/queryone";//查询收费
    public static String UrlQueryRefundList = UrlRoot + "refund/querylist";//查询退款
    public static String UrlApplyRefound = UrlRoot + "refund/add";//申请退款
    public static String UrlQueryOrder = UrlRoot + "orderinfo/querys";//查询我的订单
    public static String UrlQueryMsgList = UrlRoot + "message/querylist";//查询我的消息列表
    public static String UrlQueryDict = UrlRoot + "dict/query";//查询商家设定的各种参数
    public static String UrlAddProblem = UrlRoot + "problem/add";//故障反馈
    public static String UrlCheckPhone = UrlRoot + "student/checkphone";//（修改手机时）检查（原）手机号
    public static String UrlUpdatePhone = UrlRoot + "student/updatephone";//修改手机号
    public static String UrlGetAlipayInfo = UrlRoot + "receive/query";//获取支付宝支付信息
    public static String UrlGetWXpayInfo = UrlRoot + "receive/querywx";//获取微信支付信息
    public static String UrlGetStopList = UrlRoot + "stopposition/querylistss";//商家获取停车场列表
    public static String UrlGetSchoolInfo = UrlRoot + "school/query";//查询学校

    public static String CXHeadPic       = "headPic";
    public static String CXCardPicFace   = "cardPicFace";
    public static String CXCardPicBack   = "cardPicBack";
    public static String CXStudentPic    = "studentPic";
    public static String CXProblemPic    = "problemPic";

    public static final Map<String , String> UrlMap = new HashMap<String , String>(){{
        put(UrlAuthCode, "获取验证码");  put(UrlRegister, "注册");  put(UrlLogin, "登录");  put(UrlSellerLogin, "商户登录");
        put(UrlEditPwd, "修改密码");  put(UrlUpdateUserInfo, "修改用户信息");  put(UrlQueryUserInfo, "查询用户信息");  put(UrlQueryBikeList, "查询附近车辆信息");
        put(UrlRentBike, "扫码租车");  put(UrlLockBike, "锁车/开锁");  put(UrlCheckReturn, "检查当前位置是否可还车");  put(UrlReturnBike, "还车");
        put(UrlQueryBikeType, "查询车辆类型");  put(UrlAddBike, "车辆录入");  put(UrlQueryAccount, "查询账户信息");  put(UrlRecharge, "充值");
        put(UrlQueryCostList, "查询收支明细");  put(UrlQueryCost, "查询收费");  put(UrlQueryRefundList, "查询退款");  put(UrlApplyRefound, "申请退款");
        put(UrlQueryStopList, "查询附近停车场");   put(UrlQueryOrder, "查询我的订单");   put(UrlGetBike, "查询车辆信息");put(UrlQueryMsgList, "查询我的消息列表");
        put(UrlQueryDict, "查询商家设定的各种参数");put(UrlAddProblem, "故障反馈");put(UrlCheckPhone, "检查原手机号");put(UrlUpdatePhone, "修改手机号");
        put(UrlGetAlipayInfo, "请求支付宝支付信息");put(UrlGetStopList, "商家获取停车场列表");put(UrlGetBikeAnyTime, "查询车辆信息-任何时候");
        put(UrlGetSchoolInfo,"查询学校信息");
    }};

    //获取验证码
    public static void getAuthCode(String phone,Handler feedbackHandler){
        if(MyMethod.isEmpty(phone)){
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone",phone);
            post(jsonObject, UrlAuthCode, feedbackHandler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //注册
    public static void regist(String userId,String phone,String pwd,String authCode,Handler feedbackHandler){
        if(MyMethod.isEmpty(userId) || MyMethod.isEmpty(phone) || MyMethod.isEmpty(pwd) || MyMethod.isEmpty(authCode)){
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",userId);
            jsonObject.put("phone",phone);
            jsonObject.put("pwd",pwd);
            jsonObject.put("authCode",authCode);
            post(jsonObject, UrlRegister, feedbackHandler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //学生登录
    public static void login(String phone,String verifyCode,Handler feedbackHandler){
        if(MyMethod.isEmpty(phone) || MyMethod.isEmpty(verifyCode)){
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone",phone);
            jsonObject.put("authCode",verifyCode);
            post(jsonObject, UrlLogin, feedbackHandler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //商户登录
    public static void sellerLogin(String userId,String pwd,Handler feedbackHandler){
        if(MyMethod.isEmpty(userId) || MyMethod.isEmpty(pwd)){
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",userId);
            jsonObject.put("pwd",pwd);
            post(jsonObject, UrlSellerLogin, feedbackHandler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //注册
    public static void editPwd(String userId,String phone,String pwd,String authCode,Handler feedbackHandler){
        if(MyMethod.isEmpty(userId) || MyMethod.isEmpty(phone) || MyMethod.isEmpty(pwd) || MyMethod.isEmpty(authCode)){
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",userId);
            jsonObject.put("phone",phone);
            jsonObject.put("pwd",pwd);
            jsonObject.put("authCode",authCode);
            post(jsonObject, UrlEditPwd, feedbackHandler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //修改用户信息
    public static void updateUserInfo(String userId,DStudent student,Handler feedbackHandler){
        if(student == null){
            return;
        }
        JSONObject jsonObject = student.toJSONObject();
        try {
            jsonObject.put("userId",userId);
            post(jsonObject, UrlUpdateUserInfo, feedbackHandler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //修改用户信息--手机号码
    public static void updatePhone(String userId,String phone,Handler feedbackHandler){
        if(MyMethod.isEmpty(userId) || MyMethod.isEmpty(phone)){
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",userId);
            jsonObject.put("phone",phone);
            post(jsonObject, UrlUpdateUserInfo, feedbackHandler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //获取用户信息
    public static void getUserInfo(String userId,Handler feedbackHandler){
        if(MyMethod.isEmpty(userId)){
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",userId);
            post(jsonObject, UrlQueryUserInfo, feedbackHandler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //查询附近车辆列表
    public static void getBikeList(String Longitude,String Latitude,int page,Handler feedbackHandler){
        if(MyMethod.isEmpty(Longitude) || MyMethod.isEmpty(Latitude)){
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("stopLongitude",Longitude);
            jsonObject.put("stopLatitud",Latitude);
            jsonObject.put("page",page);
            post(jsonObject, UrlQueryBikeList, feedbackHandler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询学校信息
     * @param feedbackHandler 接口回调
     */
    public static void getSchoolList(Handler feedbackHandler) {
        post(null,UrlGetSchoolInfo, feedbackHandler);
    }

    //查询车辆信息
    public static void getBike(String userId,String carEncode,Handler feedbackHandler){
        if(MyMethod.isEmpty(userId) || MyMethod.isEmpty(carEncode)){
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",userId);
            jsonObject.put("carEncode",carEncode);//车辆编号
            post(jsonObject, UrlGetBike, feedbackHandler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //查询车辆信息
    public static void getBikeAnyTime(String userId,String carEncode,Handler feedbackHandler){
        if(MyMethod.isEmpty(userId) || MyMethod.isEmpty(carEncode)){
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",userId);
            jsonObject.put("carEncode",carEncode);//车辆编号
            post(jsonObject, UrlGetBikeAnyTime, feedbackHandler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //扫码租车
    public static void rentBike(String userId,String carEncode,Handler feedbackHandler){
        if(MyMethod.isEmpty(userId) || MyMethod.isEmpty(carEncode)){
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",userId);
            jsonObject.put("carEncode",carEncode);
            post(jsonObject, UrlRentBike, feedbackHandler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //锁车/开锁
    public static void lockBike(String carEncode,int opr,Handler feedbackHandler){
        if(MyMethod.isEmpty(carEncode)){
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("carEncode",carEncode);
            jsonObject.put("lockStatus",opr);
            post(jsonObject, UrlLockBike, feedbackHandler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //查询附近停车场列表
    public static void getStopList(double Longitude,double Latitude,Handler feedbackHandler){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("stopLongitude",Longitude);
            jsonObject.put("stopLatitud",Latitude);
            post(jsonObject, UrlQueryStopList, feedbackHandler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //检查某地点是否可还车
    public static void checkReturn(double Longitude,double Latitude,Handler feedbackHandler){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("stopLongitude",Longitude);
            jsonObject.put("stopLatitud",Latitude);
            post(jsonObject, UrlCheckReturn, feedbackHandler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //还车,订单号，停车点
    public static void returnBike(String orderId,String endStop,Handler feedbackHandler){
        if(MyMethod.isEmpty(orderId) || MyMethod.isEmpty(endStop)){
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderId",orderId);
            //jsonObject.put("endStop",endStop);
            jsonObject.put("returnCarPoint",endStop);
            post(jsonObject, UrlReturnBike, feedbackHandler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //查询所有的车辆类型
    public static void queryAllBikeType(Handler feedbackHandler){
        post(null, UrlQueryBikeType, feedbackHandler);
    }

//    //商家，录入一辆车
//    public static void addBike(DBike bike,Handler feedbackHandler){
//        if(bike == null){
//            return;
//        }
//        JSONObject jsonObject = bike.toJSONObject();
//        post(jsonObject, UrlAddBike, feedbackHandler);
//    }

    //商家，录入一辆车
    //{"carEncode":"1","carNum":"1","carTypeId":"1","ownerId":"1","stopId":"1"}}
    public static void addBike(String carEncode,String carNum,String carTypeId,String ownerId,String stopId,Handler feedbackHandler){
        if(MyMethod.isEmpty(carEncode) || MyMethod.isEmpty(carNum) || MyMethod.isEmpty(carTypeId) || MyMethod.isEmpty(ownerId) || MyMethod.isEmpty(stopId)){
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("carEncode",carEncode);
            jsonObject.put("carNum",carNum);
            jsonObject.put("carTypeId",carTypeId);
            jsonObject.put("ownerId",ownerId);
            jsonObject.put("stopId",stopId);
            post(jsonObject, UrlAddBike, feedbackHandler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //查询账户信息
    public static void getAccount(String userId,Handler feedbackHandler){
        if(MyMethod.isEmpty(userId)){
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",userId);
            post(jsonObject, UrlQueryAccount, feedbackHandler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //账户充值
    public static void recharge(String userId,double rechargeAmount,Handler feedbackHandler){
        if(MyMethod.isEmpty(userId) || rechargeAmount<=0){
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",userId);
            jsonObject.put("rechargeAmount",rechargeAmount);
            post(jsonObject, UrlRecharge, feedbackHandler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //查询账户的收支明细
    public static void getCostList(String userId,Handler feedbackHandler){
        if(MyMethod.isEmpty(userId)){
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",userId);
            post(jsonObject, UrlQueryCostList, feedbackHandler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //查询账户的收支统计
    public static void getCostSum(String userId,Handler feedbackHandler){
        if(MyMethod.isEmpty(userId)){
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",userId);
            post(jsonObject, UrlQueryCost, feedbackHandler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //查询账户的退款列表
    public static void getRefundList(String userId,Handler feedbackHandler){
        if(MyMethod.isEmpty(userId)){
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",userId);
            post(jsonObject, UrlQueryRefundList, feedbackHandler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //申请退款（退押金）
    public static void applyRefund(String userId,Handler feedbackHandler){
        if(MyMethod.isEmpty(userId)){
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",userId);
            post(jsonObject, UrlApplyRefound, feedbackHandler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //查询我的未完成订单
    public static void getMyOrder_NoOver(String userId,Handler feedbackHandler){
        if(MyMethod.isEmpty(userId)){
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",userId);
            jsonObject.put("isOver",0);//未完成的
            post(jsonObject, UrlQueryOrder, feedbackHandler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //查询消息列表
    public static void getMsgList(Handler feedbackHandler){
        JSONObject jsonObject = new JSONObject();
        post(jsonObject, UrlQueryMsgList, feedbackHandler);
    }

    //查询商家设定的各种参数
    public static void getDict(Handler feedbackHandler){
        JSONObject jsonObject = new JSONObject();
        post(jsonObject, UrlQueryDict, feedbackHandler);
    }

    //故障反馈
    public static void addProblem(JSONObject jsonObject,Handler feedbackHandler){
        if(jsonObject == null){
            return;
        }
        post(jsonObject, UrlAddProblem, feedbackHandler);
    }

    //检查手机号
    public static void checkPhone(String userId,String phone,String authCode,Handler feedbackHandler){
        if(MyMethod.isEmpty(userId) || MyMethod.isEmpty(phone) || MyMethod.isEmpty(authCode)){
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",userId);
            jsonObject.put("phone",phone);
            jsonObject.put("authCode",authCode);
            post(jsonObject, UrlCheckPhone, feedbackHandler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //修改手机号
    public static void updatePhone(String userId,String phone,String authCode,Handler feedbackHandler){
        if(MyMethod.isEmpty(userId) || MyMethod.isEmpty(phone) || MyMethod.isEmpty(authCode)){
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",userId);
            jsonObject.put("phone",phone);
            jsonObject.put("authCode",authCode);
            post(jsonObject, UrlUpdatePhone, feedbackHandler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //请求支付信息
    public static void getAlipayInfo(String userId,double rechargeAmount,Handler feedbackHandler){
        if(MyMethod.isEmpty(userId)){
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",userId);
            jsonObject.put("rechargeAmount",rechargeAmount);
            post(jsonObject, UrlGetAlipayInfo, feedbackHandler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //请求微信支付订单信息
    public static void getWXpayInfo(String userId,double rechargeAmount,Handler feedbackHandler){
        if(MyMethod.isEmpty(userId)){
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",userId);
            jsonObject.put("rechargeAmount",rechargeAmount);
            post(jsonObject, UrlGetWXpayInfo, feedbackHandler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //获取停车场列表（商家）
    public static void getStopList(String userId,Handler feedbackHandler){
        if(MyMethod.isEmpty(userId)){
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",userId);
            post(jsonObject, UrlGetStopList, feedbackHandler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void post(JSONObject jsonObject,String url,Handler feedbackHandler){
        new postThread(jsonObject,url,feedbackHandler).start();
    }

    //发送 HTTP POST 的线程
    static class postThread extends Thread {
        protected Handler mFeedbackHandler;
        protected String mUrl;
        protected JSONObject mJsonObject;

        public postThread(JSONObject jsonObject,String url,Handler feedbackHandler) {
            mJsonObject = jsonObject;
            mUrl = url;
            mFeedbackHandler = feedbackHandler;
        }

        @Override
        public void run() {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60*1000);// 请求超时
                httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60*1000);// 读取超时
                HttpPost httppost = new HttpPost(mUrl);
                httppost.addHeader("Content-Type", "application/json;charset=UTF-8");
                if(mJsonObject != null) {
                    StringEntity entity = new StringEntity(mJsonObject.toString(),"UTF-8");
                    entity.setContentEncoding("UTF-8");
                    httppost.setEntity(entity);
                    Log.i("http 发送", UrlMap.get(mUrl) + " , " + mJsonObject.toString());
                }
                HttpResponse response;
                response = httpclient.execute(httppost);
                //检验状态码，如果成功接收数据
                int code = response.getStatusLine().getStatusCode();
                if (code == 200) {
                    String rev = EntityUtils.toString(response.getEntity());
                    Log.i("http 收到", UrlMap.get(mUrl) + " , " + rev);
                    JSONObject jsonObject = new JSONObject(rev);
                    if(jsonObject.has("data")) {
                        MyMethod.sendMsgToHandler(mFeedbackHandler, getJsonArray(jsonObject,"data"), jsonObject.getInt("result"));
                    }else if(jsonObject.has("rows")) {
                        MyMethod.sendMsgToHandler(mFeedbackHandler, getJsonArray(jsonObject,"rows"), jsonObject.getInt("result"));
                    }else {
                        MyMethod.sendMsgToHandler(mFeedbackHandler, jsonObject.getInt("result"));
                    }
                }else{
                    Log.i("http 收到", UrlMap.get(mUrl) + " , " + "code = " + code);
                    MyMethod.sendMsgToHandler(mFeedbackHandler,0);
                }
            } catch (Exception e) {
                e.printStackTrace();
                MyMethod.sendMsgToHandler(mFeedbackHandler, 0);
            }
        }

        //从 jsonObject 中取出 key 区域的值，作为 JSONArray 返回。如果不是 JSONArray 则转换成 JSONArray，如果是空的 JSONArray 则返回 null
        private JSONArray getJsonArray(JSONObject jsonObject,String key){
            JSONArray jsonArray = new JSONArray();
            try {
                jsonArray = jsonObject.getJSONArray(key);
            } catch (JSONException e) {
                try {
                    JSONObject jo = jsonObject.getJSONObject(key);
                    jsonArray.put(0,jo);
                }catch (JSONException e1) {
                    try {
                        jsonArray.put(0,jsonObject);
                    } catch (JSONException ignored) {}
                }
            }
            if(jsonArray.length() > 0) {
                return jsonArray;
            }else{
                return null;
            }
        }
    }


    public static void uploadPic(String filePath,String reName,Handler feedbackHandler){
        new uploadPicThread(filePath,reName,feedbackHandler).start();
    }

    public static void uploadPic(Bitmap bitmap,String reName,Handler feedbackHandler){
        new uploadPicThread(bitmap,reName,feedbackHandler).start();
    }

    //发送 HTTP POST 的线程
    static class uploadPicThread extends Thread {
        protected Handler mFeedbackHandler;
        protected String mFilePath;//上传Bitmap或文件
        protected String mReName;
        protected Bitmap mBitmap;//上传Bitmap或文件

        public uploadPicThread(String filePath, String reName, Handler feedbackHandler) {
            mFilePath = filePath;
            mFeedbackHandler = feedbackHandler;
            mReName = reName;
        }

        public uploadPicThread(Bitmap bitmap, String reName, Handler feedbackHandler) {
            mBitmap = bitmap;
            mFeedbackHandler = feedbackHandler;
            mReName = reName;
        }

        @Override
        public void run() {
            try {
                String boundary = UUID.randomUUID().toString(); // 边界标识 随机生成
                String prefix = "--", end = "\r\n";
                HttpURLConnection conn = getHttpURLConnection(UrlUpload, boundary);
                OutputStream outputSteam = conn.getOutputStream();
                DataOutputStream dos = new DataOutputStream(outputSteam);
                if (mFilePath != null) {
                    File file = new File(mFilePath);
                    if (file.exists()) {
                        writePart_File(dos, file, mReName, "image/jpeg", boundary);
                    }
                }else if(mBitmap != null){
                    writePart_Bitmap(dos, mBitmap, mReName, "image/jpeg", boundary);
                }
                dos.writeBytes(prefix + boundary + prefix + end);//结束 http 流
                dos.flush();
                int res = conn.getResponseCode();
                if (res == 200) {//获取响应码 200=成功 当响应成功，获取响应的流
                    //conn.getInputStream();
                    MyMethod.sendMsgToHandler(mFeedbackHandler, 1);
                } else {
                    MyMethod.sendMsgToHandler(mFeedbackHandler, 0);
                }

            } catch (IOException e) {
                e.printStackTrace();
                MyMethod.sendMsgToHandler(mFeedbackHandler, 0);
            }
        }
    }

    //获取一个 HttpURLConnection
    public static  HttpURLConnection getHttpURLConnection(String urlstr, String boundary) throws IOException {
        int TIME_OUT = 24 * 60 * 60 * 1000; // 超时时间一天
        URL url = new URL(urlstr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(TIME_OUT);
        conn.setConnectTimeout(TIME_OUT);
        conn.setDoInput(true); // 允许输入流
        conn.setDoOutput(true); // 允许输出流
        conn.setUseCaches(false); // 不允许使用缓存
        conn.setRequestMethod("POST"); // 请求方式
        conn.setRequestProperty("Charset", "utf-8"); // 设置编码
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Content-Type", "multipart/form-data" + ";boundary=" + boundary);
        return conn;
    }

    //multipart 的报文里写一个 (文件的)part
    public static  void writePart_File(DataOutputStream dos,File file,String reName,String fileType,String boundary) throws IOException {
        String prefix = "--", end = "\r\n";
        dos.writeBytes(prefix + boundary + end);
        String fileAtrr;
        if(MyMethod.isEmpty(reName)) {
            fileAtrr = "Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"" + end;
        }else {
            fileAtrr = "Content-Disposition: form-data; name=\"file\"; filename=\"" + reName + "\"" + end;
        }
        fileAtrr += "Content-Type: " + fileType + "; Charset=" + "utf-8" + end + end;

        dos.writeBytes(fileAtrr);

        InputStream is = new FileInputStream(file);
        byte[] bytes = new byte[8192];//8k
        int len = 0;
        while ((len = is.read(bytes)) != -1) {
            dos.write(bytes, 0, len);
            dos.flush();
        }
        is.close();
        dos.write(end.getBytes());//文件结束标志
    }

    //multipart 的报文里写一个 (文件的)part
    public static  void writePart_Bitmap(DataOutputStream dos,Bitmap bitmap,String reName,String fileType,String boundary) throws IOException {
        String prefix = "--", end = "\r\n";
        dos.writeBytes(prefix + boundary + end);
        String fileAtrr;
        if(MyMethod.isEmpty(reName)) {
            return;
        }else {
            fileAtrr = "Content-Disposition: form-data; name=\"file\"; filename=\"" + reName + "\"" + end;
        }
        fileAtrr += "Content-Type: " + fileType + "; Charset=" + "utf-8" + end + end;

        dos.writeBytes(fileAtrr);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());

        //InputStream is = new FileInputStream(file);
        byte[] bytes = new byte[8192];//8k
        int len = 0;
        while ((len = is.read(bytes)) != -1) {
            dos.write(bytes, 0, len);
            dos.flush();
        }
        is.close();
        dos.write(end.getBytes());//文件结束标志
    }

}
