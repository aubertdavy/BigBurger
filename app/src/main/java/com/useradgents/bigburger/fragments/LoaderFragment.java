package com.useradgents.bigburger.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

/**
 * Created by AUBERT on 11/03/2017.
 */

public abstract class LoaderFragment<T> extends Fragment implements LoaderManager.LoaderCallbacks<T> {

    public abstract Loader onCreateLoader(int id, Bundle args);

    public abstract void onLoaded(int id, T result);

    public static class ContentLoader<T> extends AsyncTaskLoader {
        T mLastResult;
        T mResult;

        public ContentLoader(Context context, T result) {
            super(context);
            mResult = result;
        }

        @Override
        public Object loadInBackground() {
            try {
                mLastResult = mResult;
                return mLastResult;
            }
            catch (Exception e)  {
                return new Object();
            }
        }

        @Override
        protected void onStartLoading() {
            if (takeContentChanged() || mLastResult == null) {
                forceLoad();
            } else {
                deliverResult(mLastResult);
            }
        }

        @Override
        protected void onReset() {
            super.onReset();
            onStopLoading();
        }

        @Override
        protected void onStopLoading() {
            super.onStopLoading();
            cancelLoad();
        }
    }
}