package com.android.materialplayer.services;

import android.app.Notification;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.materialplayer.Settings;
import com.android.materialplayer.activities.MainActivity;
import com.android.materialplayer.models.ExtendedSong;

import java.util.List;

import static android.media.AudioManager.AUDIOFOCUS_GAIN;
import static android.media.AudioManager.AUDIOFOCUS_LOSS;
import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;
import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK;
import static android.media.AudioManager.STREAM_MUSIC;

/**
 * Created by dl1998 on 17.12.17.
 */

public class MusicService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    private static final String LOG_TAG = "MusicService";

    private MainActivity activity;

    private AudioManager audioManager;
    private MediaPlayer player;
    private List<ExtendedSong> songs;
    private int songPosition;

    private boolean playedNow;

    private IBinder musicBind;

    private float MEDIA_PLAYER_LEFT_VOLUME_LOW = 0.1f;
    private float MEDIA_PLAYER_RIGHT_VOLUME_LOW = 0.1f;
    private float MEDIA_PLAYER_LEFT_VOLUME = 0.1f;
    private float MEDIA_PLAYER_RIGHT_VOLUME = 0.1f;

    private AudioManager.OnAudioFocusChangeListener mAudioFocusListener;

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.songPosition = 0;
        this.playedNow = false;
        player = new MediaPlayer();

        initMusicPlayer();
    }

    private void setupAudioManager() {
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mAudioFocusListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                Log.d("AudioFocus", String.valueOf(focusChange));
                switch (focusChange) {
                    case AUDIOFOCUS_GAIN:
                        if (player != null && !player.isPlaying()) {
                            player.start();
                            //player.setVolume(MEDIA_PLAYER_LEFT_VOLUME, MEDIA_PLAYER_RIGHT_VOLUME);
                        }
                        break;
                    case AUDIOFOCUS_LOSS:
                        if (player != null && player.isPlaying()) {
                            Intent intent = new Intent("Main Activity");
                            intent.putExtra("playerState", "pause");
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                            int music_level = audioManager.getStreamVolume(STREAM_MUSIC);
                            int music_level_max = audioManager.getStreamMaxVolume(STREAM_MUSIC);
                            MEDIA_PLAYER_LEFT_VOLUME = (float) music_level / music_level_max;
                            MEDIA_PLAYER_RIGHT_VOLUME = (float) music_level / music_level_max;
                        }
                        break;
                    case AUDIOFOCUS_LOSS_TRANSIENT:
                        if (player != null && player.isPlaying()) {
                            player.pause();
                        }
                        break;
                    case AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                        if (player != null && player.isPlaying()) {
                            player.setVolume(MEDIA_PLAYER_LEFT_VOLUME_LOW,
                                    MEDIA_PLAYER_RIGHT_VOLUME_LOW);
                        }
                }
            }
        };
        audioManager.requestAudioFocus(mAudioFocusListener, AudioManager.STREAM_MUSIC, AUDIOFOCUS_GAIN);
    }

    /*@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(Settings.Action.STARTFOREGROUND_ACTION)){
            Log.i(LOG_TAG, "Received Start Foreground Intent");

            Intent previousIntent = new Intent(this, MusicService.class);
            previousIntent.setAction(Settings.Action.PREV_ACTION);
            PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
                    previousIntent, 0);

            Intent playIntent = new Intent(this, MusicService.class);
            playIntent.setAction(Settings.Action.PLAY_ACTION);
            PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                    playIntent, 0);

            Intent nextIntent = new Intent(this, MusicService.class);
            nextIntent.setAction(Settings.Action.NEXT_ACTION);
            PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                    nextIntent, 0);

        }
    }*/

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (musicBind == null) musicBind = new MusicBinder();
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        player.stop();
        player.release();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        nextSong();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        mediaPlayer.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

        mediaPlayer.start();

        ExtendedSong song = songs.get(songPosition);

        if (activity != null) {
            activity.setSong(song);
        }

        com.android.materialplayer.notifications.Notification notification =
                new com.android.materialplayer.notifications.Notification(this, "channel_id_0");
        notification.setChanelName("Player Notification");
        notification.setSong(song);
        Notification notificationMain = notification.create();
        startForeground(Settings.NOTIFICATION_ID.FOREGROUND_SERVICE, notificationMain);

    }

    public void initMusicPlayer() {
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        player.setAudioAttributes(audioAttributes);

        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);

        Settings.player = player;

        setupAudioManager();
    }

    public void setList(List<ExtendedSong> songs) {
        this.songs = songs;
    }

    public List<ExtendedSong> getList() {
        return this.songs;
    }

    public void setSong(int songIndex) {
        this.songPosition = songIndex;
    }

    public void previousSong() {
        if (songPosition > 0) {
            songPosition--;
            playSong();
        }
    }

    public void nextSong() {
        if (songPosition < songs.size() - 1) {
            songPosition++;
            playSong();
        }
    }

    public void playSong() {
        player.reset();

        ExtendedSong playedSong = songs.get(songPosition);
        Settings.song = playedSong;
        long currentSong = playedSong.getSongId();
        Uri trackUri = ContentUris.withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currentSong
        );

        try {
            player.setDataSource(getApplicationContext(), trackUri);
        } catch (Exception e) {
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }

        player.prepareAsync();
        playedNow = true;
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    public void playPause() {
        if (playedNow) {
            player.pause();
            playedNow = false;
        } else {
            player.start();
            playedNow = true;
        }
    }

    public void stopSong() {
        if (player != null) {
            player.reset();
            player.pause();
            playedNow = false;
        }
    }
}
