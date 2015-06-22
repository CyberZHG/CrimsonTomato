package zhaohg.crimson.data;

import java.util.Date;

public class Tomato {

    private int id;
    private Date begin;
    private Date end;
    private String note;
    private String location;
    private boolean uploaded;

    public Tomato() {
        this.id = 0;
        this.begin = new Date();
        this.end = new Date();
        this.note = "";
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }

}
