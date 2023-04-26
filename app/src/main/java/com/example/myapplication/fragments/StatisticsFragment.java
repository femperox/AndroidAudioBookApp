package com.example.myapplication.fragments;

import android.animation.ValueAnimator;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.app.Fragment;

import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.androidplot.pie.PieChart;
import com.androidplot.pie.PieRenderer;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;
import com.androidplot.ui.DynamicTableModel;
import com.example.myapplication.DatabaseHelper;
import com.example.myapplication.R;
import com.example.myapplication.classes.BookMainItem;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticsFragment extends Fragment {

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
    public PieChart pie;
    public static final int SELECTED_SEGMENT_OFFSET = 10;

    public StatisticsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatisticsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatisticsFragment newInstance(String param1, String param2) {
        StatisticsFragment fragment = new StatisticsFragment();
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
        View v = inflater.inflate(R.layout.fragment_statistics, container, false);

        databaseHelper = new DatabaseHelper(getActivity());
        db = databaseHelper.getReadableDatabase();

        pie = (PieChart) v.findViewById(R.id.mySimplePieChart);
        setGenreStatistics(pie);

        pie.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                PointF click = new PointF(motionEvent.getX(), motionEvent.getY());
                if(pie.getPie().containsPoint(click)) {
                    Segment segment = pie.getRenderer(PieRenderer.class).getContainingSegment(click);

                    if(segment != null) {
                        final boolean isSelected = getFormatter(segment).getOffset() != 0;
                        deselectAll();
                        setSelected(segment, !isSelected);
                        pie.redraw();
                    }
                }
                return false;
            }

            private SegmentFormatter getFormatter(Segment segment) {
                return pie.getFormatter(segment, PieRenderer.class);
            }

            private void deselectAll() {
                List<Segment> segments = pie.getRegistry().getSeriesList();
                for(Segment segment : segments) {
                    setSelected(segment, false);
                }
            }

            private void setSelected(Segment segment, boolean isSelected) {
                SegmentFormatter f = getFormatter(segment);
                if(isSelected) {
                    f.setOffset(SELECTED_SEGMENT_OFFSET);
                } else {
                    f.setOffset(0);
                }
            }
        });



        return v;
    }

    private void setGenreStatistics(PieChart pie)
    {
        // enable the legend:
        pie.getLegend().setVisible(true);

        pie.getLegend().setTableModel(new DynamicTableModel(4, 2));



        TextPaint textPaint = new TextPaint();
        //textPaint.setTextSize(10);
        textPaint.setColor(Color.BLACK);

        pie.getLegend().setTextPaint(textPaint);

        pie.getBackgroundPaint().setColor(Color.TRANSPARENT);
        pie.getBorderPaint().setColor(Color.TRANSPARENT);

        userCursor =  db.rawQuery("select count(*) as count,"+ DatabaseHelper.COLUMN_GENRE + " from "+ DatabaseHelper.TABLE_BI + " GROUP BY " + DatabaseHelper.COLUMN_GENRE, null);
        ArrayList<Pair<Integer, String>> count = new ArrayList<Pair<Integer, String>>();
        userCursor.moveToFirst();
        while (!userCursor.isAfterLast())
        {
            Integer countG = userCursor.getInt(userCursor.getColumnIndexOrThrow("count"));
            String genres = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GENRE));
            count.add(Pair.of(countG, genres));

            userCursor.moveToNext();
        }
        userCursor.close();

        int r = 170;
        int g = 102;
        int b = 204;

        for (Pair<Integer, String> item: count)
        {
            Segment segment = new Segment(item.getRight(), item.getLeft());
            SegmentFormatter formatter = new SegmentFormatter(Color.rgb(r, g, b), Color.TRANSPARENT);
            pie.addSegment(segment, formatter);
            r += 15; g += 10; b -= 2;
        }

        PieRenderer pieRenderer = pie.getRenderer(PieRenderer.class);
        pieRenderer.setDonutSize((float)0.35, PieRenderer.DonutMode.PERCENT);

        pieRenderer.setExtentDegs(0);
        // animate a scale value from a starting val of 0 to a final value of 1:
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);

        // use an animation pattern that begins and ends slowly:
        animator.setInterpolator(new AccelerateDecelerateInterpolator());

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float scale = valueAnimator.getAnimatedFraction();
                pieRenderer.setExtentDegs(360 * scale);
                pie.redraw();
            }
        });

        // the animation will run for 1.5 seconds:
        animator.setDuration(1500);
        animator.start();


    }
}