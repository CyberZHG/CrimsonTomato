package zhaohg.crimson.goal;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import zhaohg.crimson.R;
import zhaohg.crimson.main.MainActivity;
import zhaohg.crimson.setting.Setting;

public class GoalActivity extends AppCompatActivity {

    public static final String KEY_GOAL_ID = "goal_id";

    private Goal goal;
    private GoalData goalData;
    private final Setting setting = Setting.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        final int goalId = getIntent().getIntExtra(KEY_GOAL_ID, -1);
        goalData = new GoalData(this);
        goal = goalData.getGoal(goalId);
        if (goal == null) {
            this.finish();
            return;
        }

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        this.initTitle();
        this.initPeriod();
        this.initPriority();
        this.initSpent();
        this.initStartNow();
        this.initDelete();
    }

    private void initTitle() {
        final EditText editTextTitle = (EditText) findViewById(R.id.edit_text_title);
        editTextTitle.setText(goal.getTitle());
        editTextTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                goalData.updateTitle(goal, editTextTitle.getText().toString());
            }
        });
    }

    private void initPeriod() {
        final SeekBar seekBarPeriod = (SeekBar) findViewById(R.id.seek_bar_period);
        final TextView textViewPeriodNum = (TextView) findViewById(R.id.text_view_period_num);
        seekBarPeriod.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {
                    progress = 1;
                }
                textViewPeriodNum.setText(progress + getString(R.string.setting_period_num_suffix));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                if (progress == 0) {
                    progress = 1;
                }
                goalData.updatePeriod(goal, progress);
            }
        });
        seekBarPeriod.setProgress(goal.getPeriod());
    }

    private void initPriority() {
        final PriorityGroupView priorityGroup = (PriorityGroupView) findViewById(R.id.priority_group);
        priorityGroup.setPriority(goal.getPriority());
        priorityGroup.setOnPriorityChangedListener(new PriorityGroupView.OnPriorityChangedListener() {
            @Override
            public void onPriorityChanged(int priority) {
                goalData.updatePriority(goal, priority);
            }
        });
    }

    private void initSpent() {
        final TextView textViewTomatoSpent = (TextView) findViewById(R.id.text_view_tomato_spent);
        final TextView textViewTimeSpent = (TextView) findViewById(R.id.text_view_time_spent);
        textViewTomatoSpent.setText(getString(R.string.goal_item_text_tomato_spent) + " " + goal.getTomatoSpent());
        textViewTimeSpent.setText(getString(R.string.goal_item_text_time_spent) + " " + goal.getFormattedMinuteSpent(this));
    }

    private void initStartNow() {
        final Button buttonStartNow = (Button) findViewById(R.id.button_start_now);
        if (setting.getLastBegin() != null) {
            buttonStartNow.setVisibility(View.GONE);
        }
        buttonStartNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Setting setting = Setting.getInstance();
                setting.setLastGoalId(goal.getId());
                setting.setFastStart(true);
                Intent intent = new Intent();
                intent.setClass(GoalActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initDelete() {
        final Button buttonDelete = (Button) findViewById(R.id.button_delete);
        if (setting.getLastGoalId() == goal.getId()) {
            buttonDelete.setVisibility(View.GONE);
        }
        final Activity activity = this;
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(getString(R.string.dialog_delete_title));
                builder.setMessage(getString(R.string.dialog_delete_message));
                builder.setNegativeButton(getString(R.string.action_cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.setPositiveButton(getString(R.string.action_confirm),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                goalData.deleteGoal(goal.getId());
                                dialog.dismiss();
                                finish();
                            }
                        });
                builder.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_goal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
