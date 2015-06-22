package zhaohg.crimson.main;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import java.util.Timer;
import java.util.TimerTask;

import zhaohg.crimson.data.TomatoData;
import zhaohg.crimson.scene.MainScene;
import zhaohg.crimson.scene.Scene;

public class MainActivity extends Activity {

    private MainView mainView;
    private Scene scene;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TomatoData tomatoData = new TomatoData(this);
        tomatoData.initDatabase();

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mainView = new MainView(this);
        this.scene = new MainScene(this, this.mainView);
        this.setContentView(this.mainView);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                scene.onTimeEvent();
            }
        }, 0, 50);
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
