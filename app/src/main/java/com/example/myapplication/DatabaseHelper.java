package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "audiobooks.db";
    private static final int SCHEMA = 2; // версия базы данных

    public static final String TABLE_BI = "BookItem"; // название таблицы в бд

    // названия столбцов
    public static final String COLUMN_BOOK_ID = "BookId";
    public static final String COLUMN_TITLE = "Title";
    public static final String COLUMN_AUTHOR = "Author";
    public static final String COLUMN_READER = "Reader";
    public static final String COLUMN_PATH = "Path";
    public static final String COLUMN_DESC = "Description";
    public static final String COLUMN_TIME = "Time";
    public static final String COLUMN_GENRE = "Genre";


    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL("CREATE TABLE "+ TABLE_BI +"(" + COLUMN_BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                                                  + COLUMN_TITLE + " TEXT, "
                                                  + COLUMN_AUTHOR + " TEXT, "
                                                  + COLUMN_READER + " TEXT, "
                                                  + COLUMN_PATH + " TEXT, "
                                                  + COLUMN_DESC + " TEXT, "
                                                  + COLUMN_TIME + " REAL, "
                                                  + COLUMN_GENRE + " TEXT);");

        // добавление начальных данных
        db.execSQL("INSERT INTO "+ TABLE_BI +" (" + COLUMN_TITLE + ", " + COLUMN_AUTHOR + ", " + COLUMN_READER + ", " +COLUMN_PATH + ", " + COLUMN_DESC + ", " +COLUMN_TIME+ ", " +COLUMN_GENRE + ") " +
                   "VALUES ('1984', 'Орвелл', 'Вася Пупкин', '/path/to/book', 'Для подавления массовых волнений Океании правящая партия заново воссоздает прошлое и настоящее. За каждым гражданином неусыпно наблюдают, каждому с помощью телевидения промывают мозги. И даже двое любовников вынуждены скрывать свои чувства, поскольку секс и межличностные отношения объявлены вне закона.', '600', 'социальная фантастика');");
        db.execSQL("INSERT INTO "+ TABLE_BI +" (" + COLUMN_TITLE + ", " + COLUMN_AUTHOR + ", " + COLUMN_READER + ", " +COLUMN_PATH + ", " + COLUMN_DESC + ", " +COLUMN_TIME+ ", " +COLUMN_GENRE + ") " +
                "VALUES ('1985', 'Орвелл', 'Вася Пупкин', '/path/to/book', 'Для подавления массовых волнений Океании правящая партия заново воссоздает прошлое и настоящее. За каждым гражданином неусыпно наблюдают, каждому с помощью телевидения промывают мозги. И даже двое любовников вынуждены скрывать свои чувства, поскольку секс и межличностные отношения объявлены вне закона.', '600', 'социальная фантастика');");
        db.execSQL("INSERT INTO "+ TABLE_BI +" (" + COLUMN_TITLE + ", " + COLUMN_AUTHOR + ", " + COLUMN_READER + ", " +COLUMN_PATH + ", " + COLUMN_DESC + ", " +COLUMN_TIME+ ", " +COLUMN_GENRE + ") " +
                "VALUES ('1986', 'Орвелл', 'Вася Пупкин', '/path/to/book', 'Для подавления массовых волнений Океании правящая партия заново воссоздает прошлое и настоящее. За каждым гражданином неусыпно наблюдают, каждому с помощью телевидения промывают мозги. И даже двое любовников вынуждены скрывать свои чувства, поскольку секс и межличностные отношения объявлены вне закона.', '600', 'социальная фантастика');");
        db.execSQL("INSERT INTO "+ TABLE_BI +" (" + COLUMN_TITLE + ", " + COLUMN_AUTHOR + ", " + COLUMN_READER + ", " +COLUMN_PATH + ", " + COLUMN_DESC + ", " +COLUMN_TIME+ ", " +COLUMN_GENRE + ") " +
                "VALUES ('1987', 'Орвелл', 'Вася Пупкин', '/path/to/book', 'Для подавления массовых волнений Океании правящая партия заново воссоздает прошлое и настоящее. За каждым гражданином неусыпно наблюдают, каждому с помощью телевидения промывают мозги. И даже двое любовников вынуждены скрывать свои чувства, поскольку секс и межличностные отношения объявлены вне закона.', '600', 'социальная фантастика');");
        db.execSQL("INSERT INTO "+ TABLE_BI +" (" + COLUMN_TITLE + ", " + COLUMN_AUTHOR + ", " + COLUMN_READER + ", " +COLUMN_PATH + ", " + COLUMN_DESC + ", " +COLUMN_TIME+ ", " +COLUMN_GENRE + ") " +
                "VALUES ('1988', 'Орвелл', 'Вася Пупкин', '/path/to/book', 'Для подавления массовых волнений Океании правящая партия заново воссоздает прошлое и настоящее. За каждым гражданином неусыпно наблюдают, каждому с помощью телевидения промывают мозги. И даже двое любовников вынуждены скрывать свои чувства, поскольку секс и межличностные отношения объявлены вне закона.', '600', 'социальная фантастика');");
        db.execSQL("INSERT INTO "+ TABLE_BI +" (" + COLUMN_TITLE + ", " + COLUMN_AUTHOR + ", " + COLUMN_READER + ", " +COLUMN_PATH + ", " + COLUMN_DESC + ", " +COLUMN_TIME+ ", " +COLUMN_GENRE + ") " +
                "VALUES ('1989', 'Орвелл', 'Вася Пупкин', '/path/to/book', 'Для подавления массовых волнений Океании правящая партия заново воссоздает прошлое и настоящее. За каждым гражданином неусыпно наблюдают, каждому с помощью телевидения промывают мозги. И даже двое любовников вынуждены скрывать свои чувства, поскольку секс и межличностные отношения объявлены вне закона.', '600', 'социальная фантастика');");
        db.execSQL("INSERT INTO "+ TABLE_BI +" (" + COLUMN_TITLE + ", " + COLUMN_AUTHOR + ", " + COLUMN_READER + ", " +COLUMN_PATH + ", " + COLUMN_DESC + ", " +COLUMN_TIME+ ", " +COLUMN_GENRE + ") " +
                "VALUES ('1990', 'Орвелл', 'Вася Пупкин', '/path/to/book', 'Для подавления массовых волнений Океании правящая партия заново воссоздает прошлое и настоящее. За каждым гражданином неусыпно наблюдают, каждому с помощью телевидения промывают мозги. И даже двое любовников вынуждены скрывать свои чувства, поскольку секс и межличностные отношения объявлены вне закона.', '600', 'социальная фантастика');");
        db.execSQL("INSERT INTO "+ TABLE_BI +" (" + COLUMN_TITLE + ", " + COLUMN_AUTHOR + ", " + COLUMN_READER + ", " +COLUMN_PATH + ", " + COLUMN_DESC + ", " +COLUMN_TIME+ ", " +COLUMN_GENRE + ") " +
                "VALUES ('1991', 'Орвелл', 'Вася Пупкин', '/path/to/book', 'Для подавления массовых волнений Океании правящая партия заново воссоздает прошлое и настоящее. За каждым гражданином неусыпно наблюдают, каждому с помощью телевидения промывают мозги. И даже двое любовников вынуждены скрывать свои чувства, поскольку секс и межличностные отношения объявлены вне закона.', '600', 'социальная фантастика');");
        db.execSQL("INSERT INTO "+ TABLE_BI +" (" + COLUMN_TITLE + ", " + COLUMN_AUTHOR + ", " + COLUMN_READER + ", " +COLUMN_PATH + ", " + COLUMN_DESC + ", " +COLUMN_TIME+ ", " +COLUMN_GENRE + ") " +
                "VALUES ('1992', 'Орвелл', 'Вася Пупкин', '/path/to/book', 'Для подавления массовых волнений Океании правящая партия заново воссоздает прошлое и настоящее. За каждым гражданином неусыпно наблюдают, каждому с помощью телевидения промывают мозги. И даже двое любовников вынуждены скрывать свои чувства, поскольку секс и межличностные отношения объявлены вне закона.', '600', 'социальная фантастика');");
        db.execSQL("INSERT INTO "+ TABLE_BI +" (" + COLUMN_TITLE + ", " + COLUMN_AUTHOR + ", " + COLUMN_READER + ", " +COLUMN_PATH + ", " + COLUMN_DESC + ", " +COLUMN_TIME+ ", " +COLUMN_GENRE + ") " +
                "VALUES ('1993', 'Орвелл', 'Вася Пупкин', '/path/to/book', 'Для подавления массовых волнений Океании правящая партия заново воссоздает прошлое и настоящее. За каждым гражданином неусыпно наблюдают, каждому с помощью телевидения промывают мозги. И даже двое любовников вынуждены скрывать свои чувства, поскольку секс и межличностные отношения объявлены вне закона.', '600', 'социальная фантастика');");
        db.execSQL("INSERT INTO "+ TABLE_BI +" (" + COLUMN_TITLE + ", " + COLUMN_AUTHOR + ", " + COLUMN_READER + ", " +COLUMN_PATH + ", " + COLUMN_DESC + ", " +COLUMN_TIME+ ", " +COLUMN_GENRE + ") " +
                "VALUES ('1994', 'Орвелл', 'Вася Пупкин', '/path/to/book', 'Для подавления массовых волнений Океании правящая партия заново воссоздает прошлое и настоящее. За каждым гражданином неусыпно наблюдают, каждому с помощью телевидения промывают мозги. И даже двое любовников вынуждены скрывать свои чувства, поскольку секс и межличностные отношения объявлены вне закона.', '600', 'социальная фантастика');");
        db.execSQL("INSERT INTO "+ TABLE_BI +" (" + COLUMN_TITLE + ", " + COLUMN_AUTHOR + ", " + COLUMN_READER + ", " +COLUMN_PATH + ", " + COLUMN_DESC + ", " +COLUMN_TIME+ ", " +COLUMN_GENRE + ") " +
                "VALUES ('1995', 'Орвелл', 'Вася Пупкин', '/path/to/book', 'Для подавления массовых волнений Океании правящая партия заново воссоздает прошлое и настоящее. За каждым гражданином неусыпно наблюдают, каждому с помощью телевидения промывают мозги. И даже двое любовников вынуждены скрывать свои чувства, поскольку секс и межличностные отношения объявлены вне закона.', '600', 'социальная фантастика');");




    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_BI);
        onCreate(db);
    }
}
