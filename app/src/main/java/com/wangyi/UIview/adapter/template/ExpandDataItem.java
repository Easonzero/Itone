package com.wangyi.UIview.adapter.template;

import android.content.Context;
import android.support.annotation.StringRes;

import com.marshalchen.ultimaterecyclerview.expanx.ExpandableItemData;
import com.marshalchen.ultimaterecyclerview.expanx.LinearExpanxURVAdapter;
import com.wangyi.define.BookData;
import com.wangyi.utils.ItOneUtils;

import java.util.List;
import java.util.UUID;

/**
 * Created by eason on 5/17/16.
 */
public class ExpandDataItem extends ExpandableItemData {
    private BookData bookData = null;

    public ExpandDataItem(BookData book, int depth) {
        super(LinearExpanxURVAdapter.ExpandableViewTypes.ITEM_TYPE_CHILD, ItOneUtils.generateMessage(book.bookName,"下载量："+book.downloadNumber), "", UUID.randomUUID().toString(), depth,null);
        this.bookData = book;
    }

    public ExpandDataItem(String title, int depth, List<ExpandDataItem> children) {
        super(LinearExpanxURVAdapter.ExpandableViewTypes.ITEM_TYPE_PARENT, title, "", UUID.randomUUID().toString(), depth, children);
    }

    public static ExpandDataItem parent(final String text, final List<ExpandDataItem> carrying_list) {
        return new ExpandDataItem(text, 0, carrying_list);
    }

    public static ExpandDataItem child(final BookData bookData) {
        return new ExpandDataItem(bookData, 1);
    }

    public BookData getBookData(){
        return  bookData;
    }
}
