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
public class SearchBookListVH extends UltimateRecyclerviewViewHolder {
    @ViewInject(R.id.title)
    public TextView bookName;
    @ViewInject(R.id.subject)
    public TextView bookSubject;
    @ViewInject(R.id.downloadNUM)
    public TextView downloadNUM;
    @ViewInject(R.id.item_icontitle)
    public TextView bookNameInPic;

    public SearchBookListVH(View itemView) {
        super(itemView);
        x.view().inject(this,itemView);
    }
}
