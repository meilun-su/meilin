package meilun.adapter;
/***
 *  fragment2右边适配器
 */
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.meilun.meilun_su.AppData;
import com.meilun.meilun_su.R;

import java.util.List;

import meilun.activity.Main2Activity;
import meilun.tools.Bird;
import meilun.tools.GlideRectRound;

public class RightAdapter extends RecyclerView.Adapter<RightAdapter.ViewHolder> {
    public Context context;
    private List<Bird> list;
    private AppData app;

    public RightAdapter(Context context,List<Bird> list){
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        app=(AppData) parent.getContext().getApplicationContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item0,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Bird bird=list.get(position);
        Glide.with(context)
                .load(list.get(position).getImageUrl())
                .fitCenter()
                .transform(new GlideRectRound(context,5))
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        holder.iv.setImageDrawable(resource);
                    }
                });
        holder.nametv.setText(bird.getName());
        holder.price.setText("￥"+bird.getPrice());
        holder.bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.list.add(bird);
                for (int i = 0; i < list.size(); i++) {
                    for (int j = 0; j < i; j++) {
                        if (list.get(i).getName().equals(list.get(j).getName())) {
                            list.remove(i);
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
                Toast.makeText(holder.itemView.getContext(),"已添加",Toast.LENGTH_SHORT).show();
                app.price_up();
            }
        });
        holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < app.birds.size(); i++) {
                    app.birds.remove(i);
                }
                app.birds.add(bird);
                Intent intent = new Intent(context, Main2Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);     //必须添加这个方法，否之无法传输除第一组数据外的数据
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iv;
        TextView nametv,price;
        Button bt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv=itemView.findViewById(R.id.frag2_rimag);
            nametv=itemView.findViewById(R.id.frag2_name);
            price=itemView.findViewById(R.id.frag2_price);
            bt=itemView.findViewById(R.id.frag2_add);
        }
    }
}
