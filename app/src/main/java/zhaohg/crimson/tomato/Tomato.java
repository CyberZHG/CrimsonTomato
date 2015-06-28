package zhaohg.crimson.tomato;

import android.content.Context;

import java.util.Date;

import zhaohg.crimson.R;
import zhaohg.crimson.goal.Goal;

public class Tomato {

    private int id;
    private Date begin;
    private Date end;
    private String title;
    private String location;
    private String description;
    private boolean uploaded;

    public Tomato() {
        this.id = 0;
        this.begin = new Date();
        this.end = new Date();
        this.title = "";
        this.location = "";
        this.uploaded = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDescription(Context context, Goal goal) {
        description = "";
        description += context.getString(R.string.goal_item_text_tomato_spent);
        description += goal.getTomatoSpent();
        description += " ";
        description += context.getString(R.string.goal_item_text_time_spent);
        description += goal.getFormattedMinuteSpent(context);
    }

    public boolean isSynced() {
        return uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }

}
