package zhaohg.crimson.data;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Vector;

import zhaohg.crimson.R;

public class TomatoData {

    public static final int PAGE_SIZE = 50;

    private final Context context;

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
        if (cursor != null) {
            cursor.close();
        }
        db.close();
    }

    public void dropDatabase() {
        SQLiteDatabase db = getDatabase();
        db.execSQL("DROP TABLE tomato;");
        db.close();
    }

    private static String sqliteEscape(String keyWord){
        keyWord = keyWord.replace("/", "//");
        keyWord = keyWord.replace("'", "''");
        keyWord = keyWord.replace("[", "/[");
        keyWord = keyWord.replace("]", "/]");
        keyWord = keyWord.replace("%", "/%");
        keyWord = keyWord.replace("&","/&");
        keyWord = keyWord.replace("_", "/_");
        keyWord = keyWord.replace("(", "/(");
        keyWord = keyWord.replace(")", "/)");
        return keyWord;
    }

    public void addTomato(Tomato tomato) {
        SQLiteDatabase db = getDatabase();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (tomato.getTitle().isEmpty()) {
            tomato.setTitle(context.getString(R.string.app_name));
        }
        db.execSQL(
                "INSERT INTO tomato (begin_time, end_time, title, location, uploaded) VALUES (" +
                "    '" + format.format(tomato.getBegin()) + "', " +
                "    '" + format.format(tomato.getEnd()) + "', " +
                "    '" + sqliteEscape(tomato.getTitle()) + "', " +
                "    '" + sqliteEscape(tomato.getLocation()) + "', " +
                "    0" +
                ");"
        );
        Setting setting = Setting.getInstance();
        if (setting.isSyncToCalendar()) {
            Cursor cur = db.rawQuery("SELECT * " +
                                     "FROM tomato " +
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
        SQLiteDatabase db = getDatabase();
        db.execSQL("DELETE FROM tomato;");
        db.close();
    }

    public void deleteTomato(int id) {
        SQLiteDatabase db = getDatabase();
        db.execSQL("DELETE FROM tomato " +
                   "WHERE id=" + id + ";");
        db.close();
    }

    Vector<Tomato> getTomatoesFromCursor(Cursor cur) {
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

    Vector<Tomato> getUnsyncedTomatoes() {
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

        SQLiteDatabase db = getDatabase();
        db.execSQL(
                "UPDATE tomato " +
                "SET uploaded=1 " +
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
