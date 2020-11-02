package meilun.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meilun.meilun_su.AppData;
import com.meilun.meilun_su.R;

import java.util.ArrayList;
import java.util.List;

import meilun.adapter.LeftAdapter;
import meilun.adapter.RightAdapter;
import meilun.tools.Bird;

public class Frag2 extends Fragment {
    public static RecyclerView right;
    public static List<List<Bird>> rightata=new ArrayList<>();
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView==null){
            rootView=inflater.inflate(R.layout.frag2, null);
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
        tv.setText("点菜");
        Toast.makeText(getActivity(),"正在加载点菜页面",Toast.LENGTH_SHORT).show();
        frag2_btn_up();
    }

    private void initView(View view){
        //初始化左边列表数据
        List<String> data1=new ArrayList<>();
        data1.add("北京小吃");
        data1.add("成都小吃");
        data1.add("台湾小吃");
        data1.add("天津小吃");
        data1.add("重庆小吃");

        //初始化右边列表数据
        List<Bird> data21=getList(AppData.name[0],AppData.imageUrl[0],AppData.number[0],AppData.footid[0],AppData.groupid[0],AppData.price[0],AppData.detailed[0]);
        List<Bird> data22=getList(AppData.name[1],AppData.imageUrl[1],AppData.number[1],AppData.footid[1],AppData.groupid[1],AppData.price[1],AppData.detailed[1]);
        List<Bird> data23=getList(AppData.name[2],AppData.imageUrl[2],AppData.number[2],AppData.footid[2],AppData.groupid[2],AppData.price[2],AppData.detailed[2]);
        List<Bird> data24=getList(AppData.name[3],AppData.imageUrl[3],AppData.number[3],AppData.footid[3],AppData.groupid[3],AppData.price[3],AppData.detailed[3]);
        List<Bird> data25=getList(AppData.name[4],AppData.imageUrl[4],AppData.number[4],AppData.footid[4],AppData.groupid[4],AppData.price[4],AppData.detailed[4]);
        rightata.add(data21);
        rightata.add(data22);
        rightata.add(data23);
        rightata.add(data24);
        rightata.add(data25);

        //初始化左边列表
        RecyclerView left=view.findViewById(R.id.leftRecycler);
        left.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(),RecyclerView.VERTICAL,false));
        left.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(),RecyclerView.VERTICAL));
        left.setAdapter(new LeftAdapter(getActivity().getApplicationContext(),data1));

        //初始化右边列表
        right=view.findViewById(R.id.rightRecycler);
        right.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.VERTICAL,false));
        right.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(),RecyclerView.VERTICAL));
        right.setAdapter(new RightAdapter(getActivity(),data21));
    }

    /**封装右边列表数据**/
    public  List<Bird> getList(String[] names,String[] imageUrls,int[] number,int[] footid,int[] mainid,int[] price,String[] detailed){
        List<Bird> list=new ArrayList<>();
        Bird bird;
        for(int i=0;i<names.length;i++){
            bird=new Bird();
            bird.setImageUrl(imageUrls[i]);
            bird.setName(names[i]);
            bird.setNumber(number[i]);
            bird.setFootid(footid[i]);
            bird.setGroupid(mainid[i]);
            bird.setPrice(price[i]);
            bird.setDetailed(detailed[i]);
            list.add(bird);
        }
        return list;
    }

    private void frag2_btn_up(){
        Button button1 = getActivity().findViewById(R.id.mbt1);
        Button button2 = getActivity().findViewById(R.id.mbt2);
        Button button3 = getActivity().findViewById(R.id.mbt3);
        Button button4 = getActivity().findViewById(R.id.mbt4);

        Drawable drawable1 = getResources().getDrawable(R.mipmap.zhuye);
        Drawable drawable2 = getResources().getDrawable(R.mipmap.shangpin_a);
        Drawable drawable3 = getResources().getDrawable(R.mipmap.caigou);
        Drawable drawable4 = getResources().getDrawable(R.mipmap.user);

        button1.setCompoundDrawablesWithIntrinsicBounds(null,drawable1,null,null);
        button2.setCompoundDrawablesWithIntrinsicBounds(null,drawable2,null,null);
        button3.setCompoundDrawablesWithIntrinsicBounds(null,drawable3,null,null);
        button4.setCompoundDrawablesWithIntrinsicBounds(null,drawable4,null,null);
    }
}
