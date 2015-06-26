package zhaohg.crimson.tomato;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.provider.CalendarContract;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Vector;

import zhaohg.crimson.R;
import zhaohg.crimson.data.DatabaseUtil;
import zhaohg.crimson.setting.Setting;

public class TomatoData {

    private static final String TABLE_NAME = "tomato_1";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_BEGIN_DATE = "begin_date";
    private static final String COLUMN_END_DATE = "end_date";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_SYNCED = "synced";

    private final Context context;

    public TomatoData(Context context) {
        this.context = context;
    }

    public void initDatabase() {
        if (!DatabaseUtil.isTableExisted(context, TABLE_NAME)) {
            SQLiteDatabase db = DatabaseUtil.getDatabase(context);
            db.execSQL(
                    "CREATE TABLE " + TABLE_NAME + " (" +
                    "    " + COLUMN_ID + "         INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "    " + COLUMN_BEGIN_DATE + " VARCHAR(20), " +
                    "    " + COLUMN_END_DATE + "   VARCHAR(20), " +
                    "    " + COLUMN_TITLE + "      VARCHAR(140), " +
                    "    " + COLUMN_LOCATION + "   VARCHAR(140), " +
                    "    " + COLUMN_SYNCED + "     BOOLEAN" +
                    "); "
            );
            db.close();
        }
    }

    public void dropDatabase() {
        DatabaseUtil.dropTable(context, TABLE_NAME);
    }

    public void addTomato(Tomato tomato) {
        SQLiteDatabase db = DatabaseUtil.getDatabase(context);
        if (tomato.getTitle().isEmpty()) {
            tomato.setTitle(context.getString(R.string.app_name));
        }
        db.execSQL(
                "INSERT INTO " + TABLE_NAME + " (" +
                        COLUMN_BEGIN_DATE + ", " +
                        COLUMN_END_DATE + ", " +
                        COLUMN_TITLE + ", " +
                        COLUMN_LOCATION + ", " +
                        COLUMN_SYNCED + ") VALUES (" +
                        "    '" + DatabaseUtil.formatDate(tomato.getBegin()) + "', " +
                        "    '" + DatabaseUtil.formatDate(tomato.getEnd()) + "', " +
                        "    '" + DatabaseUtil.sqliteEscape(tomato.getTitle()) + "', " +
                        "    '" + DatabaseUtil.sqliteEscape(tomato.getLocation()) + "', " +
                        "    0" +
                        ");"
        );
        Setting setting = Setting.getInstance();
        if (setting.isSyncToCalendar()) {
            Cursor cur = db.rawQuery("SELECT * " +
                                     "FROM " + TABLE_NAME + " " +
                                     "ORDER BY id DESC " +
                                     "LIMIT 1;", null);
            Vector<Tomato> tomatoes = getTomatoesFromCursor(cur);
            if (tomatoes.size() > 0) {
                this.syncToCalendar(tomatoes.get(0));
            }
        }
        db.close();
    }

    public void clearTomato() {
        DatabaseUtil.deleteAll(context, TABLE_NAME);
    }

    public void deleteTomato(int id) {
        DatabaseUtil.deleteById(context, TABLE_NAME, id);
    }

    Vector<Tomato> getTomatoesFromCursor(Cursor cur) {
        Vector<Tomato> tomatoes = new Vector();
        while (cur.moveToNext()) {
            Tomato tomato = new Tomato();
            tomato.setId(cur.getInt(cur.getColumnIndex(COLUMN_ID)));
            tomato.setBegin(DatabaseUtil.parseDate(cur.getString(cur.getColumnIndex(COLUMN_BEGIN_DATE))));
            tomato.setEnd(DatabaseUtil.parseDate(cur.getString(cur.getColumnIndex(COLUMN_END_DATE))));
            tomato.setTitle(cur.getString(cur.getColumnIndex(COLUMN_TITLE)));
            tomato.setLocation(cur.getString(cur.getColumnIndex(COLUMN_LOCATION)));
            tomato.setUploaded(cur.getInt(cur.getColumnIndex(COLUMN_SYNCED)) > 0);
            tomatoes.add(tomato);
        }
        cur.close();
        return tomatoes;
    }

