package us.xingkong.jueqian.module.Forum.NewAnswer;


import android.content.Context;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import us.xingkong.jueqian.base.BasePresenterImpl;
import us.xingkong.jueqian.bean.ForumBean.BombBean.Answer;
import us.xingkong.jueqian.bean.ForumBean.BombBean.Question;
import us.xingkong.jueqian.bean.ForumBean.BombBean._User;


/**
 * Created by lenovo on 2017/3/30.
 */

public class NewAnswerPresenter extends BasePresenterImpl implements NewAnswerContract.Presenter {

    private final NewAnswerContract.View mView;

    public NewAnswerPresenter(NewAnswerContract.View view) {
        mView = view;
        this.mView.setPresenter(this);
    }


    @Override
    public void addNewAnswer(Context context,String newAnswer,String questionID) {
        _User user= BmobUser.getCurrentUser(context,_User.class);
        Question question=new Question();
        question.setObjectId(questionID);
        Answer answer=new Answer();
        answer.setUser(user);
        answer.setQuestion(question);
        answer.setMcontent(newAnswer);
        answer.setState(1);//用户状态
        answer.setUps(0);//点赞数
        answer.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                mView.showToast("save successful");
            }

            @Override
            public void onFailure(int i, String s) {
                mView.showToast("save fail");
            }
        });
    }

}
