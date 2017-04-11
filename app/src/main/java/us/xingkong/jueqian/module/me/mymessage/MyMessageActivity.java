package us.xingkong.jueqian.module.me.mymessage;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import us.xingkong.jueqian.JueQianAPP;
import us.xingkong.jueqian.R;
import us.xingkong.jueqian.adapter.MyMessageRecyclerAdapter;
import us.xingkong.jueqian.base.BaseActivity;
import us.xingkong.jueqian.bean.ForumBean.BombBean.Answer;
import us.xingkong.jueqian.bean.ForumBean.BombBean.Comment;
import us.xingkong.jueqian.bean.ForumBean.BombBean.Question;

/**
 * Created by PERFECTLIN on 2017/1/10 0010.
 */

public class MyMessageActivity extends BaseActivity<MyMessageContract.Presenter> implements MyMessageContract.View {

    @BindView(R.id.mymessage_recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    List<Comment> answers = new ArrayList<>();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case 2:
                    initRecyclerView();
                    break;
            }
        }
    };

    @Override
    protected MyMessageContract.Presenter createPresenter() {
        return new MyMessagePresenter(this);
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_mymessage;
    }

    @Override
    protected void prepareData() {
        BmobUser bmobUser = BmobUser.getCurrentUser(JueQianAPP.getAppContext());
        BmobQuery<Comment> query = new BmobQuery<>();
        query.addWhereRelatedTo("comments", new BmobPointer(bmobUser));
        query.findObjects(JueQianAPP.getAppContext(), new FindListener<Comment>() {
            @Override
            public void onSuccess(List<Comment> list) {
                System.out.println("555555555555555555555555"+list.size());
                answers = list;
                showToast("获取我的消息列表成功");
                System.out.println("555555555555555555555555"+answers.size());
                mHandler.sendEmptyMessage(2);
            }

            @Override
            public void onError(int i, String s) {
                showToast("获取我的消息列表失败");
            }
        });
    }

    @Override
    protected void initView() {
        setToolbar();
        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new MyMessageRecyclerAdapter(mHandler, answers));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(MyMessageActivity.this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


    }

    private void setToolbar() {
        ActionBar acb = getSupportActionBar();
        acb.setTitle("我的消息");
        acb.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
    }

    @Override
    protected void initEvent() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        mSwipeRefreshLayout.setProgressViewEndTarget(true, 200);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2500);
                            mHandler.sendEmptyMessage(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loadSuccess() {

    }

    @Override
    public void loadFailure() {

    }

    @Override
    public void showRefresh(boolean isRefresh) {

    }
}
