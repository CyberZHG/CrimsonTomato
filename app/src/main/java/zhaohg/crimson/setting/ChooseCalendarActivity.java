package zhaohg.crimson.setting;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import zhaohg.crimson.R;

public class ChooseCalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_calendar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(Uri.parse("content://com.android.calendar/calendars"),
                (new String[] { "_id", "name"}), null, null, null);
        LinearLayout layout = (LinearLayout) findViewById(R.id.linear_layout);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String _id = cursor.getString(0);
                String name = cursor.getString(1);
                CalendarItem item = new CalendarItem(this);
                item.setCalendarId(_id);
                item.setText(name);
                item.setTextSize(24.0f);
                layout.addView(item);
            }
            cursor.close();
        }
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

    private class CalendarItem extends TextView {

        private String calendarId;

        public CalendarItem(Context context) {
            super(context);
            this.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Setting setting = Setting.getInstance();
                    setting.setCalendarId(calendarId);
                    setting.setCalendarName(getText().toString());
                    finish();
                }
            });
        }

        public void setCalendarId(String id) {
            this.calendarId = id;
        }
    }

}
