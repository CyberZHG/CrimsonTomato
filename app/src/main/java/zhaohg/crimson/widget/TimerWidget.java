package zhaohg.crimson.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;

import zhaohg.crimson.R;
import zhaohg.crimson.data.Tomato;
import zhaohg.crimson.data.TomatoData;

public class TimerWidget extends Widget {

    private static final int STATE_WAIT = 0;
    private static final int STATE_TRANS_TO_RUNNING = 1;
    private static final int STATE_RUNNING = 2;
    private static final int STATE_FINISHED = 4;

    private int state = STATE_WAIT;
    private Date begin;
    private Date end;
    private String note;

    public TimerWidget(Context context) {
        super(context);
    }

    @Override
    public void selfDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.rgb(244, 10, 6));
        paint.setStrokeWidth(3.0f);
        paint.setStyle(Paint.Style.STROKE);
        RectF oval = new RectF(x, y, x + w, y + h);
        canvas.drawArc(oval, 0, 360, false, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (this.state) {
            case STATE_WAIT:
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    this.begin = new Date();
                    this.state = STATE_TRANS_TO_RUNNING;
                }
                break;
            case STATE_TRANS_TO_RUNNING:
            case STATE_RUNNING:
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
                    builder.setTitle(this.context.getString(R.string.dialog_give_up_title));
                    builder.setMessage(this.context.getString(R.string.dialog_give_up_message));
                    builder.setPositiveButton(this.context.getString(R.string.action_confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            state = STATE_WAIT;
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton(this.context.getString(R.string.action_confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
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

                    builder.setPositiveButton(this.context.getString(R.string.action_confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            end = new Date();
                            state = STATE_WAIT;
                            note = editText.getText().toString();
                            addTomatoToDatabase();
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton(this.context.getString(R.string.action_confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }
                break;
        }
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
}
