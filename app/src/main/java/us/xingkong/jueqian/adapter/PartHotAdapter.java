package us.xingkong.jueqian.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import us.xingkong.jueqian.R;
import us.xingkong.jueqian.bean.RealSBean.Results;
import us.xingkong.jueqian.module.common.WebActivity;
import us.xingkong.jueqian.utils.TimeDifferenceUtils;

/**
 * Created by hugeterry(http://hugeterry.cn)
 * Date: 17/1/11 15:01
 */

public class  PartHotAdapter extends AddFooterBaseAdapter<Results> {

    private Context context;
    private View mFooterView;

    public PartHotAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_part_hot, parent, false);
            return new PartViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            mFooterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_foot, parent, false);
            return new FooterViewHolder(mFooterView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof PartViewHolder) {
            PartViewHolder viewHolder = (PartViewHolder) holder;
            onBindViewHolder(viewHolder, position);
        }
    }

    private void onBindViewHolder(PartViewHolder holder, final int position) {
        List<String> images = mData.get(position).getImages();
        if (!images.isEmpty()) {
            holder.iv_img.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(images.get(0))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(100, 100)
                    .thumbnail(0.5f)
                    .into(holder.iv_img);
        } else {
            holder.iv_img.setVisibility(View.GONE);
        }
        holder.relativeLayout.setVisibility(View.VISIBLE);
        holder.textView.setText(mData.get(position).getDesc());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebActivity.newIntent(context,
                        mData.get(position).getUrl(),
                        mData.get(position).getDesc());
            }
        });

        switch (mData.get(position).getType()) {
            case "Android":
                holder.iv_icon.setImageResource(R.mipmap.android_icon);
                break;
            case "iOS":
                holder.iv_icon.setImageResource(R.mipmap.ios_icon);
                break;
            case "前端":
                holder.iv_icon.setImageResource(R.mipmap.js_icon);
                break;
            default:
                holder.iv_icon.setImageResource(R.mipmap.other_icon);
                break;
        }

        String author = mData.get(position).getWho();
        if (author != null) {
            holder.tv_author.setText(author);
            holder.tv_author.setTextColor(Color.parseColor("#87000000"));
        } else {
            holder.tv_author.setText("匿名");
        }

        holder.tv_time.setText(mData.get(position).getCreatedAt());
        holder.tv_type.setText(mData.get(position).getType());
    }

    class PartViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rl_part_message)
        RelativeLayout relativeLayout;
        @BindView(R.id.tv_part_author)
        TextView tv_author;
        @BindView(R.id.tv_part_time)
        TextView tv_time;
        @BindView(R.id.tv_part_type)
        TextView tv_type;
        @BindView(R.id.iv_part_type_icon)
        ImageView iv_icon;
        @BindView(R.id.iv_part_img)
        ImageView iv_img;
        @BindView(R.id.tv_part)
        TextView textView;

        public PartViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
