package us.xingkong.jueqian.adapter;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import us.xingkong.jueqian.JueQianAPP;
import us.xingkong.jueqian.R;
import us.xingkong.jueqian.bean.ForumBean.BombBean.Question;
import us.xingkong.jueqian.module.Forum.QuestionPage.QuestionActivity;

/**
 * Created by PERFECTLIN on 2017/1/11 0011.
 */

public class MyCollectionAdapter extends RecyclerView.Adapter<MyCollectionAdapter.MyViewHolder> {
    private Handler mHandler;
    private List<Question> questions;

    public MyCollectionAdapter(Handler mHandler, List<Question> questions) {
        this.mHandler = mHandler;
        this.questions = questions;
    }


    @Override
    public MyCollectionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyCollectionAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mycollection, parent, false));
    }

    @Override
    public void onBindViewHolder(MyCollectionAdapter.MyViewHolder holder, final int position) {
        holder.tv_questiontitle.setText(questions.get(position).getMtitle());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)  {
                String questionID;
                questionID = questions.get(position).getObjectId();
                Intent intent = new Intent(JueQianAPP.getAppContext(), QuestionActivity.class);
                intent.putExtra("questionid",questionID);
                JueQianAPP.getAppContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_questiontitle;
        private RelativeLayout layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_questiontitle = (TextView) itemView.findViewById(R.id.item_mycollection_tv_questiontitle);
            layout= (RelativeLayout) itemView.findViewById(R.id.item);
        }
    }
}
