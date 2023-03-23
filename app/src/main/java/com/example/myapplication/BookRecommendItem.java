package com.example.myapplication;

public class BookRecommendItem
{
    private int mPicture;
    private String mTitle;
    private String mReader;

    int getPicture() {
        return mPicture;
    }
    String getTitle() {
        return mTitle;
    }
    String getReader() {
        return mReader;
    }

    BookRecommendItem(int picture, String title, String reader)
    {   mPicture = picture;
        mTitle = title;
        mReader = reader;
    }
}
