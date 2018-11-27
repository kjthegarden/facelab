package snu.facelab.model;

public class Picture {
    int id;
    String path;
    int date;
    long dateTime;

    public Picture() {

    }

    public Picture(String path, int date, long date_time) {
        this.path = path;
        this.date = date;
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

    public long getDateTime() { return dateTime; }

    public void setDateTime(long dateTime) { this.dateTime = dateTime; }

}
