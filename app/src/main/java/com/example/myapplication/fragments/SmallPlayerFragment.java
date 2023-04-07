package com.example.myapplication.fragments;

import static com.example.myapplication.fragments.BookFragment.mPlayer;
import static com.example.myapplication.fragments.BookFragment.stopPlayer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.PlaybackParams;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.myapplication.DatabaseHelper;
import com.example.myapplication.R;

import org.apache.commons.lang3.time.DurationFormatUtils;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

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

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    boolean playFlag = false;
    boolean firstPlay = true;

    static Context myContext;
    TextView tv_current_time;
    SeekBar sk;

    int rewind_time = 10000;

    private Handler handler;
    private Runnable handlerTask;

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
        Button btn_playStop = v.findViewById(R.id.btn_smallPlayer_playStop3);
        Button btn_speed = v.findViewById(R.id.btn_smallPlayer_speed);
        Button btn_left = v.findViewById(R.id.btn_smallPlayer_left);
        Button btn_right = v.findViewById(R.id.btn_smallPlayer_right);
        TextView tv_author_title = v.findViewById(R.id.tv_smallPlayer_titleAuthor);
        TextView tv_full_time = v.findViewById(R.id.tv_smallPlayer_fullTime);
        tv_current_time = v.findViewById(R.id.tv_smallPlayer_startTime);
        LinearLayout ll = v.findViewById(R.id.ll_smallPlayer);
        sk = v.findViewById(R.id.sb_smallPlayer);



        Bundle bundle = this.getArguments();

        myContext = this.getContext();

        databaseHelper = new DatabaseHelper(getActivity());
        db = databaseHelper.getReadableDatabase();

        int id = bundle.getInt(DatabaseHelper.COLUMN_BOOK_ID);
        //получаем данные из бд в виде курсора
        userCursor =  db.rawQuery("select * from "+ DatabaseHelper.TABLE_BI + " where " + DatabaseHelper.COLUMN_BOOK_ID + " = " + id, null);
        // определяем, какие столбцы из курсора будут выводиться в ListView

        userCursor.moveToFirst();
        String path = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PATH));
        String title = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
        String author = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AUTHOR));

        userCursor.close();

        tv_author_title.setText(title + " - " + author);

        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new FullPlayerFragment(), id);
            }
        });

        sk.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                mPlayer.seekTo(seekBar.getProgress());
            }
        });


        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                getFragmentManager().beginTransaction().remove(SmallPlayerFragment.this).commit();
                stopPlayer();
            }
        });

        btn_playStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!playFlag)
                {
                    if (firstPlay)
                    {
                        try
                        {   mPlayer.reset();
                            mPlayer.setDataSource(path);
                            mPlayer.prepare();

                            int max = mPlayer.getDuration();
                            sk.setMax(mPlayer.getDuration());

                            String d = DurationFormatUtils.formatDuration(mPlayer.getDuration(), "HH:mm:ss", true);
                            tv_full_time.setText(d);
                            sk.setProgress(0);

                            firstPlay = false;
                        }

                        catch (IOException e)
                        {
                            throw new RuntimeException(e);
                        }
                    }

                    mPlayer.start();
                    btn_playStop.setText("||");

                }
                else
                {
                    mPlayer.pause();
                    btn_playStop.setText("|>");
                }
                playFlag = !playFlag;
            }
        });

        btn_speed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (mPlayer.isPlaying())
                {
                    PlaybackParams playbackParams = new PlaybackParams();
                    playbackParams.setSpeed(2);
                    playbackParams.setPitch(1);
                    mPlayer.setPlaybackParams(playbackParams);
                }
            }
        });


        btn_left.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (mPlayer.isPlaying())
                {
                    mPlayer.seekTo(mPlayer.getCurrentPosition() - rewind_time);
                }
            }
        });

        btn_right.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (mPlayer.isPlaying())
                {
                    mPlayer.seekTo(mPlayer.getCurrentPosition() + rewind_time);

                }
            }
        });

        // ползунок проигрывания

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (mPlayer.isPlaying())
                {   int curr = mPlayer.getCurrentPosition();
                    sk.setProgress(curr);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run()
                        {   String d = DurationFormatUtils.formatDuration(curr, "HH:mm:ss", true);
                            tv_current_time.setText(d);
                        }
                    });
                    //

                }
            }
        },0,100);




        return v;
    }

    public void loadFragment(Fragment fragment, int id)
    {
        // create a FragmentManager
        FragmentManager fm = getFragmentManager();

        Bundle bundle = new Bundle();
        bundle.putInt(DatabaseHelper.COLUMN_BOOK_ID, id);
        fragment.setArguments(bundle);


        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        //fragmentTransaction.setCustomAnimations(R.animator.slide_in_left);
        fragmentTransaction.setCustomAnimations(R.animator.slide_up, R.animator.slide_down, 0, R.animator.slide_down);
        fragmentTransaction.replace(R.id.fr_bigPlayer, fragment);
        fragmentTransaction.commit(); // save the changes
    }


}