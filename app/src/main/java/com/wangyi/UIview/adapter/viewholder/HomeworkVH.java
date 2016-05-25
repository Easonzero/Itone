package com.wangyi.UIview.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.wangyi.reader.R;

import org.xutils.view.annotation.ViewInject;

/**
 * Created by eason on 5/21/16.
 */
public class HomeworkVH extends UltimateRecyclerviewViewHolder {
    @ViewInject(R.id.course)
    public TextView course;
    @ViewInject(R.id.date)
    public TextView date;
    @ViewInject(R.id.uploader)
    public TextView uploader;
    public HomeworkVH(View itemView) {
        super(itemView);
    }
}
