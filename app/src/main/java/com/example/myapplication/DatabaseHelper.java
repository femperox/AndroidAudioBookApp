package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myapplication.classes.BookMainItem;

public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "audiobooks.db";
    private static final int SCHEMA = 12; // версия базы данных

    // названия столбцов
    public static final String TABLE_BI = "BookItem"; // таблица личных книг

    public static final String COLUMN_BOOK_ID = "BookId";
    public static final String COLUMN_TITLE = "Title";
    public static final String COLUMN_AUTHOR = "Author";
    public static final String COLUMN_READER = "Reader";
    public static final String COLUMN_PATH = "Path";
    public static final String COLUMN_DESC = "Description";
    public static final String COLUMN_TIME = "Time";
    public static final String COLUMN_GENRE = "Genre";

    public static final String TABLE_STATUS = "BookStatus"; // статус прослушивания
    public static final String COLUMN_STATUS_ID = "StatusId";
    public static final String COLUMN_STATUS_NAME = "StatusName";

    public static final String TABLE_FAVLIST = "FavList"; // списки избранного
    public static final String COLUMN_FAVLIST_ID = "FavListId";
    public static final String COLUMN_FAVLIST_NAME = "FavListName";


    public static final String TABLE_STATISTICS = "BookStatistics"; // статистика по книге
    public static final String COLUMN_STATISTICS_TIME_LISTENED = "TimeListened";

    public static final String TABLE_NOTETYPES = "Note"; // типы заметок
    public static final String COLUMN_NOTETYPES_ID = "NoteTypeId";
    public static final String COLUMN_NOTETYPES_NAME = "NoteTypeName";


    public static final String TABLE_NOTES = "BookNotes"; // заметки по книге
    public static final String COLUMN_NOTES_TITLE = "NoteTitle";
    public static final String COLUMN_NOTES_DESC = "NoteDesc";
    public static final String COLUMN_NOTES_TIMECODE = "NoteTimecode";

    public static final String TABLE_SETTINGS = "Settings"; // настройки



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
        }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_BI);
        onCreate(db);
    }
}
