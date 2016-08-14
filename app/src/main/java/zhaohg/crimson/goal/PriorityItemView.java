package zhaohg.crimson.goal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import zhaohg.crimson.R;

public class PriorityItemView extends View {

    private int priority = Priority.PRIORITY_BLUE;
    private boolean selected = false;
    private int priorityColor = Color.WHITE;
    private int selectedColor = Color.BLACK;

    private OnSelectedChangedListener onSelectedChangedListener;

    public PriorityItemView(Context context) {
        super(context);
    }

    public PriorityItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray values = this.getContext().obtainStyledAttributes(attrs, R.styleable.crimson);
        this.selected = values.getBoolean(R.styleable.crimson_selected, false);
        this.priorityColor = values.getColor(R.styleable.crimson_priority_color, Color.WHITE);
        this.selectedColor = values.getColor(R.styleable.crimson_selected_color, Color.BLACK);
        values.recycle();
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        if (this.selected != selected) {
            this.selected = selected;
            this.postInvalidate();
            if (this.onSelectedChangedListener != null) {
                this.onSelectedChangedListener.onSelectedChanged(selected);
            }
        }
    }

    public int getPriorityColor() {
        return priorityColor;
    }

    public void setPriorityColor(int priorityColor) {
        this.priorityColor = priorityColor;
        this.postInvalidate();
    }

    public int getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
        this.postInvalidate();
    }

    public void setOnSelectedChangedListener(OnSelectedChangedListener onSelectedChangedListener) {
        this.onSelectedChangedListener = onSelectedChangedListener;
    }

    @Override
    public void onDraw(Canvas canvas) {
        int cx = getWidth() / 2;
        int cy = getHeight() / 2;
        int halfLen = (int)(Math.min(getWidth(), getHeight()) / 2 * 0.95);
        RectF rect = new RectF(cx - halfLen, cy - halfLen, cx + halfLen, cy + halfLen);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(this.priorityColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rect, paint);
        if (this.selected) {
            paint.setColor(this.selectedColor);
            int strokeWidth = (int)(getWidth() * 0.10);
            if (strokeWidth % 2 == 1) {
                --strokeWidth;
            }
            paint.setStrokeWidth(strokeWidth);
            int halfStrokeWidth = strokeWidth / 2;
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(rect.left + halfStrokeWidth, rect.top + halfStrokeWidth,
                    rect.right - halfStrokeWidth, rect.bottom - halfStrokeWidth, paint);
        }
    }

    public interface OnSelectedChangedListener {
        void onSelectedChanged(boolean selected);
    }
}
