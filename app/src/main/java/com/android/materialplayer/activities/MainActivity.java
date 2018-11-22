package com.android.materialplayer.activities;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.materialplayer.AsyncTaskEnded;
import com.android.materialplayer.R;
import com.android.materialplayer.Settings;
import com.android.materialplayer.asyncloaders.AsyncLoadAlbums;
import com.android.materialplayer.asyncloaders.AsyncLoadArtists;
import com.android.materialplayer.asyncloaders.AsyncLoadSongs;
import com.android.materialplayer.bitmap_converter.BlurBuilder;
import com.android.materialplayer.fragments.FragmentMain;
import com.android.materialplayer.listeners.AsyncEndListener;
import com.android.materialplayer.models.ExtendedSong;
import com.android.materialplayer.services.MusicService;

import java.util.Collections;

public class MainActivity extends AppCompatActivity implements AsyncEndListener, View.OnClickListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private AsyncLoadSongs taskSongs;
    private AsyncLoadAlbums taskAlbums;
    private AsyncLoadArtists taskArtists;

    private Integer asyncsEnd;

    private ConstraintLayout peekLayout;
    private BottomSheetBehavior bottomSheetBehavior;

    private Intent playIntent;
    private MusicService musicService;
    private boolean musicBound;

    private ImageView ivSmallCover;
    private TextView tvSongNameSmall;
    private TextView tvArtistNameSmall;
    private TextView tvSongName;
    private TextView tvArtistName;
    private ImageView ivCover;
    private ImageButton btnPlayPause;
    private ImageButton ibNavPlayPause;
    private ImageButton ibPrevious;
    private ImageButton ibNext;
    private SeekBar seekBar;
    private ProgressBar progressBar;

    private CoordinatorLayout switchableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConstraintLayout bottomSheetLayout = findViewById(R.id.bottomSheet);
        peekLayout = findViewById(R.id.peekLayout);

        ivSmallCover = findViewById(R.id.ivNavCoverSmall);
        tvSongNameSmall = findViewById(R.id.tvSongNameSmall);
        tvArtistNameSmall = findViewById(R.id.tvArtistNameSmall);
        tvSongName = findViewById(R.id.tvSongName);
        tvArtistName = findViewById(R.id.tvArtistName);
        ivCover = findViewById(R.id.ivNavCover);
        btnPlayPause = findViewById(R.id.ibNavButton);
        ibNavPlayPause = findViewById(R.id.ibNavPlayPause);
        ibNext = findViewById(R.id.ibNavNext);
        ibPrevious = findViewById(R.id.ibNavPrevious);
        seekBar = findViewById(R.id.sbNav);
        progressBar = findViewById(R.id.pbNavTime);

        switchableLayout = findViewById(R.id.switchableLayout);

        btnPlayPause.setOnClickListener(this);
        ibNavPlayPause.setOnClickListener(this);
        ibPrevious.setOnClickListener(this);
        ibNext.setOnClickListener(this);

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        asyncsEnd = 0;

        ivCover.setTag(R.drawable.cover_not_available);

        Settings.preferences = getSharedPreferences(Settings.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        Bitmap blurBitmap = BlurBuilder.blur(this, BitmapFactory.decodeResource(getResources(), (Integer) ivCover.getTag()));
        ivCover.setImageBitmap(blurBitmap);
    }

    @Override
    protected void onStart() {
        super.onStart();

        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                boolean state;

                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    state = false;
                } else {
                    state = true;
                }

                switchableLayout.setClickable(state);
                switchableLayout.setActivated(state);
                switchableLayout.setEnabled(state);

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                peekLayout.animate().alpha(1 - slideOffset).setDuration(0).start();
            }
        });

        startPlayIntent();

        handleSeekBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deinitSeekBar();
        /*stopService(playIntent);
        unbindService(Settings.serviceConnection);

        musicService = null;
        Settings.musicService = null;
        Settings.serviceConnection = musicConnection;
        Settings.playIntent = playIntent;
        super.onDestroy();*/
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStack();
        } else {
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    initNavigationView();

                    Boolean first_run = Settings.preferences.getBoolean(Settings.SP_FIRST_RUN, true);

                    taskSongs = new AsyncLoadSongs(this);
                    taskAlbums = new AsyncLoadAlbums(this);
                    taskArtists = new AsyncLoadArtists(this);

                    taskSongs.setOnAsyncEnd(this);
                    taskAlbums.setOnAsyncEnd(this);
                    taskArtists.setOnAsyncEnd(this);

                    if (first_run) {
                        asyncsEnd = 0;

                        taskSongs.execute(Settings.DB_ADD);
                        taskAlbums.execute(Settings.DB_ADD);
                        taskArtists.execute(Settings.DB_ADD);

                        SharedPreferences.Editor editor = Settings.preferences.edit();
                        editor.putBoolean(Settings.SP_FIRST_RUN, false);
                        editor.apply();
                    } else {
                        initFragmentWithViewPager();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void initFragmentWithViewPager() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.switchableLayout, new FragmentMain());
        fragmentTransaction.addToBackStack("fragment_main");
        fragmentTransaction.commit();
    }

    public void initActionBarDrawerToggle(Toolbar toolbar) {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.iLibrary:
                        showLibrary();
                        break;
                    case R.id.iPlaylist:
                        showPlaylist();
                        break;
                    case R.id.iNowPlaying:
                        showNowPlaying();
                        break;
                }

                return true;
            }
        });
    }

    private void showNowPlaying() {
        Intent intent = new Intent(MainActivity.this, PlayedTrackActivity.class);
        startActivity(intent);
    }

    private void showLibrary() {
        //viewPager.setCurrentItem(0);
    }

    private void showPlaylist() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_shuffle) {
            Settings.player.stop();
            Collections.shuffle(Settings.songs);
            Settings.song = Settings.songs.get(0);
            setSong(Settings.song);
            play();
        }

        return super.onOptionsItemSelected(item);
    }

    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            musicService = binder.getService();
            musicService.setActivity(MainActivity.this);
            Settings.musicService = musicService;
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    public void onAsyncEnd(AsyncTaskEnded task) {
        if (task == taskSongs) asyncsEnd++;
        if (task == taskAlbums) asyncsEnd++;
        if (task == taskArtists) asyncsEnd++;

        if (isAllAsyncsEnd()) {
            initFragmentWithViewPager();
        }
    }

    private boolean isAllAsyncsEnd() {
        return asyncsEnd >= 3;
    }

    private void togglePlay() {
        if (Settings.player != null) {
            if (Settings.player.isPlaying()) {
                pause();
            } else {
                play();
            }
        }
        Settings.musicService.playPause();
    }

    private void startPlayIntent() {
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
            Settings.serviceConnection = musicConnection;
            Settings.playIntent = playIntent;
        }
    }

    private void handleSeekBar() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (Settings.player != null && fromUser) {
                    Settings.player.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private Handler handler;
    private Runnable runnable;

    public void initSeekBar() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                seekBar.setMax(Settings.song.getDuration() / 1000);
                progressBar.setMax(Settings.song.getDuration() / 1000);
                int mpCurrentPosition = Settings.player.getCurrentPosition() / 1000;
                seekBar.setProgress(mpCurrentPosition);
                progressBar.setProgress(mpCurrentPosition);
                handler.postDelayed(this, 1000);
            }
        };
        runOnUiThread(runnable);
    }

    public void deinitSeekBar() {
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }

    private void play() {
        btnPlayPause.setImageResource(R.drawable.ic_simple_pause);
        ibNavPlayPause.setImageResource(R.drawable.ic_simple_pause);
    }

    private void pause() {
        btnPlayPause.setImageResource(R.drawable.ic_simple_play);
        ibNavPlayPause.setImageResource(R.drawable.ic_simple_play);
    }

    public void setSong(ExtendedSong song) {
        Bitmap cov = BitmapFactory.decodeFile(song.getArtPath());
        if (cov == null) cov = BitmapFactory.decodeResource(getResources(), R.drawable.headphone);
        ivCover.setImageBitmap(BlurBuilder.blur(this, cov));
        ivSmallCover.setImageBitmap(cov);
        tvArtistNameSmall.setText(song.getArtistName());
        tvArtistName.setText(song.getArtistName());
        tvSongNameSmall.setText(song.getSongName());
        tvSongName.setText(song.getSongName());
        btnPlayPause.setImageResource(R.drawable.ic_simple_pause);
        ibNavPlayPause.setImageResource(R.drawable.ic_simple_pause);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibNavButton:
            case R.id.ibNavPlayPause:
                togglePlay();
                break;
            case R.id.ibNavPrevious:
                Settings.musicService.previousSong();
                setSong(Settings.song);
                break;
            case R.id.ibNavNext:
                Settings.musicService.nextSong();
                setSong(Settings.song);
                break;
        }
    }
}
