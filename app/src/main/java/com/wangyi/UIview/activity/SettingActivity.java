package com.wangyi.UIview.activity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import com.wangyi.UIview.BaseActivity;
import com.wangyi.define.SettingName;
import com.wangyi.function.UserManagerFunc;
import com.wangyi.reader.R;
import com.wangyi.utils.PreferencesReader;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by maxchanglove on 2016/2/29.
 */
@ContentView(R.layout.set_me)
public class SettingActivity extends BaseActivity {
    @ViewInject(R.id.item1)
    SwitchCompat autoupdate;
    @ViewInject(R.id.item2)
    SwitchCompat nowifidownload;
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        autoupdate.setChecked(UserManagerFunc.getInstance().getSetting(SettingName.AUTOUPDATE));
        nowifidownload.setChecked(UserManagerFunc.getInstance().getSetting(SettingName.NOWIFIDOWNLOAD));
    }

    @Event(value=R.id.item1,type=CompoundButton.OnCheckedChangeListener.class)
    private void onAutoupdateClick(CompoundButton compoundButton, boolean b){
        UserManagerFunc.getInstance().setSetting(SettingName.AUTOUPDATE,b);
    }

    @Event(value=R.id.item2,type=CompoundButton.OnCheckedChangeListener.class)
    private void onWifidownloadClick(CompoundButton compoundButton, boolean b){
        UserManagerFunc.getInstance().setSetting(SettingName.NOWIFIDOWNLOAD,b);
    }

    @Event(R.id.back)
    private void onBackClick(View view){
        SettingActivity.this.finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        UserManagerFunc.getInstance().ifNeedSave();
    }
}
