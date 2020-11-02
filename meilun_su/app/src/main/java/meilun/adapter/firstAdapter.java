package meilun.adapter;
/**
 * 主页面（fragment1）瀑布流适配器
 */

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.meilun.meilun_su.AppData;
import com.meilun.meilun_su.R;

import java.util.List;

import meilun.activity.Main2Activity;
import meilun.tools.Bird;
import meilun.tools.GlideRectRound;

public class firstAdapter extends RecyclerView.Adapter<firstAdapter.ViewHolder> {

    public Context context;
    private List<Bird> birdList;
    private AppData app;

    public firstAdapter(Context context, List<Bird> birdList){
        this.context = context;
        this.birdList = birdList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        app=(AppData) viewGroup.getContext().getApplicationContext();
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final Bird bird=birdList.get(i);
        /**
         使用centerCrop是利用图片图填充ImageView设置的大小，如果ImageView的Height是match_parent则图片就会被拉伸填充
         */
        Glide.with(context)
                .load(birdList.get(i).getImageUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new GlideRectRound(context,5))
//                .centerCrop()
                .dontAnimate()
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        viewHolder.imageView.setImageDrawable(resource);
                    }
                });
        viewHolder.tv_name.setText(bird.getName());
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < app.birds.size(); i++) {
                    app.birds.remove(i);
                }
                app.birds.add(bird);
                Intent intent = new Intent(context, Main2Activity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return birdList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView tv_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.bird_image);
            tv_name = itemView.findViewById(R.id.bird_name);
        }
    }
}