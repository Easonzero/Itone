package com.wangyi.UIview.adapter.template;

import com.marshalchen.ultimaterecyclerview.expanx.ExpandableItemData;
import com.marshalchen.ultimaterecyclerview.expanx.LinearExpanxURVAdapter;
import com.wangyi.utils.ItOneUtils;

import java.util.List;
import java.util.UUID;

/**
 * Created by eason on 5/23/16.
 */
public class ExpandDataItem<T> extends ExpandableItemData {
    private T Dataset = null;

    public ExpandDataItem(String title, List<T> children) {
        super(LinearExpanxURVAdapter.ExpandableViewTypes.ITEM_TYPE_PARENT, title, "", UUID.randomUUID().toString(), 0, children);
    }

    public ExpandDataItem(String title,String message,T data) {
        super(LinearExpanxURVAdapter.ExpandableViewTypes.ITEM_TYPE_CHILD, ItOneUtils.generateMessage(title,message), "", UUID.randomUUID().toString(), 1, null);
        this.Dataset = data;
    }

    public T getDataset(){
        return  Dataset;
    }
}