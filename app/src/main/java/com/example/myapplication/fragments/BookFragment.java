package com.example.myapplication.fragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.net.Uri;
import android.os.Bundle;

import com.example.myapplication.classes.FileUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;
import android.app.Fragment;
import android.Manifest;

import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.core.app.ActivityCompat;

import com.example.myapplication.BookInfoActivity;
import com.example.myapplication.DatabaseHelper;
import com.example.myapplication.MainActivity;
import com.example.myapplication.classes.BookMainItem;
import com.example.myapplication.adapters.ListMainViewAdapter;
import com.example.myapplication.R;

import java.io.Console;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookFragment extends Fragment {



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static MediaPlayer mPlayer = new MediaPlayer();
    public static Equalizer mEq = new Equalizer(0, mPlayer.getAudioSessionId());
    private static final int MY_REQUEST_CODE_PERMISSION = 1000;
    private static final int MY_RESULT_CODE_FILECHOOSER = 2000;
    private static final int MY_RESULT_CODE_BOOK_ITEM = 1999;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    ListView listView;
    ListMainViewAdapter adapter;
    Spinner sp_filter;
    TextInputEditText et_search;

    public static ContentResolver resolver;

    public BookFragment() {
        // Required empty public constructor
    }
    public String FRAGMENT_TAG = "SmallPlayer";
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookFragment newInstance(String param1, String param2) {
        BookFragment fragment = new BookFragment();
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
        View v = inflater.inflate(R.layout.fragment_book, container, false);

        Button btn_add = v.findViewById(R.id.button_add_book);
        et_search = v.findViewById(R.id.text_input_search);
        sp_filter = v.findViewById(R.id.spinner_filter_book);

        sp_filter.setSelection(0);

        databaseHelper = new DatabaseHelper(getActivity());
        db = databaseHelper.getReadableDatabase();

        resolver = this.getContext().getContentResolver();

        listView = v.findViewById(R.id.listView);
        updateDB();

        mEq.setEnabled(true);
        short p = 0;
        System.out.println(mEq.getPresetName(p));

        sp_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {


                et_search.setHint("Фильтрация "+ sp_filter.getSelectedItem().toString().toLowerCase());
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                adapter.filter(cs.toString(), sp_filter.getSelectedItemPosition());
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                BookMainItem bk = (BookMainItem)listView.getItemAtPosition(i);
                loadFragment(new SmallPlayerFragment(), bk.getId());

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                BookMainItem bk = (BookMainItem)listView.getItemAtPosition(i);
                Intent nIntent = new Intent(getActivity(), BookInfoActivity.class);
                nIntent.putExtra("BOOK_SELECTED", bk.getId());
                startActivityForResult(nIntent, MY_RESULT_CODE_BOOK_ITEM);
                //startActivity(nIntent);
                return false;
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                doBrowseFile();
            }
        });

        return v;
    }

    private void updateDB()
    {
        final String[] favs = new String[] {"ok?"};

        ArrayList<BookMainItem> items = new ArrayList<>();

        //получаем данные из бд в виде курсора
        userCursor =  db.rawQuery("select * from "+ DatabaseHelper.TABLE_BI, null);
        // определяем, какие столбцы из курсора будут выводиться в ListView

        userCursor.moveToFirst();
        while (!userCursor.isAfterLast())
        {   int id =  userCursor.getInt(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BOOK_ID));
            String title = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
            Float time = userCursor.getFloat(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TIME));
            String reader = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_READER));
            String genres = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GENRE));
            items.add(new BookMainItem(id, R.drawable.user, title, time, reader, genres, favs));

            userCursor.moveToNext();
        }
        userCursor.close();

        adapter = new ListMainViewAdapter(this.getContext(), items);
        listView.setAdapter(adapter);
    }

    private void doBrowseFile()
    {
        Intent chooseFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFileIntent.setType("*/*");
        // Only return URIs that can be opened with ContentResolver
        chooseFileIntent.addCategory(Intent.CATEGORY_OPENABLE);

        chooseFileIntent = Intent.createChooser(chooseFileIntent, "Choose a file");
        startActivityForResult(chooseFileIntent, MY_RESULT_CODE_FILECHOOSER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MY_RESULT_CODE_FILECHOOSER:
                if (resultCode == Activity.RESULT_OK ) {
                    if(data != null)  {
                        String fileUri = data.getDataString();
                        Uri uri = data.getData();

                        //this.getContext().getContentResolver().takePersistableUriPermission(uri, (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION));

                        //ContentResolver resolver = this.getContext().getContentResolver();



                        /*
                        String readOnlyMode = "r";
                        try (ParcelFileDescriptor pfd =
                                     resolver.openFileDescriptor(fileUri, readOnlyMode)) {

                                System.out.println(pfd.getFileDescriptor());
                                //mPlayer.setDataSource(pfd.getFileDescriptor());
                            // Perform operations on "pfd".
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        */



                        //System.out.println("content://com.android.providers.media.documents"+fileUri.getPath());


                        ContentValues cv = new ContentValues();
                        cv.put(DatabaseHelper.COLUMN_BOOK_ID, 1);
                        cv.put(DatabaseHelper.COLUMN_AUTHOR, "test2");
                        cv.put(DatabaseHelper.COLUMN_GENRE, "test");
                        cv.put(DatabaseHelper.COLUMN_TITLE, "test title5");
                        cv.put(DatabaseHelper.COLUMN_READER, "test");
                        cv.put(DatabaseHelper.COLUMN_DESC, "test");
                        cv.put(DatabaseHelper.COLUMN_PATH, fileUri);

                        db.insert(DatabaseHelper.TABLE_BI, null, cv);
                        updateDB();


                    }
                }
                break;
            case MY_RESULT_CODE_BOOK_ITEM:
                updateDB();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void loadFragment(Fragment fragment, int id)
    {
        // create a FragmentManager
        FragmentManager fm = getFragmentManager();

        Bundle bundle = new Bundle();
        bundle.putInt(DatabaseHelper.COLUMN_BOOK_ID, id);
        fragment.setArguments(bundle);
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.fr_smallPlayer, fragment);
        fragmentTransaction.commit(); // save the changes
    }

    public static void stopPlayer()
    {
        if (mPlayer.isPlaying()) mPlayer.stop();
    }
}