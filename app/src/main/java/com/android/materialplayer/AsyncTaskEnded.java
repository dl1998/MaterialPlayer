package com.android.materialplayer;

import android.os.AsyncTask;

import com.android.materialplayer.listeners.AsyncEndListener;

/**
 * Created by dl1998 on 10.12.17.
 */

public abstract class AsyncTaskEnded<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    private AsyncEndListener asyncEndListener;

    public void setOnAsyncEnd(AsyncEndListener listener) {
        this.asyncEndListener = listener;
    }

    private void stop() {
        if (asyncEndListener != null) {
            this.asyncEndListener.onAsyncEnd(this);
        }
    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        this.stop();
    }
}
