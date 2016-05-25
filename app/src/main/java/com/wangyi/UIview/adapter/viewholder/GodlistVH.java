package com.wangyi.UIview.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.wangyi.UIview.widget.view.NumberView;
import com.wangyi.reader.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by eason on 5/11/16.
 */
public class GodlistVH extends UltimateRecyclerviewViewHolder {
    @ViewInject(R.id.pic)
    public ImageView pic;
    @ViewInject(R.id.name)
    public TextView name;
    @ViewInject(R.id.downloadnum)
    public TextView downloadNum;
    @ViewInject(R.id.from)
    public TextView from;
    @ViewInject(R.id.number)
    public NumberView number;

    public GodlistVH(View itemView) {
        super(itemView);
        x.view().inject(this,itemView);
    }
}
