package saiyi.com.xiande.data;

import com.amap.api.location.AMapLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * 缓存各种数据
 */
public class MyData {
    public int mLoginType;//登录用户类型：0学生，1商户，2调度员
    public String mUserId;//用户名
    public String mPwd;//密码
    public boolean mIsSeller = false;//是否商户
    public DStudent mStudent = new DStudent();//学生用户信息
    public List<DBikeType> mBikeTypeList = new ArrayList<>();//车辆类型列表
    public List<DBike> mBikeList = new ArrayList<>();//附近车辆列表
    public DBike mCurBike;//当前车辆
    public DBike mScanBike;//用于扫描获取的自行车
    public AMapLocation mLastLoc;//我最后一次定位的位置
    public DAccount mAccount;//账户余额信息
    public List<DStopPosition> mSPosList;//附近停车点列表
    public long mCurOrderId;//当前的订单ID，0表示没有
    public DOrderInfo mCurOrder;//本次租车的订单信息，仅用于传递数据
    public List<DCost> mCostList;//收支明细
    public DProblem mCurProblem;//故障信息，用于保存

    public List<DMessage> mMsgList;
    public DDict mDict;//商家定义的各种标准

    public DBikeType msChooseBikeType;//商家选择的车辆类型
    public DStopPosition msChooseStop;//商家选择的停车场
}
