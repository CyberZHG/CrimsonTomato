package zhaohg.crimson.timer;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
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

import java.util.Calendar;
import java.util.Date;

import zhaohg.crimson.R;
import zhaohg.crimson.goal.Goal;
import zhaohg.crimson.goal.GoalData;
import zhaohg.crimson.setting.Setting;
import zhaohg.crimson.tomato.Tomato;
import zhaohg.crimson.tomato.TomatoData;
import zhaohg.crimson.widget.Widget;

public class TimerWidget extends Widget {

    private static final int PADDING = 5;

    private static final int STATE_WAIT = 0;
    private static final int STATE_TRANS_TO_RUNNING = 1;
    private static final int STATE_RUNNING = 2;
    private static final int STATE_TRANS_TO_FINISHED = 3;
    private static final int STATE_FINISHED = 4;

    private Setting setting = Setting.getInstance();

    private int state = STATE_WAIT;
    private int period = 25;
    private Date begin;
    private Date current;
    private Date end;
    private String title;
    private boolean isBreakFinished;

    private int timerColor;
    private int textColor;

    private float transStrokeWidth;
    private float fontAlpha = 1.0f;

    public TimerWidget(Context context, View view) {
        super(context, view);
        this.timerColor = context.getResources().getColor(R.color.color_primary);
        this.textColor = context.getResources().getColor(R.color.text_color_primary);
        this.onResume();
    }

    @Override
    public void selfDraw(Canvas canvas) {
        switch (state) {
            case STATE_WAIT:
                this.drawWhenWait(canvas);
                break;
            case STATE_TRANS_TO_RUNNING:
                this.drawWhenTransToRunning(canvas);
                break;
            case STATE_RUNNING:
                this.drawWhenRunning(canvas);
                break;
            case STATE_TRANS_TO_FINISHED:
                this.drawWhenTransToFinished(canvas);
                break;
            case STATE_FINISHED:
                this.drawWhenFinished(canvas);
                break;
        }
    }

