package com.wangyi.UIview.adapter;

import android.view.View;

import com.marshalchen.ultimaterecyclerview.quickAdapter.easyRegularAdapter;
import com.wangyi.UIview.adapter.viewholder.GodlistVH;
import com.wangyi.define.bean.UserRank;
import com.wangyi.function.HttpsFunc;
import com.wangyi.reader.R;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by eason on 5/9/16.
 */
public class GodlistAdapter extends easyRegularAdapter<UserRank,GodlistVH> {
    public GodlistAdapter(ArrayList<UserRank> users) {
        super(users);
    }

    @Override
    protected int getNormalLayoutResId() {
        return R.layout.byorderlist_item;
    }

    @Override
    protected GodlistVH newViewHolder(View view) {
        return new GodlistVH(view);
    }

    @Override
    protected void withBindHolder(GodlistVH holder, UserRank data, int position) {
        holder.name.setText(data.userName);
        holder.downloadNum.setText("影响力："+data.downloadNum);
        holder.from.setText(data.university);
        holder.number.setMessage(data.rank+"");
        ImageOptions options=new ImageOptions.Builder()
                .setLoadingDrawableId(R.drawable.headpic)
                .setFailureDrawableId(R.drawable.headpic)
                .setUseMemCache(true)
                .setCircular(true)
                .setIgnoreGif(false)
                .build();
        x.image().bind(
                holder.pic, HttpsFunc.host +
                        data.url +
                        "headPic.jpg",options
        );
    }
}
