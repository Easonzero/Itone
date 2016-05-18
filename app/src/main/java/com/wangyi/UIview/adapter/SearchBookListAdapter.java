package com.wangyi.UIview.adapter;

import com.marshalchen.ultimaterecyclerview.quickAdapter.easyRegularAdapter;
import com.wangyi.UIview.adapter.viewholder.SearchBookListVH;
import com.wangyi.define.BookData;
import com.wangyi.function.BookManagerFunc;
import com.wangyi.reader.R;
import android.view.View;

import java.util.List;

public class SearchBookListAdapter extends easyRegularAdapter<BookData,SearchBookListVH> {
    public SearchBookListAdapter(List<BookData> books){
        super(books);
    }

    @Override
    protected int getNormalLayoutResId() {
        return R.layout.book_list_item;
    }

    @Override
    protected SearchBookListVH newViewHolder(View view) {
        return new SearchBookListVH(view);
    }

    @Override
    protected void withBindHolder(SearchBookListVH holder, BookData data, int position) {
        holder.bookName.setText(data.bookName);
        holder.bookNameInPic.setText(data.bookName);
        holder.bookSubject.setText("科目:"+data.subject);
        holder.downloadNUM.setText("下载量:"+data.downloadNumber);
    }
}
