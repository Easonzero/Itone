package com.wangyi.UIview.adapter;

import com.marshalchen.ultimaterecyclerview.quickAdapter.easyRegularAdapter;
import com.wangyi.UIview.adapter.viewholder.SearchBookVH;
import com.wangyi.define.bean.BookData;
import com.wangyi.function.HttpsFunc;
import com.wangyi.reader.R;
import android.view.View;

import org.xutils.image.ImageOptions;
import org.xutils.x;

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
        if(data.pic != null){
            ImageOptions options=new ImageOptions.Builder()
                    .setUseMemCache(true)
                    .setIgnoreGif(true)
                    .build();
            x.image().bind(
                    holder.pic, HttpsFunc.host +
                            data.pic,options
            );
        }
        else holder.bookNameInPic.setText(data.bookName);
        holder.categroy.setText("分类:"+(data.occupation.equals("*")?"通用":data.occupation)+","
                + (data.subject.equals("*")?"通用":data.subject)+","+data.category);
        holder.uploader.setText("上传人:"+data.uploader);
    }

    @Override
    public SearchBookVH newFooterHolder(View view) {
        return new SearchBookVH(view);
    }

    public BookData getItemData(int pos){
        return getItem(pos);
    }
}
