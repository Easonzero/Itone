package com.wangyi.UIview.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.wangyi.reader.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by eason on 5/11/16.
 */
public class MessageListVH extends UltimateRecyclerviewViewHolder {
    @ViewInject(R.id.pic)
    public ImageView pic;
    @ViewInject(R.id.name)
    public TextView name;
    @ViewInject(R.id.topic)
    public TextView topic;
    @ViewInject(R.id.content)
    public TextView content;
    @ViewInject(R.id.bottom_progress_bar)
    public ProgressBar progressBar;

    public MessageListVH(View itemView) {
        super(itemView);
        x.view().inject(this,itemView);
    }
}
