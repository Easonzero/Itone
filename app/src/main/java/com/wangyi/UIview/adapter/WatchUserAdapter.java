package com.wangyi.UIview.adapter;

import android.content.Context;
import com.wangyi.UIview.adapter.template.ExpandDataItem;
import com.wangyi.UIview.adapter.template.ExpandListAdapter;
import com.wangyi.define.BookData;
import com.wangyi.reader.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eason on 5/11/16.
 */
public class WatchUserAdapter extends ExpandListAdapter{

    public WatchUserAdapter(Context context) {
        super(context);
    }

    public static List<ExpandDataItem> getPreCodeMenu(BookData[] bookData) {
        List<ExpandDataItem> e = new ArrayList<>();
        final List<ExpandDataItem> ppt = new ArrayList<>();
        final List<ExpandDataItem> note = new ArrayList<>();
        final List<ExpandDataItem> review = new ArrayList<>();

        for(BookData book:bookData){
            if(book.category.equals("ppt")){
                ppt.add(ExpandDataItem.child(book));
            }else if(book.category.equals("note")){
                note.add(ExpandDataItem.child(book));
            }else if(book.category.equals("review")){
                review.add(ExpandDataItem.child(book));
            }
        }

        e.add(ExpandDataItem.parent("我的ppt", ppt));
        e.add(ExpandDataItem.parent("我的笔记", note));
        e.add(ExpandDataItem.parent("我的复习资料", review));
        return e;
    }

    @Override
    protected int getLayoutResParent() {
        return R.layout.exp_wu_father;
    }

    @Override
    protected int getLayoutResChild() {
        return R.layout.exp_wu_child;
    }
}
