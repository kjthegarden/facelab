package snu.facelab;

import java.io.Serializable;

public class Photo implements Serializable { // 자바 빈 (java Bean)
    int img; // 사진 - res/drawable
    String info = "";

    // 생성자가 있으면 객체 생성시 편리하다
    public Photo(int img, String info) {
        this.img = img;
        this.info = info;
    }
    public Photo() {}// 기존 코드와 호환을 위해서 생성자 작업시 기본생성자도 추가

    public int getImg() {
        return img;
    }

    public String getInfo() {
        return info;
    }
}
