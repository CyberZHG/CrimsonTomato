package zhaohg.crimson.timer;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;

import zhaohg.crimson.R;
import zhaohg.crimson.goal.Goal;
import zhaohg.crimson.goal.GoalData;
import zhaohg.crimson.setting.Setting;
import zhaohg.crimson.widget.Widget;

public class CurrentGoalWidget extends Widget {

    private static final int STATE_HIDE = 0;
    private static final int STATE_SHOWING = 1;
    private static final int STATE_SHOW = 2;
    private static final int STATE_HIDING = 3;

    private final Setting setting = Setting.getInstance();

    private int state = STATE_HIDE;
    private float textAlpha = 0.0f;

    private int failGoalId = -1;
    private Goal goal;
    private GoalData goalData;

    public CurrentGoalWidget(Context context, View view) {
        super(context, view);
        goalData = new GoalData(context);
    }

    @Override
    protected void selfDraw(Canvas canvas) {
        if (goal == null) {
            return;
        }
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setAlpha((int) (textAlpha * 255));
        paint.setStyle(Paint.Style.FILL);
        paint.setTypeface(Typeface.SANS_SERIF);
        paint.setTextSize((int) (h * 0.8));
        paint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float fontHeight = fontMetrics.bottom - fontMetrics.top;
        float textBaseY = getBottom() - (getH() - fontHeight) / 2 - fontMetrics.bottom;
        String text = context.getString(R.string.goal_current_prefix) + " " + goal.getTitle();
        float textWidth = paint.measureText(text);
        while (textWidth > getW() * 0.85) {
            text = text.substring(0, text.length() - 4) + "...";
            textWidth = paint.measureText(text);
        }
        canvas.drawText(text, getCenterX(), textBaseY, paint);
    }

    @Override
    public void onTimerEvent() {
        switch (state) {
            case STATE_HIDE:
                this.timerEventWhenHide();
                break;
            case STATE_SHOWING:
                this.timerEventWhenShowing();
                break;
            case STATE_SHOW:
                this.timerEventWhenShow();
                break;
            case STATE_HIDING:
                this.timerEventWhenHiding();
                break;
        }
        super.onTimerEvent();
    }

    private void timerEventWhenHide() {
        int goalId = setting.getLastGoalId();
        if (goalId >= 0 && goalId != failGoalId) {
            goal = goalData.getGoal(goalId);
            if (goal == null) {
                failGoalId = goalId;
            } else {
                state = STATE_SHOWING;
            }
        }
    }

    private void timerEventWhenShow() {
        int goalId = setting.getLastGoalId();
        boolean isGone = false;
        if (Math.random() < 0.01) {
            goal = goalData.getGoal(goalId);
            if (goal == null) {
                isGone = true;
            }
        }
        if (goalId < 0 || isGone) {
            failGoalId = goalId;
            state = STATE_HIDING;
        }
    }

    private void timerEventWhenHiding() {
        textAlpha -= 0.07f;
        if (textAlpha < 0.0f) {
            textAlpha = 0.0f;
            state = STATE_HIDE;
        }
        postInvalidate();
    }

    private void timerEventWhenShowing() {
        textAlpha += 0.07f;
        if (textAlpha > 1.0f) {
            textAlpha = 1.0f;
            state = STATE_SHOW;
        }
        postInvalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_UP) {
            return false;
        }
        switch (state) {
            case STATE_SHOWING:
            case STATE_SHOW:
                return this.touchEventWhenShow(event);
            case STATE_HIDING:
            case STATE_HIDE:
                return this.touchEventWhenHide(event);
        }
        return false;
    }

    private boolean touchEventWhenShow(MotionEvent event) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle(this.context.getString(R.string.dialog_remove_current_goal_title));
        builder.setMessage(this.context.getString(R.string.dialog_remove_current_goal_message));
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
                        setting.setLastGoalId(-1);
                        state = STATE_HIDING;
                        postInvalidate();
                        dialog.dismiss();
                    }
                });
        builder.show();
        return true;
    }

    private boolean touchEventWhenHide(MotionEvent event) {
        return false;
    }

    @Override
    public void onResume() {
        goal = goalData.getGoal(setting.getLastGoalId());
        this.postInvalidate();
    }

}
