package snu.facelab;

import java.io.Serializable;

public class Person implements Serializable { // 자바 빈 (java Bean)
    String mainImg; // 사진 - res/drawable

    String name = "";

    // 생성자
    public Person(String mainImg, String name) {
        this.mainImg = mainImg;
        this.name = name;
        }

    public Person(String name) {
        this.name = name;
    }

    public Person() {}// 기존 코드와 호환을 위해서 생성자 작업시 기본생성자도 추가

    public String getMainImg() {
        return mainImg;
    }

//    public int [] getImgs() {
//        return imgs;
//    }
}
