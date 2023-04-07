package com.example.myapplication.fragments;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.myapplication.R;

import static com.example.myapplication.fragments.BookFragment.mPlayer;
import static com.example.myapplication.fragments.BookFragment.stopPlayer;

import androidx.core.view.DragStartHelper;

import org.apache.commons.lang3.time.DurationFormatUtils;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FullPlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FullPlayerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    boolean playFlag = false;
    int rewind_time = 10000;
    TextView tv_current_time;
    SeekBar sk;

    public FullPlayerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FullPlayerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FullPlayerFragment newInstance(String param1, String param2) {
        FullPlayerFragment fragment = new FullPlayerFragment();
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
        View v = inflater.inflate(R.layout.fragment_full_player, container, false);

        Button btn_playStop = v.findViewById(R.id.btn_bigPlayer_playStop);
        //Button btn_speed = v.findViewById(R.id.btn_smallPlayer_speed);
        Button btn_left = v.findViewById(R.id.btn_bigPlayer_left);
        Button btn_right = v.findViewById(R.id.btn_bigPlayer_right);
        TextView tv_full_time = v.findViewById(R.id.tv_bigPlayer_fullTime);
        tv_current_time = v.findViewById(R.id.tv_bigPlayer_startTime);
        TextView tv_close = v.findViewById(R.id.tv_bigPlayer_close);
        LinearLayout ll = v.findViewById(R.id.ll_bigPlayer);

        sk = v.findViewById(R.id.sb_bigPlayer);
        SeekBar sk_volume = v.findViewById(R.id.sk_bigPlayer_volume);

        sk.setMax(mPlayer.getDuration());
        sk.setProgress(mPlayer.getCurrentPosition());
        String d = DurationFormatUtils.formatDuration(mPlayer.getDuration(), "HH:mm:ss", true);
        tv_full_time.setText(d);

        AudioManager audioManager = (AudioManager) this.getContext().getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curValue = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        sk_volume.setMax(maxVolume);
        sk_volume.setProgress(curValue);

        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_close.setRotation(180);
                getFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_down, R.animator.slide_down).remove(FullPlayerFragment.this).commit();

            }
        });

        sk_volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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

        btn_playStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!playFlag)
                {
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
}