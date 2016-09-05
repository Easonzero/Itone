package com.wangyi.UIview.adapter.viewholder;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.expanx.Util.ItemDataClickListener;
import com.marshalchen.ultimaterecyclerview.expanx.Util.parent;
import com.wangyi.UIview.adapter.template.ExpandDataItem;
import com.wangyi.define.bean.BookData;
import com.wangyi.reader.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by eason on 5/11/16.
 */
public class BookCategory extends parent<ExpandDataItem<BookData>> {
    @ViewInject(R.id.exp_section_icon)
    public ImageView icon;
    @ViewInject(R.id.exp_section_title)
    public TextView text;
    @ViewInject(R.id.exp_section_notification_number)
    private TextView count;
    @ViewInject(R.id.exp_indication_arrow)
    public ImageView expand;
    private boolean capitalized = false;
    private boolean countenabled = true;
    @ViewInject(R.id.exp_section_ripple_wrapper_click)
    public RelativeLayout relativeLayout;
    private ExpandDataItem<BookData> item;
    @ViewInject(R.id.exp_section_adjustment_layout)
    public RelativeLayout adjustmentlayout;

    public BookCategory(View itemView) {
        super(itemView);
        x.view().inject(this,itemView);
        itemMargin = itemView.getContext().getResources().getDimensionPixelSize(com.marshalchen.ultimaterecyclerview.R.dimen.item_margin);
    }

    protected ExpandDataItem<BookData> getItem() {
        return item;
    }

    @Override
    protected void setCountVisible(int visibility) {
        if (countenabled)
            count.setVisibility(visibility);
    }

    @Override
    protected void updateCountNumber(String text) {
        if (countenabled)
            count.setText(text);
    }

    @Override
    public void bindView(final ExpandDataItem<BookData> itemData, final int position, final ItemDataClickListener imageClickListener) {
        adjustmentlayout.setLayoutParams(getParamsLayout(adjustmentlayout, itemData));

        String title = itemData.getText().toString();

        if(title.equals("我的ppt")) icon.setImageResource(R.drawable.ic_sub_ppt);
        else if(title.equals("我的笔记")) icon.setImageResource(R.drawable.ic_sub_homework);
        else if(title.equals("我的课本")) icon.setImageResource(R.drawable.ic_sub_book);

        text.setText(title);

        setHandleInitiatedViewStatus(itemData, expand, count);
        setRelativeLayoutClickable(relativeLayout, itemData, imageClickListener, position);
        item = itemData;
    }


    @Override
    public void onParentItemClick(String path) {

    }

    @Override
    public int openDegree() {
        return 90;
    }

    @Override
    public int closeDegree() {
        return 0;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        expand.setRotation((Float) animation.getAnimatedValue());
    }
}
