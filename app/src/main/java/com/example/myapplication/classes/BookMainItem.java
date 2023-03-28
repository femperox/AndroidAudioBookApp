package com.example.myapplication.classes;

import java.util.ArrayList;

public class BookMainItem
{
    private int mPicture;
    private String mTitle;
    private Float mTime;
    private String mReader;
    private String mGenres;

    private String[] mFavList;

    public int getPicture() {
        return mPicture;
    }

    public String getTitle() {
        return mTitle;
    }

    public Float getTime() {
        return mTime;
    }

    public String getReader() {
        return mReader;
    }

    public String getGenres() {
        return mGenres;
    }

    String[] getFavList() {return mFavList;}

    public BookMainItem(int picture, String title, Float time, String reader, String genres, String[] favList) {
        mPicture = picture;
        mTitle = title;
        mTime = time;
        mReader = reader;
        mGenres = genres;
        mFavList = favList;
    }


}