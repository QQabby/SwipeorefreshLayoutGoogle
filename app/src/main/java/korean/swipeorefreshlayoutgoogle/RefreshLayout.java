package korean.swipeorefreshlayoutgoogle;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by xuqianqian on 2016/8/31.
 */

public class RefreshLayout extends SwipeRefreshLayout implements AbsListView.OnScrollListener {

    private View mListViewFooter;

    private ListView mListView;

    private RecyclerView mRecyclerView;

    private boolean isLoading = false;

    private OnLoadListener mLoadListener;

    public int lastIndex = 0;

    public RefreshLayout(Context context) {
        super(context);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mListViewFooter = LayoutInflater.from(context).inflate(R.layout.listview_footer, null, false);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mListView == null) {
            getListView();
        }
    }

    private void getListView() {

        int childs = getChildCount();
        if (childs > 0) {
            View childView = getChildAt(0);
            if (childView instanceof ListView) {
                mListView = (ListView) childView;
                mListView.setOnScrollListener(this);
            } else if (childView instanceof RecyclerView) {
                mRecyclerView = (RecyclerView) childView;
                mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        //判断是否到了底部，并且不是在加载数据的状态
//                        if(mListView.getLastVisiblePosition() == mListView.getAdapter().getCount() - 1
//                                && isLoading == false){
//                            //首先设置加载状态
//                            setLoading(true);
//                            //调用加载更多的方法
//                            mLoadListener.onLoadMore();
//                        }
                        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                        lastIndex = linearLayoutManager.findLastVisibleItemPosition()+1;
                        Log.d("TAG" ,"linearLayoutManager.findLastVisibleItemPosition():"+linearLayoutManager.findLastCompletelyVisibleItemPosition());
//                        if (linearLayoutManager.findLastVisibleItemPosition() == linearLayoutManager.getItemCount()
//                                && isLoading == false) {
                        if(linearLayoutManager.findLastVisibleItemPosition() == 19 && isLoading == false){
                            //首先设置加载状态
                            setLoading(true,true);
                            //调用加载更多的方法
                            mLoadListener.onLoadMore();
                        }
                    }
                });
            }
        }
    }

    public void setLoading(boolean loading) {
        this.isLoading = loading;
        if (isLoading) {
            mListView.addFooterView(mListViewFooter);
        } else {

            mListView.removeFooterView(mListViewFooter);
        }
    }
    public void setLoading(boolean loading,boolean flag) {
        this.isLoading = loading;
        if (isLoading) {

            mRecyclerView.addView(mListViewFooter,20);
            //mListView.addFooterView(mListViewFooter);
        } else {
            mRecyclerView.removeViewAt(20);
            //mListView.removeFooterView(mListViewFooter);
        }
    }


    public void setOnLoadListener(OnLoadListener loadListener) {
        this.mLoadListener = loadListener;
    }

    // 加载更多的接口
    public interface OnLoadListener {
        public void onLoadMore();
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

        //判断是否到了底部，并且不是在加载数据的状态
        if (mListView.getLastVisiblePosition() == mListView.getAdapter().getCount() - 1
                && isLoading == false) {
            //首先设置加载状态
            setLoading(true);
            //调用加载更多的方法
            mLoadListener.onLoadMore();
        }
    }
}
