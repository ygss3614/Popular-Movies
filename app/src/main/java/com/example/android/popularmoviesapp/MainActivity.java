package com.example.android.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.android.popularmoviesapp.model.AppDatabase;
import com.example.android.popularmoviesapp.model.MovieDB;
import com.example.android.popularmoviesapp.utilities.JsonUtils;
import com.example.android.popularmoviesapp.utilities.MyQueryTask;
import com.example.android.popularmoviesapp.utilities.MyAsyncTaskLoader;
import com.example.android.popularmoviesapp.utilities.NetworkUtils;

import org.json.JSONException;

import java.net.URL;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private AppDatabase mDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        URL popularMoviesURL = NetworkUtils.buildPopularMoviesUrl();
        makeMovieDbSearch(popularMoviesURL);
        mDb = AppDatabase.getInstance(getApplicationContext());
    }

    public void makeMovieDbSearch(URL popularMoviesURL){
        LoaderManager loaderManager = getSupportLoaderManager();
        new MyAsyncTaskLoader(
                new MyAsyncTaskLoader.AsyncResponse(){
                    @Override
                    public void processFinish(String output){
                        try {
                            List<MovieDB> movieDBList = JsonUtils.parseMovieDBJson(output);
                            initializeMovieObject(movieDBList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, this
            ).startAsyncTaskLoader(popularMoviesURL, loaderManager, MyAsyncTaskLoader.MOVIEDB_SEARCH_LOADER);

    }

    public void initializeMovieObject(List<MovieDB> movieDBList){
        RecyclerView mMoviesList = findViewById(R.id.rv_movie_list);
        GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2);
        mMoviesList.setLayoutManager(layoutManager);

        MoviesAdapter mAdapter = new MoviesAdapter(movieDBList, new MoviesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MovieDB movieDB) {
                Context context = MainActivity.this;
                if (movieDB != null) {
                    Intent intent = new Intent(context, MoviesDetails.class);
                    intent.putExtra(MoviesDetails.EXTRA_MOVIE, movieDB);
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "movieDB object is null", Toast.LENGTH_LONG).show();
                }
            }
        });
        mMoviesList.setAdapter(mAdapter);
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
            makeMovieDbSearch(popularMoviesURL);
            return true;
        }

        if( menuItemThatWasSelected == R.id.action_highest_rated ) {
            URL highestRatedURL = NetworkUtils.buildPopularMovieHighestRated();
            makeMovieDbSearch(highestRatedURL);
            return true;
        }

        if (menuItemThatWasSelected == R.id.action_favorite_movies) {
            List<MovieDB> favoriteMovieList = mDb.movieDao().loadFavoriteMovies();
            initializeMovieObject(favoriteMovieList);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
