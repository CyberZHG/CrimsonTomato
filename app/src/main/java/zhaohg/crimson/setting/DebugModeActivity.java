package zhaohg.crimson.setting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import zhaohg.crimson.R;

public class DebugModeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_mode);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final TextView textViewDebugInfo = (TextView) findViewById(R.id.text_view_debug_info);

        // Append setting information.
        Setting setting = Setting.getInstance();
        textViewDebugInfo.append("Setting: \n");
        textViewDebugInfo.append("Last Period: " + setting.getLastPeriod() + "\n");
        textViewDebugInfo.append("Vibrate: " + setting.isVibrate() + "\n");
        textViewDebugInfo.append("Vibrated: " + setting.isVibrated() + "\n");
        textViewDebugInfo.append("Last Begin: " + setting.getLastBegin() + "\n");
        textViewDebugInfo.append("Last Finished: " + setting.getLastFinished() + "\n");
        textViewDebugInfo.append("\n");
    }
}
