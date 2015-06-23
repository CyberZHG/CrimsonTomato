package zhaohg.crimson.widget;

import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;

import zhaohg.crimson.R;
import zhaohg.crimson.main.HistoryActivity;

public class IconHistory extends DrawableWidget {

    public IconHistory(Context context, View view) {
        super(context, view);
        this.setDrawable(R.drawable.icon_history);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Intent intent = new Intent();
            intent.setClass(context, HistoryActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        return true;
    }

}
