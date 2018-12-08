package snu.facelab.model;

import java.io.Serializable;

public class Picture implements Serializable {
    int id;
    String path;
    int date;
    int month;
    long dateTime;
    boolean checked = false;

    public Picture() {

    }

    public Picture(String path, int date, long date_time) {
        this.path = path;
        this.date = date;
        this.month = date/100;
        this.dateTime = date_time;
    }

    // setters and getters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getDate() { return date; }

    public void setDate(int date) { this.date = date; }

    public int getMonth() { return month; }

    public void setMonth(int month) { this.month = month; }

    public void setChecked(boolean b) { this.checked = b; }

    public long getDateTime() { return dateTime; }

    public void setDateTime(long dateTime) { this.dateTime = dateTime; }

    public boolean getChecked() { return this.checked; }


}
