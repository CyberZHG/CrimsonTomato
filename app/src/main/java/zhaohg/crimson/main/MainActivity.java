package zhaohg.crimson.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

import zhaohg.crimson.R;
import zhaohg.crimson.goal.GoalActivity;
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

        Setting.getInstance().init(this);

        new TomatoData(this).initDatabase();
        new GoalData(this).initDatabase();

        this.mainView = new MainView(this);
        this.scene = new TimerScene(this, this.mainView);
        this.setContentView(this.mainView);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                scene.onTimeEvent();
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
                    intent.setClass(MainActivity.this, GoalActivity.class);
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

    public class MainView extends View {
        public MainView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            scene.onDraw(canvas);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            return scene.onTouchEvent(event);
        }
    }

}
