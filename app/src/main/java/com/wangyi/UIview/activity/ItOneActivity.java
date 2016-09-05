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
import com.wangyi.define.EventName;
import com.wangyi.define.SettingName;
import com.wangyi.function.*;
import org.xutils.common.Callback;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.Event;
import org.xutils.x;
import com.wangyi.UIview.BaseFragment;
import com.wangyi.UIview.widget.container.FragmentIndicator;
import com.wangyi.UIview.widget.container.FragmentIndicator.OnIndicateListener;
import com.wangyi.reader.R;
import com.wangyi.service.UpdateService;
import com.wangyi.utils.ItOneUtils;
import com.wangyi.utils.PreferencesReader;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import me.drakeet.materialdialog.MaterialDialog;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@SuppressLint("NeedOnRequestPermissionsResult")
@RuntimePermissions
public class ItOneActivity extends AppCompatActivity implements HomeFragment.OnMenuListener{
	private BaseFragment[] mFragments;
    private ProfileDrawerItem mProfileDrawerItem;
    private AccountHeader mAccountHeader;
	private Drawer drawer;

	private Handler handler = new Handler(){
		@Override
		public void handleMessage(final Message msg){
			switch(msg.what){
				case EventName.UI.SUCCESS:
					final String url = (String)msg.obj;
					if(UserManagerFunc.getInstance().getSetting(SettingName.AUTOUPDATE)){
						Intent intent = new Intent(ItOneActivity.this, UpdateService.class);
						intent.putExtra("url",url);
						startService(intent);
					}else{
						final MaterialDialog alert = new MaterialDialog(ItOneActivity.this);
						alert.setTitle("学派更新")
								.setMessage("发现新的版本，是否更新？")
								.setPositiveButton(getString(R.string.yes),new View.OnClickListener() {
									public void onClick(View view) {
										Intent intent = new Intent(ItOneActivity.this, UpdateService.class);
										intent.putExtra("url",url);
										startService(intent);
										alert.dismiss();
									}
								})
								.setNegativeButton(getString(R.string.no),new View.OnClickListener() {
									public void onClick(View view) {
										alert.dismiss();
									}
								});
						alert.show();
					}
					break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);//xuils3莫名奇妙的bug，第一个activity没法注入

		ItOneActivityPermissionsDispatcher.onGrantedWithCheck(this);

		ScheduleFunc.getInstance().init(this);
		BookManagerFunc.getInstance().init(this);
		MessageFunc.getInstance().init(this);
		HomeworkFunc.getInstance().init(this);
		UserManagerFunc.getInstance().init(this);
		HttpsFunc.getInstance().init(this);

		UserManagerFunc.getInstance().clear();

		setFragmentIndicator(0);

		initDrawer();

		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				autoUpdate();
			}
		},1000);

		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				autoLogin();
			}
		},2000);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		// NOTE: delegate the permission handling to generated method
		ItOneActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
	}

	private void autoLogin(){
		String[] user = PreferencesReader.getUser();
		if(!user[0].equals("none")&&!UserManagerFunc.getInstance().isLogin()){
			HttpsFunc.getInstance().disconnect();
			HttpsFunc.getInstance().Login(user[0],user[1]);
		}
	}

	private void autoUpdate(){
		HttpsFunc.getInstance().connect(handler).update(PreferencesReader.getVersionCode(this)+"");
	}

	@NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
	public void onGranted(){
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

	@OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
	void onDenied() {
		finish();
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
						new PrimaryDrawerItem().withName("消息通知").withIdentifier(4),
						new PrimaryDrawerItem().withName("我是班长").withIdentifier(5)
				)
				.withOnDrawerListener(new Drawer.OnDrawerListener() {
					@Override
					public void onDrawerOpened(View view) {
						if(UserManagerFunc.getInstance().isLogin()){
							mAccountHeader.updateProfile(mProfileDrawerItem.withName(
									UserManagerFunc.getInstance().getUserInfo().userName
							).withEmail(
									UserManagerFunc.getInstance().getUserInfo().faculty
							));
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
													mProfileDrawerItem.withIcon(result));
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
							case 5:
								if(!UserManagerFunc.getInstance().isLogin()){
									ItOneUtils.showToast(x.app(),"请先登陆");
									break;
								}
								if(UserManagerFunc.getInstance().getUserPlus().isMonitor.equals("FALSE")){
									ItOneUtils.showToast(x.app(),"快叫你的班长来");
									break;
								}
								intent = new Intent(ItOneActivity.this.getApplicationContext(),PublishActivity.class);
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
