package zhaohg.crimson.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import zhaohg.crimson.R;
import zhaohg.crimson.data.Setting;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        final Setting setting = Setting.getInstance();
        setting.init(this);

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

        // Init Sync setting.
        final EditText editTextDefaultTitle = (EditText) this.findViewById(R.id.edit_text_title);
        editTextDefaultTitle.setText(setting.getDefaultTitle());
        editTextDefaultTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                setting.setDefaultTitle(s.toString());
            }
        });
        final CheckBox checkBoxSyncToGoogleCalendar = (CheckBox) this.findViewById(R.id.check_box_sync_to_calendar);
        checkBoxSyncToGoogleCalendar.setChecked(setting.isSyncToCalendar());
        checkBoxSyncToGoogleCalendar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setting.setSyncToCalendar(isChecked);
            }
        });

        // Init calendar setting.
        final Button buttonChooseCalendar = (Button) findViewById(R.id.button_choose_calendar);
        buttonChooseCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SettingActivity.this, ChooseCalendarActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        final Setting setting = Setting.getInstance();
        setting.init(this);
        final TextView textViewCurrentCalendar = (TextView) findViewById(R.id.text_view_current_calendar);
        textViewCurrentCalendar.setText(setting.getCalendarName());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
