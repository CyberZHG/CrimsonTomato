package zhaohg.crimson.scene;

import android.content.Context;
import android.graphics.Canvas;

import zhaohg.crimson.widget.WidgetContainer;

public abstract class Scene extends WidgetContainer {

    protected Context context;

    public Scene(Context context) {
        this.context = context;
    }

    public abstract void onDraw(Canvas canvas);
}
