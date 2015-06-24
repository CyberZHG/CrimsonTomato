package zhaohg.crimson.widget;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.Vector;

public abstract class WidgetContainer {

    private final Vector<Widget> widgets = new Vector();
    private Widget lastTouchWidget;

    protected void addChild(Widget widget) {
        this.widgets.add(widget);
    }

    protected void removeChild(Widget widget) {
        this.widgets.remove(widget);
    }

    void drawChildren(Canvas canvas) {
        for (Widget widget : this.widgets) {
            if (widget.isVisible()) {
                widget.onDraw(canvas);
            }
        }
    }

    protected void onDraw(Canvas canvas) {
        selfDraw(canvas);
        drawChildren(canvas);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN) {
            if (this.lastTouchWidget != null) {
                if (this.lastTouchWidget.onTouchEvent(event)) {
                    return true;
                }
                this.lastTouchWidget = null;
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

    public void onResume() {
        for (Widget widget : this.widgets) {
            widget.onResume();
        }
    }

    public void onPause() {
        for (Widget widget : this.widgets) {
            widget.onPause();
        }
    }

    protected abstract void selfDraw(Canvas canvas);

}
