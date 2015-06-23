package zhaohg.crimson.scene;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;

import zhaohg.crimson.widget.IconHistory;
import zhaohg.crimson.widget.IconSetting;
import zhaohg.crimson.widget.TimerWidget;

public class MainScene extends Scene {

    private TimerWidget timerWidget;

    public MainScene(Context context, View view) {
        super(context, view);
    }

    @Override
    public void initScene() {
        int cx = this.width / 2;
        int cy = this.height / 2 - (int)(this.height * 0.06);
        int radius = (int)(Math.min(cx, cy) * 0.8);
        int diameter = radius * 2;
        this.timerWidget = new TimerWidget(context, view);
        this.timerWidget.setGeometry(cx - radius, cy - radius, diameter, diameter);
        this.addChild(this.timerWidget);

        int iconSize = (int)(Math.min(cx, cy) * 0.2);
        int top = (int) (this.height * 0.9);
        int left = (int) (this.width * 0.8);
        IconSetting iconSetting = new IconSetting(context, view);
        iconSetting.setGeometry(left, top, iconSize, iconSize);
        this.addChild(iconSetting);

        left -= iconSize * 1.7;
        IconHistory iconHistory = new IconHistory(context, view);
        iconHistory.setGeometry(left, top, iconSize, iconSize);
        this.addChild(iconHistory);
    }

    @Override
    public void selfDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR);
    }

}
