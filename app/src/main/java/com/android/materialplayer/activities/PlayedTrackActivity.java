package com.android.materialplayer.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.android.materialplayer.BlurBuilder;
import com.android.materialplayer.R;

/**
 * Created by dl1998 on 30.11.17.
 */

public class PlayedTrackActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivCover;
    private ImageView ivCoverBlur;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_played_track);

        Toolbar toolbar = findViewById(R.id.toolbar_played_music);
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
    }

    @Override
    public void onStart() {
        super.onStart();

        /* Change simple image view on circle image view */
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
