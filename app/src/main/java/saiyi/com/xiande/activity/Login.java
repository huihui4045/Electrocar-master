package saiyi.com.xiande.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import saiyi.com.xiande.R;
import saiyi.com.xiande.base.BaseActivity;

public class Login extends BaseActivity implements View.OnClickListener{
    private ImageView mTabLine;// 指导线
    private int screenWidth;// 屏幕的宽度

    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private TextView mTvLogin;
    private TextView mTvRegist;
    private ViewPager mViewPager;
    int FontBlack;
    int FontGray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();

        FontBlack = getResources().getColor(R.color.FontBlack);
        FontGray = getResources().getColor(R.color.FontGray);
    }

    private void initViews() {
        mTvLogin = (TextView) findViewById(R.id.tvLogin);
        mTvLogin.setOnClickListener(this);
        mTvRegist = (TextView) findViewById(R.id.tvRegister);
        mTvRegist.setOnClickListener(this);

        mTabLine = (ImageView) findViewById(R.id.ivTabLine);
        //获取屏幕的宽度
        DisplayMetrics outMetrics=new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        screenWidth=outMetrics.widthPixels;

        //设置mTabLine宽度//获取控件的(注意：一定要用父控件的LayoutParams写LinearLayout.LayoutParams)
        RelativeLayout.LayoutParams lp=(RelativeLayout.LayoutParams) mTabLine.getLayoutParams();//获取控件的布局参数对象
        lp.width=screenWidth/2;
        mTabLine.setLayoutParams(lp); //设置该控件的layoutParams参数

        mFragments.add(new PhoneLoginFragment());
        mFragments.add(new SellerLoginFragment());

        mViewPager = (ViewPager) findViewById(R.id.vp1);
        TabAdapter mAdapter = new TabAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(new TabOnPageChangeListener());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvLogin:
                mViewPager.setCurrentItem(0);
//                Intent intent  = new Intent("android.media.action.IMAGE_CAPTURE");
//                startActivityForResult(intent, 1);
                break;
            case R.id.tvRegister:
                mViewPager.setCurrentItem(1);
                break;
        }
    }

    /**
     * 页卡滑动改变事件
     */
    public class TabOnPageChangeListener implements ViewPager.OnPageChangeListener {

        /**
         * 当滑动状态改变时调用
         * state=0的时候表示什么都没做，就是停在那
         * state=1的时候表示正在滑动
         * state==2的时候表示滑动完毕了
         */
        public void onPageScrollStateChanged(int state) {

        }

        /**
         * 当前页面被滑动时调用
         * position:当前页面
         * positionOffset:当前页面偏移的百分比
         * positionOffsetPixels:当前页面偏移的像素位置
         */
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            RelativeLayout.LayoutParams lp=(RelativeLayout.LayoutParams) mTabLine.getLayoutParams();
            //获取组件距离左侧组件的距离
            lp.leftMargin=(int) ((positionOffset+position)*screenWidth/2);
            mTabLine.setLayoutParams(lp);
        }

        //当新的页面被选中时调用
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    mTvLogin.setTextColor(FontBlack);
                    mTvRegist.setTextColor(FontGray);
                    break;
                case 1:
                    mTvLogin.setTextColor(FontGray);
                    mTvRegist.setTextColor(FontBlack);
                    break;
            }
        }
    }
}


class TabAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments;

    public TabAdapter(FragmentManager fm, List<Fragment> mFragments) {
        super(fm);
        this.mFragments = mFragments;
    }

    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    public int getCount() {
        return mFragments.size();
    }
}
