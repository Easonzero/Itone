package com.wangyi.UIview.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.wangyi.UIview.widget.ScrollingTextView;
import com.wangyi.reader.R;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by eason on 5/11/16.
 */
public class BookVH extends UltimateRecyclerviewViewHolder {
    @ViewInject(R.id.item_title)
    public TextView item_left_text;
    @ViewInject(R.id.item_right_txt)
    public TextView item_right_text;
    @ViewInject(R.id.item_icontitle)
    public ScrollingTextView text_icon;
    @ViewInject(R.id.moreoverflowMenu)
    public ImageView button;
    @ViewInject(R.id.item_left)
    public View item_left;
    @ViewInject(R.id.item_right)
    public View item_right;
    public BookVH(View itemView) {
        super(itemView);
        x.view().inject(this,itemView);
    }
}
