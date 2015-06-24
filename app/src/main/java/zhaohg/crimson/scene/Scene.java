package zhaohg.crimson.scene;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

import zhaohg.crimson.widget.WidgetContainer;

public abstract class Scene extends WidgetContainer {

    final Context context;
    final View view;
    private boolean initialized = false;
    int width;
    int height;

    Scene(Context context, View view) {
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

    protected abstract void initScene();

}
