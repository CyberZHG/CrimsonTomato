package zhaohg.crimson.widget;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import zhaohg.crimson.R;

public class IconSync extends DrawableWidget {

    public IconSync(Context context, View view) {
        super(context, view);
        this.setDrawable(R.drawable.icon_sync);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            // TODO
        }
        return true;
    }

}
