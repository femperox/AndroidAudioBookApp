package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class BookInfoActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);

        final ImageButton button_back = (ImageButton) findViewById(R.id.back_btn);
        final TextView tv_author_title = (TextView) findViewById(R.id.tv_author_title);
        final TextView tv_genres = (TextView) findViewById(R.id.tv_genres);
        final TextView tv_desc = (TextView) findViewById(R.id.tv_desc);
        final TextView tv_reder = (TextView) findViewById(R.id.tv_reader);
        final TextView tv_time = (TextView) findViewById(R.id.tv_time);


        Intent secondIntent = getIntent();
        int id = secondIntent.getIntExtra("BOOK_SELECTED", 0);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        db = databaseHelper.getReadableDatabase();

        //получаем данные из бд в виде курсора
        userCursor =  db.rawQuery("select * from "+ DatabaseHelper.TABLE_BI + " where " + DatabaseHelper.COLUMN_BOOK_ID + " = " + id, null);
        // определяем, какие столбцы из курсора будут выводиться в ListView

        userCursor.moveToFirst();

        String title = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
        Float time = userCursor.getFloat(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TIME));
        String reader = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_READER));
        String genres = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GENRE));
        String desc = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESC));
        String author = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AUTHOR));

        userCursor.close();

        tv_author_title.setText(title + " - " + author);
        tv_desc.setText(desc);
        tv_genres.setText(genres);
        tv_reder.setText(reader);
        tv_time.setText(time.toString());


        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}