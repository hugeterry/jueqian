package us.xingkong.jueqian.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import us.xingkong.jueqian.R;
import us.xingkong.jueqian.bean.ForumBean.BombBean.Answer;
import us.xingkong.jueqian.bean.ForumBean.BombBean.Ding;
import us.xingkong.jueqian.bean.ForumBean.BombBean.Question;
import us.xingkong.jueqian.bean.ForumBean.BombBean._User;
import us.xingkong.jueqian.module.Forum.QuestionPage.richtext.RichText;
import us.xingkong.jueqian.module.Login.LoginActivity;
import us.xingkong.jueqian.module.main.MainActivity;
import us.xingkong.jueqian.module.me.mainpage.MainPageAcitivity;


/**
 * Created by Garfield on 1/9/17.
 */

public class QuestionRecyclerViewAdapter extends RecyclerView.Adapter<QuestionRecyclerViewAdapter.VH> {
    private List<Answer> answers;
    private int HEADER = 1;
    private int CONTENT = 2;
    private Handler mHandler;
    private Question getQuestion;
    private Context context;
    private BmobFile bmobFile;
    private String dingID[];
    final List<String> list = new ArrayList<>();

    public QuestionRecyclerViewAdapter(Context context, Question getQuestion, List<Answer> answers, Handler mHandler) {
        this.getQuestion = getQuestion;
        this.answers = answers;
        this.mHandler = mHandler;
        this.context = context;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER) {
            return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_questionpage_header, parent, false));
        } else if (viewType == CONTENT) {
            return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_questionpage, parent, false));
        } else {
            return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER;
        } else {
            return CONTENT;
        }

    }

    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        dingID = new String[answers.size() + 1];
        if (position == 0) {
            final _User now = BmobUser.getCurrentUser(context, _User.class);
            if (now != null && getQuestion != null && now.getObjectId().equals(getQuestion.getUser().getObjectId())) {
                holder.question_delete.setVisibility(View.VISIBLE);
                holder.question_delete.setClickable(true);
                holder.question_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new MaterialDialog.Builder(context)
                                .title("提示")
                                .content("是否删除？")
                                .negativeText("取消")
                                .positiveText("确认")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        Question question = new Question();
                                        question.setObjectId(getQuestion.getObjectId());
                                        question.delete(context, new DeleteListener() {
                                            @Override
                                            public void onSuccess() {
                                                MainActivity.instance.finish();
                                                Intent intent = new Intent(context, MainActivity.class);
                                                context.startActivity(intent);
                                                Message msg = new Message();
                                                Bundle bundle = new Bundle();
                                                bundle.putString("questionID", getQuestion.getObjectId());
                                                msg.setData(bundle);
                                                msg.what = 9;
                                                mHandler.sendMessage(msg);
                                            }

                                            @Override
                                            public void onFailure(int i, String s) {
                                            }
                                        });

                                    }
                                })
                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                    }
                                }).show();
                    }
                });
            } else {
                holder.question_delete.setVisibility(View.GONE);
                holder.question_delete.setClickable(false);
            }


            if (getQuestion.getMtitle() != null) {
                holder.title_question.setText(getQuestion.getMtitle());

                if (getQuestion.getImageFiles() != null) {
                    for (int i = 0; i < getQuestion.getImageFiles().size(); i++) {
                        list.add(getQuestion.getImageFiles().get(i));
                    }
                    if (list.size() != 0) {
//                        DisplayMetrics dm = context.getResources().getDisplayMetrics();
//                        int imageSize = dm.widthPixels;
                        float imageSize = holder.content_question.getTextSize();
                        String text = getQuestion.getMcontent();
//                        SpannableString spannableString = new SpannableString(text);
                        Pattern p = Pattern.compile("\\/[^ .]+.(gif|jpg|jpeg|png)");//"\\/[^ .]+.(gif|jpg|jpeg|png)" <img src="[^"]+" />
                        final Matcher matcher = p.matcher(text);
                        while (matcher.find()) {
//                            ImageSpan imageSpan;
//                            String group=matcher.group();
                            String url = list.get(0);
//                            Drawable drawable = new URLImageParser(holder.content_question, context, (int) imageSize).getDrawable(url);//异步获取网络图片
//                            imageSpan = new ImageSpan(drawable,ImageSpan.ALIGN_BOTTOM);
//                            spannableString.setSpan(imageSpan, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                            spannableString.removeSpan(imageSpan);
                            text = text.replace(matcher.group(), url);

                            list.remove(0);
                        }
                        holder.content_question.setRichText(text);
                    }
                } else {
                    holder.content_question.setText(getQuestion.getMcontent());//没有图片就直接setText
                }
                holder.tag1.setText(getQuestion.getTAG1_ID());
                holder.tag2.setText(getQuestion.getTAG2_ID());
                holder.time.setText(getQuestion.getUpdatedAt());
                if (getQuestion.getUser().getNickname() != null) {
                    holder.username.setText(getQuestion.getUser().getNickname());
                } else {
                    holder.username.setText(getQuestion.getUser().getUsername());
                }

            }
            if (now != null) {
                BmobQuery<_User> likeQuery = new BmobQuery<>();
                Question question = new Question();
                question.setObjectId(getQuestion.getObjectId());
                likeQuery.addWhereRelatedTo("likepeople", new BmobPointer(question));
                likeQuery.findObjects(context, new FindListener<_User>() {
                    @Override
                    public void onSuccess(List<_User> list) {
                        int count = list.size();
                        holder.likecount.setText("赞的人数:" + count);
                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });
            }


        }

        if (position != 0) {
            final _User now = BmobUser.getCurrentUser(context, _User.class);
            holder.username_answer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MainPageAcitivity.class);
                    intent.putExtra("intentUserID", answers.get(position - 1).getUser().getObjectId());
                    context.startActivity(intent);
                }
            });

            if (now != null && (now.getObjectId().equals(getQuestion.getUser().getObjectId()) || now.getObjectId().equals(answers.get(position - 1).getUser().getObjectId()))) {
                holder.delete.setVisibility(View.VISIBLE);
                holder.delete.setClickable(true);
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new MaterialDialog.Builder(context)
                                .title("提示")
                                .content("是否删除？")
                                .negativeText("取消")
                                .positiveText("确认")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        Answer answer = new Answer();
                                        answer.setObjectId(answers.get(position - 1).getObjectId());
                                        answer.delete(context, new DeleteListener() {
                                            @Override
                                            public void onSuccess() {
                                                answers.remove(position - 1);
                                                notifyItemRemoved(position - 1);
                                                notifyItemRangeChanged(position - 1, answers.size());
                                                Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                                                Message msg = new Message();
                                                Bundle bundle = new Bundle();
                                                bundle.putString("questionID", getQuestion.getObjectId());
                                                msg.setData(bundle);
                                                msg.what = 8;
                                                mHandler.sendMessage(msg);
                                            }

                                            @Override
                                            public void onFailure(int i, String s) {

                                            }
                                        });

                                    }
                                })
                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                    }
                                }).show();


                    }
                });
            } else {
                holder.delete.setVisibility(View.GONE);
                holder.delete.setClickable(false);
            }


            bmobFile = answers.get(position - 1).getUser().getProfile();
            if (bmobFile != null) {
                Picasso.with(context).load(bmobFile.getUrl()).into(holder.answer_icon);
            } else {
                holder.answer_icon.setBackgroundResource(R.mipmap.ic_launcher);
            }
            holder.content.setText(answers.get(position - 1).getMcontent());
            if (answers.get(position - 1).getUser().getNickname() != null) {
                holder.username_answer.setText(answers.get(position - 1).getUser().getNickname());
            } else {
                holder.username_answer.setText(answers.get(position - 1).getUser().getUsername());
            }

            holder.like.setText(String.valueOf(answers.get(position - 1).getUps()));
            holder.question_time.setText(answers.get(position - 1).getUpdatedAt());
            if (answers.get(position - 1).getUser().getState() == 2) {
                holder.state_questionpage.setVisibility(View.VISIBLE);
            } else {
                holder.state_questionpage.setVisibility(View.GONE);
            }

            if (now != null) {
                BmobQuery<Ding> f1 = new BmobQuery<>();
                f1.addWhereEqualTo("ding", new BmobPointer(now));
                BmobQuery<Ding> f2 = new BmobQuery<>();
                f2.addWhereEqualTo("dinged", new BmobPointer(answers.get(position - 1)));
                List<BmobQuery<Ding>> queries = new ArrayList<>();
                queries.add(f1);
                queries.add(f2);
                BmobQuery<Ding> query = new BmobQuery<>();
                query.and(queries);
                query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
                query.findObjects(context, new FindListener<Ding>() {
                    @Override
                    public void onSuccess(List<Ding> list) {
                        if (list.size() == 0) {
                            holder.goodImag.setText("顶");
                        } else if (list.size() == 1) {
                            if (list.get(0).getObjectId() != null) {
                                dingID[position - 1] = list.get(0).getObjectId();
                                holder.goodImag.setText("已顶");
                            }
                        } else {
                            Toast.makeText(context, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(context, "网络连接超时", Toast.LENGTH_SHORT).show();
                    }
                });
            }


            holder.zanLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (now != null) {
                        if (holder.goodImag.getText().toString() == "顶") {
                            final Answer answer = new Answer();
                            answer.increment("ups");
                            answer.update(context, answers.get(position - 1).getObjectId(), new UpdateListener() {
                                @Override
                                public void onSuccess() {
                                    holder.goodImag.setText("已顶");
                                    String up = (String) holder.like.getText();
                                    holder.like.setText(String.valueOf((Integer.parseInt(up)) + 1));
                                    holder.like.setTextColor(Color.parseColor("#ffffff"));
                                    Message message = new Message();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("answerID", answers.get(position - 1).getObjectId());
                                    bundle.putInt("flag", 0);
                                    bundle.putInt("pos", position - 1);
                                    message.setData(bundle);
                                    message.obj = dingID;
                                    message.what = 7;
                                    mHandler.sendMessage(message);

                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    holder.goodImag.setText("顶");
                                }
                            });
                        } else if (holder.goodImag.getText().toString() == "已顶") {
                            Answer answer = new Answer();
                            answer.increment("ups", -1);
                            answer.update(context, answers.get(position - 1).getObjectId(), new UpdateListener() {
                                @Override
                                public void onSuccess() {
                                    holder.goodImag.setText("顶");
                                    String a = (String) holder.like.getText();
                                    holder.like.setText(String.valueOf((Integer.parseInt(a)) - 1));
                                    holder.like.setTextColor(Color.parseColor("#ffffff"));
                                    Message message = new Message();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("answerID", answers.get(position - 1).getObjectId());
                                    bundle.putInt("flag", 1);
                                    bundle.putString("dingID", dingID[position - 1]);
                                    message.setData(bundle);
                                    message.what = 7;
                                    mHandler.sendMessage(message);
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    Toast.makeText(context, "传输异常", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                        Toast.makeText(context, "要先登录才能顶哦！", Toast.LENGTH_SHORT).show();
                        mHandler.sendEmptyMessage(0);
                    }
                }
            });

            holder.item_question.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("answerID", answers.get(position - 1).getObjectId());
                    bundle.putString("answer_userID", answers.get(position - 1).getUser().getObjectId());
                    msg.setData(bundle);
                    msg.obj = v;
                    msg.what = 6;
                    mHandler.sendMessage(msg);
                }
            });
        }

        mHandler.sendEmptyMessage(15);
    }

    @Override
    public int getItemCount() {
        return answers.size() + 1;
    }

    class VH extends RecyclerView.ViewHolder {
        TextView content;
        ImageButton question_delete;
        TextView title_question;
        RichText content_question;
        TextView tag1;
        TextView tag2;
        TextView username;
        TextView time;
        TextView username_answer;
        TextView like;
        TextView question_time;
        LinearLayout item_question;
        ImageView answer_icon;
        ImageView state_questionpage;
        LinearLayout zanLayout;
        TextView goodImag;
        ImageButton delete;
        TextView likecount;

        public VH(View itemView) {
            super(itemView);
            delete = (ImageButton) itemView.findViewById(R.id.delete11);
            item_question = (LinearLayout) itemView.findViewById(R.id.item_question);
            question_time = (TextView) itemView.findViewById(R.id.question_time);
            like = (TextView) itemView.findViewById(R.id.like_questionpage_item);
            username_answer = (TextView) itemView.findViewById(R.id.username_questionpage);
            content = (TextView) itemView.findViewById(R.id.content_questionpage);
            question_delete = (ImageButton) itemView.findViewById(R.id.more_questionpage);
            title_question = (TextView) itemView.findViewById(R.id.title_questionpage);
            content_question = (RichText) itemView.findViewById(R.id.content_question);
            tag1 = (TextView) itemView.findViewById(R.id.tag1_questionpage);
            tag2 = (TextView) itemView.findViewById(R.id.tag2_questionpage);
            username = (TextView) itemView.findViewById(R.id.username_question);
            time = (TextView) itemView.findViewById(R.id.time_question);
            answer_icon = (ImageView) itemView.findViewById(R.id.user_icon_questionpage);
            state_questionpage = (ImageView) itemView.findViewById(R.id.state_questionpage);
            zanLayout = (LinearLayout) itemView.findViewById(R.id.zanlayout_question_item);
            goodImag = (TextView) itemView.findViewById(R.id.good_question_item);
            likecount = (TextView) itemView.findViewById(R.id.likecount_question);
        }
    }


}