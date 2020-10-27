package com.shoaib.ecommerce.Model;

public class Home {

    private int mImage;
    private String mTitle;


    public Home(int mImage,String mTitle) {

        this.mImage = mImage;
        this.mTitle = mTitle;
    }



    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public int getmImage() {
        return mImage;
    }

    public void setmImage(int mImage) {
        this.mImage = mImage;
    }
}
