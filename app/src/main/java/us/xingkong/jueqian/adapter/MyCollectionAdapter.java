package us.xingkong.jueqian.adapter;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;
import us.xingkong.jueqian.JueQianAPP;
import us.xingkong.jueqian.R;
import us.xingkong.jueqian.bean.ForumBean.BombBean.Question;
import us.xingkong.jueqian.bean.ForumBean.BombBean._User;

/**
 * Created by PERFECTLIN on 2017/1/11 0011.
 */

public class MyCollectionAdapter extends RecyclerView.Adapter<MyCollectionAdapter.MyViewHolder> {
    private Handler mHandler;
    private List<Question> questions;
//    private ArrayList<String> mArrayList;

    public MyCollectionAdapter(Handler mHandler, List<Question> questions) {
        this.mHandler = mHandler;
        this.questions = questions;
//        this.mArrayList = mArrayList;

    }


    @Override
    public MyCollectionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyCollectionAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mycollection, parent, false));
    }

    @Override
    public void onBindViewHolder(MyCollectionAdapter.MyViewHolder holder, int position) {
//        holder.tv_questiontitle.setText(questions.get(position).getMtitle());
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
            tv_questiontitle = (TextView) itemView.findViewById(R.id.item_mycollection_tv_questiontitle);
        }
    }
}
