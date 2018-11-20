package snu.facelab;

import java.io.Serializable;

public class Photo implements Serializable { // 자바 빈 (java Bean)
    String img; // 사진

    // 생성자
    public Photo(String img) {
        this.img = img;
    }
    public Photo() {}// 기존 코드와 호환을 위해서 생성자 작업시 기본생성자도 추가

    public String getImg() {
        return img;
    }

}
