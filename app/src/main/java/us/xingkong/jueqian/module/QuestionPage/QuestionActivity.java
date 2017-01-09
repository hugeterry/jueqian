package us.xingkong.jueqian.module.QuestionPage;

import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.ArrayList;

import butterknife.BindView;
import us.xingkong.jueqian.R;
import us.xingkong.jueqian.adapter.QuestionRecyclerViewAdapter;
import us.xingkong.jueqian.base.BaseActivity;
import us.xingkong.jueqian.module.Comment.CommentActivity;


/**
 * Created by boluoxiaomo
 * Date: 17/1/9
 */

public class QuestionActivity extends BaseActivity<QuestionContract.Presenter> implements QuestionContract.View {


    @BindView(R.id.recyclerview_questionpage)
    RecyclerView recyclerviewQuestionpage;
    private QuestionRecyclerViewAdapter recyclerViewAdapter;
    private ArrayList questionSets;
    private ArrayList<ArrayList> answerSetsArr;
    private Handler mHandler;


    @Override
    protected QuestionContract.Presenter createPresenter() {
        return new QuestionPresenter(this);
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_question;
    }

    @Override
    protected void prepareData() {

        questionSets = new ArrayList();
        questionSets.add("header");
        answerSetsArr = new ArrayList();
        for (int i = 0; i < 30; ++i) {
            ArrayList answerSets = new ArrayList();
            answerSets.add("position " + i);
            answerSetsArr.add(answerSets);
        }

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        break;
                    case 2:
                        Intent intent = new Intent(getApplicationContext(), CommentActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Override
    protected void initView() {
        setToolbarBackEnable("问题详情");
        recyclerViewAdapter = new QuestionRecyclerViewAdapter(questionSets, answerSetsArr,mHandler);
        recyclerviewQuestionpage.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerviewQuestionpage.setAdapter(recyclerViewAdapter);
        recyclerviewQuestionpage.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


    }


    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
