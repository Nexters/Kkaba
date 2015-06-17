package com.teamnexters.kkaba.client.image;

public class Badge {

    //나중에 뜻 추가 하기
    private String name;
    private int image;

    public Badge(String name, int imageId) {
      this.name = name;
      this.image = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

}
