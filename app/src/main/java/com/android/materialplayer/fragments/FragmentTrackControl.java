package com.android.materialplayer.fragments;

import android.app.Fragment;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.android.materialplayer.R;

/**
 * Created by dl1998 on 30.11.17.
 */

public class FragmentTrackControl extends Fragment implements View.OnClickListener {

    private RepeatMode repeatMode;
    private Boolean shuffleMode;
    private Boolean playMode;
    private ImageButton btnShuffle;
    private ImageButton btnPlayPause;
    private ImageButton btnRepeat;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_track_controls, container, true);

        btnShuffle = view.findViewById(R.id.btnShuffle);
        btnPlayPause = view.findViewById(R.id.btnPlayPause);
        btnRepeat = view.findViewById(R.id.btnRepeat);

        shuffleMode = false;
        playMode = true;
        repeatMode = RepeatMode.WITHOUT_REPEAT;

        btnShuffle.setOnClickListener(this);
        btnPlayPause.setOnClickListener(this);
        btnRepeat.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnShuffle:
                changeShuffleMode();
                break;
            case R.id.btnPlayPause:
                changeMode();
                break;
            case R.id.btnRepeat:
                changeRepeatMode();
                break;
        }

    }

    private void changeShuffleMode() {
        shuffleMode = !shuffleMode;
        if (shuffleMode) {
            btnShuffle.setImageTintList(ColorStateList.valueOf(Color.rgb(255, 65, 129)));
        } else {
            btnShuffle.setImageTintList(ColorStateList.valueOf(Color.WHITE));
        }
    }

    private void changeRepeatMode() {
        if (repeatMode == RepeatMode.WITHOUT_REPEAT) {
            repeatMode = RepeatMode.REPEAT;
            btnRepeat.setImageTintList(ColorStateList.valueOf(Color.rgb(255, 65, 129)));
        } else if (repeatMode == RepeatMode.REPEAT) {
            repeatMode = RepeatMode.REPEAT_ONE;
            btnRepeat.setImageResource(R.drawable.ic_repeat_one);
        } else {
            repeatMode = RepeatMode.WITHOUT_REPEAT;
            btnRepeat.setImageResource(R.drawable.ic_repeat);
            btnRepeat.setImageTintList(ColorStateList.valueOf(Color.WHITE));
        }
    }

    private void changeMode() {
        playMode = !playMode;
        if (playMode) {
            btnPlayPause.setImageResource(R.drawable.ic_pause);
        } else {
            btnPlayPause.setImageResource(R.drawable.ic_play);
        }
    }

    private enum RepeatMode {WITHOUT_REPEAT, REPEAT, REPEAT_ONE}
}
