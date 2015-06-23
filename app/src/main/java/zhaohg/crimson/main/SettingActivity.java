package zhaohg.crimson.main;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import zhaohg.crimson.R;
import zhaohg.crimson.data.Setting;

public class SettingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        final Setting setting = Setting.getInstance();

        // Init period setting.
        final SeekBar seekBarPeriod = (SeekBar) this.findViewById(R.id.seek_bar_period);
        final TextView textViewPeriodNum = (TextView) this.findViewById(R.id.text_view_period_num);
        final TextView textViewPeriodDesc = (TextView) this.findViewById(R.id.text_view_period_desc);
        seekBarPeriod.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {
                    progress = 1;
                }
                setting.setPeriod(progress);
                textViewPeriodNum.setText(progress + getString(R.string.setting_period_num_suffix));
                if (progress < 20) {
                    textViewPeriodDesc.setText(getString(R.string.setting_period_too_short));
                } else if (progress > 60) {
                    textViewPeriodDesc.setText(getString(R.string.setting_period_too_long));
                } else {
                    textViewPeriodDesc.setText(getString(R.string.setting_period_normal));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekBarPeriod.setProgress(setting.getPeriod());

        // Init vibrate setting.
        final CheckBox checkBoxVibrate = (CheckBox) this.findViewById(R.id.check_box_vibrate);
        checkBoxVibrate.setChecked(setting.isVibrate());
        checkBoxVibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setting.setVibrate(isChecked);
            }
        });
    }
}
