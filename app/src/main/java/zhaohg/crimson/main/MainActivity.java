package zhaohg.crimson.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
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

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.color_primary_dark));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);

        Setting.getInstance().init(this);

        new TomatoData(this).initDatabase();
        new GoalData(this).initDatabase();

        this.mainView = (MainView) findViewById(R.id.main_view);
        this.scene = new TimerScene(this, this.mainView);
        this.mainView.setScene(this.scene);
        this.initDrawer();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                scene.onTimerEvent();
            }
        }, 0, 50);
    }

    private void initDrawer() {
        NavigationView drawer = (NavigationView) findViewById(R.id.drawer);
        drawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
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
                return true;
            }
        });

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
                syncState();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                syncState();
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBarDrawerToggle.syncState();
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

}
