package zhaohg.crimson.widget;

import android.content.Context;
import android.view.View;

public abstract class Widget extends WidgetContainer {

    protected Context context;
    protected View view;

    protected int x;
    protected int y;
    protected int w;
    protected int h;
    protected boolean visible = true;

    public Widget(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    public void setGeometry(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getLeft() {
        return this.x;
    }

    public int getRight() {
        return this.x + this.w;
    }

    public int getTop() {
        return this.y;
    }

    public int getBottom() {
        return this.y + this.h;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void postInvalidate() {
        this.view.postInvalidate(this.getLeft(), this.getTop(), this.getRight(), this.getBottom());
    }
}
