package com.wangyi.UIview.adapter;

import org.xutils.x;
import org.xutils.view.annotation.ViewInject;

import com.wangyi.UIview.widget.ScrollingTextView;
import com.wangyi.define.BookData;
import com.wangyi.function.BookManagerFunc;
import com.wangyi.reader.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
        ViewHolder holder = null;
        final int thisPosition = position;
        if (null == convertView){
        	convertView = inflater.inflate(R.layout.browseitem,parent,false);
        	holder = new ViewHolder();
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        }
        else {
        	holder = (ViewHolder)convertView.getTag();
        }
        LinearLayout.LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT,
        		LayoutParams.MATCH_PARENT);
        holder.item_left.setLayoutParams(lp1);
        LinearLayout.LayoutParams lp2 = new LayoutParams(mRightWidth, LayoutParams.MATCH_PARENT);
        holder.item_right.setLayoutParams(lp2);
        holder.item_right_text.setText("É¾³ý");
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
	
	private class ViewHolder{
		@ViewInject(R.id.item_title)
        private TextView item_left_text;
		@ViewInject(R.id.item_right_txt)
        private TextView item_right_text;
		@ViewInject(R.id.item_icontitle)
        private ScrollingTextView text_icon;
		@ViewInject(R.id.moreoverflowMenu)
        private ImageView button;
		@ViewInject(R.id.item_left)
        private View item_left;
		@ViewInject(R.id.item_right)
        private View item_right;
    }	
}
