package meilun.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.meilun.meilun_su.AppData;
import com.meilun.meilun_su.R;

import meilun.fragment.Frag4;


public class payActivity extends AppCompatActivity {

    private AppData app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);                  //去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,       //去掉状态栏
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pay);

        TextView textView = findViewById(R.id.pay_price);
        textView.setText("您本次消费的总金额为："+ app.SumPrice + "元");

        Button button =findViewById(R.id.pay_bt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog1 = new AlertDialog.Builder(payActivity.this)
                        .setTitle("提示")//标题
                        .setMessage("支付成功")//内容
                        .setIcon(R.mipmap.app)//图标
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .create();
                alertDialog1.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 5000);

                for (int i=0;i<app.number.length;i++){
                    for (int j=0;j<app.number[0].length;j++){
                        app.number[i][j]=0;
                    }
                }
                app.list.clear();
                Frag4.claer();
                app.str1="";
                app.str="";

                Log.e("TAG", "订单支付状态: " + "支付成功");
            }
        });
    }
}
