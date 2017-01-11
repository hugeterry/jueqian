package us.xingkong.jueqian.module.RealS.Content;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import us.xingkong.jueqian.R;
import us.xingkong.jueqian.adapter.BaseAdapter;
import us.xingkong.jueqian.adapter.PartHotAdapter;
import us.xingkong.jueqian.adapter.PartTypeAdapter;
import us.xingkong.jueqian.base.BaseFragment;
import us.xingkong.jueqian.bean.RealSBean.Results;
import us.xingkong.jueqian.data.RealSData.RealSRepository;
import us.xingkong.jueqian.utils.LogUtils;

/**
 * Created by hugeterry(http://hugeterry.cn)
 * Date: 17/1/8 15:30
 */

public class ContentFragment extends BaseFragment<ContentContract.Presenter> implements ContentContract.View {

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.rv_content)
    RecyclerView mRecyclerView;

    private LinearLayoutManager mLinearLayoutManager;
    private PartHotAdapter mPartHotAdapter;
    private PartTypeAdapter mPartTypeAdapter;

    private int pageNum = 1;
    private String mRealSTitle;
    private static final String REALS_TITLE = "reals_title";
    private List<Results> mResults;

    public static ContentFragment getInstance(String title) {
        ContentFragment fra = new ContentFragment();
        Bundle bundle = new Bundle();
        bundle.putString(REALS_TITLE, title);
        fra.setArguments(bundle);
        return fra;
    }

    @Override
    protected ContentContract.Presenter createPresenter() {
        return new ContentPresenter(new RealSRepository(), this);
    }

    @Override
    protected int bindLayout() {
        return R.layout.fragment_reals_content;
    }

    @Override
    protected void prepareData(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        mRealSTitle = bundle.getString(REALS_TITLE);
        if (mRealSTitle.equals("热门")) mRealSTitle = "all";
    }

    @Override
    protected void initView(View rootView) {
        initRecyclerView();
    }

    private void initRecyclerView() {
        mLinearLayoutManager = new LinearLayoutManager(mRecyclerView.getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        switch (mRealSTitle) {
            case "all":
                mPartHotAdapter = new PartHotAdapter(getActivity());
                mRecyclerView.setAdapter(mPartHotAdapter);
                break;
            default:
                mPartTypeAdapter = new PartTypeAdapter(getActivity());
                mRecyclerView.setAdapter(mPartTypeAdapter);
                break;
        }

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mPresenter.getRealSList(mRealSTitle, 1);
    }

    @Override
    protected void initEvent() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getRealSList(mRealSTitle, 1);
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastVisiableItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();
                BaseAdapter commonAdapter = mPartHotAdapter != null ? mPartHotAdapter : mPartTypeAdapter;
                if (lastVisiableItemPosition + 1 == commonAdapter.getItemCount()) {
                    if (pageNum == 1) {
                        pageNum++;
                    }
                    mPresenter.getRealSList(mRealSTitle, pageNum);
                }

            }
        });
    }

    @Override
    public void loadSuccess(int page) {
        if (page == 1) {
            mSwipeRefreshLayout.setRefreshing(false);
            pageNum = 1;
        } else {
            mRecyclerView.scrollToPosition((page - 1) * 20);
            pageNum = ++page;
        }
    }

    @Override
    public void loadFailure(int page) {
        if (page == 1) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showRefresh(boolean isRefresh) {
        mSwipeRefreshLayout.setRefreshing(isRefresh);
    }

    @Override
    public void showRealSList(int page, List<Results> realSList) {
        switch (mRealSTitle) {
            case "all":
                if (page == 1) {
                    mPartHotAdapter.replaceData(realSList);
                } else {
                    mPartHotAdapter.addData(realSList);
                }
                break;
            default:
                if (page == 1) {
                    mPartTypeAdapter.replaceData(realSList);
                } else {
                    mPartTypeAdapter.addData(realSList);
                }
                break;
        }
    }
}