    public Vector<Tomato> getTomatoesOnPage(int pageNum) {
        SQLiteDatabase db = DatabaseUtil.getDatabase(context);
        Cursor cur = DatabaseUtil.getPageSortedById(db, TABLE_NAME, pageNum);
        Vector<Tomato> tomatoes = getTomatoesFromCursor(cur);
        db.close();
        return tomatoes;
    }

    Vector<Tomato> getUnsyncedTomatoes() {
        SQLiteDatabase db = DatabaseUtil.getDatabase(context);
        Cursor cur = db.rawQuery("SELECT * " +
                                 "FROM " + TABLE_NAME + " " +
                                 "WHERE " + COLUMN_SYNCED + "=0;", null);
        Vector<Tomato> tomatoes = getTomatoesFromCursor(cur);
        db.close();
        return tomatoes;
    }

    public void syncToCalendar() {
        Setting setting = Setting.getInstance();
        if (setting.getCalendarId().equals("")) {
            return;
        }
        Vector<Tomato> tomatoes = this.getUnsyncedTomatoes();
        for (Tomato tomato : tomatoes) {
            syncToCalendar(tomato);
        }
    }

    void syncToCalendar(Tomato tomato) {
        Setting setting = Setting.getInstance();
        if (setting.getCalendarId().equals("")) {
            return;
        }
        Calendar beginTime = Calendar.getInstance();
        beginTime.setTime(tomato.getBegin());
        long startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.setTime(tomato.getEnd());
        long endMillis = endTime.getTimeInMillis();

        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        TimeZone timeZone = TimeZone.getDefault();
        values.put(CalendarContract.Events.CALENDAR_ID, setting.getCalendarId());
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
        values.put(CalendarContract.Events.TITLE, tomato.getTitle());
        values.put(CalendarContract.Events.DESCRIPTION, context.getString(R.string.app_name));
        values.put(CalendarContract.Events.EVENT_LOCATION, tomato.getLocation());
        values.put(CalendarContract.Events.EVENT_COLOR, Color.rgb(212, 46, 24));
        cr.insert(CalendarContract.Events.CONTENT_URI, values);

        SQLiteDatabase db = DatabaseUtil.getDatabase(context);
        db.execSQL(
                "UPDATE " + TABLE_NAME + " " +
                "SET " + COLUMN_SYNCED + "=1 " +
                "WHERE id=" + tomato.getId() + "; "
        );
        db.close();
    }

    String addCsvEscape(String s) {
        String ret = "\"";
        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) == '"' || s.charAt(i) == '\\') {
                ret += "\\";
            }
            ret += s.charAt(i);
        }
        ret += "\"";
        return ret;
    }

    public void exportToCsv(FileOutputStream output) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yy/MM/dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss aa");
        OutputStreamWriter writer = new OutputStreamWriter(output);
        writer.write("Subject,Start Date,Start Time,End Date,End Time,All Day Event,Description,Location,Private" + "\n");
        for (int pageNum = 0; ; ++pageNum) {
            Vector<Tomato> tomatoes = this.getTomatoesOnPage(pageNum);
            if (tomatoes.size() == 0) {
                break;
            }
            for (Tomato tomato : tomatoes) {
                writer.write(addCsvEscape(tomato.getTitle()) + ",");
                writer.write(dateFormat.format(tomato.getBegin()) + ",");
                writer.write(timeFormat.format(tomato.getBegin()) + ",");
                writer.write(dateFormat.format(tomato.getEnd()) + ",");
                writer.write(timeFormat.format(tomato.getEnd()) + ",");
                writer.write("False,");
                writer.write(addCsvEscape(context.getString(R.string.app_name)) + ",");
                writer.write(addCsvEscape(tomato.getLocation()) + ",");
                writer.write("True\n");
            }
        }
        writer.close();
    }

}
