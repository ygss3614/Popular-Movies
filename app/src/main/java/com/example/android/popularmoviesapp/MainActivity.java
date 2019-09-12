package com.example.android.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.util.List;


import com.example.android.popularmoviesapp.model.MovieDB;
import com.example.android.popularmoviesapp.utilities.*;


import org.json.JSONException;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FrameLayout fl = findViewById(R.id.activity_main_frame_layout);
        fl.setBackgroundColor(Color.DKGRAY);

        URL popularMoviesURL = NetworkUtils.buildPopularMoviesUrl();
        new TheMovieDbQueryTask().execute(popularMoviesURL);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemThatWasSelected = item.getItemId();
        if( menuItemThatWasSelected == R.id.action_most_popular ){
            URL popularMoviesURL = NetworkUtils.buildPopularMoviesUrl();
            new TheMovieDbQueryTask().execute(popularMoviesURL);
            return true;
        }

        if( menuItemThatWasSelected == R.id.action_highest_rated ){
            URL highestRatedURL = NetworkUtils.buildHighestRated();
            new TheMovieDbQueryTask().execute(highestRatedURL);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    class TheMovieDbQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL searchURL = urls[0];
            String popularMoviesSearchResults = null;
            try {
                popularMoviesSearchResults = NetworkUtils.getResponseFromHttpUrl(searchURL);
            }catch (IOException e) {
                e.printStackTrace();
            }
            return popularMoviesSearchResults;
        }

        @Override
        protected void onPostExecute(String searchResult) {
            if (searchResult != null && !searchResult.equals(" ")) {
                try {
                    RecyclerView mMoviesList = findViewById(R.id.rv_movie_list);
                    GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 3);
                    mMoviesList.setLayoutManager(layoutManager);
                    List<MovieDB> movieDBList = JsonUtils.parseMovieDBJson(searchResult);
                    MoviesAdapter mAdapter = new MoviesAdapter(movieDBList, new MoviesAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(MovieDB movieDB) {
                            Context context = MainActivity.this;
                            if (movieDB != null){
                                Intent intent = new Intent(context, MoviesDetails.class);
                                intent.putExtra(MoviesDetails.EXTRA_MOVIE, movieDB );
                                context.startActivity(intent);
                            } else {
                                Toast.makeText(context, "movieDB object is null", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    mMoviesList.setAdapter(mAdapter);

                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }

    }


}
