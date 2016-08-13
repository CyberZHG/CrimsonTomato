package zhaohg.crimson.scene;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import zhaohg.crimson.widget.WidgetContainer;

public abstract class Scene extends WidgetContainer {

    protected final Context context;
    protected final View view;
    private boolean initialized = false;
    protected int width;
    protected int height;

    protected Scene(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (!this.initialized) {
            this.width = this.view.getWidth();
            this.height = this.view.getHeight();
            initScene();
            this.initialized = true;
        }
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return true;
    }

    protected abstract void initScene();

}
