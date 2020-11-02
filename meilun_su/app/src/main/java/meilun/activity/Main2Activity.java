package meilun.activity;
/**
 * 小吃详情页面activity
 */

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meilun.meilun_su.AppData;
import com.meilun.meilun_su.R;

import meilun.adapter.detailedAdapter;
import meilun.tools.Bird;
import meilun.tools.SpacesItemDecoration;

public class Main2Activity extends AppCompatActivity {
    private AppData app;
    private detailedAdapter adapter;
    private Bird bird;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);                  //去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,       //去掉状态栏
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main2);
        initView();
        btn_fanhui();
        btn_gouwu();
    }

    private void initView(){
        RecyclerView rv=findViewById(R.id.m2_RecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(Main2Activity.this,RecyclerView.VERTICAL,false));
        adapter=new detailedAdapter(Main2Activity.this,app.birds);
        rv.setAdapter(adapter);

        //设置item间隔
        final SpacesItemDecoration decoration=new SpacesItemDecoration(2);
        rv.addItemDecoration(decoration);

        //接口回调
        adapter.setOnXXXClickListener (new detailedAdapter.XXXListener(){
            @Override
            public void onXXXClick() {
                bird = adapter.bird;
            }
        });
    }

    private void btn_fanhui(){
        Button button = findViewById(R.id.m2_bt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void btn_gouwu(){
        Button button1 = findViewById(R.id.m2_bt1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.list.add(bird);
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
                ++app.number[bird.getGroupid()][bird.getFootid()];
                bird.setNumber(app.number[bird.getGroupid()][bird.getFootid()]);
                Log.e("TAG", bird.getName()+"id: "+ bird.getFootid()+
                        "\t所在组id: "+ bird.getGroupid() +
                        "\t购买数量: " + app.number[bird.getGroupid()][bird.getFootid()]);
                Toast.makeText(getApplication(),"已添加",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
