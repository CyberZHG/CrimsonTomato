package zhaohg.crimson.goal;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import zhaohg.crimson.R;
import zhaohg.crimson.main.MainActivity;
import zhaohg.crimson.setting.Setting;
import zhaohg.crimson.setting.SettingActivity;

public class GoalActivity extends AppCompatActivity {

    public static final String KEY_GOAL_ID = "goal_id";

    private Goal goal;

    private Button buttonStartNow;
    private EditText editTextTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        final int goalId = getIntent().getIntExtra(KEY_GOAL_ID, -1);
        final GoalData goalData = new GoalData(this);
        goal = goalData.getGoal(goalId);
        if (goal == null) {
            this.finish();
            return;
        }

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        final Setting setting = Setting.getInstance();

        buttonStartNow = (Button) findViewById(R.id.button_start_now);
        if (setting.getLastBegin() != null) {
            buttonStartNow.setVisibility(View.INVISIBLE);
        }
        buttonStartNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Setting setting = Setting.getInstance();
                setting.setLastGoalId(goal.getId());
                Intent intent = new Intent();
                intent.setClass(GoalActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        editTextTitle = (EditText) findViewById(R.id.edit_text_title);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_goals, menu);
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
