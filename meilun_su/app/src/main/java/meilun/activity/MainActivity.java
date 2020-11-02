package meilun.activity;
/**
 *主页页面activity
 */

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.meilun.meilun_su.AppData;
import com.meilun.meilun_su.R;

import java.util.ArrayList;
import java.util.List;

import meilun.fragment.Frag1;
import meilun.fragment.Frag2;
import meilun.fragment.Frag3;
import meilun.fragment.Frag4;

public class MainActivity extends AppCompatActivity {
    ViewPager2 vp;
    private AppData app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);                  //去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,       //去掉状态栏
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        initFragment();
        initClick();
    }
    /**
     *  实现Fragment+ViewPager
     */
    private void initFragment(){
        /**创建Fragment集合*/
        final List<Fragment> list = new ArrayList<Fragment>();
        list.add(new Frag1());
        list.add(new Frag2());
        list.add(new Frag3());
        list.add(new Frag4());

        /**读取ViewPager控件*/
        vp = findViewById(R.id.main_ViewPager);

        /**设置Fragment适配器*/
        FragmentStateAdapter adapter = new FragmentStateAdapter(MainActivity.this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {      //设置Fragment页面对象
                return list.get(position);
            }

            @Override
            public int getItemCount() {     //设置Fragment页面数量
                return list.size();
            }
        };

        /**把适配器添加给ViewPager控件*/

        vp.setAdapter(adapter);
    }

    /**
     *  实现点击选项，打开对应的Fragment
     */
    private void initClick(){
        final TextView tv = findViewById(R.id.tv_title);

        final Drawable drawable1 = getResources().getDrawable(R.mipmap.zhuye_a);
        final Drawable drawable2 = getResources().getDrawable(R.mipmap.shangpin_a);
        final Drawable drawable3 = getResources().getDrawable(R.mipmap.caigou_a);
        final Drawable drawable4 = getResources().getDrawable(R.mipmap.user_a);

        final Drawable drawable11 = getResources().getDrawable(R.mipmap.zhuye);
        final Drawable drawable22 = getResources().getDrawable(R.mipmap.shangpin);
        final Drawable drawable33 = getResources().getDrawable(R.mipmap.caigou);
        final Drawable drawable44 = getResources().getDrawable(R.mipmap.user);

        final Button button1 = findViewById(R.id.mbt1);
        final Button button2 = findViewById(R.id.mbt2);
        final Button button3 = findViewById(R.id.mbt3);
        final Button button4 = findViewById(R.id.mbt4);

        button1.setCompoundDrawablesWithIntrinsicBounds(null,drawable1,null,null);

        //点击按钮1，打开对应的布局
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vp.setCurrentItem(0,false);
                tv.setText("首页");
                button1.setCompoundDrawablesWithIntrinsicBounds(null,drawable1,null,null);
                button2.setCompoundDrawablesWithIntrinsicBounds(null,drawable22,null,null);
                button3.setCompoundDrawablesWithIntrinsicBounds(null,drawable33,null,null);
                button4.setCompoundDrawablesWithIntrinsicBounds(null,drawable44,null,null);
            }
        });

        //点击按钮2，打开对应的布局
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vp.setCurrentItem(1,false);
                tv.setText("点菜");
                button1.setCompoundDrawablesWithIntrinsicBounds(null,drawable11,null,null);
                button2.setCompoundDrawablesWithIntrinsicBounds(null,drawable2,null,null);
                button3.setCompoundDrawablesWithIntrinsicBounds(null,drawable33,null,null);
                button4.setCompoundDrawablesWithIntrinsicBounds(null,drawable44,null,null);
            }
        });

        //点击按钮3，打开对应的布局
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vp.setCurrentItem(2,false);
                tv.setText("我的菜单");
                button1.setCompoundDrawablesWithIntrinsicBounds(null,drawable11,null,null);
                button2.setCompoundDrawablesWithIntrinsicBounds(null,drawable22,null,null);
                button3.setCompoundDrawablesWithIntrinsicBounds(null,drawable3,null,null);
                button4.setCompoundDrawablesWithIntrinsicBounds(null,drawable44,null,null);
            }
        });

        //点击按钮4，打开对应的布局
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vp.setCurrentItem(3,false);
                tv.setText("个人信息");
                button1.setCompoundDrawablesWithIntrinsicBounds(null,drawable11,null,null);
                button2.setCompoundDrawablesWithIntrinsicBounds(null,drawable22,null,null);
                button3.setCompoundDrawablesWithIntrinsicBounds(null,drawable33,null,null);
                button4.setCompoundDrawablesWithIntrinsicBounds(null,drawable4,null,null);
            }
        });
    }
}
