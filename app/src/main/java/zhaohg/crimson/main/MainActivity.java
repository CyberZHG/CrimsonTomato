package zhaohg.crimson.main;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import zhaohg.crimson.scene.MainScene;
import zhaohg.crimson.scene.Scene;

public class MainActivity extends Activity {

    private MainView mainView;
    private Scene scene;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mainView = new MainView(this);
        this.scene = new MainScene(this, this.mainView);
        this.setContentView(this.mainView);
    }

    public class MainView extends View {
        public MainView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            scene.onDraw(canvas);
        }
    }

}
