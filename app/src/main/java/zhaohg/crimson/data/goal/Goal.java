package zhaohg.crimson.data.goal;

import java.util.Date;

public class Goal {

    private int id;
    private String title;
    private int priority;
    private int period;
    private boolean finished;
    private Date createDate;
    private Date finishedDate;
    private int tomatoSpent;
    private int minuteSpent;

    public Goal() {
        this.id = 0;
        this.title = "";
        this.priority = 0;
        this.period = 25;
        this.finished = false;
        this.createDate = new Date();
        this.finishedDate = new Date();
        this.tomatoSpent = 0;
        this.minuteSpent = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getFinishedDate() {
        return finishedDate;
    }

    public void setFinishedDate(Date finishedDate) {
        this.finishedDate = finishedDate;
    }

    public int getTomatoSpent() {
        return tomatoSpent;
    }

    public void setTomatoSpent(int tomatoSpent) {
        this.tomatoSpent = tomatoSpent;
    }

    public int getMinuteSpent() {
        return minuteSpent;
    }

    public void setMinuteSpent(int minuteSpent) {
        this.minuteSpent = minuteSpent;
    }
}
