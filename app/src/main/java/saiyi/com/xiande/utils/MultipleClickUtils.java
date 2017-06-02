package saiyi.com.xiande.utils;

import android.os.SystemClock;

/**
 * 双击事件 工具类
 */
public class MultipleClickUtils {
    private OnMultipleClickListener onMultipleClickListener;

    //存储时间的数组
    private long[] mHits;

    public MultipleClickUtils(int num, OnMultipleClickListener listener) {
        this.mHits = new long[num];
        this.onMultipleClickListener = listener;
    }

    /**
     * 双击事件、多击事件
     */
    public void multipleClick() {
        // 双击事件响应
        /**
         * arraycopy,拷贝数组
         * src 要拷贝的源数组
         * srcPos 源数组开始拷贝的下标位置
         * dst 目标数组
         * dstPos 开始存放的下标位置
         * length 要拷贝的长度（元素的个数）
         *
         */
        //实现数组的移位操作，点击一次，左移一位，末尾补上当前开机时间（cpu的时间）
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
        mHits[mHits.length - 1] = SystemClock.uptimeMillis();
        //双击事件的时间间隔500ms
        if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
            //多击后具体的操作
            //do
            if (onMultipleClickListener != null) {
                onMultipleClickListener.onClickFinish();
            }
        }else {

        }
    }

    public interface OnMultipleClickListener {
        void onClickFinish();
    }
}
