package meilun.adapter;
/**
 * fragment2左边列表适配器
 */
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meilun.meilun_su.R;

import java.util.ArrayList;
import java.util.List;

import meilun.fragment.Frag2;

public class LeftAdapter extends RecyclerView.Adapter<LeftAdapter.ViewHolder> {
    private List<String> list;
    public static List<ViewHolder> items=new ArrayList<>();
    Context context;

    public LeftAdapter(Context context,List<String> list){
        this.context=context;this.list=list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item1,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        items.add(holder);
        holder.tv.setText(list.get(position));
        holder.itemView.setBackgroundColor(Color.rgb(	131,139,139));
        if(position==0){
            holder.itemView.setBackgroundColor(Color.argb(25,131,139,139));
            holder.tv.setTextColor(Color.rgb(131,139,139));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Frag2.right.setAdapter(new RightAdapter(context, Frag2.rightata.get(position)));
                for(int i=0;i<items.size();i++){
                    items.get(i).itemView.setBackgroundColor(Color.rgb(131,139,139));
                    items.get(i).tv.setTextColor(Color.rgb(255,255,255));
                }
                holder.itemView.setBackgroundColor(Color.argb(25,131,139,139));
                holder.tv.setTextColor(Color.rgb(131,139,139));
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv=itemView.findViewById(R.id.frag2_ltv);
        }
    }

}
