package snu.facelab;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import snu.facelab.model.Picture;

public class PersonGrid implements Serializable { // 자바 빈 (java Bean)
    Integer date;
    List<Picture> photos;

    // 생성자가 있으면 객체 생성시 편리하다
    public PersonGrid(Integer date, List<Picture> photos) {
        this.date = date;
        this.photos = photos;
    }
    public PersonGrid() {}// 기존 코드와 호환을 위해서 생성자 작업시 기본생성자도 추가

    public void setDate(Integer date) {
        this.date = date;
    }

    public void setPhotos(List<Picture> photos) {
        this.photos = photos;
    }

    public Integer getDate() {
        return date;
    }

    public List<Picture> getPhotos() {
        return photos;
    }
}