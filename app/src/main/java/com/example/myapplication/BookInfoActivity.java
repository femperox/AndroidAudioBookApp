package com.example.myapplication;

import static com.example.myapplication.fragments.BookFragment.mPlayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.myapplication.adapters.DownloadImageFromInternet;

import org.apache.commons.lang3.time.DurationFormatUtils;

public class BookInfoActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    boolean editable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);

        final ImageButton button_back = (ImageButton) findViewById(R.id.back_btn);
        final ImageButton button_edit = (ImageButton) findViewById(R.id.ib_bookActivity_edit);
        final TextView tv_title = (TextView) findViewById(R.id.tv_title);
        final TextView tv_author = (TextView) findViewById(R.id.tv_author);
        final TextView tv_genres = (TextView) findViewById(R.id.tv_genres);
        final EditText tv_desc = (EditText) findViewById(R.id.tv_desc);
        final TextView tv_reder = (TextView) findViewById(R.id.tv_reader);
        final TextView tv_time = (TextView) findViewById(R.id.tv_time);
        final ImageView pic = (ImageView) findViewById(R.id.iv_pic);
        final TableLayout tl_info = (TableLayout) findViewById(R.id.tl_bookInfo_info);

        Intent secondIntent = getIntent();
        int id = secondIntent.getIntExtra("BOOK_SELECTED", 0);
        String method = secondIntent.getStringExtra("METHOD_SELECTED");

        databaseHelper = new DatabaseHelper(getApplicationContext());
        db = databaseHelper.getReadableDatabase();

        //получаем данные из бд в виде курсора
        if (method.equals("MAIN"))
        {   button_edit.setVisibility(View.VISIBLE);
            tl_info.setVisibility(View.VISIBLE);

            userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_BI + " where " + DatabaseHelper.COLUMN_BOOK_ID + " = " + id, null);
            // определяем, какие столбцы из курсора будут выводиться в ListView

            userCursor.moveToFirst();

            String title = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
            int time = userCursor.getInt(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TIME));
            String reader = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_READER));
            String genres = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GENRE));
            String desc = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESC));
            String author = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AUTHOR));

            userCursor.close();

            String d = DurationFormatUtils.formatDuration(time, "HH:mm:ss", true);

            tv_title.setText(title);
            tv_author.setText(author);
            tv_desc.setText(desc);
            tv_genres.setText(genres);
            tv_reder.setText(reader);
            tv_time.setText(d);
        }
        else
        {
            userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_BI_REC + " where " + DatabaseHelper.COLUMN_BOOK_ID + " = " + id, null);

            userCursor.moveToFirst();

            String title = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
            String path = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PATH));
            String genres = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GENRE));
            String desc = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESC));
            String author = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AUTHOR));

            userCursor.close();

            button_edit.setVisibility(View.GONE);
            tl_info.setVisibility(View.GONE);

            tv_title.setText(title);
            tv_author.setText(author);
            tv_desc.setText(desc);
            tv_genres.setText(genres);

            new DownloadImageFromInternet(pic).execute(path);

        }




        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editable = !editable;

                if (editable)
                {
                    button_edit.setBackgroundColor(getResources().getColor(R.color.teal_200));
                }
                else
                {
                    button_edit.setBackgroundColor(getResources().getColor(R.color.white));


                    ContentValues cv = new ContentValues();
                    cv.put(DatabaseHelper.COLUMN_AUTHOR, tv_author.getText().toString());
                    cv.put(DatabaseHelper.COLUMN_TITLE, tv_title.getText().toString());
                    cv.put(DatabaseHelper.COLUMN_READER, tv_reder.getText().toString());
                    cv.put(DatabaseHelper.COLUMN_DESC, tv_desc.getText().toString());


                    db.update(DatabaseHelper.TABLE_BI, cv, DatabaseHelper.COLUMN_BOOK_ID+"=?", new String[]{Integer.toString(id)});
                }

                tv_desc.setEnabled(editable);
                tv_title.setEnabled(editable);
                tv_author.setEnabled(editable);
                tv_reder.setEnabled(editable);

            }
        });
    }
}