package us.xingkong.jueqian.adapter;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import us.xingkong.jueqian.R;
import us.xingkong.jueqian.bean.ForumBean.BombBean.Question;

/**
 * Created by PERFECTLIN on 2017/1/11 0011.
 */

public class MyRecentLookAdapter extends RecyclerView.Adapter<MyRecentLookAdapter.MyViewHolder> {
    private Handler mHandler;
    private List<Question> questions;

    public MyRecentLookAdapter(Handler mHandler, List<Question> questions) {
        this.mHandler = mHandler;
        this.questions = questions;
    }


    @Override
    public MyRecentLookAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyRecentLookAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myrecentlook, parent, false));
    }

    @Override
    public void onBindViewHolder(MyRecentLookAdapter.MyViewHolder holder, int position) {
        holder.tv_questiontitle.setText(questions.get(position).getMtitle());

    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_questiontitle;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_questiontitle = (TextView) itemView.findViewById(R.id.item_myrecentlook_tv_questiontitle);
        }
    }
}
