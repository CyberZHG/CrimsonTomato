package zhaohg.crimson.data.goal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Vector;

import zhaohg.crimson.data.DatabaseUtil;

public class GoalData {

    private static final String TABLE_NAME = "goal_0";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_PRIORITY = "priority";
    private static final String COLUMN_PERIOD = "period";
    private static final String COLUMN_FINISHED = "finished";
    private static final String COLUMN_CREATE_DATE = "create_date";
    private static final String COLUMN_FINISHED_DATE = "finished_date";
    private static final String COLUMN_TOMATO_SPENT = "tomato_spent";
    private static final String COLUMN_MINUTE_SPENT = "minute_spent";

    private final Context context;

    public GoalData(Context context) {
        this.context = context;
    }

    public void initDatabase() {
        if (!DatabaseUtil.isTableExisted(context, TABLE_NAME)) {
            SQLiteDatabase db = DatabaseUtil.getDatabase(context);
            db.execSQL(
                    "CREATE TABLE " + TABLE_NAME + " (" +
                    "    " + COLUMN_ID + "            INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "    " + COLUMN_TITLE + "         VARCHAR(140), " +
                    "    " + COLUMN_PRIORITY + "      INTEGER, " +
                    "    " + COLUMN_PERIOD + "        INTEGER, " +
                    "    " + COLUMN_FINISHED + "      BOOLEAN, " +
                    "    " + COLUMN_CREATE_DATE + "   VARCHAR(20), " +
                    "    " + COLUMN_FINISHED_DATE + " VARCHAR(20), " +
                    "    " + COLUMN_TOMATO_SPENT + "  INTEGER, " +
                    "    " + COLUMN_MINUTE_SPENT + "  INTEGER" +
                    "); "
            );
            db.close();
        }
    }

    public void dropDatabase() {
        DatabaseUtil.dropTable(context, TABLE_NAME);
    }

    public void addGoal(Goal goal) {
        SQLiteDatabase db = DatabaseUtil.getDatabase(context);
        db.execSQL(
                "INSERT INTO " + TABLE_NAME + " (" +
                COLUMN_TITLE + ", " +
                COLUMN_PRIORITY + ", " +
                COLUMN_PERIOD + ", " +
                COLUMN_FINISHED + ", " +
                COLUMN_CREATE_DATE + ", " +
                COLUMN_FINISHED_DATE + ", " +
                COLUMN_TOMATO_SPENT + ", " +
                COLUMN_MINUTE_SPENT + ") VALUES (" +
                "    '" + DatabaseUtil.sqliteEscape(goal.getTitle()) + "', " +
                "    " + goal.getPriority() + ", " +
                "    " + goal.getPeriod() + ", " +
                "    0, " +
                "    '" + DatabaseUtil.formatDate(goal.getCreateDate()) + "', " +
                "    '" + DatabaseUtil.formatDate(goal.getFinishedDate()) + "', " +
                "    " + goal.getTomatoSpent() + ", " +
                "    " + goal.getMinuteSpent() + "" +
                ");"
        );
    }

    public void clearGoal() {
        DatabaseUtil.deleteAll(context, TABLE_NAME);
    }

    public void deleteGoal(int id) {
        DatabaseUtil.deleteById(context, TABLE_NAME, id);
    }

    Vector<Goal> getGoalsFromCursor(Cursor cur) {
        Vector<Goal> goals = new Vector();
        while (cur.moveToNext()) {
            Goal goal = new Goal();
            goal.setId(cur.getInt(cur.getColumnIndex(COLUMN_ID)));
            goal.setTitle(cur.getString(cur.getColumnIndex(COLUMN_TITLE)));
            goal.setPriority(cur.getInt(cur.getColumnIndex(COLUMN_PRIORITY)));
            goal.setPeriod(cur.getInt(cur.getColumnIndex(COLUMN_PERIOD)));
            goal.setFinished(cur.getInt(cur.getColumnIndex(COLUMN_FINISHED)) > 0);
            goal.setCreateDate(DatabaseUtil.parseDate(cur.getString(cur.getColumnIndex(COLUMN_CREATE_DATE))));
            goal.setCreateDate(DatabaseUtil.parseDate(cur.getString(cur.getColumnIndex(COLUMN_FINISHED_DATE))));
            goal.setTomatoSpent(cur.getInt(cur.getColumnIndex(COLUMN_TOMATO_SPENT)));
            goal.setMinuteSpent(cur.getInt(cur.getColumnIndex(COLUMN_MINUTE_SPENT)));
            goals.add(goal);
        }
        cur.close();
        return goals;
    }

    private Vector<Goal> getAllGoals() {
        SQLiteDatabase db = DatabaseUtil.getDatabase(context);
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_NAME + ";", null);
        Vector<Goal> goals = getGoalsFromCursor(cur);
        db.close();
        return goals;
    }

    private Vector<Goal> getUnfinishedGoals() {
        SQLiteDatabase db = DatabaseUtil.getDatabase(context);
        Cursor cur = db.rawQuery(
                "SELECT * " +
                "FROM " + TABLE_NAME + "" +
                "WHERE " + COLUMN_FINISHED + "=0;", null);
        Vector<Goal> goals = getGoalsFromCursor(cur);
        db.close();
        return goals;
    }

    private Vector<Goal> getFinishedGoals() {
        SQLiteDatabase db = DatabaseUtil.getDatabase(context);
        Cursor cur = db.rawQuery(
                "SELECT * " +
                "FROM " + TABLE_NAME + "" +
                "WHERE " + COLUMN_FINISHED + "=0;", null);
        Vector<Goal> goals = getGoalsFromCursor(cur);
        db.close();
        return goals;
    }

    public Vector<Goal> getTomatoesOnPage(int pageNum) {
        SQLiteDatabase db = DatabaseUtil.getDatabase(context);
        Cursor cur = DatabaseUtil.getPageSortedById(db, TABLE_NAME, pageNum);
        Vector<Goal> goals = getGoalsFromCursor(cur);
        db.close();
        return goals;
    }

}
