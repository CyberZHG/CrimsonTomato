package zhaohg.crimson.main;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import zhaohg.crimson.scene.Scene;

public class MainView extends View {

    private Scene scene;

    public MainView(Context context) {
        super(context);
    }

    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MainView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (scene != null) {
            scene.onDraw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (scene != null) {
            return scene.onTouchEvent(event);
        }
        return false;
    }
}