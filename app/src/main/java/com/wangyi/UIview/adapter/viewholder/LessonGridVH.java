package com.wangyi.UIview.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.wangyi.reader.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by eason on 5/11/16.
 */
public class LessonGridVH extends UltimateRecyclerviewViewHolder {
    @ViewInject(R.id.lesson_name)
    public TextView lesson_name;
    @ViewInject(R.id.location)
    public TextView location;
    @ViewInject(R.id.class_from_to)
    public TextView class_from_to;

    public LessonGridVH(View itemView) {
        super(itemView);
        x.view().inject(this,itemView);
    }
}
