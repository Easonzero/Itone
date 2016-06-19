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
import com.wangyi.UIview.fragment.HomeFragment;
import com.wangyi.function.*;

import org.xutils.common.Callback;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import com.wangyi.UIview.BaseActivity;
import com.wangyi.UIview.BaseFragment;
import com.wangyi.UIview.widget.container.FragmentIndicator;
import com.wangyi.UIview.widget.container.FragmentIndicator.OnIndicateListener;
import com.wangyi.reader.R;
import com.wangyi.utils.ItOneUtils;
import com.wangyi.utils.PreferencesReader;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

@ContentView(R.layout.activity_main)
public class ItOneActivity extends BaseActivity implements HomeFragment.OnMenuListener{
	private BaseFragment[] mFragments;
    private ProfileDrawerItem mProfileDrawerItem;
    private AccountHeader mAccountHeader;
	private Drawer drawer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ScheduleFunc.getInstance().init(this);
		BookManagerFunc.getInstance().init(this);
		MessageFunc.getInstance().init(this);
		HomeworkFunc.getInstance().init(this);
		UserManagerFunc.getInstance().init(this);
		HttpsFunc.getInstance().init(this);

		UserManagerFunc.getInstance().clear();

		setFragmentIndicator(0);

		initDrawer();

		autoLogin();
	}

	private void autoLogin(){
		String[] user = PreferencesReader.getUser();
		user[0] = "13115511080";
		user[1] = "qwe123";
		if(!user[0].equals("none")&&!UserManagerFunc.getInstance().isLogin()){
			HttpsFunc.getInstance().disconnect();
			HttpsFunc.getInstance().Login(user[0],user[1]);
		}
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

	private void initDrawer(){
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
						if(!UserManagerFunc.getInstance().isLogin())
							intent = new Intent(ItOneActivity.this.getApplicationContext(),LoginActivity.class);
						else
							intent = new Intent(ItOneActivity.this.getApplicationContext(),WatchUser.class);
						startActivity(intent);
						return false;
					}
				})
				.build();

		drawer = new DrawerBuilder()
				.withActivity(this)
				.withAccountHeader(mAccountHeader)
				.withStatusBarColorRes(R.color.basebar_color)
				.addDrawerItems(
						new DividerDrawerItem().withIdentifier(1),
						new PrimaryDrawerItem().withName("查看作业").withIdentifier(2),
						new PrimaryDrawerItem().withName("大神榜").withIdentifier(3),
						new PrimaryDrawerItem().withName("消息通知").withIdentifier(4)
				)
				.withOnDrawerListener(new Drawer.OnDrawerListener() {
					@Override
					public void onDrawerOpened(View view) {
						if(UserManagerFunc.getInstance().isLogin()){
							ImageOptions options=new ImageOptions.Builder()
									.setLoadingDrawableId(R.drawable.headpic)
									.setFailureDrawableId(R.drawable.headpic)
									.setUseMemCache(true)
									.setCircular(true)
									.setIgnoreGif(false)
									.build();
							x.image().loadDrawable(
									HttpsFunc.host +UserManagerFunc.getInstance().getUserInfo().picture+"headPic.jpg",
									options,
									new Callback.CommonCallback< Drawable >(){

										@Override
										public void onSuccess(Drawable result) {
											mAccountHeader.updateProfile(
													mProfileDrawerItem.withIcon(
															result
													).withName(
															UserManagerFunc.getInstance().getUserInfo().userName
													).withEmail(
															UserManagerFunc.getInstance().getUserInfo().faculty
													));
										}

										@Override
										public void onError(Throwable ex, boolean isOnCallback) {
										}

										@Override
										public void onCancelled(CancelledException cex) {
										}

										@Override
										public void onFinished() {
										}
									});
						}else{
							mAccountHeader.updateProfile(mProfileDrawerItem.withIcon(R.drawable.headpic));
							mAccountHeader.updateProfile(mProfileDrawerItem.withName("未登录"));
							mAccountHeader.updateProfile(mProfileDrawerItem.withEmail(""));
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
							case 2:
								if(!UserManagerFunc.getInstance().isLogin()){
									ItOneUtils.showToast(x.app(),"请先登陆");
									break;
								}
								intent = new Intent(ItOneActivity.this.getApplicationContext(),HomeWorkActivity.class);
								startActivity(intent);
								break;
							case 3:
								if(!UserManagerFunc.getInstance().isLogin()){
									ItOneUtils.showToast(x.app(),"请先登陆");
									break;
								}
								intent = new Intent(ItOneActivity.this.getApplicationContext(),GodlistActivity.class);
								startActivity(intent);
								break;
							case 4:
								if(!UserManagerFunc.getInstance().isLogin()){
									ItOneUtils.showToast(x.app(),"请先登陆");
									break;
								}
								intent = new Intent(ItOneActivity.this.getApplicationContext(),MessageActivity.class);
								startActivity(intent);
								break;
						}
						return false;
					}
				})
				.build();
	}

	@Override
	public void show() {
		if(drawer != null){
			drawer.openDrawer();
		}
	}

	@Override
	public void hide() {
		if(drawer != null){
			drawer.closeDrawer();
		}
	}
}
