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
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import us.xingkong.jueqian.R;
import us.xingkong.jueqian.bean.RealSBean.Results;
import us.xingkong.jueqian.listener.LoadMoreDataAgainListener;
import us.xingkong.jueqian.module.common.WebActivity;
import us.xingkong.jueqian.utils.LogUtils;
import us.xingkong.jueqian.utils.TimeDifferenceUtils;

/**
 * Created by hugeterry(http://hugeterry.cn)
 * Date: 17/1/11 15:01
 */

public class PartHotAdapter extends BaseAdapter<Results> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private Context context;
    private View mFooterView;
    private FooterViewHolder mFooterViewHolder;

    public PartHotAdapter(Context context) {
        this.context = context;
    }

    private LoadMoreDataAgainListener mLoadMoreDataAgainListener;

    @Override
    public void setOnMoreDataLoadAgainListener(LoadMoreDataAgainListener onMoreDataLoadAgainListener) {
        mLoadMoreDataAgainListener = onMoreDataLoadAgainListener;
    }
    @Override
    public void setloadFailureView() {
        mFooterViewHolder.mAvLoadingIndicatorView.setVisibility(View.GONE);
        mFooterViewHolder.mTvLoadDataAgain.setVisibility(View.VISIBLE);
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
        if (holder instanceof PartHotAdapter.PartViewHolder) {
            PartHotAdapter.PartViewHolder viewHolder = (PartHotAdapter.PartViewHolder) holder;
            onBindViewHolder(viewHolder, position);
        } else if (holder instanceof PartHotAdapter.FooterViewHolder) {
            mFooterViewHolder = ((FooterViewHolder) holder);
            mFooterViewHolder.mAvLoadingIndicatorView.setVisibility(View.VISIBLE);
            mFooterViewHolder.mTvLoadDataAgain.setVisibility(View.GONE);
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

        String time = mData.get(position).getCreatedAt();
        String type = mData.get(position).getType();
        if (time != null) {
            holder.tv_time.setText(TimeDifferenceUtils.getTimeDifference(time));
        } else {
            holder.tv_time.setText("");
        }
        holder.tv_type.setText(type);

    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) return TYPE_FOOTER;
        else return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mData.size() + 1;
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

    class FooterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.loadingIndicatorView)
        AVLoadingIndicatorView mAvLoadingIndicatorView;
        @BindView(R.id.tv_load_data_again)
        TextView mTvLoadDataAgain;

        public FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.loadingIndicatorView, R.id.tv_load_data_again})
        public void onClick(View view) {
            mLoadMoreDataAgainListener.loadMoreDataAgain(mTvLoadDataAgain, mAvLoadingIndicatorView);
        }
    }
}
