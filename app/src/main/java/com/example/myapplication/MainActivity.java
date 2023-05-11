package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Fragment;
import com.chaquo.python.android.AndroidPlatform;
import android.app.FragmentManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.chaquo.python.Python;
import com.example.myapplication.fragments.BookFragment;
import com.example.myapplication.fragments.Recommendation;
import com.example.myapplication.fragments.SmallPlayerFragment;
import com.example.myapplication.fragments.UserFragment;

public class MainActivity extends AppCompatActivity{


    public String FRAGMENT_TAG = "SmallPlayer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        final Button button_rec = (Button) findViewById(R.id.rec_btn);
        final Button button_book = (Button) findViewById(R.id.book_btn);
        final ImageButton button_usr = (ImageButton) findViewById(R.id.usr_btn);

        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }



        loadFragment(new BookFragment());

        button_rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button_rec.setTextColor(Color.WHITE);
                button_book.setTextColor(Color.BLACK);
                button_usr.setImageResource(R.drawable.user);

                loadFragment(new Recommendation());
            }
        });

        button_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button_rec.setTextColor(Color.BLACK);
                button_book.setTextColor(Color.WHITE);
                button_usr.setImageResource(R.drawable.user);

                loadFragment(new BookFragment());
            }
        });

        button_usr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button_usr.setImageResource(R.drawable.user_clicked);
                button_rec.setTextColor(Color.BLACK);
                button_book.setTextColor(Color.BLACK);

                loadFragment(new UserFragment());
            }
        });

    }

    public void loadFragment(Fragment fragment)
    {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }


}