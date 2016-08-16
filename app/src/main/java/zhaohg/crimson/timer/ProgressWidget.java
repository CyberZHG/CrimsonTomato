package zhaohg.crimson.timer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.view.View;

import java.util.Calendar;
import java.util.Date;

import zhaohg.crimson.R;
import zhaohg.crimson.setting.Setting;
import zhaohg.crimson.widget.Widget;

public class ProgressWidget extends Widget {

    private final Setting setting = Setting.getInstance();

    public ProgressWidget(Context context, View view) {
        super(context, view);
    }

    @Override
    protected void selfDraw(Canvas canvas) {
        if (!setting.isShowProgress()) {
            return;
        }
        int dayCount = setting.getDayCount();
        if (dayCount == 0) {
            return;
        }
        int suiteCount = dayCount / setting.getSuiteNum();
        int singleCount = dayCount % setting.getSuiteNum();
        int iconSize = (int)(getH() * 0.9);
        int halfIconSize = iconSize >> 1;
        iconSize = (int)(iconSize * 1.5);
        int totalWidth = (suiteCount + singleCount - 1) * iconSize;
        int x = getCenterX() - (totalWidth) / 2;
        int y = getCenterY();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(ContextCompat.getColor(context, R.color.color_primary));
        for (int i = 0; i < suiteCount + singleCount; ++i) {
            paint.setStrokeWidth(Math.max(1, (int)(iconSize * 0.1)));
            paint.setStyle(Paint.Style.STROKE);
            RectF oval = new RectF(x - halfIconSize, y - halfIconSize, x + halfIconSize, y + halfIconSize);
            canvas.drawArc(oval, 0, 360, false, paint);
            if (i < suiteCount) {
                paint.setStyle(Paint.Style.FILL);
                int innerSize = (int)(halfIconSize * 0.6);
                oval = new RectF(x - innerSize, y - innerSize, x + innerSize, y + innerSize);
                canvas.drawArc(oval, 0, 360, false, paint);
            }
            x += iconSize;
        }
    }

    @Override
    public void onResume() {
        Calendar last = Calendar.getInstance();
        Calendar current = Calendar.getInstance();
        last.setTime(setting.getLastFinished());
        current.setTime(new Date());
        if (last.get(Calendar.YEAR) != current.get(Calendar.YEAR) ||
            last.get(Calendar.DAY_OF_YEAR) != current.get(Calendar.DAY_OF_YEAR)) {
            setting.setDayCount(0);
        }
    }

}
