package com.example.myapplication.classes;

public class BookRecommendItem
{
    private String mPicture;
    private int mId;
    private String mTitle;
    private String mAuthor;
    private String mDesc;
    private String mGenre;

    public int getId() {return mId;}
    public String getPicture() {
        return mPicture;
    }
    public String getTitle() {
        return mTitle;
    }
    public String getAuthor() {
        return mAuthor;
    }
    public String getDesc() {
        return mDesc;
    }
    public String getGenre() {
        return mGenre;
    }

    public BookRecommendItem(int id, String picture, String title, String author, String desc, String genre)
    {   mId = id;
        mPicture = picture;
        mTitle = title;
        mAuthor = author;
        mDesc = desc;
        mGenre = genre;
    }
}
