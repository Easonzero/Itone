package com.wangyi.UIview.adapter;

import android.view.View;

import com.marshalchen.ultimaterecyclerview.quickAdapter.easyRegularAdapter;
import com.wangyi.UIview.adapter.viewholder.TextVH;
import com.wangyi.reader.R;

import java.util.List;

/**
 * Created by eason on 5/25/16.
 */
public class TextAdapter extends easyRegularAdapter<String,TextVH>{
    public TextAdapter(List<String> texts){
        super(texts);
    }

    @Override
    protected int getNormalLayoutResId() {
        return R.layout.list_item_text;
    }

    @Override
    protected TextVH newViewHolder(View view) {
        return new TextVH(view);
    }

    @Override
    protected void withBindHolder(TextVH holder, String data, int position) {
        holder.item.setText(data);
    }
}
