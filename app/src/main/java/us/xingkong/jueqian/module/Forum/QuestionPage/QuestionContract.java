package us.xingkong.jueqian.module.Forum.QuestionPage;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;

import us.xingkong.jueqian.base.BasePresenter;
import us.xingkong.jueqian.base.BaseView;
import us.xingkong.jueqian.bean.ForumBean.BombBean.Answer;

/**
 * Created by boluoxiaomo
 * Date: 17/1/9
 */

public interface QuestionContract {

    interface View extends BaseView<Presenter> {
        void setRecyclewViewBug();
    }

    interface Presenter extends BasePresenter {
        void getQuestion(Context context,String questionID,Handler handler);
        ArrayList<Answer> getQuestionAnswer(Context context, Handler handler, String questionID,ArrayList<Answer> answers);
        void zan(Context context,Handler handler,String questionID);
        void shoucan(Context context,Handler handler,String questionID);
    }
}
