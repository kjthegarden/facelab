package snu.facelab.model;

import java.io.Serializable;

public class Name implements Serializable {
    int id;
    String name;

    public Name() {

    }

    public Name(String name) {
        this.name = name;
    }

    // setters and getters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
