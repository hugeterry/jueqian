package us.xingkong.jueqian.module.Forum.QuestionPage.Comment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;

import butterknife.BindView;
import us.xingkong.jueqian.R;
import us.xingkong.jueqian.adapter.CommentRecyclerViewAdapter;
import us.xingkong.jueqian.base.BaseActivity;
import us.xingkong.jueqian.bean.ForumBean.BombBean.Answer;
import us.xingkong.jueqian.bean.ForumBean.BombBean.Comment;


public class CommentActivity extends BaseActivity<CommentContract.Presenter> implements CommentContract.View {


    @BindView(R.id.comment_recyclerview)
    RecyclerView recyclerviewCommentpage;
    private CommentRecyclerViewAdapter recyclerViewAdapter;
    private Context mContext;
    private String answerID;
    private String questionID;
    private Answer answer = new Answer();
    private ArrayList<Comment> comments = new ArrayList<>();
    private String comment_content;
    @BindView(R.id.edit_comment)
    EditText edit_comment;
    @BindView(R.id.edit_send)
    Button edit_send;
    @BindView(R.id.comment_tab)
    LinearLayout tab;
    @BindView(R.id.refreshLayout_comment)
    SwipeRefreshLayout refreshLayout;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    finish();
                    break;
                case 1:
                    answer = (Answer) msg.obj;
                    handler.sendEmptyMessage(2);
                    break;
                case 2:
                    initRecyclerView();
                    break;
//                case 3: //刷新数据
//                    comments.clear();
//                    mPresenter.getAnswerComments(mContext, handler, answerID, comments);
//                    recyclerViewAdapter.notifyDataSetChanged();
//                    refreshLayout.setRefreshing(false);
//                    break;
                case 4:
                    int position = (int) msg.obj;
                    recyclerViewAdapter.notifyDataSetChanged();
                    recyclerViewAdapter.notifyItemInserted(position);

                    break;
            }
        }
    };

    @Override
    protected CommentContract.Presenter createPresenter() {
        return new CommentPresenter(this);
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_comment;
    }

    @Override
    protected void prepareData() {
        mContext = this;
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        answerID = bundle.getString("answerID");
        questionID = bundle.getString("questionID");
        refreshLayout.setRefreshing(true);
        mPresenter.getAnswerComments(mContext, handler, answerID, comments);
        mPresenter.getAnswer(mContext, answerID, handler);
        refreshLayout.setRefreshing(false);
//        mHandler = new Handler(getMainLooper()){
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                switch (msg.what){
//                    case 1:
//                        new MaterialDialog.Builder(mContext)
//                                .items(R.array.option_head)
//                                .itemsCallback(new MaterialDialog.ListCallback() {
//                                    @Override
//                                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
//                                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
//                                    }
//                                })
//                                .show();
//                        break;
//                    case 2:
//                        break;
//                    case 3:
//                        break;
//                }
//            }
//        };
    }

    private void initSwipeRefreshLayout() {
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        refreshLayout.setProgressViewEndTarget(true, 300);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                Toast.makeText(getApplicationContext(), "刷新", Toast.LENGTH_SHORT).show();
//                handler.sendEmptyMessage(3);
                refreshLayout.setRefreshing(false);
            }
        });
    }


    @Override
    protected void initView() {
        setToolbarBackEnable("评论");
        initSwipeRefreshLayout();
    }

    private void initRecyclerView() {
        recyclerViewAdapter = new CommentRecyclerViewAdapter(mContext, handler, answer, comments);
        recyclerviewCommentpage.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerviewCommentpage.setAdapter(recyclerViewAdapter);
        recyclerviewCommentpage.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerviewCommentpage.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    if (!recyclerView.canScrollVertically(1)) {
////                        Toast.makeText(getApplicationContext(), "到底啦", Toast.LENGTH_SHORT).show();
//                    }
//                    if (!recyclerView.canScrollVertically(-1)) {
//                        Toast.makeText(getApplicationContext(), "刷新", Toast.LENGTH_SHORT).show();
//                        refreshLayout.setRefreshing(true);
//                        handler.sendEmptyMessage(3);
//                    }
//                }
//            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    tab.setVisibility(View.GONE);//底部的tab隐藏和出现
                } else if (dy < 0) {
                    tab.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {
        edit_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment_content = edit_comment.getText().toString();
                if (comment_content.isEmpty()) {
                    showToast("评论内容不能为空");
                } else {
                    Comment comment;
                    comment = mPresenter.addNewComment(mContext, handler, comment_content, answerID, questionID);
                    recyclerViewAdapter.addItem(0, comment);
                    edit_comment.setText("");
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


}