    private Paint getDrawPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTypeface(Typeface.SANS_SERIF);
        paint.setTextSize((int)(h * 0.2));
        paint.setTextAlign(Paint.Align.CENTER);
        return paint;
    }

    private float getTextMidX() {
        return (getLeft() + getRight()) * 0.5f;
    }

    private float getTextBaseY(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float fontHeight = fontMetrics.bottom - fontMetrics.top;
        return getBottom() - (getH() - fontHeight) / 2 - fontMetrics.bottom;
    }

    private int getSubTextSize() {
        return (int)(h * 0.06);
    }

    private float getStrokeThick() {
        return 10.0f * w / 480;
    }

    private float getStrokeThin() {
        return Math.max(1.0f, 1.0f * w / 480);
    }

    private RectF getOval() {
        int left = x + PADDING;
        int top = y + PADDING;
        int right = x + w - PADDING;
        int bottom = y + h - PADDING;
        return new RectF(left, top, right, bottom);
    }

    private void drawWhenWait(Canvas canvas) {
        Paint paint = getDrawPaint();
        paint.setColor(this.timerColor);
        paint.setStrokeWidth(getStrokeThick());
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(getOval(), 0, 360, false, paint);
        paint.setColor(this.textColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha((int) (255 * fontAlpha));
        String text = "";
        if (isBreakFinished) {
            text = context.getString(R.string.timer_start);
        } else {
            text = context.getString(R.string.timer_break);
        }
        canvas.drawText(text, getTextMidX(), (int)(getTextBaseY(paint) - h * 0.03), paint);
        paint.setTextSize(getSubTextSize());
        canvas.drawText(getRestTimeString(), getTextMidX(), (int)(y + h * 0.70), paint);
    }

    private void drawWhenTransToRunning(Canvas canvas) {
        Paint paint = getDrawPaint();
        paint.setColor(this.timerColor);
        paint.setStrokeWidth(this.transStrokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(getOval(), 0, 360, false, paint);
        paint.setColor(this.textColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha((int) (255 * fontAlpha));
        canvas.drawText(context.getString(R.string.timer_start), getTextMidX(), (int)(getTextBaseY(paint) - h * 0.03), paint);
        paint.setTextSize(getSubTextSize());
        canvas.drawText(getRestTimeString(), getTextMidX(), (int)(y + h * 0.70), paint);
    }

    private void drawWhenRunning(Canvas canvas) {
        Paint paint = getDrawPaint();
        paint.setColor(this.timerColor);
        paint.setStrokeWidth(getStrokeThin());
        paint.setStyle(Paint.Style.STROKE);
        RectF oval = getOval();
        canvas.drawArc(oval, 0, 360, false, paint);
        long interval = current.getTime() - begin.getTime();
        float second = interval % (1000 * 60) / 1000.0f / 60.0f;
        float minute = interval / (1000.0f * 60) / period;
        float innerAngle = second * 360;
        float outerAngle = minute * 360;
        paint.setStrokeWidth(getStrokeThin() * 2);
        float margin = 12.0f * w / 480;
        if ((interval / 1000 / 60) % 2 == 0) {
            canvas.drawArc(new RectF(oval.left + margin,
                    oval.top + margin,
                    oval.right - margin,
                    oval.bottom - margin), -90, innerAngle, false, paint);
        } else {
            canvas.drawArc(new RectF(oval.left + margin,
                    oval.top + margin,
                    oval.right - margin,
                    oval.bottom - margin), -90, innerAngle - 360, false, paint);
        }
        paint.setStrokeWidth(getStrokeThick());
        canvas.drawArc(new RectF(oval.left - getStrokeThick() / 2,
                oval.top - getStrokeThick() / 2,
                oval.right + getStrokeThick() / 2,
                oval.bottom + getStrokeThick() / 2), -90, outerAngle, false, paint);
        paint.setColor(this.textColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha((int) (255 * fontAlpha));
        canvas.drawText(getRemainTimeString(), getTextMidX(), getTextBaseY(paint), paint);
    }

    private void drawWhenTransToFinished(Canvas canvas) {
        Paint paint = getDrawPaint();
        paint.setColor(this.timerColor);
        paint.setStrokeWidth(getStrokeThin());
        paint.setStyle(Paint.Style.STROKE);
        RectF oval = getOval();
        canvas.drawArc(oval, 0, 360, false, paint);
        long interval = current.getTime() - begin.getTime();
        float second = interval % (1000 * 60) / 1000.0f / 60.0f;
        float minute = interval / (1000.0f * 60) / period;
        float innerAngle = second * 360;
        float outerAngle = minute * 360;
        paint.setStrokeWidth(getStrokeThin() * 2);
        float margin = 12.0f * w / 480;
        if ((interval / 1000 / 60) % 2 == 0) {
            canvas.drawArc(new RectF(oval.left + margin,
                    oval.top + margin,
                    oval.right - margin,
                    oval.bottom - margin), -90, innerAngle, false, paint);
        } else {
            canvas.drawArc(new RectF(oval.left + margin,
                    oval.top + margin,
                    oval.right - margin,
                    oval.bottom - margin), -90, innerAngle - 360, false, paint);
        }
        paint.setStrokeWidth(getStrokeThick());
        canvas.drawArc(new RectF(oval.left - getStrokeThick() / 2,
                oval.top - getStrokeThick() / 2,
                oval.right + getStrokeThick() / 2,
                oval.bottom + getStrokeThick() / 2), -90, outerAngle, false, paint);
        paint.setColor(this.textColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha((int) (255 * fontAlpha));
        canvas.drawText(getRemainTimeString(), getTextMidX(), getTextBaseY(paint), paint);
    }

    private void drawWhenFinished(Canvas canvas) {
        Paint paint = getDrawPaint();
        paint.setColor(this.timerColor);
        paint.setStrokeWidth(getStrokeThick());
        paint.setStyle(Paint.Style.STROKE);
        RectF oval = getOval();
        float margin = 12.0f * w / 480;
        paint.setStrokeWidth(getStrokeThin() * 2);
        canvas.drawArc(new RectF(oval.left + margin,
                oval.top + margin,
                oval.right - margin,
                oval.bottom - margin), 0, 360, false, paint);
        paint.setStrokeWidth(getStrokeThick());
        canvas.drawArc(new RectF(oval.left - getStrokeThick() / 2,
                oval.top - getStrokeThick() / 2,
                oval.right + getStrokeThick() / 2,
                oval.bottom + getStrokeThick() / 2), 0, 360, false, paint);
        paint.setColor(this.textColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha((int) (255 * fontAlpha));
        canvas.drawText(this.context.getString(R.string.timer_finished), getTextMidX(), (int)(getTextBaseY(paint) - h * 0.03), paint);
        paint.setTextSize(getSubTextSize());
        canvas.drawText(getPassedTimeString(), getTextMidX(), (int)(y + h * 0.70), paint);
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
                return this.touchEventWhenWait(event);
            case STATE_TRANS_TO_RUNNING:
            case STATE_RUNNING:
                return this.touchEventWhenRunning(event);
            case STATE_TRANS_TO_FINISHED:
            case STATE_FINISHED:
                return touchEventWhenFinished(event);
        }
        this.postInvalidate();
        return true;
    }

    private boolean touchEventWhenWait(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            this.period = setting.getPeriod();
            int lastGoalId = setting.getLastGoalId();
            GoalData goalData = new GoalData(context);
            Goal goal = goalData.getGoal(lastGoalId);
            if (goal != null) {
                this.period = goal.getPeriod();
            }
            this.begin = new Date();
            this.current = new Date();
            setting.setLastBegin(this.begin);
            setting.setLastPeriod(this.period);
            setting.setVibrated(false);
            this.state = STATE_TRANS_TO_RUNNING;
            this.transStrokeWidth = 10.0f * w / 480;
            this.fontAlpha = 1.0f;
            return true;
        }
        return false;
    }

    private boolean touchEventWhenRunning(MotionEvent event) {
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
                            setting.setLastGoalId(-1);
                            state = STATE_WAIT;
                            postInvalidate();
                            dialog.dismiss();
                        }
                    });
            builder.show();
            return true;
        }
        return false;
    }

    private boolean touchEventWhenFinished(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
            builder.setTitle(this.context.getString(R.string.dialog_comment_title));

            final Setting setting = Setting.getInstance();
            final int lastGoalId = setting.getLastGoalId();
            final GoalData goalData = new GoalData(context);
            final Goal goal = goalData.getGoal(lastGoalId);

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
            editText.setText(setting.getDefaultTitle());
            if (goal != null) {
                editText.setText(goal.getTitle());
            }
            editText.selectAll();
            layout.addView(editText, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            builder.setView(layout);
            builder.setNegativeButton(this.context.getString(R.string.action_cancel),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setting.setLastBegin(null);
                            setting.setLastGoalId(-1);
                            setting.setLastFinished();
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
                            Calendar last = Calendar.getInstance();
                            Calendar current = Calendar.getInstance();
                            last.setTime(setting.getLastBegin());
                            current.setTime(new Date());
                            if (last.get(Calendar.YEAR) == current.get(Calendar.YEAR) &&
                                last.get(Calendar.DAY_OF_YEAR) == current.get(Calendar.DAY_OF_YEAR)) {
                                setting.setDayCount(setting.getDayCount() + 1);
                            } else {
                                setting.setDayCount(1);
                            }
                            setting.setLastBegin(null);
                            setting.setLastFinished();
                            end = new Date();
                            state = STATE_WAIT;
                            title = editText.getText().toString();
                            if (goal != null) {
                                goalData.addTomatoAndMinute(goal, (int) ((end.getTime() - begin.getTime()) / 1000 / 60));
                            }
                            addTomatoToDatabase();
                            postInvalidate();
                            dialog.dismiss();
                        }
                    });
            builder.show();
            return true;
        }
        return false;
    }

    private void addTomatoToDatabase() {
        Tomato tomato = new Tomato();
        tomato.setBegin(this.begin);
        tomato.setEnd(this.end);
        tomato.setTitle(this.title);
        int lastGoalId = setting.getLastGoalId();
        GoalData goalData = new GoalData(context);
        Goal goal = goalData.getGoal(lastGoalId);
        if (goal == null) {
            tomato.setDescription(context.getString(R.string.app_name));
        } else {
            tomato.setDescription(context, goal);
        }
        TomatoData tomatoData = new TomatoData(this.context);
        tomatoData.addTomato(tomato);
    }

    @Override
    public void onTimerEvent() {
        switch (state) {
            case STATE_TRANS_TO_RUNNING:
                this.timerEventWhenTransToRunning();
                break;
            case STATE_RUNNING:
                this.timerEventWhenRunning();
                break;
            case STATE_TRANS_TO_FINISHED:
                this.timerEventWhenTransToFinished();
                break;
            case STATE_FINISHED:
                this.timerEventWhenFinished();
                break;
        }
        this.postInvalidate();
        super.onTimerEvent();
    }

    private void timerEventWhenTransToRunning() {
        if (this.transStrokeWidth > 1.0f) {
            this.transStrokeWidth -= 0.7f;
        } else {
            this.state = STATE_RUNNING;
        }
        if (this.fontAlpha > 0.0f) {
            this.fontAlpha -= 0.07f;
            if (this.fontAlpha < 0.0f) {
                this.fontAlpha = 0.0f;
            }
        }
    }

    private void timerEventWhenRunning() {
        this.current = new Date();
        long interval = (current.getTime() - begin.getTime()) / 1000 / 60;
        if (interval >= period) {
            if (setting.isVibrate() && !setting.isVibrated()) {
                Vibrator vibrator = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
                long[] pattern = {100, 240, 100, 240};
                vibrator.vibrate(pattern, -1);
                setting.setVibrated(true);
            }
            this.state = STATE_TRANS_TO_FINISHED;
        }
        if (this.fontAlpha < 1.0f) {
            this.fontAlpha += 0.07f;
            if (this.fontAlpha > 1.0f) {
                this.fontAlpha = 1.0f;
            }
        }
    }

    private void timerEventWhenTransToFinished() {
        if (this.fontAlpha > 0.0f) {
            this.fontAlpha -= 0.07f;
            if (this.fontAlpha < 0.0f) {
                this.fontAlpha = 0.0f;
                this.state = STATE_FINISHED;
            }
        }
    }

    private void timerEventWhenFinished() {
        if (this.fontAlpha < 1.0f) {
            this.fontAlpha += 0.07f;
            if (this.fontAlpha > 1.0f) {
                this.fontAlpha = 1.0f;
            }
        }
    }

    private String getFormattedTimeString(long interval) {
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

    private String getRestTimeString() {
        int breakPeriod = setting.getShortBreak();
        if (setting.getDayCount() == 0) {
            breakPeriod = 0;
        } else if (setting.getDayCount() % setting.getSuiteNum() == 0) {
            breakPeriod = setting.getLongBreak();
        }
        long remainTime = breakPeriod * 60 - (new Date().getTime() - setting.getLastFinished().getTime()) / 1000;
        this.isBreakFinished = remainTime <= 0;
        return getFormattedTimeString(remainTime);
    }

    private String getRemainTimeString() {
        return getFormattedTimeString(period * 60 - (current.getTime() - begin.getTime()) / 1000);
    }

    private String getPassedTimeString() {
        return getFormattedTimeString((new Date().getTime() - begin.getTime()) / 1000);
    }

    @Override
    public void onResume() {
        int lastGoalId = setting.getLastGoalId();
        GoalData goalData = new GoalData(context);
        Goal goal = goalData.getGoal(lastGoalId);
        if (goal != null) {
            this.period = goal.getPeriod();
            if (setting.isFastStart()) {
                this.begin = new Date();
                this.current = new Date();
                this.state = STATE_RUNNING;
                setting.setFastStart(false);
            }
        } else {
            this.period = setting.getLastPeriod();
            setting.setFastStart(false);
        }
        if (setting.getLastBegin() != null) {
            this.begin = setting.getLastBegin();
            this.current = new Date();
            this.state = STATE_RUNNING;
            if ((current.getTime() - begin.getTime()) / 1000 / 60 >= period) {
                this.state = STATE_FINISHED;
            }
        }
    }

    @Override
    public void onPause() {
    }
}
