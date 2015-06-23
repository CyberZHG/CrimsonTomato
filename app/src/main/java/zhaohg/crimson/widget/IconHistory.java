package zhaohg.crimson.widget;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import zhaohg.crimson.R;

public class IconHistory extends DrawableWidget {

    public IconHistory(Context context, View view) {
        super(context, view);
        this.setDrawable(R.drawable.icon_history);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            // TODO
        }
        return true;
    }

}
