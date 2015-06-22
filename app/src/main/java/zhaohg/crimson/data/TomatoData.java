package zhaohg.crimson.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class TomatoData {

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
                    "    note       VARCHAR(140), " +
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
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        db.execSQL(
                "INSERT INTO tomato (begin_time, end_time, note, location, uploaded) VALUES (" +
                "    '" + format.format(tomato.getBegin()) + "', " +
                "    '" + format.format(tomato.getEnd()) + "', " +
                "    '" + tomato.getNote() + "', " +
                "    '" + tomato.getLocation() + "', " +
                "    0" +
                ");"
        );
        db.close();
    }

    public void clearTomato() {
        SQLiteDatabase db = getDatabase();
        db.execSQL("DELETE FROM tomato;");
        db.close();
    }

    public Vector<Tomato> getAllTomatoes() {
        SQLiteDatabase db = getDatabase();
        Vector<Tomato> tomatoes = new Vector();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Cursor cur = db.rawQuery("SELECT * FROM tomato;", null);
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
            tomato.setNote(cur.getString(cur.getColumnIndex("note")));
            tomato.setLocation(cur.getString(cur.getColumnIndex("location")));
            tomato.setUploaded(cur.getInt(cur.getColumnIndex("uploaded")) > 0);
            tomatoes.add(tomato);
        }
        cur.close();
        db.close();
        return tomatoes;
    }

}
