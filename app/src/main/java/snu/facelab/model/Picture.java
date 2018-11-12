package snu.facelab.model;

public class Picture {
    int id;
    String path;

    public Picture() {

    }

    public Picture(String path) {
        this.path = path;
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
}
