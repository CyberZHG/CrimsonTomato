package zhaohg.crimson.timer;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;

import zhaohg.crimson.R;
import zhaohg.crimson.setting.Setting;
import zhaohg.crimson.tomato.Tomato;
import zhaohg.crimson.tomato.TomatoData;
import zhaohg.crimson.widget.Widget;

public class TimerWidget extends Widget {

    private static final int PADDING = 5;

    private static final int STATE_WAIT = 0;
    private static final int STATE_TRANS_TO_RUNNING = 1;
    private static final int STATE_RUNNING = 2;
    private static final int STATE_FINISHED = 4;

    private int period = 25;

    private int state = STATE_WAIT;
    private Date begin;
    private Date current;
    private Date end;
    private String title;

    private float transStrokeWidth;
    private float fontAlpha = 1.0f;

    public TimerWidget(Context context, View view) {
        super(context, view);
        this.onResume();
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
        float strokeThickWidth = 10.0f * w / 480;
        float strokeThinWidth = Math.max(1.0f, 1.0f * w / 480);
        RectF oval = new RectF(left, top, right, bottom);
        switch (state) {
            case STATE_WAIT: {
                    paint.setStrokeWidth(strokeThickWidth);
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawArc(oval, 0, 360, false, paint);
                    paint.setColor(Color.WHITE);
                    paint.setStyle(Paint.Style.FILL);
                    paint.setAlpha((int) (255 * fontAlpha));
                    canvas.drawText(this.context.getString(R.string.timer_start), midX, textBaseY, paint);
                }
                break;
            case STATE_TRANS_TO_RUNNING: {
                    paint.setStrokeWidth(this.transStrokeWidth);
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawArc(oval, 0, 360, false, paint);
                    paint.setColor(Color.WHITE);
                    paint.setStyle(Paint.Style.FILL);
                    paint.setAlpha((int) (255 * fontAlpha));
                    canvas.drawText(this.context.getString(R.string.timer_start), midX, textBaseY, paint);
                }
                break;
            case STATE_FINISHED: {
                    paint.setStrokeWidth(strokeThickWidth);
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawArc(oval, 0, 360, false, paint);
                    paint.setColor(Color.WHITE);
                    paint.setStyle(Paint.Style.FILL);
                    paint.setAlpha((int) (255 * fontAlpha));
                    canvas.drawText(this.context.getString(R.string.timer_finished), midX, textBaseY, paint);
                }
                break;
            case STATE_RUNNING: {
                    paint.setStrokeWidth(strokeThinWidth);
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawArc(oval, 0, 360, false, paint);
                    long interval = current.getTime() - begin.getTime();
                    float second = interval % (1000 * 60) / 1000.0f / 60.0f;
                    float minute = interval / (1000.0f * 60) / period;
                    float innerAngle = second * 360;
                    float outerAngle = minute * 360;
                    paint.setStrokeWidth(strokeThinWidth * 2);
                    float margin = 12.0f * w / 480;
                    if ((interval / 1000 / 60) % 2 == 0) {
                        canvas.drawArc(new RectF(left + margin,
                                top + margin,
                                right - margin,
                                bottom - margin), -90, innerAngle, false, paint);
                    } else {
                        canvas.drawArc(new RectF(left + margin,
                                top + margin,
                                right - margin,
                                bottom - margin), -90, innerAngle - 360, false, paint);
                    }
                    paint.setStrokeWidth(strokeThickWidth);
                    canvas.drawArc(new RectF(left - strokeThickWidth / 2,
                            top - strokeThickWidth / 2,
                            right + strokeThickWidth / 2,
                            bottom + strokeThickWidth / 2), -90, outerAngle, false, paint);
                    paint.setColor(Color.WHITE);
                    paint.setStyle(Paint.Style.FILL);
                    paint.setAlpha((int) (255 * fontAlpha));
                    canvas.drawText(getRemainTimeString(), midX, textBaseY, paint);
                }
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
                    Setting setting = Setting.getInstance();
                    this.period = setting.getPeriod();
                    this.begin = new Date();
                    this.current = new Date();
                    setting.setLastBegin(this.begin);
                    setting.setLastPeriod(this.period);
                    setting.setVibrated(false);
                    this.state = STATE_TRANS_TO_RUNNING;
                    this.transStrokeWidth = 10.0f * w / 480;
                    this.fontAlpha = 1.0f;
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
                            Setting setting = Setting.getInstance();
                            setting.setLastBegin(null);
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
                    layout.setPadding(15, 15, 15, 15);

                    LinearLayout.LayoutParams textViewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    textViewLayoutParams.leftMargin = 5;
                    textViewLayoutParams.bottomMargin = 15;

                    TextView textView = new TextView(this.context);
                    textView.setText(this.context.getString(R.string.dialog_comment_message));
                    layout.addView(textView, textViewLayoutParams);

                    final EditText editText = new EditText(this.context);
                    Setting setting = Setting.getInstance();
                    editText.setText(setting.getDefaultTitle());
                    editText.selectAll();
                    layout.addView(editText, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                    builder.setView(layout);
                    builder.setNegativeButton(this.context.getString(R.string.action_cancel),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Setting setting = Setting.getInstance();
                                    setting.setLastBegin(null);
                                    end = new Date();
                                    state = STATE_WAIT;
                                    title = editText.getText().toString();
                                    postInvalidate();
                                    dialog.dismiss();
                                }
                            });
                    builder.setPositiveButton(this.context.getString(R.string.action_confirm),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Setting setting = Setting.getInstance();
                                    setting.setLastBegin(null);
                                    end = new Date();
                                    state = STATE_WAIT;
                                    title = editText.getText().toString();
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
        tomato.setTitle(this.title);
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
                if (this.fontAlpha > 0.0f) {
                    this.fontAlpha -= 0.04f;
                    if (this.fontAlpha < 0.0f) {
                        this.fontAlpha = 0.0f;
                    }
                }
                this.postInvalidate();
                break;
            case STATE_RUNNING:
                this.current = new Date();
                long interval = (current.getTime() - begin.getTime()) / 1000 / 60;
                if (interval >= period) {
                    Setting setting = Setting.getInstance();
                    if (setting.isVibrate() && !setting.isVibrated()) {
                        Vibrator vibrator = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
                        long[] pattern = {100, 240, 100, 240};
                        vibrator.vibrate(pattern, -1);
                        setting.setVibrated(true);
                    }
                    this.state = STATE_FINISHED;
                }
                if (this.fontAlpha < 1.0f) {
                    this.fontAlpha += 0.04f;
                    if (this.fontAlpha > 1.0f) {
                        this.fontAlpha = 1.0f;
                    }
                }
                this.postInvalidate();
                break;
        }
        super.onTimeEvent();
    }

    private String getRemainTimeString() {
        long interval = period * 60 - (current.getTime() - begin.getTime()) / 1000;
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

    @Override
    public void onResume() {
        Setting setting = Setting.getInstance();
        if (setting.getLastBegin() != null) {
            this.begin = setting.getLastBegin();
            this.period = setting.getLastPeriod();
            this.current = new Date();
            this.state = STATE_RUNNING;
        }
    }

    @Override
    public void onPause() {
    }
}