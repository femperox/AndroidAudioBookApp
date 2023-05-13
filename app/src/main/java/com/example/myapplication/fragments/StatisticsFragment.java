package com.example.myapplication.fragments;

import android.animation.ValueAnimator;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import org.apache.commons.math3.util.Pair;
import com.androidplot.Region;
import com.androidplot.pie.PieChart;
import com.androidplot.pie.PieRenderer;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;
import com.androidplot.ui.Anchor;
import com.androidplot.ui.DynamicTableModel;
import com.androidplot.ui.HorizontalPositioning;
import com.androidplot.ui.SeriesBundle;
import com.androidplot.ui.Size;
import com.androidplot.ui.SizeMode;
import com.androidplot.ui.TextOrientation;
import com.androidplot.ui.VerticalPositioning;
import com.androidplot.ui.widget.TextLabelWidget;
import com.androidplot.util.PixelUtils;
import com.androidplot.xy.BarFormatter;
import com.androidplot.xy.BarRenderer;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.StepMode;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYSeriesFormatter;
import com.example.myapplication.DatabaseHelper;
import com.example.myapplication.R;
import com.example.myapplication.classes.BookMainItem;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


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
    //public TextView barText;
    private TextLabelWidget barText;
    public XYPlot plot;
    private Pair<Integer, XYSeries> selection;
    public static final int SELECTED_SEGMENT_OFFSET = 10;
    private static final String NO_SELECTION_TXT = "Touch bar to select.";

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

        plot = (XYPlot) v.findViewById(R.id.BarPlot);
        try {
            setListenedStatistics();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

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

        plot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    onPlotClicked(new PointF(motionEvent.getX(), motionEvent.getY()));
                }
                return true;
            }
        });



        return v;
    }

    private void setListenedStatistics() throws ParseException {
        plot.getBackgroundPaint().setColor(Color.TRANSPARENT);

        plot.getBorderPaint().setColor(Color.TRANSPARENT);

        //barText = v.findViewById(R.id.tv_statistics_bar);
        /*
        barText = new TextLabelWidget(plot.getLayoutManager(), NO_SELECTION_TXT,
                new Size(
                        PixelUtils.dpToPix(100), SizeMode.ABSOLUTE,
                        PixelUtils.dpToPix(100), SizeMode.ABSOLUTE),
                TextOrientation.HORIZONTAL);

        barText.getLabelPaint().setTextSize(PixelUtils.dpToPix(16));

        // add a dark, semi-transparent background to the selection label widget:
        Paint p = new Paint();
        p.setARGB(100, 0, 0, 0);
        barText.setBackgroundPaint(p);

        barText.position(
                0, HorizontalPositioning.RELATIVE_TO_CENTER,
                PixelUtils.dpToPix(45), VerticalPositioning.ABSOLUTE_FROM_TOP,
                Anchor.TOP_MIDDLE);
        barText.pack();
        */

        userCursor =  db.rawQuery("select count(*) as count, strftime('%m', " + DatabaseHelper.COLUMN_DATE_START + ") as month from "+ DatabaseHelper.TABLE_BI + " GROUP BY month", null);

        List<Integer> months = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12));
        List<Integer> counts = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        if (userCursor.moveToFirst()) {
            do {
                int month = userCursor.getInt(userCursor.getColumnIndex("month"));
                int count = userCursor.getInt(userCursor.getColumnIndex("count"));
                //months.set(month-1, month);
                counts.set(month-1, count);
            } while (userCursor.moveToNext());
        }

        XYSeries series = new SimpleXYSeries(months, counts, "");

        plot.setRangeStep(StepMode.INCREMENT_BY_VAL, 1);
        plot.setDomainStep(StepMode.INCREMENT_BY_VAL, 1);
        plot.setDomainBoundaries(1, 12, BoundaryMode.FIXED);
        //plot.set

        BarRenderer barRenderer = plot.getRenderer(BarRenderer.class);
        //barRenderer.setBarGroupWidth(BarRenderer.BarGroupWidthMode.FIXED_WIDTH, PixelUtils.dpToPix(25));
        //barRenderer.setBarGroupWidth(BarRenderer.BarGroupWidthMode.FIXED_WIDTH, 0.7f); // задание фиксированной ширины для группы столбцов


        // настройка параметров диаграммы

        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.LEFT).setFormat(new DecimalFormat("0"));
        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new DecimalFormat("0"));
        //plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new SimpleDateFormat("MMM"));

        plot.getGraph().setPaddingLeft(32.0f);
        plot.getGraph().setPaddingRight(32.0f);
        plot.getGraph().setPaddingTop(32.0f);
        plot.getGraph().setPaddingBottom(32.0f);

        int r = 170;
        int g = 102;
        int b = 204;
        BarFormatter formatter = new BarFormatter(Color.rgb(r, g, b), Color.DKGRAY);

// создание объекта BarSeries и добавление его в диаграмму
        //BarSeries series = new BarSeries("Count", SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, counts, months);
        plot.addSeries(series, formatter);


    }

    private void setGenreStatistics(PieChart pie)
    {
        // enable the legend:
        //pie.getLegend().setVisible(true);
        //pie.getLegend().setTableModel(new DynamicTableModel(4, 2));

        TextPaint textPaint = new TextPaint();
        //textPaint.setTextSize(20);
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
            count.add(Pair.create(countG, genres));

            userCursor.moveToNext();
        }
        userCursor.close();

        int r = 170;
        int g = 102;
        int b = 204;

        for (Pair<Integer, String> item: count)
        {
            Segment segment = new Segment(item.getSecond(), item.getFirst());
            SegmentFormatter formatter = new SegmentFormatter(Color.rgb(r, g, b), Color.TRANSPARENT);
            formatter.getLabelPaint().setTextSize(35);
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

    private void onPlotClicked(PointF point) {

        // make sure the point lies within the graph area.  we use gridrect
        // because it accounts for margins and padding as well.
        if (plot.containsPoint(point.x, point.y)) {
            Number x = plot.getXVal(point);
            Number y = plot.getYVal(point);

            selection = null;
            double xDistance = 0;
            double yDistance = 0;

            // find the closest value to the selection:
            for (SeriesBundle<XYSeries, ? extends XYSeriesFormatter> sfPair : plot
                    .getRegistry().getSeriesAndFormatterList()) {
                XYSeries series = sfPair.getSeries();
                for (int i = 0; i < series.size(); i++) {
                    Number thisX = series.getX(i);
                    Number thisY = series.getY(i);
                    if (thisX != null && thisY != null) {
                        double thisXDistance =
                                Region.measure(x, thisX).doubleValue();
                        double thisYDistance =
                                Region.measure(y, thisY).doubleValue();
                        if (selection == null) {
                            selection = new Pair<>(i, series);
                            xDistance = thisXDistance;
                            yDistance = thisYDistance;
                        } else if (thisXDistance < xDistance) {
                            selection = new Pair<>(i, series);
                            xDistance = thisXDistance;
                            yDistance = thisYDistance;
                        } else if (thisXDistance == xDistance &&
                                thisYDistance < yDistance &&
                                thisY.doubleValue() >= y.doubleValue()) {
                            selection = new Pair<>(i, series);
                            xDistance = thisXDistance;
                            yDistance = thisYDistance;
                        }
                    }
                }
            }

        } else {
            // if the press was outside the graph area, deselect:
            selection = null;
        }

        if (selection == null) {
            barText.setText(NO_SELECTION_TXT);
        } else {
            barText.setText("Selected: " + selection.getSecond().getTitle() +
                    " Value: " + selection.getSecond().getY(selection.getFirst()));

        }
        plot.redraw();
    }
}