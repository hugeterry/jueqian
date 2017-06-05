package us.xingkong.jueqian.module.me.mymessage.allmessage;

import us.xingkong.jueqian.base.BasePresenter;
import us.xingkong.jueqian.base.BaseView;

/**
 * Created by PERFECTLIN on 2017/1/10 0010.
 */

public class AllMessageContract {
    interface View extends BaseView<Presenter> {
        void loadSuccess();

        void loadFailure();

        void showRefresh(boolean isRefresh);
    }

    interface Presenter extends BasePresenter {
        void getMessageList();
    }

}
