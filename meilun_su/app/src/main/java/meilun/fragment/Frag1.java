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
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.meilun.meilun_su.AppData;
import com.meilun.meilun_su.R;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import meilun.adapter.firstAdapter;
import meilun.tools.Bird;
import meilun.tools.GlideImageLoader;
import meilun.tools.SpacesItemDecoration;

public class Frag1 extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Bird> birdList;
    private firstAdapter adapter;
    private AppData app;
    private Banner banner;
    List<String> images=new ArrayList<>();   //定义图片集合

    private View rootView;

    /**
     *  创建Fragment页面
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView==null){
            rootView=inflater.inflate(R.layout.frag1, null);
            initView(rootView);
            imageroll(rootView);
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
        tv.setText("首页");
        Toast.makeText(getActivity(),"正在加载首页",Toast.LENGTH_SHORT).show();
        frag1_btn_up();
    }

    private void initView(View view){
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        birdList = initData();
        adapter = new firstAdapter(getActivity(), birdList);
        recyclerView.setAdapter(adapter);

        //设置item间隔
        SpacesItemDecoration decoration=new SpacesItemDecoration(2);
        recyclerView.addItemDecoration(decoration);
    }

    private ArrayList<Bird> initData(){
        ArrayList<Bird> birds = new ArrayList<>();
        for (int i = 0; i < app.imageUrl.length; i++ ){
            for (int j=0;j<app.imageUrl[i].length;j++) {
                Bird bird = new Bird();
                bird.setGroupid(app.groupid[i][j]);
                bird.setFootid(app.footid[i][j]);
                bird.setImageUrl(app.imageUrl[i][j]);
                bird.setNumber(app.number[i][j]);
                bird.setName(app.name[i][j]);
                bird.setPrice(app.price[i][j]);
                bird.setDetailed(app.detailed[i][j]);
                birds.add(bird);
            }
        }
        return birds;
    }

    /**
     * banner轮播台湾数组图片：appData.imageUrl[i]，i=0 北京，i=1 成都，i=2 台湾，i=3 天津，i=4 重庆
     */
    private void imageroll(View view){
        for (int i=0;i<app.imageUrl[2].length;i++){
            images.add(app.imageUrl[2][i]);
        }
        //图片路径
        banner = view.findViewById(R.id.banner);
        banner.setImageLoader(new GlideImageLoader());   //设置图片加载器
        banner.setImages(images);  //设置banner中显示图片
        banner.start();  //设置完毕后调用
    }

    private void frag1_btn_up(){
        Button button1 = getActivity().findViewById(R.id.mbt1);
        Button button2 = getActivity().findViewById(R.id.mbt2);
        Button button3 = getActivity().findViewById(R.id.mbt3);
        Button button4 = getActivity().findViewById(R.id.mbt4);

        Drawable drawable1 = getResources().getDrawable(R.mipmap.zhuye_a);
        Drawable drawable2 = getResources().getDrawable(R.mipmap.shangpin);
        Drawable drawable3 = getResources().getDrawable(R.mipmap.caigou);
        Drawable drawable4 = getResources().getDrawable(R.mipmap.user);

        button1.setCompoundDrawablesWithIntrinsicBounds(null,drawable1,null,null);
        button2.setCompoundDrawablesWithIntrinsicBounds(null,drawable2,null,null);
        button3.setCompoundDrawablesWithIntrinsicBounds(null,drawable3,null,null);
        button4.setCompoundDrawablesWithIntrinsicBounds(null,drawable4,null,null);
    }
}
