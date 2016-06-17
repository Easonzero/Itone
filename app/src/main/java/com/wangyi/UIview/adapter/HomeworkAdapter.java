package com.wangyi.UIview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.expanx.LinearExpanxURVAdapter;
import com.wangyi.UIview.adapter.template.ExpandDataItem;
import com.wangyi.UIview.adapter.viewholder.HomeworkCategory;
import com.wangyi.UIview.adapter.viewholder.HomeworkSubCategory;
import com.wangyi.define.bean.Homework;
import com.wangyi.reader.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eason on 5/21/16.
 */
public class HomeworkAdapter extends LinearExpanxURVAdapter<ExpandDataItem<Homework>, HomeworkCategory, HomeworkSubCategory> {

    public HomeworkAdapter(Context context) {
        super(context);
    }

    @Override
    protected HomeworkCategory iniCustomParentHolder(View parentview) {
        return new HomeworkCategory(parentview);
    }

    @Override
    protected HomeworkSubCategory iniCustomChildHolder(View childview) {
        return new HomeworkSubCategory(childview);
    }

    @Override
    protected int getLayoutResParent() {
        return R.layout.list_item_text;
    }

    @Override
    protected int getLayoutResChild() {
        return R.layout.exp_wu_child;
    }

    @Override
    protected List<ExpandDataItem<Homework>> getChildrenByPath(String path, int depth, int position) {
        return null;
    }

    public static List<ExpandDataItem<Homework>> getPreCodeMenu(Homework[] Dataset) {
        List<ExpandDataItem<Homework>> e = new ArrayList<>();
        final List<ExpandDataItem<Homework>> compulsory = new ArrayList<>();
        final List<ExpandDataItem<Homework>> elective = new ArrayList<>();

        for(Homework data:Dataset){
            String message;
            if(data.message == null) message = "(暂未上传)";
            else message = "(" + data.getFDate() + ")    上传者：" + data.uname;
            if(1==data.is){
                compulsory.add(new ExpandDataItem(data.course,message,data));
            }else{
                elective.add(new ExpandDataItem(data.course,message,data));
            }
        }

        e.add(new ExpandDataItem("必修", null));
        e.addAll(compulsory);
        e.add(new ExpandDataItem("选修", null));
        e.addAll(elective);
        return e;
    }

    @Override
    public RecyclerView.ViewHolder newFooterHolder(View view) {
        return new UltimateRecyclerviewViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder newHeaderHolder(View view) {
        return new UltimateRecyclerviewViewHolder(view);
    }

    public void removeAll(){
        this.removeAllInternal(getSet());
    }
}
