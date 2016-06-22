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
public class SearchBookVH extends UltimateRecyclerviewViewHolder {
    @ViewInject(R.id.title)
    public TextView bookName;
    @ViewInject(R.id.categroy)
    public TextView categroy;
    @ViewInject(R.id.uploader)
    public TextView uploader;
    @ViewInject(R.id.item_icontitle)
    public TextView bookNameInPic;
    @ViewInject(R.id.item_icon)
    public ImageView pic;
    @ViewInject(R.id.bottom_progress_bar)
    public ProgressBar progressBar;

    public SearchBookVH(View itemView) {
        super(itemView);
        x.view().inject(this,itemView);
    }
}
