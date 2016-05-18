package com.wangyi.UIview.adapter;

import android.view.View;
import android.view.ViewGroup;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.quickAdapter.easyRegularAdapter;
import com.wangyi.UIview.adapter.viewholder.MessageListVH;
import com.wangyi.define.Message;
import com.wangyi.function.HttpsFunc;
import com.wangyi.reader.R;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by eason on 5/8/16.
 */
public class MessageListAdapter extends easyRegularAdapter<Message,MessageListVH> {
    public MessageListAdapter(List<Message> msg){
        super(msg);
    }

    @Override
    protected int getNormalLayoutResId() {
        return R.layout.message_item;
    }

    @Override
    protected MessageListVH newViewHolder(View view) {
        return new MessageListVH(view);
    }

    @Override
    protected void withBindHolder(MessageListVH holder, Message data, int position) {
        holder.name.setText(data.getUid());
        holder.content.setText(data.getDate() + "    " + data.getMessage() + "....");
        holder.topic.setText(data.getCategory() + (data.isvisited()?"*":""));
        ImageOptions options=new ImageOptions.Builder()
                .setLoadingDrawableId(R.drawable.ic_me)
                .setFailureDrawableId(R.drawable.ic_me)
                .setUseMemCache(true)
                .setCircular(true)
                .setIgnoreGif(false)
                .build();
        x.image().bind(
                holder.pic, HttpsFunc.host +
                        data.getPicUrl() +
                        "headPic.jpg",options
        );
    }
}
