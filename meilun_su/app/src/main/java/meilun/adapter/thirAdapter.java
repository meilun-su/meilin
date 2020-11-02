package meilun.adapter;
/**
 *  fragment3菜单适配器
 */
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.meilun.meilun_su.AppData;
import com.meilun.meilun_su.R;

import java.util.List;

import meilun.tools.Bird;
import meilun.tools.GlideRectRound;

public class thirAdapter extends RecyclerView.Adapter<thirAdapter.ViewHolder> {
    public Context context;
    private List<Bird> list;
    private AppData app;
    private XXListener mXXListener;

    public thirAdapter(Context context, List<Bird> list){
        this.context=context;
        this.list=list;
    }

    /**
     * 接口回调
     */
    public interface XXListener {
        void onXXClick();
    }

    public void setOnXXClickListener (XXListener  XXListener) {
        this.mXXListener = XXListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        app=(AppData) parent.getContext().getApplicationContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item2,parent,false);
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
        holder.tv_number.setText(""+app.number[bird.getGroupid()][bird.getFootid()]);
        holder.nametv.setText(bird.getName());
        holder.price.setText("￥"+bird.getPrice());
        //fragm3的添加按钮监听事件
        holder.bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++app.number[bird.getGroupid()][bird.getFootid()];
                bird.setNumber(app.number[bird.getGroupid()][bird.getFootid()]);
                holder.tv_number.setText(""+app.number[bird.getGroupid()][bird.getFootid()]);
                Log.e("TAG", bird.getName()+"购买数量: " + app.number[bird.getGroupid()][bird.getFootid()]);
//                Toast.makeText(holder.itemView.getContext(),"已添加",Toast.LENGTH_SHORT).show();
                app.price_up();
                mXXListener.onXXClick();

            }
        });
        //fragm3的减少按钮监听事件
        holder.bt_rm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (app.number[bird.getGroupid()][bird.getFootid()] != 0){
                    --app.number[bird.getGroupid()][bird.getFootid()];
                    bird.setNumber(app.number[bird.getGroupid()][bird.getFootid()]);
                    holder.tv_number.setText(""+app.number[bird.getGroupid()][bird.getFootid()]);
                    Log.e("TAG", bird.getName()+"购买数量: " + app.number[bird.getGroupid()][bird.getFootid()]);
                    app.price_up();
                    mXXListener.onXXClick();
                }


                if (app.number[bird.getGroupid()][bird.getFootid()] == 0){
                    app.list.remove(bird);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iv;
        TextView nametv,tv_number,price;
        Button bt_add,bt_rm;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv=itemView.findViewById(R.id.frag3_imag);
            nametv=itemView.findViewById(R.id.frag3_name);
            price=itemView.findViewById(R.id.frag3_price);
            bt_add=itemView.findViewById(R.id.frag3_add);
            tv_number=itemView.findViewById(R.id.tv_number);
            bt_rm=itemView.findViewById(R.id.frag3_rm);
        }
    }

}
