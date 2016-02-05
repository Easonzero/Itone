package com.wangyi.adapter;

import java.util.ArrayList;
import com.wangyi.widget.ScrollingTextView;
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
    private ArrayList<String> names = null;
    private ArrayList<String> paths = null;
    private Context context = null;
    private int mRightWidth = 0;
    private IOnItemRightClickListener mListener = null;

    public interface IOnItemRightClickListener {
        void onRightClick(View v, int position);
    }

    public BookAdapter(Context context,ArrayList<String> na,ArrayList<String> pa,int rightWidth, IOnItemRightClickListener l){
        names = na;
        paths = pa;
        this.context = context;
        mRightWidth = rightWidth;
        mListener = l;
        inflater = LayoutInflater.from(context);
    }
	@Override
    public int getCount() {
        // TODO Auto-generated method stub
        return names.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return names.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
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
            holder.item_left = (View)convertView.findViewById(R.id.item_left);
            holder.item_right = (View)convertView.findViewById(R.id.item_right);
            holder.item_right_text = (TextView)convertView.findViewById(R.id.item_right_txt);
            holder.item_left_text = (TextView)convertView.findViewById(R.id.item_title);
            holder.text_icon = (ScrollingTextView)convertView.findViewById(R.id.item_icontitle);
            holder.button = (ImageView) convertView.findViewById(R.id.moreoverflowMenu);
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
        holder.item_left_text.setText(names.get(position));
        holder.text_icon.setText(names.get(position));
        holder.item_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRightClick(v, thisPosition);
                }
            }
        });
        holder.button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
        	
        });
        return convertView;
	}
	
	private class ViewHolder{
        private TextView item_left_text;
        private TextView item_right_text;
        private ScrollingTextView text_icon;
        private ImageView button;
        private View item_left;
        private View item_right;
    }	
}
