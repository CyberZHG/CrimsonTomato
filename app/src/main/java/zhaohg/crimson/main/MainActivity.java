package zhaohg.crimson.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.Timer;
import java.util.TimerTask;

import zhaohg.crimson.R;
import zhaohg.crimson.goal.GoalsActivity;
import zhaohg.crimson.goal.GoalData;
import zhaohg.crimson.setting.Setting;
import zhaohg.crimson.setting.SettingActivity;
import zhaohg.crimson.tomato.HistoryActivity;
import zhaohg.crimson.tomato.TomatoData;
import zhaohg.crimson.timer.TimerScene;
import zhaohg.crimson.scene.Scene;

public class MainActivity extends AppCompatActivity {

    private MainView mainView;
    private Scene scene;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);

        Setting.getInstance().init(this);

        new TomatoData(this).initDatabase();
        new GoalData(this).initDatabase();

        this.mainView = (MainView) findViewById(R.id.main_view);
        this.scene = new TimerScene(this, this.mainView);
        this.mainView.setScene(this.scene);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                scene.onTimerEvent();
            }
        }, 0, 50);
    }

    @Override
    protected void onResume() {
        super.onResume();
        scene.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scene.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_goal: {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, GoalsActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.menu_item_history: {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, HistoryActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.menu_item_setting: {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, SettingActivity.class);
                    startActivity(intent);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
