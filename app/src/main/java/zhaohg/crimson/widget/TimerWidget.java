package zhaohg.crimson.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class TimerWidget extends Widget {

    public TimerWidget(Context context) {
        super(context);
    }

    @Override
    public void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.rgb(244, 10, 6));
        paint.setStrokeWidth(3.0f);
        paint.setStyle(Paint.Style.STROKE);
        RectF oval = new RectF(x, y, x + w, y + h);
        canvas.drawArc(oval, 0, 360, false, paint);
    }
}
