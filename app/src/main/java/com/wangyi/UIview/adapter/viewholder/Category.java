package com.wangyi.UIview.adapter.viewholder;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.marshalchen.ultimaterecyclerview.expanx.Util.easyTemplateParent;
import com.wangyi.UIview.adapter.template.ExpandDataItem;

/**
 * Created by eason on 5/11/16.
 */
public class Category extends easyTemplateParent<ExpandDataItem, RelativeLayout, TextView> {
    public Category(View itemView) {
        super(itemView);
    }
}
