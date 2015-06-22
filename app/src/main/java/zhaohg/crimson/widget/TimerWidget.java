package zhaohg.crimson.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;

import zhaohg.crimson.R;
import zhaohg.crimson.data.Tomato;
import zhaohg.crimson.data.TomatoData;

public class TimerWidget extends Widget {

    private static int PADDING = 5;

    private static final int STATE_WAIT = 0;
    private static final int STATE_TRANS_TO_RUNNING = 1;
    private static final int STATE_RUNNING = 2;
    private static final int STATE_FINISHED = 4;

    private int state = STATE_WAIT;
    private Date begin;
    private Date current;
    private Date end;
    private String note;

    private float transStrokeWidth;

    public TimerWidget(Context context, View view) {
        super(context, view);
    }

    @Override
    public void selfDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.rgb(212, 46, 24));
        paint.setTypeface(Typeface.SANS_SERIF);
        paint.setTextSize((int)(h * 0.2));
        paint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float fontHeight = fontMetrics.bottom - fontMetrics.top;
        float textBaseY = y + getH() - (getH() - fontHeight) / 2 - fontMetrics.bottom;
        int left = x + PADDING;
        int top = y + PADDING;
        int right = x + w - PADDING;
        int bottom = y + h - PADDING;
        int midX = (left + right) / 2;
        RectF oval = new RectF(left, top, right, bottom);
        switch (state) {
            case STATE_WAIT:
                paint.setStrokeWidth(7.0f);
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(oval, 0, 360, false, paint);
                paint.setColor(Color.WHITE);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawText(this.context.getString(R.string.timer_start), midX, textBaseY, paint);
                break;
            case STATE_TRANS_TO_RUNNING:
                paint.setStrokeWidth(this.transStrokeWidth);
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(oval, 0, 360, false, paint);
                break;
            case STATE_FINISHED:
                paint.setStrokeWidth(7.0f);
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(oval, 0, 360, false, paint);
                paint.setColor(Color.WHITE);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawText(this.context.getString(R.string.timer_finished), midX, textBaseY, paint);
                break;
            case STATE_RUNNING:
                paint.setStrokeWidth(1.0f);
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(oval, 0, 360, false, paint);
                long interval = current.getTime() - begin.getTime();
                float second = interval % (1000 * 60) / 1000.0f / 60.0f;
                float minute = interval / (1000.0f * 60) / 25.0f;
                float innerAngle = second * 360;
                float outerAngle = minute * 360;
                if ((interval / 1000 / 60) % 2 == 0) {
                    canvas.drawArc(new RectF(left + 3, top + 3, right - 3, bottom - 3), -90, innerAngle, false, paint);
                } else {
                    canvas.drawArc(new RectF(left + 3, top + 3, right - 3, bottom - 1), -90, innerAngle - 360, false, paint);
                }
                paint.setStrokeWidth(5.0f);
                canvas.drawArc(new RectF(left - 2, top - 2, right + 2, bottom + 2), -90, outerAngle, false, paint);
                paint.setColor(Color.WHITE);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawText(getRemainTimeString(), midX, textBaseY, paint);
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getX() < getLeft() || getRight() < event.getX()) {
            return false;
        }
        if (event.getY() < getTop() || getBottom() < event.getY()) {
            return false;
        }
        switch (this.state) {
            case STATE_WAIT:
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    this.begin = new Date();
                    this.current = new Date();
                    this.state = STATE_TRANS_TO_RUNNING;
                    this.transStrokeWidth = 7.0f;
                }
                break;
            case STATE_TRANS_TO_RUNNING:
            case STATE_RUNNING:
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
                    builder.setTitle(this.context.getString(R.string.dialog_give_up_title));
                    builder.setMessage(this.context.getString(R.string.dialog_give_up_message));
                    builder.setNegativeButton(this.context.getString(R.string.action_cancel),
                            new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton(this.context.getString(R.string.action_confirm),
                            new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            state = STATE_WAIT;
                            postInvalidate();
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
                break;
            case STATE_FINISHED:
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
                    builder.setTitle(this.context.getString(R.string.dialog_comment_title));

                    LinearLayout layout = new LinearLayout(this.context);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setLayoutParams(params);
                    layout.setGravity(Gravity.CLIP_VERTICAL);
                    layout.setPadding(2, 2, 2, 2);

                    LinearLayout.LayoutParams textViewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    textViewLayoutParams.bottomMargin = 5;

                    TextView textView = new TextView(this.context);
                    textView.setText(this.context.getString(R.string.dialog_comment_message));
                    layout.addView(textView, textViewLayoutParams);

                    final EditText editText = new EditText(this.context);
                    editText.setText(this.context.getString(R.string.app_name));
                    editText.selectAll();
                    layout.addView(editText, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                    builder.setView(layout);
                    builder.setNegativeButton(this.context.getString(R.string.action_cancel),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.setPositiveButton(this.context.getString(R.string.action_confirm),
                            new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            end = new Date();
                            state = STATE_WAIT;
                            note = editText.getText().toString();
                            addTomatoToDatabase();
                            postInvalidate();
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
                break;
        }
        this.postInvalidate();
        return true;
    }

    private void addTomatoToDatabase() {
        Tomato tomato = new Tomato();
        tomato.setBegin(this.begin);
        tomato.setEnd(this.end);
        tomato.setNote(this.note);
        TomatoData tomatoData = new TomatoData(this.context);
        tomatoData.addTomato(tomato);
    }

    @Override
    public void onTimeEvent() {
        switch (state) {
            case STATE_TRANS_TO_RUNNING:
                if (this.transStrokeWidth > 1.0f) {
                    this.transStrokeWidth -= 0.2f;
                } else {
                    this.state = STATE_RUNNING;
                }
                this.postInvalidate();
                break;
            case STATE_RUNNING:
                this.current = new Date();
                long interval = (current.getTime() - begin.getTime()) / 1000 / 60;
                if (interval > 25) {
                    this.state = STATE_FINISHED;
                }
                this.postInvalidate();
                break;
        }
        super.onTimeEvent();
    }

    private String getRemainTimeString() {
        long interval = 25 * 60 - (current.getTime() - begin.getTime()) / 1000;
        if (interval < 0) {
            interval = 0;
        }
        long minute = interval / 60;
        long second = interval % 60;
        String ret = "";
        if (minute < 10) {
            ret += "0";
        }
        ret += minute + " : ";
        if (second < 10) {
            ret += "0";
        }
        ret += second;
        return ret;
    }
}
