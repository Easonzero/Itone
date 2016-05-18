package com.wangyi.UIview.activity;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.wangyi.define.EventName;
import com.wangyi.define.SettingName;
import com.wangyi.function.*;
import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import com.wangyi.UIview.BaseActivity;
import com.wangyi.UIview.BaseFragment;
import com.wangyi.UIview.widget.FragmentIndicator;
import com.wangyi.UIview.widget.FragmentIndicator.OnIndicateListener;
import com.wangyi.reader.R;
import com.wangyi.utils.ItOneUtils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

@ContentView(R.layout.activity_main)
public class ItOneActivity extends BaseActivity{
	private BaseFragment[] mFragments;
    private ProfileDrawerItem mProfileDrawerItem;
    private AccountHeader mAccountHeader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ScheduleFunc.getInstance().init(this);
		BookManagerFunc.getInstance().init(this);
		MessageFunc.getInstance().init(this);
		UserManagerFunc.getInstance().init(this);
		HttpsFunc.getInstance().init(this);
		SensorFunc.getInstance().init(this);

		setFragmentIndicator(0);

        mProfileDrawerItem = new ProfileDrawerItem().withIdentifier(0);
        mAccountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.basebar_color)
                .addProfiles(
                        mProfileDrawerItem
                )
                .withOnAccountHeaderSelectionViewClickListener(new AccountHeader.OnAccountHeaderSelectionViewClickListener() {
                    @Override
                    public boolean onClick(View view, IProfile profile) {
                        return true;
                    }
                })
                .withAlternativeProfileHeaderSwitching(false)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        Intent intent;
                        if(UserManagerFunc.getInstance().isLogin())
                            intent = new Intent(ItOneActivity.this.getApplicationContext(),LoginActivity.class);
                        else
                            intent = new Intent(ItOneActivity.this.getApplicationContext(),WatchUser.class);
                        startActivity(intent);
                        return false;
                    }
                })
                .build();

		Drawer drawer = new DrawerBuilder()
				.withActivity(this)
                .withAccountHeader(mAccountHeader)
				.withStatusBarColorRes(R.color.basebar_color)
				.addDrawerItems(
                        new PrimaryDrawerItem().withName("查看作业").withIdentifier(1),
                        new PrimaryDrawerItem().withName("大神榜").withIdentifier(2),
                        new PrimaryDrawerItem().withName("消息通知").withIdentifier(3)
				)
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View view) {
                        if(UserManagerFunc.getInstance().isLogin()){
                            mAccountHeader.updateProfile(mProfileDrawerItem.withName(
                                    UserManagerFunc.getInstance().getUserInfo().userName + "\n" +
                                    UserManagerFunc.getInstance().getUserInfo().faculty + "\n"
                            ));
                        }else{
                            mAccountHeader.updateProfile(mProfileDrawerItem.withName("未登录"));
                        }
                    }

                    @Override
                    public void onDrawerClosed(View view) {}

                    @Override
                    public void onDrawerSlide(View view, float v) {}
                })
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {

                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
						Intent intent;
						switch (position){
							case 1:
								if(UserManagerFunc.getInstance().isLogin()){
									ItOneUtils.showToast(x.app(),"请先登陆");
									break;
								}
								intent = new Intent(ItOneActivity.this.getApplicationContext(),MessageActivity.class);
								intent.putExtra("category", EventName.Message.HOMEWORK);
								startActivity(intent);
								break;
							case 2:
								if(UserManagerFunc.getInstance().isLogin()){
									ItOneUtils.showToast(x.app(),"请先登陆");
									break;
								}
								intent = new Intent(ItOneActivity.this.getApplicationContext(),GodlistActivity.class);
								startActivity(intent);
								break;
							case 3:
								if(UserManagerFunc.getInstance().isLogin()){
									ItOneUtils.showToast(x.app(),"请先登陆");
									break;
								}
								intent = new Intent(ItOneActivity.this.getApplicationContext(),MessageActivity.class);
								intent.putExtra("category", EventName.Message.MESSAGE);
								startActivity(intent);
								break;
						}
                        return false;
                    }
                })
				.build();
	}

	private void setFragmentIndicator(int whichIsDefault) {
		mFragments = new BaseFragment[3];
		mFragments[0] = (BaseFragment)getSupportFragmentManager().findFragmentById(R.id.home);
		mFragments[1] = (BaseFragment)getSupportFragmentManager().findFragmentById(R.id.allsubject);
		mFragments[2] = (BaseFragment)getSupportFragmentManager().findFragmentById(R.id.me);

		getSupportFragmentManager().beginTransaction().hide(mFragments[0])
				.hide(mFragments[1]).hide(mFragments[2])
				.show(mFragments[whichIsDefault]).commit();

		FragmentIndicator mIndicator = (FragmentIndicator) findViewById(R.id.indicator);
		FragmentIndicator.setIndicator(whichIsDefault);
		mIndicator.setOnIndicateListener(new OnIndicateListener() {

			@Override
			public void onIndicate(View v, int which) {
				getSupportFragmentManager().beginTransaction()
						.hide(mFragments[0]).hide(mFragments[1])
						.hide(mFragments[2]).show(mFragments[which]).commit();
			}
		});
	}
}
