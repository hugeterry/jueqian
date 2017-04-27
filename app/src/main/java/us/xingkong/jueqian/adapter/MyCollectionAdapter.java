package us.xingkong.jueqian.adapter;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import cn.bmob.v3.listener.DeleteListener;
import us.xingkong.jueqian.JueQianAPP;
import us.xingkong.jueqian.R;
import us.xingkong.jueqian.bean.ForumBean.BombBean.Question;
import us.xingkong.jueqian.module.Forum.QuestionPage.QuestionActivity;

/**
 * Created by PERFECTLIN on 2017/1/11 0011.
 */

public class MyCollectionAdapter extends RecyclerView.Adapter<MyCollectionAdapter.MyViewHolder> implements View.OnLongClickListener {
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
            public void onClick(View view) {
                String questionID;
                questionID = questions.get(position).getObjectId();
                Intent intent = new Intent(JueQianAPP.getAppContext(), QuestionActivity.class);
                intent.putExtra("questionid", questionID);
                JueQianAPP.getAppContext().startActivity(intent);
            }
        });
        holder.layout.setLongClickable(true);
        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new MaterialDialog.Builder(view.getContext())
                        .title("提示")
                        .content("是否删除？")
                        .negativeText("取消")
                        .positiveText("确认")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                final Question question = new Question();
                                question.setObjectId(questions.get(position).getObjectId());
                                question.delete(JueQianAPP.getAppContext(), new DeleteListener() {
                                    @Override
                                    public void onSuccess() {
                                        questions.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, questions.size());
                                        Toast.makeText(JueQianAPP.getAppContext(), "删除成功", Toast.LENGTH_SHORT).show();
                                        mHandler.sendEmptyMessage(2);
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {
                                        Toast.makeText(JueQianAPP.getAppContext(), "删除失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            }
                        }).show();
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    @Override
    public boolean onLongClick(final View view) {

        return false;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_questiontitle;
        private RelativeLayout layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_questiontitle = (TextView) itemView.findViewById(R.id.item_mycollection_tv_questiontitle);
            layout = (RelativeLayout) itemView.findViewById(R.id.item);
        }
    }
}
