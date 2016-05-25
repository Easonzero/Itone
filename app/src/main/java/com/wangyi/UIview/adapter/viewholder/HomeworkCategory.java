package com.wangyi.UIview.adapter.viewholder;
import android.animation.ValueAnimator;
import android.view.View;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.expanx.Util.ItemDataClickListener;
import com.marshalchen.ultimaterecyclerview.expanx.Util.parent;
import com.wangyi.UIview.adapter.template.ExpandDataItem;
import com.wangyi.define.Homework;
import com.wangyi.reader.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by eason on 5/23/16.
 */
public class HomeworkCategory extends parent<ExpandDataItem<Homework>> {
    @ViewInject(R.id.tv_list_item)
    TextView title;
    public HomeworkCategory(View itemView) {
        super(itemView);
        x.view().inject(this,itemView);
    }

    @Override
    protected void setCountVisible(int visibility) {

    }

    @Override
    protected void updateCountNumber(String text) {

    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {

    }

    @Override
    public void bindView(ExpandDataItem itemData, int position, ItemDataClickListener imageClickListener) {
        title.setText(itemData.getText());
    }

    @Override
    public void onParentItemClick(String path) {

    }

    @Override
    public int openDegree() {
        return 0;
    }

    @Override
    public int closeDegree() {
        return 0;
    }
}
