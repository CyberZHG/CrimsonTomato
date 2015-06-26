package zhaohg.crimson.goal;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import zhaohg.crimson.R;

public class NewGoalActivity extends AppCompatActivity {

    private EditText editTextTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_goal);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        editTextTitle = (EditText) this.findViewById(R.id.edit_text_title);
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
        GoalData goalData = new GoalData(this);
        goalData.addGoal(goal);
    }
}
