package com.wangyi.UIview.adapter;

import android.support.v7.widget.RecyclerView;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import org.xutils.x;
import org.xutils.view.annotation.ViewInject;
import com.wangyi.function.BookManagerFunc;
import com.wangyi.reader.R;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SearchBookListAdapter extends UltimateViewAdapter<SearchBookListAdapter.ViewHolder> {
    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

    }

    class ViewHolder extends UltimateRecyclerviewViewHolder {
		@ViewInject(R.id.title)
		public TextView bookName;
		@ViewInject(R.id.subject)
        public TextView bookSubject;
		@ViewInject(R.id.downloadNUM)
        public TextView downloadNUM;
		@ViewInject(R.id.item_icontitle)
        public TextView bookNameInPic;

        public ViewHolder(View itemView) {
            super(itemView);
            x.view().inject(this,itemView);
        }
    }

    @Override
    public ViewHolder getViewHolder(View view) {
        return null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        ViewHolder holder = new ViewHolder(View.inflate(parent.getContext(), R.layout.book_list_item, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bookName.setText(BookManagerFunc.getInstance().getBookData(position).bookName);
        holder.bookNameInPic.setText(BookManagerFunc.getInstance().getBookData(position).bookName);
        holder.bookSubject.setText("科目:"+BookManagerFunc.getInstance().getBookData(position).subject);
        holder.downloadNUM.setText("下载量:"+BookManagerFunc.getInstance().getBookData(position).downloadNumber);
    }

    @Override
    public int getAdapterItemCount() {
        return BookManagerFunc.getInstance().getBooksNum();
    }

    @Override
    public long generateHeaderId(int i) {
        return 0;
    }
}
