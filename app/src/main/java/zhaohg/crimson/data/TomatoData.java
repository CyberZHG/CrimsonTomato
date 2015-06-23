package zhaohg.crimson.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Vector;

import zhaohg.crimson.R;

public class TomatoData {

    public static int PAGE_SIZE = 50;

    private Context context;

    public TomatoData(Context context) {
        this.context = context;
    }

    private SQLiteDatabase getDatabase() {
        return this.context.openOrCreateDatabase("tomato.db", Context.MODE_PRIVATE, null);
    }

    public void initDatabase() {
        SQLiteDatabase db = getDatabase();
        Cursor cursor = db.rawQuery("SELECT tbl_name " +
                                    "FROM   sqlite_master " +
                                    "WHERE  tbl_name='tomato';", null);
        if (cursor == null || cursor.getCount() == 0) {
            db.execSQL(
                    "CREATE TABLE tomato (" +
                    "    id         INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "    begin_time VARCHAR(20), " +
                    "    end_time   VARCHAR(20), " +
                    "    title      VARCHAR(140), " +
                    "    location   VARCHAR(140), " +
                    "    uploaded   BOOLEAN" +
                    "); "
            );
        }
        cursor.close();
        db.close();
    }

    public void dropDatabase() {
        SQLiteDatabase db = getDatabase();
        db.execSQL("DROP TABLE tomato;");
        db.close();
    }

    public void addTomato(Tomato tomato) {
        SQLiteDatabase db = getDatabase();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        db.execSQL(
                "INSERT INTO tomato (begin_time, end_time, title, location, uploaded) VALUES (" +
                "    '" + format.format(tomato.getBegin()) + "', " +
                "    '" + format.format(tomato.getEnd()) + "', " +
                "    '" + tomato.getTitle() + "', " +
                "    '" + tomato.getLocation() + "', " +
                "    0" +
                ");"
        );
        db.close();
        Setting setting = Setting.getInstance();
        if (setting.isSyncToCalendar()) {
            this.syncToCalendar();
        }
    }

    public void clearTomato() {
        SQLiteDatabase db = getDatabase();
        db.execSQL("DELETE FROM tomato;");
        db.close();
    }

    public Vector<Tomato> getTomatoesFromCursor(Cursor cur) {
        Vector<Tomato> tomatoes = new Vector();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        while (cur.moveToNext()) {
            Tomato tomato = new Tomato();
            tomato.setId(cur.getInt(cur.getColumnIndex("id")));
            try {
                tomato.setBegin(format.parse(cur.getString(cur.getColumnIndex("begin_time"))));
                tomato.setEnd(format.parse(cur.getString(cur.getColumnIndex("end_time"))));
            } catch (ParseException e) {
                e.printStackTrace();
                continue;
            }
            tomato.setTitle(cur.getString(cur.getColumnIndex("title")));
            tomato.setLocation(cur.getString(cur.getColumnIndex("location")));
            tomato.setUploaded(cur.getInt(cur.getColumnIndex("uploaded")) > 0);
            tomatoes.add(tomato);
        }
        cur.close();
        return tomatoes;
    }

    public Vector<Tomato> getAllTomatoes() {
        SQLiteDatabase db = getDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM tomato;", null);
        Vector<Tomato> tomatoes = getTomatoesFromCursor(cur);
        db.close();
        return tomatoes;
    }

    public Vector<Tomato> getTomatoesOnPage(int pageNum) {
        SQLiteDatabase db = getDatabase();
        Cursor cur = db.rawQuery("SELECT * " +
                                 "FROM tomato " +
                                 "ORDER BY id DESC " +
                                 "LIMIT " + PAGE_SIZE + " " +
                                 "OFFSET " + (PAGE_SIZE * pageNum) + ";", null);
        Vector<Tomato> tomatoes = getTomatoesFromCursor(cur);
        db.close();
        return tomatoes;
    }

    public Vector<Tomato> getUnsyncedTomatoes() {
        SQLiteDatabase db = getDatabase();
        Cursor cur = db.rawQuery("SELECT * " +
                                 "FROM tomato " +
                                 "WHERE uploaded=0;", null);
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

    public void syncToCalendar(Tomato tomato) {
        Setting setting = Setting.getInstance();
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

        SQLiteDatabase db = getDatabase();
        db.execSQL(
                "UPDATE tomato " +
                "SET uploaded=1 " +
                "WHERE id=" + tomato.getId() + "; "
        );
        db.close();
    }

}
