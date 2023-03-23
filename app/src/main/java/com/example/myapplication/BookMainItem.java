package com.example.myapplication;

import java.util.ArrayList;

public class BookMainItem
{
    private int mPicture;
    private String mTitle;
    private String mTime;
    private String mReader;
    private String mGenres;

    private String[] mFavList;

    int getPicture() {
        return mPicture;
    }

    String getTitle() {
        return mTitle;
    }

    String getTime() {
        return mTime;
    }

    String getReader() {
        return mReader;
    }

    String getGenres() {
        return mGenres;
    }

    String[] getFavList() {return mFavList;}

    BookMainItem (int picture, String title, String time, String reader, String genres, String[] favList) {
        mPicture = picture;
        mTitle = title;
        mTime = time;
        mReader = reader;
        mGenres = genres;
        mFavList = favList;
    }


}
