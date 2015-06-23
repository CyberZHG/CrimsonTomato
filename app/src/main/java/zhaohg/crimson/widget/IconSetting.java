package zhaohg.crimson.widget;

import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;

import zhaohg.crimson.R;
import zhaohg.crimson.main.SettingActivity;

public class IconSetting extends DrawableWidget {

    public IconSetting(Context context, View view) {
        super(context, view);
        this.setDrawable(R.drawable.icon_settings);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Intent intent = new Intent();
            intent.setClass(context, SettingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        return true;
    }

}
