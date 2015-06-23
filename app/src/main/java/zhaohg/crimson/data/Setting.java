package zhaohg.crimson.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import zhaohg.crimson.R;

public class Setting {

    private static final String PREFERENCE_NAME = "setting";
    private static final String KEY_PERIOD = "period";
    private static final String KEY_VIBRATE = "vibrate";
    private static final String KEY_LAST_BEGIN = "last_begin";
    private static final String KEY_DEFAULT_TITLE = "default_title";
    private static final String KEY_SYNC_TO_GOOGLE_CALENDAR = "sync_to_google_calendar";

    private static Setting setting;
    private Context context;

    private int period;
    private boolean vibrate;
    private Date lastBegin;

    private String defaultTitle;
    private boolean syncToGoogleCalendar;

    private Setting() {
    }

    public static Setting getInstance() {
        if (setting == null) {
            setting = new Setting();
        }
        return setting;
    }

    public void init(Context context) {
        this.context = context;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SharedPreferences settings = this.getSharedPreference();
        this.period = settings.getInt(KEY_PERIOD, 25);
        this.vibrate = settings.getBoolean(KEY_VIBRATE, true);
        try {
            this.lastBegin = format.parse(settings.getString(KEY_LAST_BEGIN, "###"));
        } catch (ParseException e) {
            this.lastBegin = null;
        }

        this.defaultTitle = settings.getString(KEY_DEFAULT_TITLE, context.getString(R.string.app_name));
        this.syncToGoogleCalendar = settings.getBoolean(KEY_SYNC_TO_GOOGLE_CALENDAR, true);
    }

    private SharedPreferences getSharedPreference() {
        return this.context.getSharedPreferences(PREFERENCE_NAME, Activity.MODE_PRIVATE);
    }

    public void editValue(String key, int value) {
        SharedPreferences settings = this.getSharedPreference();
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void editValue(String key, boolean value) {
        SharedPreferences settings = this.getSharedPreference();
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void editValue(String key, String value) {
        SharedPreferences settings = this.getSharedPreference();
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public int getPeriod() {
        return this.period;
    }

    public void setPeriod(int period) {
        this.period = period;
        this.editValue(KEY_PERIOD, period);
    }

    public boolean isVibrate() {
        return this.vibrate;
    }

    public void setVibrate(boolean vibrate) {
        this.vibrate = vibrate;
        this.editValue(KEY_VIBRATE, vibrate);
    }

    public Date getLastBegin() {
        return this.lastBegin;
    }

    public void setLastBegin(Date lastBegin) {
        this.lastBegin = lastBegin;
        if (lastBegin == null) {
            this.editValue(KEY_LAST_BEGIN, "###");
        } else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            this.editValue(KEY_LAST_BEGIN, format.format(lastBegin));
        }
    }

    public String getDefaultTitle() {
        return this.defaultTitle;
    }

    public void setDefaultTitle(String defaultTitle) {
        this.defaultTitle = defaultTitle;
        editValue(KEY_DEFAULT_TITLE, defaultTitle);
    }

    public boolean isSyncToGoogleCalendar() {
        return this.syncToGoogleCalendar;
    }

    public void setSyncToGoogleCalendar(boolean syncToGoogleCalendar) {
        this.syncToGoogleCalendar = syncToGoogleCalendar;
        editValue(KEY_SYNC_TO_GOOGLE_CALENDAR, syncToGoogleCalendar);
    }
}
