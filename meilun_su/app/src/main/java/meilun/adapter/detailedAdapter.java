package meilun.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * 小吃详情页面
 */

public class detailedAdapter extends RecyclerView.Adapter<detailedAdapter.ViewHolder> {
    public Context context;
    private List<Bird> list;
    private AppData app;
    public Bird bird;
    private  boolean isshow;
    private XXXListener mXXXListener;

    public detailedAdapter(Context context, List<Bird> list){
        this.context=context;
        this.list=list;
    }

    /**
     * 详情页面接口回调
     */
    public interface XXXListener {
        void onXXXClick();
    }

    public void setOnXXXClickListener (XXXListener  XXXListener) {
        this.mXXXListener = XXXListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        app=(AppData) parent.getContext().getApplicationContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item3,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        bird=list.get(position);
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
        holder.tv_des.setText(bird.getDetailed());
        holder.tv_pri.setText("价钱：￥"+bird.getPrice());
        holder.tv_show_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isshow)
                {
                    //isshow=false;
                    holder.tv_des.setEllipsize(TextUtils.TruncateAt.END);//收起
                    holder.tv_des.setLines(3);
                    holder.tv_show_more.setText("展开");
                }else
                {
                    // isshow=true;
                    //  tv_des.setEllipsize(TextUtils.TruncateAt.END);//收起
                    holder.tv_des.setEllipsize(null);//展开
                    holder.tv_des.setSingleLine(false);//这个方法是必须设置的，否则无法展开
                    holder.tv_show_more.setText("收起");

                }
                isshow=!isshow;
            }
        });
        mXXXListener.onXXXClick();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iv;
        TextView nametv,tv_show_more,tv_des,tv_pri;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv=itemView.findViewById(R.id.footimage);
            nametv=itemView.findViewById(R.id.foodname);
            tv_show_more=itemView.findViewById(R.id.tv_show_more1);
            tv_des=itemView.findViewById(R.id.tv_des1);
            tv_pri=itemView.findViewById(R.id.textprice);
        }
    }
}
