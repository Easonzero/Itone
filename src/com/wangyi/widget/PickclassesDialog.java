package com.wangyi.widget;

import com.wangyi.reader.R;

import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PickclassesDialog extends AlertDialog {
	private View view;
	public int date,fromClass,toClass;
	public PickclassesDialog(Context context,String weekday,String classfrom) {
		super(context);
		// TODO Auto-generated constructor stub
		LayoutInflater factory = LayoutInflater.from(context);
		view = factory.inflate(R.layout.pick_classes_dialog, null);
		
		final WheelView date = (WheelView) view.findViewById(R.id.date);
		date.setWheelBackground(R.drawable.wheel_bg_holo);
		date.setWheelForeground(R.drawable.wheel_val_holo);
		date.setShadowColor(0x00ffffff, 0x00ffffff, 0x00ffffff);
		date.setViewAdapter(new DateAdapter(context,0));
		date.setCurrentItem(3);
		date.setVisibleItems(7);
		final WheelView fromClass = (WheelView) view.findViewById(R.id.from_class);
		fromClass.setWheelBackground(R.drawable.wheel_bg_holo);
		fromClass.setWheelForeground(R.drawable.wheel_val_holo);
		fromClass.setShadowColor(0x00ffffff, 0x00ffffff, 0x00ffffff);
		fromClass.setViewAdapter(new DateAdapter(context,1));
		fromClass.setCurrentItem(3);
		fromClass.setVisibleItems(12);
		final WheelView toClass = (WheelView) view.findViewById(R.id.to_class);
		toClass.setWheelBackground(R.drawable.wheel_bg_holo);
		toClass.setWheelForeground(R.drawable.wheel_val_holo);
		toClass.setShadowColor(0x00ffffff, 0x00ffffff, 0x00ffffff);
		toClass.setViewAdapter(new DateAdapter(context,2));
		toClass.setCurrentItem(3);
		toClass.setVisibleItems(12);
		
		date.setCurrentItem(Integer.parseInt(weekday) - 1);
		fromClass.setCurrentItem(Integer.parseInt(classfrom) - 1);
		toClass.setCurrentItem(Integer.parseInt(classfrom) - 1);
		
		this.date = date.getCurrentItem();
		this.fromClass = fromClass.getCurrentItem();
		this.toClass = toClass.getCurrentItem();
		
		fromClass.addScrollingListener(new OnWheelScrollListener(){

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				PickclassesDialog.this.fromClass = wheel.getCurrentItem();
				if(PickclassesDialog.this.fromClass > PickclassesDialog.this.toClass){
					toClass.setCurrentItem(PickclassesDialog.this.fromClass);
				}
			}
			
		});
		
		toClass.addScrollingListener(new OnWheelScrollListener(){

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				if(PickclassesDialog.this.fromClass > wheel.getCurrentItem()){
					toClass.setCurrentItem(PickclassesDialog.this.fromClass);
				}
				PickclassesDialog.this.toClass = wheel.getCurrentItem();
			}
			
		});
		
		date.addScrollingListener(new OnWheelScrollListener(){

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				PickclassesDialog.this.date = wheel.getCurrentItem();
			}
			
		});
		
		this.setView(view);
	}
	
	private class DateAdapter extends AbstractWheelTextAdapter {
		String[] weekdays;

		protected DateAdapter(Context context,int which) {
			super(context, R.layout.classes_item, NO_RESOURCE);
			if(which == 0){
				weekdays = new String[] {"周一", "周二", "周三", "周四", "周五","周六","周日"};
			}
			else if(which == 1){
				weekdays = new String[] {"第1节", "第2节", "第3节", "第4节", "第5节","第6节","第7节","第8节", "第9节", "第10节", "第11节", "第12节"};
			}
			else if(which == 2){
				weekdays = new String[] {"到1节", "到2节", "到3节", "到4节", "到5节","到6节","到7节","到8节", "到9节", "到10节", "到11节", "到12节"};
			}
			setItemTextResource(R.id.class_item);
		}
		
		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return weekdays.length;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return weekdays[index];
		}
	}

}
