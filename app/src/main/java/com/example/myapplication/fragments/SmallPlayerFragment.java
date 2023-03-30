package com.example.myapplication.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.myapplication.DatabaseHelper;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SmallPlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SmallPlayerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MediaPlayer mPlayer;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;

    public SmallPlayerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SmallPlayerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SmallPlayerFragment newInstance(String param1, String param2) {
        SmallPlayerFragment fragment = new SmallPlayerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_small_player, container, false);

        Button btn_close = v.findViewById(R.id.btn_smallPlayer_close);
        TextView tv_author_title = v.findViewById(R.id.tv_smallPlayer_titleAuthor);
        TextView tv_test = v.findViewById(R.id.tv_smallPlayer_test);
        Bundle bundle = this.getArguments();


        databaseHelper = new DatabaseHelper(getActivity());
        db = databaseHelper.getReadableDatabase();

        //получаем данные из бд в виде курсора
        userCursor =  db.rawQuery("select * from "+ DatabaseHelper.TABLE_BI + " where " + DatabaseHelper.COLUMN_BOOK_ID + " = " + bundle.getInt(DatabaseHelper.COLUMN_BOOK_ID), null);
        // определяем, какие столбцы из курсора будут выводиться в ListView

        userCursor.moveToFirst();
        String path = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PATH));
        String title = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
        String author = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AUTHOR));

        userCursor.close();

        tv_author_title.setText(title + " - " + author);
        System.out.println(Uri.parse(path));
        mPlayer = new MediaPlayer();


        try
        {
            mPlayer.setDataSource(path);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().remove(SmallPlayerFragment.this).commit();
            }
        });

        return v;
    }


    public void setSelectedItem(int selectedItem) {
        TextView tv_author_title = getView().findViewById(R.id.tv_smallPlayer_titleAuthor);
        tv_author_title.setText("LOL");

        databaseHelper = new DatabaseHelper(getActivity());
        db = databaseHelper.getReadableDatabase();

        //получаем данные из бд в виде курсора
        userCursor =  db.rawQuery("select * from "+ DatabaseHelper.TABLE_BI + " where " + DatabaseHelper.COLUMN_BOOK_ID + " = " + selectedItem, null);
        // определяем, какие столбцы из курсора будут выводиться в ListView

        userCursor.moveToFirst();
        String path = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PATH));
        String title = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
        String author = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AUTHOR));

        userCursor.close();

        tv_author_title.setText(title + " - " + author);
        try
        {   System.out.println(Uri.parse(path));
            mPlayer.setDataSource(this.getContext(), Uri.parse(path));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }


}