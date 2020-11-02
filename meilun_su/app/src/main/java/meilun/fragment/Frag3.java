package meilun.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meilun.meilun_su.AppData;
import com.meilun.meilun_su.R;

import meilun.activity.payActivity;
import meilun.adapter.thirAdapter;
import meilun.tools.SpacesItemDecoration;

public class Frag3 extends Fragment {
    private AppData app;
    private thirAdapter adapter;
    private View rootView;

    /**
     *  创建Fragment页面并且实现fragment页面状态保存
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        app = (AppData) getActivity().getApplication();
        if(rootView==null){
            rootView=inflater.inflate(R.layout.frag3, null);
            initView(rootView);
        }
        //缓存的rootView需要判断是否已经被加过parent，
        // 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
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
        tv.setText("我的菜单");
        app.price_up();
        TextView sum = rootView.findViewById(R.id.SumPrice);
        sum.setText("总价：￥"+app.SumPrice);
        EditText editText = rootView.findViewById(R.id.frag_et);
        editText.setText(app.str+"\n"+app.str1);
        Toast.makeText(getActivity(),"正在加载菜单页面",Toast.LENGTH_SHORT).show();
        for (int i = 0; i < app.list.size(); i++) {
            for (int j = 0; j < i; j++) {
                if (app.list.get(i).getName().equals(app.list.get(j).getName())) {
                    app.list.remove(i);
                    //下标会减1
                    i = i - 1;
                    break;
                }
            }
        }
        frag3_btn_up();
        adapter.notifyDataSetChanged();                               //实时刷新列表数据
    }

    /***
     * 实现已选列表
     */
    private void initView(final View view){
        RecyclerView rv=view.findViewById(R.id.rv2);
        rv.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        adapter=new thirAdapter(getActivity().getApplicationContext(),app.list);
        rv.setAdapter(adapter);

        //设置item间隔
        SpacesItemDecoration decoration=new SpacesItemDecoration(2);
        rv.addItemDecoration(decoration);

        //接口回调
        adapter.setOnXXClickListener (new thirAdapter.XXListener(){
            @Override
            public void onXXClick(){
                TextView sum = rootView.findViewById(R.id.SumPrice);
                sum.setText("总价：￥"+app.SumPrice);
            }
        });
        Button bt1 = rootView.findViewById(R.id.frag3_zf);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (app.state == 1) {
                    if (AppData.SumPrice == 0) {
                        AlertDialog alertDialog1 = new AlertDialog.Builder(getActivity())
                                .setTitle("提示")//标题
                                .setMessage("您还未点菜")//内容
                                .setIcon(R.mipmap.app)//图标
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                })
                                .create();
                        alertDialog1.show();
                    } else {
                        Intent intent = new Intent(getActivity(), payActivity.class);
                        startActivity(intent);
                    }
                }else {
                    AlertDialog alertDialog1 = new AlertDialog.Builder(getActivity())
                            .setTitle("提示")//标题
                            .setMessage("你还未登录，请先登录")//内容
                            .setIcon(R.mipmap.app)//图标
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            })
                            .create();
                    alertDialog1.show();
                }
            }
        });
    }

    private void frag3_btn_up(){
        Button button1 = getActivity().findViewById(R.id.mbt1);
        Button button2 = getActivity().findViewById(R.id.mbt2);
        Button button3 = getActivity().findViewById(R.id.mbt3);
        Button button4 = getActivity().findViewById(R.id.mbt4);

        Drawable drawable1 = getResources().getDrawable(R.mipmap.zhuye);
        Drawable drawable2 = getResources().getDrawable(R.mipmap.shangpin);
        Drawable drawable3 = getResources().getDrawable(R.mipmap.caigou_a);
        Drawable drawable4 = getResources().getDrawable(R.mipmap.user);

        button1.setCompoundDrawablesWithIntrinsicBounds(null,drawable1,null,null);
        button2.setCompoundDrawablesWithIntrinsicBounds(null,drawable2,null,null);
        button3.setCompoundDrawablesWithIntrinsicBounds(null,drawable3,null,null);
        button4.setCompoundDrawablesWithIntrinsicBounds(null,drawable4,null,null);
    }
}
