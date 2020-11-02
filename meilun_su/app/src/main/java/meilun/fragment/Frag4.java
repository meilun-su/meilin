package meilun.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.meilun.meilun_su.AppData;
import com.meilun.meilun_su.R;

public class Frag4 extends Fragment {

    private LinearLayout mData;
    private ConstraintLayout mData1,mData2,mData3;
    private TextView mShowHidden,mShowHidden1,mShowHidden2,mShowHidden3;
    private int height,height1,height2,height3;
    private View rootView;
    private static CheckBox check1,check2,check3,check4,check5;
    private static RadioButton RB;
    private static boolean isselectall = false;
    private static boolean isselectall1 = false;
    private AppData app;

    private CompoundButton.OnCheckedChangeListener listenter = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            //选中多选框
            CheckBox check = (CheckBox)buttonView;
            //取出当前勾选值
            String str = check.getText().toString();
            String str2 = check.getText().toString();
            //判断是否勾选状态
            boolean status = app.str.contains(str);
            if(isChecked){
                if (!status){
                    app.str += str + ",";
                    isselectall1=true;
                }
            }else {
                if (status){
                    app.str= app.str.replace(str2+",","");
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView==null){
            rootView=inflater.inflate(R.layout.frag4, null);
            initView(rootView);
            btn_no(rootView);
        }
        //缓存的rootView需要判断是否已经被加过parent，
        //如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    /**
     *  滑动页面或刷新页面时，要执行的代码
     */
    @Override
    public void onResume() {
        super.onResume();
        TextView tv = getActivity().findViewById(R.id.tv_title);
        tv.setText("个人信息");
        Toast.makeText(getActivity(),"正在加载我的页面",Toast.LENGTH_SHORT).show();
        frag4_btn_up();
    }

    private void initView(final View view) {
        //口味页面
        mData = view.findViewById(R.id.mData);

        mShowHidden = view.findViewById(R.id.mShowHidden);
        mShowHidden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click(v);
            }
        });

        //登录页面
        mData1 = view.findViewById(R.id.mData1);
        mShowHidden1 = view.findViewById(R.id.mShowHidden1);
        mShowHidden1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click(v);
            }
        });
        //注册页面
        mData2 = view.findViewById(R.id.mData2);
        mShowHidden2 = view.findViewById(R.id.mShowHidden2);
        mShowHidden2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click(v);
            }
        });
        //修改页面
        mData3 = view.findViewById(R.id.mData3);
        mShowHidden3 = view.findViewById(R.id.mShowHidden3);
        mShowHidden3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click(v);
            }
        });
        //获取控件高度
        initOnPreDrawListener();

        check1 = view.findViewById(R.id.checkBox1);
        check2 = view.findViewById(R.id.checkBox2);
        check3 = view.findViewById(R.id.checkBox3);
        check4 = view.findViewById(R.id.checkBox4);
        check5 = view.findViewById(R.id.checkBox5);

        //多选框点击事件
        check1.setOnCheckedChangeListener(listenter);
        check2.setOnCheckedChangeListener(listenter);
        check3.setOnCheckedChangeListener(listenter);
        check4.setOnCheckedChangeListener(listenter);
        check5.setOnCheckedChangeListener(listenter);

        final RadioGroup sex=view.findViewById(R.id.group1);//获取单选按钮组
        //为单选按钮组添加事件监听
        sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RB=view.findViewById(i);//获取被选择的单选按钮
                app.str1 =""+RB.getText();
                    if (!isselectall){
                    RB.setChecked(true);
                    isselectall = true;
                }
            }
        });
    }

    /**
     * 方法名：createDropAnimator(final View v, int start, int end)
     * 功    能：ValueAnimator动画,布局上下移动
     * 参    数：View v, int start, int end
     * 返回值：ValueAnimator animator
     */
    private ValueAnimator createDropAnimator(final View v, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(300);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                int value = (int) arg0.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                layoutParams.height = value;
                v.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    public void Click(View v) {
        switch (v.getId()) {
            //口味页面
            case R.id.mShowHidden:
                if (mData.getVisibility() == View.GONE) {
                    //开启布局动画
                    animateOpen(mData);
                    up_rm(mShowHidden);
                } else {
                    //关闭布局动画
                    animateClose(mData);
                    up_add(mShowHidden);
                }
                break;
            //登录页面
            case R.id.mShowHidden1:
                if (mData1.getVisibility() == View.GONE) {
                    //开启布局动画
                    animateOpen(mData1);
                    up_rm(mShowHidden1);
                } else {
                    //关闭布局动画
                    animateClose(mData1);
                    up_add(mShowHidden1);
                }
                break;
            //注册页面
            case R.id.mShowHidden2:
                if (mData2.getVisibility() == View.GONE) {
                    //开启布局动画
                    animateOpen(mData2);
                    up_rm(mShowHidden2);
                } else {
                    //关闭布局动画
                    animateClose(mData2);
                    up_add(mShowHidden2);
                }
                break;
            //修改页面
            case R.id.mShowHidden3:
                if (mData3.getVisibility() == View.GONE) {
                    //开启布局动画
                    animateOpen(mData3);
                    up_rm(mShowHidden3);
                } else {
                    //关闭布局动画
                    animateClose(mData3);
                    up_add(mShowHidden3);
                }
                break;
        }
    }

    /**
     * 方法名：animateOpen(View v)
     * 功    能：布局向下移动
     * 参    数：View v
     * 返回值：无
     */
    private void animateOpen(View v) {
        if (mData.equals(v)) {
            v.setVisibility(View.VISIBLE);
            ValueAnimator animator = createDropAnimator(v, 0, height);
            animator.start();
        } else if (mData1.equals(v)) {
            v.setVisibility(View.VISIBLE);
            ValueAnimator animator1 = createDropAnimator(v, 0, height1);
            animator1.start();
        } else if (mData2.equals(v)) {
            v.setVisibility(View.VISIBLE);
            ValueAnimator animator2 = createDropAnimator(v, 0, height2);
            animator2.start();
        } else if (mData3.equals(v)) {
            v.setVisibility(View.VISIBLE);
            ValueAnimator animator3 = createDropAnimator(v, 0, height3);
            animator3.start();
        }
    }

    /**
     * 方法名：animateClose(View v)
     * 功    能：布局向上移动
     * 参    数：View v
     * 返回值：无
     */
    private void animateClose(final View view) {
        int origHeight = view.getHeight();
        ValueAnimator animator = createDropAnimator(view, origHeight, 0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }

        });
        animator.start();
    }

    /**
     * 方法名：initOnPreDrawListener()
     * 功    能：获取控件高度
     * 参    数：无
     * 返回值：无
     */
    private void initOnPreDrawListener() {
        final ViewTreeObserver viewTreeObserver = getActivity().getWindow().getDecorView().getViewTreeObserver();
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                height = mData.getMeasuredHeight();
                height1 = mData1.getMeasuredHeight();
                height2 = mData2.getMeasuredHeight();
                height3 = mData3.getMeasuredHeight();
                Log.e("TAG", "口味页面的高：" + height);        //向控制台打印高
                // 移除OnPreDrawListener事件监听
                getActivity().getWindow().getDecorView().getViewTreeObserver().removeOnPreDrawListener(this);
                //获取完高度后隐藏控件
                mData.setVisibility(View.GONE);
                mData1.setVisibility(View.GONE);
                mData2.setVisibility(View.GONE);
                mData3.setVisibility(View.GONE);

                return true;
            }
        });
    }

    private void up_rm(TextView textView){
        // 使用代码设置drawableleft
        Drawable drawable = getResources().getDrawable(R.mipmap.up_circle);
        // / 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        textView.setCompoundDrawables(null, null, drawable, null);
    }

    private void up_add(TextView textView){
        // 使用代码设置drawableleft
        Drawable drawable = getResources().getDrawable(R.mipmap.down_circle);
        // / 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        textView.setCompoundDrawables(null, null, drawable, null);
    }

    /**
     * chenck按钮组和rediobutton按钮组清空选中状态
     */
    public static void claer(){
        if (isselectall1!=false) {
            check1.setChecked(false);
            check2.setChecked(false);
            check3.setChecked(false);
            check4.setChecked(false);
            check5.setChecked(false);
        }

        if (isselectall!=false){
            RB.setChecked(false);
            isselectall=false;
        }
    }

    private void frag4_btn_up(){
        Button button1 = getActivity().findViewById(R.id.mbt1);
        Button button2 = getActivity().findViewById(R.id.mbt2);
        Button button3 = getActivity().findViewById(R.id.mbt3);
        Button button4 = getActivity().findViewById(R.id.mbt4);

        Drawable drawable1 = getResources().getDrawable(R.mipmap.zhuye);
        Drawable drawable2 = getResources().getDrawable(R.mipmap.shangpin);
        Drawable drawable3 = getResources().getDrawable(R.mipmap.caigou);
        Drawable drawable4 = getResources().getDrawable(R.mipmap.user_a);

        button1.setCompoundDrawablesWithIntrinsicBounds(null,drawable1,null,null);
        button2.setCompoundDrawablesWithIntrinsicBounds(null,drawable2,null,null);
        button3.setCompoundDrawablesWithIntrinsicBounds(null,drawable3,null,null);
        button4.setCompoundDrawablesWithIntrinsicBounds(null,drawable4,null,null);
    }

    private void btn_no(final View view){
        Button login = view.findViewById(R.id.denglu);
        Button registered = view.findViewById(R.id.zhuce);
        Button update = view.findViewById(R.id.xiugai);

        final EditText edit2_account = view.findViewById(R.id.edit2_name);
        final EditText edit2_pass = view.findViewById(R.id.edit2_pass);

        final EditText edit3_username = view.findViewById(R.id.edit3_username);
        final EditText edit3_account = view.findViewById(R.id.edit3_name);
        final EditText edit3_pass = view.findViewById(R.id.edit3_pass);

        edit2_account.setText(app.admin_account);
        edit2_pass.setText(app.admin_pass);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.log_account=edit2_account.getText().toString();
                app.log_password=edit2_pass.getText().toString();

                if((app.log_account.length()<=0)||(app.log_password.length()<=0)){
                    AlertDialog alertDialog1 = new AlertDialog.Builder(getActivity())
                            .setTitle("提示")//标题
                            .setMessage("账号或者密码不能为空")//内容
                            .setIcon(R.mipmap.app)//图标
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {}
                            })
                            .create();
                    alertDialog1.show();
                }
                else if (((app.log_account.equals(app.account)) && (app.log_password.equals(app.password)))
                        || ((app.log_account.equals(app.admin_account)) && (app.log_password.equals(app.admin_pass)))) {
                        ImageView imageView = view.findViewById(R.id.frag4_tx);
                        imageView.setImageResource(R.mipmap.app);
                    AlertDialog alertDialog1 = new AlertDialog.Builder(getActivity())
                            .setTitle("提示")//标题
                            .setMessage("登录成功")//内容
                            .setIcon(R.mipmap.app)//图标
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {}
                            })
                            .create();
                    alertDialog1.show();
                    TextView textView = view.findViewById(R.id.username);
                    TextView textView1 = view.findViewById(R.id.VIP);
                    if ((app.log_account.equals(app.admin_account))){
                        textView.setText(app.admin_name);
                        textView1.setText("至尊会员");
                        Drawable drawable1 = getResources().getDrawable(R.mipmap.super_vip);
                        textView1.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                    }else {
                        textView.setText(app.username);
                        textView1.setText("普通会员");
                        Drawable drawable1 = getResources().getDrawable(R.mipmap.vip);
                        textView1.setCompoundDrawablesWithIntrinsicBounds(drawable1,null,null,null);
                    }


                    app.state=1;
                }
                else {
                    AlertDialog alertDialog1 = new AlertDialog.Builder(getActivity())
                            .setTitle("提示")//标题
                            .setMessage("账户或密码错误")//内容
                            .setIcon(R.mipmap.app)//图标
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {}
                            })
                            .create();
                    alertDialog1.show();
                }
            }
        });

        registered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.username=edit3_username.getText().toString();
                app.account=edit3_account.getText().toString();
                app.password=edit3_pass.getText().toString();

                if((app.username.length()<=0)||(app.account.length()<=0)||(app.password.length()<=0)){
                    AlertDialog alertDialog1 = new AlertDialog.Builder(getActivity())
                            .setTitle("提示")//标题
                            .setMessage("账号或者密码不能为空")//内容
                            .setIcon(R.mipmap.app)//图标
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {}
                            })
                            .create();
                    alertDialog1.show();
                }else {
                    AlertDialog alertDialog1 = new AlertDialog.Builder(getActivity())
                            .setTitle("提示")//标题
                            .setMessage("注册成功")//内容
                            .setIcon(R.mipmap.app)//图标
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {}
                            })
                            .create();
                    alertDialog1.show();
                }
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog1 = new AlertDialog.Builder(getActivity())
                        .setTitle("提示")//标题
                        .setMessage("该功能还未开发，感谢您使用本小吃APP")//内容
                        .setIcon(R.mipmap.app)//图标
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {}
                        })
                        .create();
                alertDialog1.show();
            }
        });
    }

    private void login(){
        EditText edit2_name = getActivity().findViewById(R.id.edit2_name);
        edit2_name.setText(app.username);
    }
}
