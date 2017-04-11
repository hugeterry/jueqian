package us.xingkong.jueqian.module.me.mycollection;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;
import us.xingkong.jueqian.JueQianAPP;
import us.xingkong.jueqian.R;
import us.xingkong.jueqian.adapter.MyCollectionAdapter;
import us.xingkong.jueqian.base.BaseActivity;
import us.xingkong.jueqian.bean.ForumBean.BombBean.Question;
import us.xingkong.jueqian.bean.ForumBean.BombBean._User;

/**
 * Created by PERFECTLIN on 2017/1/10 0010.
 */

public class MyCollectionActivity extends BaseActivity<MyCollectionContract.Presenter> implements MyCollectionContract.View {
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    List<Question> questions=new ArrayList<>();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    initRecyclerView();
                    break;
            }

        }
    };

    @Override
    protected MyCollectionContract.Presenter createPresenter() {
        return new MyCollectionPresenter(this, mHandler);
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_mycollection;
    }

    @Override
    protected void prepareData() {
        BmobUser bmobUser = BmobUser.getCurrentUser(JueQianAPP.getAppContext());
        BmobQuery<Question> query = new BmobQuery<Question>();;
        query.addWhereRelatedTo("collections", new BmobPointer(bmobUser));
        query.findObjects(JueQianAPP.getAppContext(), new FindListener<Question>() {
            @Override
            public void onSuccess(List<Question> list) {
                questions = list;
                showToast("获取收藏表成功");
                mHandler.sendEmptyMessage(1);
            }

            @Override
            public void onError(int i, String s) {
                showToast("获取收藏表失败");
            }
        });

    }

    @Override
    protected void initView() {
        setToolbar();
        //initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new MyCollectionAdapter(mHandler,questions));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(MyCollectionActivity.this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private void setToolbar() {
        ActionBar acb = getSupportActionBar();
        acb.setDisplayHomeAsUpEnabled(true);
        acb.setTitle("我的收藏");
    }

    @Override
    protected void initData(Bundle savedInstanceState) {


    }

    @Override
    protected void initEvent() {

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
}
