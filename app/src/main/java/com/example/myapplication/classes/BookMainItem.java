package com.example.myapplication.classes;

import java.util.ArrayList;

public class BookMainItem
{
    private int mPicture;
    private int mId;
    private String mTitle;
    private int mTime;
    private String mReader;
    private String mGenres;
    private String mAuthor;
    private String mPath;
    private String mDesc;

    private String[] mFavList;

    public int getId() {return mId;}
    public int getPicture() {
        return mPicture;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getTime() {
        return mTime;
    }

    public String getReader() {
        return mReader;
    }

    public String getGenres() {
        return mGenres;
    }

    public String getPath() {
        return mPath;
    }
    public String getAuthor() {
        return mAuthor;
    }
    public String getDesc() {
        return mDesc;
    }

    String[] getFavList() {return mFavList;}

    public BookMainItem(int id, int picture, String title, int time, String reader, String genres, String[] favList)
    {
        mId = id;
        mPicture = picture;
        mTitle = title;
        mTime = time;
        mReader = reader;
        mGenres = genres;
        mFavList = favList;
    }

    public BookMainItem(int id, int picture, String title, int time, String reader, String genres, String author, String desc, String path)
    {
        mId = id;
        mPicture = picture;
        mTitle = title;
        mTime = time;
        mReader = reader;
        mGenres = genres;
        mPath = path;
        mAuthor = author;
        mDesc = desc;
    }


}
