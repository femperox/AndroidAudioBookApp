package com.example.myapplication.classes;

public class BookRecommendItem
{
    private int mPicture;
    private String mTitle;
    private String mReader;

    public int getPicture() {
        return mPicture;
    }
    public String getTitle() {
        return mTitle;
    }
    public String getReader() {
        return mReader;
    }

    public BookRecommendItem(int picture, String title, String reader)
    {   mPicture = picture;
        mTitle = title;
        mReader = reader;
    }
}
