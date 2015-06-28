package zhaohg.crimson.setting;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

import zhaohg.crimson.R;
import zhaohg.crimson.data.DatabaseUtil;

public class Setting {

    private static final String PREFERENCE_NAME = "setting";
    private static final String KEY_PERIOD = "period";
    private static final String KEY_LAST_PERIOD = "last_period";
    private static final String KEY_VIBRATE = "vibrate";
    private static final String KEY_VIBRATED = "vibrated";
    private static final String KEY_LAST_BEGIN = "last_begin";
    private static final String KEY_LAST_FINISHED = "last_finished";
    private static final String KEY_LAST_GOAL_ID = "last_goal_id";
    private static final String KEY_DEFAULT_TITLE = "default_title";
    private static final String KEY_SYNC_TO_CALENDAR = "sync_to_calendar";
    private static final String KEY_CALENDAR_ID = "calendar_id";
    private static final String KEY_CALENDAR_NAME = "calendar_name";

    private static Setting setting;
    private Activity activity;
    private Context context;

    private int period;
    private int lastPeriod;
    private boolean vibrate;
    private boolean vibrated;
    private Date lastBegin;
    private Date lastFinished;
    private int lastGoalId;

    private String defaultTitle;
    private boolean syncToCalendar;
    private String calendarId;
    private String calendarName;

    private Setting() {
    }

    public static Setting getInstance() {
        if (setting == null) {
            setting = new Setting();
        }
        return setting;
    }

    public Activity getActivity() {
        return this.activity;
    }

    public void init(Activity activity) {
        this.activity = activity;
        init((Context)activity);
    }

    public void init(Context context) {
        this.context = context;
        SharedPreferences settings = this.getSharedPreference();
        this.period = settings.getInt(KEY_PERIOD, 25);
        this.lastPeriod = settings.getInt(KEY_LAST_PERIOD, 25);
        this.vibrate = settings.getBoolean(KEY_VIBRATE, true);
        this.vibrated = settings.getBoolean(KEY_VIBRATED, false);
        this.lastBegin = DatabaseUtil.parseDate(settings.getString(KEY_LAST_BEGIN, "###"));
        this.lastFinished = DatabaseUtil.parseDate(settings.getString(KEY_LAST_FINISHED, "###"));
        if (this.lastFinished == null) {
            this.lastFinished = new Date();
            this.setLastFinished();
        }
        this.lastGoalId = settings.getInt(KEY_LAST_GOAL_ID, -1);
        this.defaultTitle = settings.getString(KEY_DEFAULT_TITLE, context.getString(R.string.app_name));
        this.syncToCalendar = settings.getBoolean(KEY_SYNC_TO_CALENDAR, true);
        this.calendarId = settings.getString(KEY_CALENDAR_ID, "");
        this.calendarName = settings.getString(KEY_CALENDAR_NAME, context.getString(R.string.setting_sync_current_calendar_none));
    }

    private SharedPreferences getSharedPreference() {
        return this.context.getSharedPreferences(PREFERENCE_NAME, Activity.MODE_PRIVATE);
    }

    void editValue(String key, int value) {
        SharedPreferences settings = this.getSharedPreference();
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    void editValue(String key, boolean value) {
        SharedPreferences settings = this.getSharedPreference();
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    void editValue(String key, String value) {
        SharedPreferences settings = this.getSharedPreference();
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public int getPeriod() {
        return this.period;
    }

    public void setPeriod(int period) {
        this.period = period;
        this.editValue(KEY_PERIOD, period);
    }

    public int getLastPeriod() {
        return this.lastPeriod;
    }

    public void setLastPeriod(int lastPeriod) {
        this.lastPeriod = lastPeriod;
        this.editValue(KEY_LAST_PERIOD, period);
    }

    public boolean isVibrate() {
        return this.vibrate;
    }

    public void setVibrate(boolean vibrate) {
        this.vibrate = vibrate;
        this.editValue(KEY_VIBRATE, vibrate);
    }

    public boolean isVibrated() {
        return this.vibrated;
    }

    public void setVibrated(boolean vibrated) {
        this.vibrated = vibrated;
        this.editValue(KEY_VIBRATED, vibrated);
    }

    public Date getLastBegin() {
        return this.lastBegin;
    }

    public void setLastBegin(Date lastBegin) {
        this.lastBegin = lastBegin;
        if (lastBegin == null) {
            this.editValue(KEY_LAST_BEGIN, "###");
        } else {
            this.editValue(KEY_LAST_BEGIN, DatabaseUtil.formatDate(lastBegin));
        }
    }

    public Date getLastFinished() {
        return lastFinished;
    }

    public void setLastFinished() {
        lastFinished = new Date();
        this.editValue(KEY_LAST_FINISHED, DatabaseUtil.formatDate(lastFinished));
    }

    public int getLastGoalId() {
        return this.lastGoalId;
    }

    public void setLastGoalId(int lastGoalId) {
        this.lastGoalId = lastGoalId;
        editValue(KEY_LAST_GOAL_ID, lastGoalId);
    }

    public String getDefaultTitle() {
        return this.defaultTitle;
    }

    public void setDefaultTitle(String defaultTitle) {
        this.defaultTitle = defaultTitle;
        editValue(KEY_DEFAULT_TITLE, defaultTitle);
    }

    public boolean isSyncToCalendar() {
        return this.syncToCalendar;
    }

    public void setSyncToCalendar(boolean syncToCalendar) {
        this.syncToCalendar = syncToCalendar;
        editValue(KEY_SYNC_TO_CALENDAR, syncToCalendar);
    }

    public String getCalendarId() {
        return this.calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
        editValue(KEY_CALENDAR_ID, calendarId);
    }

    public String getCalendarName() {
        return this.calendarName;
    }

    public void setCalendarName(String calendarName) {
        this.calendarName = calendarName;
        editValue(KEY_CALENDAR_NAME, calendarName);
    }
}
