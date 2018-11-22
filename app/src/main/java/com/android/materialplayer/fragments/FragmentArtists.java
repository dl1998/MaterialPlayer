package com.android.materialplayer.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.materialplayer.AsyncTaskEnded;
import com.android.materialplayer.R;
import com.android.materialplayer.Settings;
import com.android.materialplayer.activities.MainActivity;
import com.android.materialplayer.adapters.ArtistsAdapter;
import com.android.materialplayer.adapters.ItemClickListener;
import com.android.materialplayer.asyncloaders.AsyncAddArtists;
import com.android.materialplayer.comparators.ArtistComparator;
import com.android.materialplayer.json_objects.LastFM;
import com.android.materialplayer.listeners.AsyncEndListener;
import com.android.materialplayer.models.Artist;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by dl1998 on 02.12.17.
 */

public class FragmentArtists extends AbstractTabFragment {

    private ArtistsAdapter adapter;

    private RecyclerView listArtists;
    private MenuItem miReversed;

    private ArrayList<Artist> artists;

    public static FragmentArtists newInstance(Context context) {
        FragmentArtists fragment = new FragmentArtists();

        fragment.context = context;
        fragment.setArguments(new Bundle());
        fragment.setTitle(context.getString(R.string.artists));

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_list, parent, false);

        listArtists = view.findViewById(R.id.rvList);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        adapter = new ArtistsAdapter(getActivity(), new ArrayList<Artist>());
        int numColumn = 2;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            numColumn = 2;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            numColumn = 3;
        listArtists.setLayoutManager(new GridLayoutManager(getActivity(), numColumn));
        listArtists.setAdapter(adapter);

        AsyncAddArtists asyncAddArtists = new AsyncAddArtists(getActivity(), adapter);


        adapter.setOnClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (!isLongClick) {
                    Artist artist = artists.get(position);
                    MainActivity activity = (MainActivity) getActivity();
                    setHasOptionsMenu(false);
                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.switchableLayout, FragmentArtist.getInstance(artist));
                    fragmentTransaction.addToBackStack("fragment_artist");
                    fragmentTransaction.commit();
                }
            }
        });

        /*adapter.setOnClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (!isLongClick) {
                    List<Artist> artists = adapter.getAdapter();
                    Artist artist = artists.get(position);

                    new AsyncTaskArtist().execute(artist.getArtistName(), String.valueOf(position));
                }
            }
        });*/
        asyncAddArtists.setOnAsyncEnd(new AsyncEndListener() {
            @Override
            public void onAsyncEnd(AsyncTaskEnded task) {
                artists = adapter.getAdapter();
                //new AsyncTaskArtist(artists).execute();
            }
        });

        asyncAddArtists.execute();
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.artist_sort_by, menu);
        miReversed = menu.findItem(R.id.action_reversed);

        final Integer ordinal = Settings.preferences.getInt(Settings.ARTIST_ORDER, 0);
        final Boolean reversed = Settings.preferences.getBoolean(Settings.ARTIST_REVERSED, false);

        if (reversed) miReversed.setChecked(true);
        if (ordinal == 0) menu.findItem(R.id.sort_by_artist_in_artist).setChecked(true);
        else if (ordinal == 1) menu.findItem(R.id.sort_by_no_albums).setChecked(true);
        else if (ordinal == 2) menu.findItem(R.id.sort_by_no_tracks).setChecked(true);

    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reversed:
                toggleReversed(item);
                break;
            case R.id.sort_by_artist_in_artist:
            case R.id.sort_by_no_albums:
            case R.id.sort_by_no_tracks:
                item.setChecked(true);
                final Settings.ArtistOrder order = ArtistComparator.getOrder(item);
                adapter.sort(new Comparator<Artist>() {
                    @Override
                    public int compare(Artist artist, Artist artist1) {
                        return ArtistComparator.compareArtist(artist, artist1, order);
                    }
                });
                if (miReversed.isChecked()) reversed();
                saveSortOrder(order);
                break;
        }
        return true;
    }

    private void saveSortOrder(Settings.ArtistOrder order) {
        Settings.preferences.edit().putInt(Settings.ARTIST_ORDER, order.ordinal()).apply();
    }

    private void saveSortReversed(Boolean reversed) {
        Settings.preferences.edit().putBoolean(Settings.ARTIST_REVERSED, reversed).apply();
    }

    private void reversed() {
        adapter.reversed();
    }

    private void toggleReversed(MenuItem item) {
        if (item.isChecked()) {
            item.setChecked(false);
            saveSortReversed(false);
        } else {
            item.setChecked(true);
            saveSortReversed(true);
        }
        reversed();
    }

    class AsyncTaskArtist extends AsyncTask<Void, String, Void> {

        private ArrayList<Artist> artists;

        public AsyncTaskArtist(ArrayList<Artist> artists) {
            this.artists = artists;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //String uri = "http://ws.audioscrobbler.com/2.0/?method=artist.getinfo&artist=%s&api_key=5e51c3a25256e2f5f7396c9db2786b21&format=json";

            for (int position = 0; position < 10/*artists.size()*/; position++) {
                LastFM data = null;
                String URI = String.format(Settings.URI, Settings.Methods.ARTIST_INFO, artists.get(position).getArtistName());

                //this.position = Integer.parseInt(strings[1]);

                //String newUri = String.format(uri, strings[0]);

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(URI)
                        .get()
                        .build();

                try {
                    Response response = client.newCall(request).execute();

                    int status = response.code();
                    Log.d("Status", String.valueOf(response.code()));

                    String json = response.body().string();

                    Gson gson = new Gson();
                    data = gson.fromJson(json, LastFM.class);

                    Log.d("IMG_SRC", data.getArtist().getImage().get(data.getArtist().getImage().size() - 1).getText());
                    if (status == 200)
                        publishProgress(String.valueOf(position), data.getArtist().getImage().get(data.getArtist().getImage().size() - 1).getText());

                    //Log.d("Info", data.getArtist().getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
            //return data;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            //List<Image> images = values[0].getArtist().getImage();
            //artist.setArtPath(images.get(images.size() - 1).getText());

            int position = Integer.parseInt(values[0]);

            Artist artist = artists.get(position);
            artist.setArtPath(values[1]);

            artists.set(position, artist);
            adapter.setAdapter(artists);
        }

        @Override
        protected void onPostExecute(Void values) {
            super.onPostExecute(values);

            adapter.setAdapter(artists);
        }
    }
}
