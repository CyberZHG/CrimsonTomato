package zhaohg.crimson.widget;

import android.graphics.Canvas;

import java.util.Vector;

public abstract class WidgetContainer {

    protected Vector<Widget> widgets = new Vector();

    protected void addChild(Widget widget) {
        this.widgets.add(widget);
    }

    protected void removeChild(Widget widget) {
        this.widgets.remove(widget);
    }

    protected void drawChildren(Canvas canvas) {
        for (Widget widget : this.widgets) {
            if (widget.isVisible()) {
                widget.onDraw(canvas);
            }
        }
    }

    public void onDraw(Canvas canvas) {
        selfDraw(canvas);
        drawChildren(canvas);
    }

    public abstract void selfDraw(Canvas canvas);

}
