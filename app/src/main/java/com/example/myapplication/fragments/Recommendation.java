package com.example.myapplication.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

import com.example.myapplication.BookInfoActivity;
import com.example.myapplication.DatabaseHelper;
import com.example.myapplication.adapters.ListMainViewAdapter;
import com.example.myapplication.classes.BookMainItem;
import com.example.myapplication.classes.BookRecommendItem;
import com.example.myapplication.adapters.ListRecommendViewAdapter;
import com.example.myapplication.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Recommendation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Recommendation extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    ListRecommendViewAdapter adapter;
    GridView gridView;

    public Recommendation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Recommendation.
     */
    // TODO: Rename and change types and number of parameters
    public static Recommendation newInstance(String param1, String param2) {
        Recommendation fragment = new Recommendation();
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_recommendation, container, false);
        Button btn_update = v.findViewById(R.id.btn_rec_update);
        gridView= v.findViewById(R.id.gridViewRecommend);
        ArrayList<BookRecommendItem> items = new ArrayList<>();


        databaseHelper = new DatabaseHelper(getActivity());
        db = databaseHelper.getReadableDatabase();

        updateDB();

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                db.delete(DatabaseHelper.TABLE_BI_REC, null, null);
                //получаем данные из бд в виде курсора

                userCursor =  db.rawQuery("select * from "+ DatabaseHelper.TABLE_BI + " Where " + DatabaseHelper.COLUMN_GENRE +" = 'Фантастика' ORDER BY RANDOM() LIMIT 1", null);
                // определяем, какие столбцы из курсора будут выводиться в ListView
                String title = "";
                String author = "";
                String genre = "";
                String desc = "";

                userCursor.moveToFirst();
                while (!userCursor.isAfterLast())
                {   int id =  userCursor.getInt(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BOOK_ID));
                    title = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
                    author = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AUTHOR));
                    genre = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GENRE));
                    desc = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESC));

                    userCursor.moveToNext();
                }
                userCursor.close();

                Python python = Python.getInstance();
                PyObject pythonFile = python.getModule("test");
                String[][] recs = pythonFile.callAttr("main", title, genre, desc, author).toJava(String[][].class);

                for (String[] rec : recs)
                {
                    //items.add(new BookRecommendItem(0,rec[0], rec[1], rec[2], rec[3], rec[4]));

                    ContentValues cv = new ContentValues();
                    cv.put(DatabaseHelper.COLUMN_PATH, rec[0]);
                    cv.put(DatabaseHelper.COLUMN_TITLE, rec[1]);
                    cv.put(DatabaseHelper.COLUMN_AUTHOR, rec[2]);
                    cv.put(DatabaseHelper.COLUMN_DESC, rec[3]);
                    cv.put(DatabaseHelper.COLUMN_GENRE, rec[4]);
                    db.insert(DatabaseHelper.TABLE_BI_REC, null, cv);
                }

                updateDB();
            }
        });


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                BookRecommendItem bk = (BookRecommendItem)gridView.getItemAtPosition(i);
                Intent nIntent = new Intent(getActivity(), BookInfoActivity.class);
                nIntent.putExtra("BOOK_SELECTED", bk.getId());
                nIntent.putExtra("METHOD_SELECTED", "REC");
                startActivity(nIntent);
            }
        });

        return v;
    }


    private void updateDB()
    {
        final String[] favs = new String[] {"ok?"};

        ArrayList<BookRecommendItem> items = new ArrayList<>();

        //получаем данные из бд в виде курсора
        userCursor =  db.rawQuery("select * from "+ DatabaseHelper.TABLE_BI_REC, null);
        // определяем, какие столбцы из курсора будут выводиться в ListView

        userCursor.moveToFirst();
        while (!userCursor.isAfterLast())
        {   int id =  userCursor.getInt(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BOOK_ID));
            String title = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
            String img_path = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PATH));
            String desc = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESC));
            String author = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AUTHOR));
            String genres = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GENRE));
            items.add(new BookRecommendItem(id, img_path, title, author, desc, genres));

            userCursor.moveToNext();
        }
        userCursor.close();

        adapter = new ListRecommendViewAdapter(this.getContext(), items);
        gridView.setAdapter(adapter);
    }
}