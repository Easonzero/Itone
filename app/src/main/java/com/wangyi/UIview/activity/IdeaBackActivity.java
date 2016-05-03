package com.wangyi.UIview.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import com.wangyi.UIview.BaseActivity;
import com.wangyi.UIview.widget.LoadingDialog;
import com.wangyi.define.EventName;
import com.wangyi.function.HttpsFunc;
import com.wangyi.reader.R;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by maxchanglove on 2016/2/29.
 */
@ContentView(R.layout.ideaback_me)
public class IdeaBackActivity extends BaseActivity {
    @ViewInject(R.id.editText)
    EditText editText;
    private LoadingDialog loading;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case EventName.UI.FINISH:
                    loading.dismiss();
                    break;
                case EventName.UI.START:
                    loading.show();
                    break;
                case EventName.UI.SUCCESS:
                    IdeaBackActivity.this.finish();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loading = new LoadingDialog(this);
    }

    @Event(R.id.commit)
    private void onCommitClick(View view) {
        String ideas = editText.getText().toString();
        HttpsFunc.getInstance().commitIdea(ideas);
    }

    @Event(R.id.back)
    private void onBackClick(View view){
        IdeaBackActivity.this.finish();
    }
}
