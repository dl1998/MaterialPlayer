package com.android.materialplayer.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.materialplayer.AsyncTaskEnded;
import com.android.materialplayer.R;
import com.android.materialplayer.Settings;
import com.android.materialplayer.adapters.ViewPagerAdapter;
import com.android.materialplayer.asyncloaders.AsyncLoadAlbums;
import com.android.materialplayer.asyncloaders.AsyncLoadArtists;
import com.android.materialplayer.asyncloaders.AsyncLoadSongs;
import com.android.materialplayer.listeners.AsyncEndListener;

public class MainActivity extends AppCompatActivity implements AsyncEndListener {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private AsyncLoadSongs taskSongs;
    private AsyncLoadAlbums taskAlbums;
    private AsyncLoadArtists taskArtists;

    private Integer asyncsEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        viewPager = findViewById(R.id.vpCategories);
        tabLayout = findViewById(R.id.tabLayout);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        asyncsEnd = 0;

        Settings.preferences = getSharedPreferences(Settings.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PlayedTrackActivity.class);
                startActivity(intent);
            }
        });
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
                        initTabs();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void initTabs() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
    }

    private void initNavigationView() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

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
                }

                return true;
            }
        });
    }

    private void showLibrary() {
        viewPager.setCurrentItem(0);
    }

    private void showPlaylist() {
        viewPager.setCurrentItem(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAsyncEnd(AsyncTaskEnded task) {
        if (task == taskSongs) asyncsEnd++;
        if (task == taskAlbums) asyncsEnd++;
        if (task == taskArtists) asyncsEnd++;

        if (isAllAsyncsEnd()) {
            initTabs();
        }
    }

    private boolean isAllAsyncsEnd() {
        return asyncsEnd >= 3;
    }
}
