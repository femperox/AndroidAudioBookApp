package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class BookInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);

        final ImageButton button_back = (ImageButton) findViewById(R.id.back_btn);
        final TextView textView = (TextView) findViewById(R.id.testID);

        Intent secondIntent = getIntent( );
        String info = secondIntent.getStringExtra("BOOK_SELECTED");
        textView.setText(info);

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}