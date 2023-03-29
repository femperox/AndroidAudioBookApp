package com.example.myapplication.classes;

public class BookRecommendItem
{
    private int mPicture;
    private int mId;
    private String mTitle;
    private String mReader;

    public int getId() {return mId;}
    public int getPicture() {
        return mPicture;
    }
    public String getTitle() {
        return mTitle;
    }
    public String getReader() {
        return mReader;
    }

    public BookRecommendItem(int id, int picture, String title, String reader)
    {   mId = id;
        mPicture = picture;
        mTitle = title;
        mReader = reader;
    }
}
