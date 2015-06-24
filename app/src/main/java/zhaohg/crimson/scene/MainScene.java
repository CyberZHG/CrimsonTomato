package zhaohg.crimson.scene;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

import zhaohg.crimson.R;
import zhaohg.crimson.widget.TimerWidget;

public class MainScene extends Scene {

    private TimerWidget timerWidget;

    public MainScene(Context context, View view) {
        super(context, view);
    }

    @Override
    public void initScene() {
        int cx = this.width / 2;
        int cy = this.height / 2;
        int radius = (int)(Math.min(cx, cy) * 0.8);
        int diameter = radius * 2;
        this.timerWidget = new TimerWidget(context, view);
        this.timerWidget.setGeometry(cx - radius, cy - radius, diameter, diameter);
        this.addChild(this.timerWidget);
    }

    @Override
    public void selfDraw(Canvas canvas) {
        canvas.drawColor(context.getResources().getColor(R.color.background_material_dark));
    }

}
