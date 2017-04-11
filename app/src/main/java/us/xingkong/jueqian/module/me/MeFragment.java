package us.xingkong.jueqian.module.me;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;
import us.xingkong.jueqian.JueQianAPP;
import us.xingkong.jueqian.R;
import us.xingkong.jueqian.base.BaseFragment;
import us.xingkong.jueqian.bean.ForumBean.BombBean._User;
import us.xingkong.jueqian.module.EditUser.EditUserActivity;
import us.xingkong.jueqian.module.Login.LoginActivity;
import us.xingkong.jueqian.module.me.myanswer.MyAnswerActivity;
import us.xingkong.jueqian.module.me.mycollection.MyCollectionActivity;
import us.xingkong.jueqian.module.me.mymessage.MyMessageActivity;
import us.xingkong.jueqian.module.me.myquestions.MyQuestionsAcitivity;
import us.xingkong.jueqian.module.me.myrecentlook.MyRecentLookActivity;
import us.xingkong.jueqian.module.me.mysettings.MySettingsActivity;

/**
 * Created by PERFECTLIN on 2017/1/10 0010.
 */

public class MeFragment extends BaseFragment<MeContract.Presenter> implements MeContract.View {
    @BindView(R.id.me_tv_nickname)
    TextView mTextView_nickname;
    @BindView(R.id.me_linerlayout_mymessage)
    RelativeLayout mLinerlayout_mymessage;
    @BindView(R.id.me_linerlayout_mycollection)
    RelativeLayout mLinerlayout_mycollection;
    @BindView(R.id.me_linerlayout_myanswer)
    RelativeLayout mLinerlayout_myanswer;
    @BindView(R.id.me_linerlayout_myrecentlook)
    RelativeLayout mLinerlayout_myrecentlook;
    @BindView(R.id.me_linerlayout_settings)
    RelativeLayout mLinerlayout_mysettings;
    @BindView(R.id.me_linerlayout_myquestions)
    RelativeLayout mLinerlayout_myquestions;
    @BindView(R.id.me_layout_edit)
    CardView mCardView_editinfo;
    @BindView(R.id.me_layout_signout)
    CardView mCardView_signout;
    @BindView(R.id.me_profile)
    CircleImageView mCircleImageView_profile;

    private int mPageCount;
    private static final String PAGE_COUNT = "page_count";
    private boolean isLogin;
    private BmobFile bmobFile=new BmobFile();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if(bmobFile==null) {
                        showToast("当前用户没有设置头像");
                        break;
                    }
                        bmobFile.download(JueQianAPP.getAppContext(), new DownloadFileListener() {
                            @Override
                            public void onSuccess(String s) {
                                showToast("下载头像成功");
                                File file = new File(s);
                                if (file.exists()) {
                                    Bitmap bm = BitmapFactory.decodeFile(s);
                                    //将图片显示到ImageView中
                                    mCircleImageView_profile.setImageBitmap(bm);
                                } else {
                                    showToast("获取头像路径失败");
                                }
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                showToast("下载头像失败");
                            }
                        });

                    break;
            }

        }
    };


    public static MeFragment getInstance(int page_count) {
        MeFragment fra = new MeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PAGE_COUNT, page_count);
        fra.setArguments(bundle);
        return fra;
    }

    @Override
    protected MeContract.Presenter createPresenter() {
        return new MePresenter(this);
    }

    @Override
    protected void prepareData(Bundle savedInstanceState) {
        _User user = BmobUser.getCurrentUser(JueQianAPP.getAppContext(), _User.class);
        if (user == null) {
        } else {
            mTextView_nickname.setText(user.getUsername());
        }
    }

    @Override
    protected int bindLayout() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initView(View rootView) {
        judgeLogin();
        toEditInfo();
        toMyMessage();
        toMyCollection();
        toMyAnswer();
        toMyRecentLook();
        toMySettings();
        toMyQusetions();
        toSignOut();
        if (isLogin) getProfile();
    }

    private void toSignOut() {
        if (isLogin == false) {
            mCardView_signout.setVisibility(View.INVISIBLE);
        }
        mCardView_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BmobUser.logOut(JueQianAPP.getAppContext());   //清除缓存用户对象
                BmobUser currentUser = BmobUser.getCurrentUser(JueQianAPP.getAppContext()); // 现在的currentUser是null了
                Intent intent = new Intent(JueQianAPP.getAppContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    private void judgeLogin() {
        BmobUser bmobUser = BmobUser.getCurrentUser(JueQianAPP.getAppContext());
        if (bmobUser == null) isLogin = false;
        else isLogin = true;
    }

    private void toMyCollection() {
        mLinerlayout_mycollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLogin) {
                    Intent intent = new Intent(getContext(), MyCollectionActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(JueQianAPP.getAppContext(), "请先登录", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void toMyAnswer() {
        mLinerlayout_myanswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLogin) {
                    Intent intent = new Intent(getContext(), MyAnswerActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(JueQianAPP.getAppContext(), "请先登录", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void toMyRecentLook() {
        mLinerlayout_myrecentlook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLogin) {
                    Intent intent = new Intent(getContext(), MyRecentLookActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(JueQianAPP.getAppContext(), "请先登录", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void toMySettings() {
        mLinerlayout_mysettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MySettingsActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void initEvent() {
    }

    private void toMyMessage() {
        mLinerlayout_mymessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLogin) {
                    Intent intent = new Intent(getContext(), MyMessageActivity.class);
                    startActivity(intent);
                } else {

                    Toast.makeText(JueQianAPP.getAppContext(), "请先登录", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void toMyQusetions() {
        mLinerlayout_myquestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLogin) {
                    Intent intent = new Intent(getContext(), MyQuestionsAcitivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(JueQianAPP.getAppContext(), "请先登录", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void toEditInfo() {
        mCardView_editinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _User user = BmobUser.getCurrentUser(JueQianAPP.getAppContext(), _User.class);
                if (user == null) {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getContext(), EditUserActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void getProfile() {

        BmobUser user = BmobUser.getCurrentUser(JueQianAPP.getAppContext());
        BmobQuery<_User> query = new BmobQuery<>();
        query.addWhereEqualTo("objectId", user.getObjectId());
        query.addQueryKeys("profile");
        query.findObjects(JueQianAPP.getAppContext(), new FindListener<_User>() {
            @Override
            public void onSuccess(List<_User> list) {
                bmobFile = list.get(0).getProfile();
                showToast("获取头像成功");
                showToast("B:" + bmobFile);
                mHandler.sendEmptyMessage(1);

            }

            @Override
            public void onError(int i, String s) {
                showToast("获取头像失败");
            }
        });
        if (bmobFile == null) return;


    }


}