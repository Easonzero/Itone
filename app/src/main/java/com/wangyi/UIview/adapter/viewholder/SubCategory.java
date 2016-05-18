package com.wangyi.UIview.adapter.viewholder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.marshalchen.ultimaterecyclerview.expanx.Util.child;
import com.wangyi.UIview.activity.WatchBook;
import com.wangyi.UIview.adapter.template.ExpandDataItem;
import com.wangyi.define.BookData;
import com.wangyi.reader.R;
import com.wangyi.utils.ItOneUtils;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by eason on 5/11/16.
 */
public class SubCategory extends child<ExpandDataItem> {
    @ViewInject(R.id.exp_section_title)
    public TextView title;
    @ViewInject(R.id.exp_section_message)
    public TextView message;
    @ViewInject(R.id.exp_section_ripple_wrapper_click)
    public RelativeLayout relativeLayout;

    public SubCategory(View itemView) {
        super(itemView);
        x.view().inject(this,itemView);
        itemMargin = itemView.getContext().getResources()
                .getDimensionPixelSize(com.marshalchen.ultimaterecyclerview.R.dimen.item_margin);
        offsetMargin = itemView.getContext().getResources()
                .getDimensionPixelSize(com.marshalchen.ultimaterecyclerview.R.dimen.expand_size);
    }

    @Override
    public void bindView(final ExpandDataItem itemData, int position) {
        String[] data = ItOneUtils.parseMessage(itemData.getText());
        title.setText(data[0]);
        message.setText(data[1]);
        title.setLayoutParams(getParamsLayoutOffset(title, itemData));
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookData bookData = itemData.getBookData();
                Activity activity = (Activity) getContext();
                Intent intent = new Intent(activity, WatchBook.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bundle = new Bundle();
                bundle.putSerializable("book", bookData);
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public void onChildItemClick(String title, String path) {}
}