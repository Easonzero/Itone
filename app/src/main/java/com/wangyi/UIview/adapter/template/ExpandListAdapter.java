package com.wangyi.UIview.adapter.template;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.expanx.LinearExpanxURVAdapter;
import com.marshalchen.ultimaterecyclerview.expanx.SmartItem;
import com.marshalchen.ultimaterecyclerview.expanx.Util.child;
import com.marshalchen.ultimaterecyclerview.expanx.Util.parent;
import com.wangyi.UIview.adapter.viewholder.Category;
import com.wangyi.UIview.adapter.viewholder.SubCategory;

import java.util.List;

/**
 * Created by eason on 5/11/16.
 */
public class ExpandListAdapter extends LinearExpanxURVAdapter<ExpandDataItem, parent<ExpandDataItem>, child<ExpandDataItem>> {
    public ExpandListAdapter(Context context) {
        super(context, EXPANDABLE_ITEMS, true);
    }

    @Override
    protected Category iniCustomParentHolder(View parentview) {
        return new Category(parentview);
    }

    @Override
    protected SubCategory iniCustomChildHolder(View childview) {
        return new SubCategory(childview);
    }

    @Override
    protected int getLayoutResParent() {
        return 0;
    }

    @Override
    protected int getLayoutResChild() {
        return 0;
    }

    @Override
    protected List<ExpandDataItem> getChildrenByPath(String path, int depth, int position) {
        return null;
    }

    @Override
    public RecyclerView.ViewHolder newFooterHolder(View view) {
        return new UltimateRecyclerviewViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder newHeaderHolder(View view) {
        return new UltimateRecyclerviewViewHolder(view);
    }
}
