package zhaohg.crimson.scene;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;

import zhaohg.crimson.widget.TimerWidget;

public class MainScene extends Scene {

    private TimerWidget timerWidget;

    public MainScene(Context context) {
        super(context);
        this.timerWidget = new TimerWidget(context);
        this.timerWidget.setGeometry(100, 100, 300, 300);
        this.addChild(this.timerWidget);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR);
        this.drawChildren(canvas);
    }

}
