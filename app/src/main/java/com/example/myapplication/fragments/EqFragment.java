package com.example.myapplication.fragments;

import static com.example.myapplication.fragments.BookFragment.mEq;

import static org.aspectj.runtime.internal.Conversions.shortValue;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.myapplication.DatabaseHelper;
import com.example.myapplication.R;
import com.example.myapplication.classes.BookMainItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.tankery.lib.circularseekbar.CircularSeekBar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EqFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EqFragment extends Fragment {

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
    ArrayList<CircularSeekBar> bars;

    public EqFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EqFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EqFragment newInstance(String param1, String param2) {
        EqFragment fragment = new EqFragment();
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
        View v = inflater.inflate(R.layout.fragment_eq, container, false);

        Spinner preset_filter = v.findViewById(R.id.spinner_filter_presets);
        Button btn_save = v.findViewById(R.id.btn_eq_save);
        Button btn_reset = v.findViewById(R.id.btn_eq_reset);

        final List<String> list = new ArrayList<String>();
        for (short i=0; i< mEq.getNumberOfPresets(); i++) list.add(mEq.getPresetName(i));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        preset_filter.setAdapter(arrayAdapter);

        //CircularSeekBar seekBar = v.findViewById(R.id.);

        short[] range = mEq.getBandLevelRange();

        for (int i=0; i<range.length; i++) System.out.println(range[i]);

        for (short i=0; i< mEq.getNumberOfBands(); i++) System.out.println(mEq.getBandLevel(i));

        short bands = mEq.getNumberOfBands();

        setBands(getBands(1));

        final short minEQLevel = mEq.getBandLevelRange()[0];
        final short maxEQLevel = mEq.getBandLevelRange()[1];

        bars = new ArrayList<CircularSeekBar>();
        ArrayList<TextView> tvs = new ArrayList<TextView>();

        tvs.add(v.findViewById(R.id.tv_eq_1));
        bars.add(v.findViewById(R.id.eqv_freq1));
        tvs.add(v.findViewById(R.id.tv_eq_2));
        bars.add(v.findViewById(R.id.eqv_freq2));
        tvs.add(v.findViewById(R.id.tv_eq_3));
        bars.add(v.findViewById(R.id.eqv_freq3));
        tvs.add(v.findViewById(R.id.tv_eq_4));
        bars.add(v.findViewById(R.id.eqv_freq4));
        tvs.add(v.findViewById(R.id.tv_eq_5));
        bars.add(v.findViewById(R.id.eqv_freq5));


        for (int i=0; i<bands; i++)
        {
            short band = shortValue(i);
            tvs.get(i).setText((mEq.getCenterFreq(band) / 1000) + " Hz\n"+(mEq.getBandLevel(band)+ minEQLevel)+ " Db");
            bars.get(i).setMax(maxEQLevel - minEQLevel);
            bars.get(i).setProgress(mEq.getBandLevel(band));

            bars.get(i).setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
                @Override
                public void onProgressChanged(@Nullable CircularSeekBar circularSeekBar, float v, boolean b) {
                    mEq.setBandLevel(band, (short) (v + minEQLevel));
                    tvs.get(band).setText((mEq.getCenterFreq(band) / 1000) + " Hz\n"+mEq.getBandLevel(band)+ " Db");
                }

                @Override
                public void onStopTrackingTouch(@Nullable CircularSeekBar circularSeekBar) {

                }

                @Override
                public void onStartTrackingTouch(@Nullable CircularSeekBar circularSeekBar) {

                }
            });


        }

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Short> bandsLevels = new ArrayList<Short>();

                for(CircularSeekBar bar: bars)
                {
                    bandsLevels.add(shortValue(bar.getProgress()));
                }

                ContentValues cv = new ContentValues();
                String id = "1";
                cv.put(DatabaseHelper.COLUMN_SETTINGS_BAND1, bandsLevels.get(0));
                cv.put(DatabaseHelper.COLUMN_SETTINGS_BAND2, bandsLevels.get(1));
                cv.put(DatabaseHelper.COLUMN_SETTINGS_BAND3, bandsLevels.get(2));
                cv.put(DatabaseHelper.COLUMN_SETTINGS_BAND4, bandsLevels.get(3));
                cv.put(DatabaseHelper.COLUMN_SETTINGS_BAND5, bandsLevels.get(4));

                db.update(DatabaseHelper.TABLE_SETTINGS, cv, DatabaseHelper.COLUMN_SETTINGS_ID+" = ?", new String[] { id });
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ArrayList<Short> bandsLevels = getBands(0);

                ContentValues cv = new ContentValues();
                String id = "1";
                cv.put(DatabaseHelper.COLUMN_SETTINGS_BAND1, bandsLevels.get(0));
                cv.put(DatabaseHelper.COLUMN_SETTINGS_BAND2, bandsLevels.get(1));
                cv.put(DatabaseHelper.COLUMN_SETTINGS_BAND3, bandsLevels.get(2));
                cv.put(DatabaseHelper.COLUMN_SETTINGS_BAND4, bandsLevels.get(3));
                cv.put(DatabaseHelper.COLUMN_SETTINGS_BAND5, bandsLevels.get(4));

                db.update(DatabaseHelper.TABLE_SETTINGS, cv, DatabaseHelper.COLUMN_SETTINGS_ID+" = ?", new String[] { id });
                setBands(bandsLevels);
                for(int i=0; i<bandsLevels.size(); i++) bars.get(i).setProgress(mEq.getBandLevel((short) i));
            }
        });

        return v;
    }

    private ArrayList<Short> getBands(int id)
    {
        databaseHelper = new DatabaseHelper(getActivity());
        db = databaseHelper.getReadableDatabase();

        userCursor =  db.rawQuery("select * from "+ DatabaseHelper.TABLE_SETTINGS + " where " + DatabaseHelper.COLUMN_SETTINGS_ID + " = " + id, null);
        // определяем, какие столбцы из курсора будут выводиться в ListView

        userCursor.moveToFirst();
        ArrayList<Short> bandsLevels = new ArrayList<Short>();
        while (!userCursor.isAfterLast())
        {   int ids =  userCursor.getInt(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SETTINGS_ID));
            bandsLevels.add(shortValue(userCursor.getInt(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SETTINGS_BAND1))));
            bandsLevels.add(shortValue(userCursor.getInt(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SETTINGS_BAND2))));
            bandsLevels.add(shortValue(userCursor.getInt(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SETTINGS_BAND3))));
            bandsLevels.add(shortValue(userCursor.getInt(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SETTINGS_BAND4))));
            bandsLevels.add(shortValue(userCursor.getInt(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SETTINGS_BAND5))));

            userCursor.moveToNext();
        }
        userCursor.close();

        return bandsLevels;
    }

    private void setBands(ArrayList<Short> bandsLevels)
    {
        for(int i=0; i<bandsLevels.size(); i++)
        {
            mEq.setBandLevel((short) i, bandsLevels.get(i));
        }
    }
}