package zhaohg.crimson.widget;

import android.content.Context;
import android.graphics.Canvas;

public abstract class Widget extends WidgetContainer {

    protected Context context;
    protected int x;
    protected int y;
    protected int w;
    protected int h;

    public Widget(Context context) {
        this.context = context;
    }

    public void setGeometry(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public abstract void onDraw(Canvas canvas);
}
