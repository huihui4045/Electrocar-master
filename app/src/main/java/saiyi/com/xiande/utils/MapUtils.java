package saiyi.com.xiande.utils;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.PolylineOptions;
import java.util.List;

/**
 * 描述：地图工具类，主要含有坐标值转换，中心点坐标计算等
 * 创建作者：黎丝军
 * 创建时间：2016/11/24 11:26
 */

public class MapUtils {

    /**
     * 获取两点中心坐标
     * @param aLatLng a点
     * @param bLatLng b点
     * @return 中心坐标
     */
    public static LatLng getCenterLatLng(LatLng aLatLng, LatLng bLatLng) {
        final double centerX = (aLatLng.latitude + bLatLng.latitude) / 2;
        final double centerY = (aLatLng.longitude + bLatLng.longitude) / 2;
        return new LatLng(centerX,centerY);
    }

    /**
     * 根据路线坐标列表，线宽度和线颜色来创建一个PolyLineOptions实例
     * @param pathList 路径列表
     * @param width 宽度
     * @param color 颜色值
     * @return PolylineOptions实例
     */
    public static PolylineOptions getPolyLine(List<LatLng> pathList,int width,int color) {
        return new PolylineOptions().addAll(pathList).width(width).color(color);
    }

    /**
     * 开始一个新的镜头
     * @param aMap 地图
     * @param point 点坐标
     */
    public static void animateCamera(AMap aMap,LatLng point) {
        animateCamera(aMap,point,point);
    }

    /**
     * 开始一个新的镜头,新的镜头在两个点中间坐标
     * @param aMap 地图
     * @param aPoint 开始点坐标
     * @param bPoint 结束点坐标
     */
    public static void animateCamera(AMap aMap,LatLng aPoint,LatLng bPoint) {
        animateCamera(aMap,aPoint,bPoint,0);
    }

    /**
     * 开始一个新的镜头,新的镜头在两个点中间坐标
     * @param aMap 地图
     * @param aPoint 开始点坐标
     * @param bPoint 结束点坐标
     * @param keepTime 持续时间
     */
    public static void animateCamera(AMap aMap,LatLng aPoint,LatLng bPoint,long keepTime) {
        animateCamera(aMap,aPoint,bPoint,0,0,keepTime);
    }

    /**
     * 开始一个新的镜头,新的镜头在两个点中间坐标
     * @param aMap 地图
     * @param aPoint 开始点坐标
     * @param bPoint 结束点坐标
     * @param angle 俯仰角0°~45°（垂直与地图时为0）
     * @param yawAngle 偏航角 0~360° (正北方为0)
     * @param keepTime 持续时间
     */
    public static void animateCamera(AMap aMap,LatLng aPoint,LatLng bPoint,int angle,int yawAngle,long keepTime) {
        animateCamera(aMap,aPoint,bPoint,angle,yawAngle,keepTime,null);
    }

    /**
     * 开始一个新的镜头,新的镜头在两个点中间坐标
     * @param aMap 地图
     * @param aPoint 开始点坐标
     * @param bPoint 结束点坐标
     * @param angle 俯仰角0°~45°（垂直与地图时为0）
     * @param yawAngle 偏航角 0~360° (正北方为0)
     * @param keepTime 持续时间
     * @param cancelableCallback 取消回调接口
     */
    public static void animateCamera(AMap aMap,LatLng aPoint,LatLng bPoint,int angle,int yawAngle,long keepTime,AMap.CancelableCallback cancelableCallback) {
        aMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                getCenterLatLng(aPoint,bPoint),//新的中心点坐标
                aMap.getScalePerPixel(), //新的缩放级别
                angle, //俯仰角0°~45°（垂直与地图时为0）
                yawAngle  //偏航角 0~360° (正北方为0)
        )),keepTime,cancelableCallback);
    }

    /**
     * 移动摄像镜头
     * @param aMap 地图实例
     * @param location 定位位置
     */
    public static void moveCamera(AMap aMap,AMapLocation location) {
        moveCamera(aMap,new LatLng(location.getLatitude(),location.getLongitude()));
    }

    /**
     * 移动摄像镜头
     * @param aMap 地图实例
     * @param location 定位位置
     */
    public static void moveCamera(AMap aMap,LatLng location) {
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                location,//新的中心点坐标
                aMap.getScalePerPixel(), //新的缩放级别
                0, //俯仰角0°~45°（垂直与地图时为0）
                0  //偏航角 0~360° (正北方为0)
        )));
    }
}
