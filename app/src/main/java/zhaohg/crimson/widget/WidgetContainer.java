package zhaohg.crimson.widget;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.Vector;

public abstract class WidgetContainer {

    protected Vector<Widget> widgets = new Vector();
    protected Widget lastTouchWidget;

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

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN) {
            if (this.lastTouchWidget != null) {
                if (!this.lastTouchWidget.onTouchEvent(event)) {
                    this.lastTouchWidget = null;
                } else {
                    return true;
                }

            }
        }
        int x = (int)event.getX();
        int y = (int)event.getY();
        for (Widget widget : this.widgets) {
            if (widget.getLeft() <= x && x <= widget.getRight()) {
                if (widget.getTop() <= y && y <= widget.getBottom()) {
                    if (widget.onTouchEvent(event)) {
                        this.lastTouchWidget = widget;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void onTimeEvent() {
        for (Widget widget : this.widgets) {
            widget.onTimeEvent();
        }
    }

    public abstract void selfDraw(Canvas canvas);

}
