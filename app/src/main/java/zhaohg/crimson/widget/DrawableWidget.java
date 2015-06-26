package zhaohg.crimson.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

public class DrawableWidget extends Widget {

    protected Drawable drawable;

    public DrawableWidget(Context context, View view) {
        super(context, view);
    }

    @Override
    public void setGeometry(int x, int y, int w, int h) {
        super.setGeometry(x, y, w, h);
        if (this.drawable != null) {
            this.drawable.setBounds(getLeft(), getTop(), getRight(), getBottom());
        }
    }

    public void setDrawable(int drawableId) {
        this.drawable = this.context.getResources().getDrawable(drawableId);
        this.drawable.setBounds(getLeft(), getTop(), getRight(), getBottom());
    }

    @Override
    public void selfDraw(Canvas canvas) {
        this.drawable.draw(canvas);
    }
}
