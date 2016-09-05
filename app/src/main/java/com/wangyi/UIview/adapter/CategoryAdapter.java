package com.wangyi.UIview.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marshalchen.ultimaterecyclerview.ItemTouchListenerAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.wangyi.UIview.widget.view.UltimateListView;
import com.wangyi.define.bean.BookData;
import com.wangyi.define.bean.UserInfo;
import com.wangyi.function.HttpsFunc;
import com.wangyi.function.UserManagerFunc;
import com.wangyi.reader.R;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by eason on 8/15/16.
 */
public class CategoryAdapter extends PagerAdapter {
    enum category{课本,作业,ppt,笔记}
    Context context;
    SearchBookAdapter[] adapters = new SearchBookAdapter[4];
    int[] starts = new int[]{0,0,0,0};
    Handler handler;
    ItemTouchListenerAdapter.RecyclerViewOnItemClickListener listener;
    public CategoryAdapter(Context context, ItemTouchListenerAdapter.RecyclerViewOnItemClickListener listener,Handler handler){
        this.context = context;
        this.listener = listener;
        this.handler = handler;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        LayoutInflater inflater = (LayoutInflater) x.app()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = null;
        if(position<4&&position>-1) {
            rootView = inflater.inflate(R.layout.layout_recycleview, null);
            UltimateRecyclerView bookList = (UltimateRecyclerView)rootView.findViewById(R.id.booklist);
            if(adapters[position]==null) {
                SearchBookAdapter adapter = new SearchBookAdapter(new ArrayList<BookData>());
                adapters[position] = adapter;
            }

            UltimateListView view = new UltimateListView(bookList,adapters[position],context);
            view.beforeFuncset();
            view.enableLoadmore(new UltimateRecyclerView.OnLoadMoreListener() {
                @Override
                public void loadMore(int itemsCount, final int maxLastVisiblePosition) {
                    if(UserManagerFunc.getInstance().isLogin()){
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                UserInfo user = UserManagerFunc.getInstance().getUserInfo();
                                HttpsFunc.getInstance().connect(handler).searchBooksBySubject("全部",user.university,getCategory(position),starts[position]);
                            }
                        }, 500);
                    }
                }
            });

            view.enableItemClick(new ItemTouchListenerAdapter(bookList.mRecyclerView, listener));
            view.afterFuncset();
            ((ViewPager) container).addView(rootView, position);
        }
        return rootView;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "原教材";
            case 1:
                return "重点归纳";
            case 2:
                return "PPT";
            case 3:
                return "学霸说";
            default:
                return "-";
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    public SearchBookAdapter getAdapter(int current){
        return adapters[current];
    }

    public int getStart(int current){
        return starts[current];
    }

    public void increaseStart(int current,int start){
        starts[current] += start;
    }

    public String getCategory(int current){
        return category.values()[current].toString();
    }
}
