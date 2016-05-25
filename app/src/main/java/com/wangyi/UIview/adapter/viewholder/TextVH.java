package com.wangyi.UIview.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.wangyi.reader.R;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by eason on 5/25/16.
 */
public class TextVH extends UltimateRecyclerviewViewHolder {
    @ViewInject(R.id.tv_list_item)
    public TextView item;

    public TextVH(View itemView) {
        super(itemView);
        x.view().inject(this,itemView);
    }
}
