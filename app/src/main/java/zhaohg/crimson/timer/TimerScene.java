package zhaohg.crimson.timer;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

import zhaohg.crimson.R;
import zhaohg.crimson.scene.Scene;

public class TimerScene extends Scene {

    public TimerScene(Context context, View view) {
        super(context, view);
    }

    @Override
    public void initScene() {
        int cx = width / 2;
        int cy = height / 2 - (int)(height * 0.06);
        int radius = (int)(Math.min(cx, cy) * 0.8);
        int diameter = radius * 2;

        TimerWidget timerWidget = new TimerWidget(context, view);
        timerWidget.setGeometry(cx - radius, cy - radius, diameter, diameter);
        this.addChild(timerWidget);

        CurrentGoalWidget currentGoalWidget = new CurrentGoalWidget(context, view);
        currentGoalWidget.setGeometry(0, (int)(height * 0.94), width, (int)(height * 0.03));
        this.addChild(currentGoalWidget);
    }

    @Override
    public void selfDraw(Canvas canvas) {
        canvas.drawColor(context.getResources().getColor(R.color.background_material_dark));
    }

}
