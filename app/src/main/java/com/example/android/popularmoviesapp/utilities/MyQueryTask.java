package com.example.android.popularmoviesapp.utilities;

import android.os.AsyncTask;
import java.io.IOException;
import java.net.URL;


public class MyQueryTask extends AsyncTask<URL, Void, String> {

    public AsyncResponse delegate = null;

    public MyQueryTask(AsyncResponse delegate){
        this.delegate = delegate;
    }

    public interface AsyncResponse {
        void processFinish(String output);
    }

    @Override
    protected String doInBackground(URL... urls) {
        URL searchURL = urls[0];
        String popularMoviesSearchResults = null;
        try {
            popularMoviesSearchResults = NetworkUtils.getResponseFromHttpUrl(searchURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return popularMoviesSearchResults;
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }



}