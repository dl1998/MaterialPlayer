package com.android.materialplayer.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.android.materialplayer.R;
import com.android.materialplayer.bitmap_converter.BlurBuilder;
import com.android.materialplayer.models.ExtendedSong;
import com.android.materialplayer.services.MusicService;

import java.util.ArrayList;

/**
 * Created by dl1998 on 30.11.17.
 */

public class PlayedTrackActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivCover;
    private ImageView ivCoverBlur;

    private MusicService musicService;
    private Intent playIntent;
    private boolean musicBound;

    private ArrayList<ExtendedSong> songs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_played_track);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayedTrackActivity.this.finish();
            }
        });
        ivCover = findViewById(R.id.ivCover);
        ivCoverBlur = findViewById(R.id.ivCoverBlur);

        ivCover.setTag(R.drawable.cover_not_available);
        ivCoverBlur.setTag(R.drawable.cover_not_available);

        //long songId = getIntent().getLongExtra("songId", -1);

        //Log.d("SID", String.valueOf(songId));

        songs = new ArrayList<>();
        //songs.add(new Song(2974L, null, null, null));
    }

    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            musicService = binder.getService();
            musicService.setList(songs);

            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            musicBound = false;
        }
    };

    @Override
    public void onStart() {
        super.onStart();

        initCover();

        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }

    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        musicService = null;
        super.onDestroy();
    }

    /**
     * Change simple image view on circle image view
     */
    private void initCover() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), (Integer) ivCover.getTag());
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundedBitmapDrawable.setCircular(true);
        ivCover.setImageDrawable(roundedBitmapDrawable);

        Bitmap blurBitmap = BlurBuilder.blur(this, BitmapFactory.decodeResource(getResources(), (Integer) ivCoverBlur.getTag()));
        ivCoverBlur.setImageBitmap(blurBitmap);
    }

    @Override
    public void onClick(View view) {
    }
}
