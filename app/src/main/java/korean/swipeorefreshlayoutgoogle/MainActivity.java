package korean.swipeorefreshlayoutgoogle;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //随笔  ctrl + alt + l 格式化代码
//    private RecyclerView listView;
    private ListView listView;

    public List<String> mList;

    private RefreshLayout mRefreshLayout;

    private SampleAdapter adapter;

    private HomeAdapter homeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listview);
        mRefreshLayout = (RefreshLayout) findViewById(R.id.refresh_widget);

        mList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mList.add(String.valueOf(i));
        }

        adapter = new SampleAdapter(this, R.layout.list_item, mList);
        listView.setAdapter(adapter);

//        homeAdapter = new HomeAdapter();
//        listView.setLayoutManager(new LinearLayoutManager(this));
//        listView.setAdapter(homeAdapter);

//        mRefreshLayout.setListView(listView);
        //2B91D8

        //调整那个下拉刷新距离顶部的距离
        //mRefreshLayout.setProgressViewEndTarget(true,400);
        //设置中间圈圈的颜色
        mRefreshLayout.setColorSchemeColors(Color.parseColor("#2B91D8"));

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override

            public void onRefresh() {
                Log.d("TAG", "正在刷新");

                //刷新2s后停止刷新
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }

        });

        mRefreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoadMore() {

                if(mList.size() >= 30){
                    mRefreshLayout.setLoading(false);
                    return;
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        for (int i = 20; i < 30; i++) {
                            mList.add(String.valueOf(i));
                        }

                        mRefreshLayout.setLoading(false);
                        adapter.notifyDataSetChanged();
                    }
                },3000);
            }
        });

        //设置正在刷新
        //mRefreshLayout.setRefreshing(true);
    }

    class SampleAdapter extends ArrayAdapter<String> {

        private final LayoutInflater mInflater;
        private final List<String> mData;

        public SampleAdapter(Context context, int layoutResourceId, List<String> data) {
            super(context, layoutResourceId, data);
            mData = data;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.list_item, parent, false);
                viewHolder.tvNum = (TextView) convertView.findViewById(R.id.tv_num);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.tvNum.setText(mData.get(position));
            return convertView;
        }

        class ViewHolder {
            TextView tvNum;
        }
    }


    class HomeAdapter extends  RecyclerView.Adapter<HomeAdapter.MyViewHolder>{


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.list_item, parent, false));

            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {

            holder.tv.setText(mList.get(position));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{

            TextView tv;
            public MyViewHolder(View itemView) {
                super(itemView);
                tv = (TextView) itemView.findViewById(R.id.tv_num);
            }
        }
    }
}
