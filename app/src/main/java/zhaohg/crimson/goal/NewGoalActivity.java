package zhaohg.crimson.goal;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import zhaohg.crimson.R;

public class NewGoalActivity extends AppCompatActivity {

    private EditText editTextTitle;
    private SeekBar seekBarPeriod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_goal);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        editTextTitle = (EditText) this.findViewById(R.id.edit_text_title);

        seekBarPeriod = (SeekBar) this.findViewById(R.id.seek_bar_period);
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
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_new_goal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.menu_item_new_goal:
                this.createNewGoal();
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createNewGoal() {
        Goal goal = new Goal();
        goal.setTitle(editTextTitle.getText().toString());
        goal.setPeriod(seekBarPeriod.getProgress() > 0 ? seekBarPeriod.getProgress() : 1);
        GoalData goalData = new GoalData(this);
        goalData.addGoal(goal);
    }
}
