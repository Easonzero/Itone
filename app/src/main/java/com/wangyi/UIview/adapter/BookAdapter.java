package com.wangyi.UIview.adapter;

import com.wangyi.UIview.adapter.viewholder.BookVH;
import com.wangyi.define.bean.BookData;
import com.wangyi.function.BookManagerFunc;
import com.wangyi.reader.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class BookAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private int mRightWidth = 0;
    private IOnItemRightClickListener mListener = null;

    public interface IOnItemRightClickListener {
        void onRightClick(View v, int position);
    }

    public BookAdapter(Context context,int rightWidth, IOnItemRightClickListener l){
        mRightWidth = rightWidth;
        mListener = l;
        inflater = LayoutInflater.from(context);
    }

    public void setRightClickListener(IOnItemRightClickListener l){
        mListener = l;
    }

    @Override
    public int getCount() {
        return BookManagerFunc.getInstance().getBooksNum();
    }

    @Override
    public BookData getItem(int position) {
        return BookManagerFunc.getInstance().getBookData(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub 
        BookVH holder = null;
        final int thisPosition = position;
        if (null == convertView){
            convertView = inflater.inflate(R.layout.browseitem,parent,false);
            holder = new BookVH(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (BookVH)convertView.getTag();
        }
        LinearLayout.LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        holder.item_left.setLayoutParams(lp1);
        LinearLayout.LayoutParams lp2 = new LayoutParams(mRightWidth, LayoutParams.MATCH_PARENT);
        holder.item_right.setLayoutParams(lp2);
        holder.item_right_text.setText("删除");
        holder.item_left_text.setText(BookManagerFunc.getInstance().getBookData(position).bookName);
        holder.text_icon.setText(BookManagerFunc.getInstance().getBookData(position).url);
        holder.item_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRightClick(v, thisPosition);
                }
            }
        });
        return convertView;
    }
}
