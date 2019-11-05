package com.example.android.popularmoviesapp.utilities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import com.example.android.popularmoviesapp.MainActivity;

import java.io.IOException;
import java.net.URL;

public class MyAsyncTaskLoader extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<String>{

    public static final String SEARCH_MOVIEDB_URL_EXTRA = "query";
    public static final int MOVIEDB_SEARCH_LOADER = 22;
    public static final int MOVIE_VIDEOS_LOADER = 23;
    public static final int MOVIE_REVIEWS_LOADER = 24;
    public AsyncResponse delegate = null;
    public Context context;


    public MyAsyncTaskLoader(AsyncResponse delegate, Context context){
        this.delegate = delegate;
        this.context = context;

    }

    public interface AsyncResponse {
        void processFinish(String output);
    }

    public void startAsyncTaskLoader(URL movieURL, LoaderManager loaderManager, int loaderID){
        Bundle urlBundle = new Bundle();
        urlBundle.putString(SEARCH_MOVIEDB_URL_EXTRA, movieURL.toString());

        Loader<String> movieDbSearchLoader = loaderManager
                .getLoader(loaderID);

        if (movieDbSearchLoader == null){
            loaderManager.initLoader(loaderID, urlBundle, this);
        }else{
            loaderManager.restartLoader(loaderID, urlBundle, this);
        }
    }

    @Override
    public Loader<String> onCreateLoader(int i, final Bundle bundle) {

        return new AsyncTaskLoader<String>(this.context) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                if(bundle == null)
                    return;
                forceLoad();
            }

            @Override
            public String loadInBackground() {
                String searchResult = null;
                if (bundle.containsKey(SEARCH_MOVIEDB_URL_EXTRA)){
                    String searchUrl = bundle.getString(SEARCH_MOVIEDB_URL_EXTRA);

                    if (searchUrl == null){
                        return null;
                    }
                    try {
                        URL movieDbUrl = new URL(searchUrl);
                        return NetworkUtils.getResponseFromHttpUrl(movieDbUrl);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                return searchResult;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String searchResult) {
        delegate.processFinish(searchResult);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

}

