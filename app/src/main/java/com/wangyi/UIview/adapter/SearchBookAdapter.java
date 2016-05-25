package com.wangyi.UIview.adapter;

import com.marshalchen.ultimaterecyclerview.quickAdapter.easyRegularAdapter;
import com.wangyi.UIview.adapter.viewholder.SearchBookVH;
import com.wangyi.define.BookData;
import com.wangyi.reader.R;
import android.view.View;

import java.util.List;

public class SearchBookAdapter extends easyRegularAdapter<BookData,SearchBookVH> {
    public SearchBookAdapter(List<BookData> books){
        super(books);
    }

    @Override
    protected int getNormalLayoutResId() {
        return R.layout.book_list_item;
    }

    @Override
    protected SearchBookVH newViewHolder(View view) {
        return new SearchBookVH(view);
    }

    @Override
    protected void withBindHolder(SearchBookVH holder, BookData data, int position) {
        holder.bookName.setText(data.bookName);
        holder.bookNameInPic.setText(data.bookName);
        holder.categroy.setText("分类:"+data.occupation+","+data.subject+","+data.category);
        holder.uploader.setText("上传人:"+data.uploader);
    }
}
