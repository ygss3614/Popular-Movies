package com.example.android.popularmoviesapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.popularmoviesapp.model.AppDatabase;
import com.example.android.popularmoviesapp.model.MovieDB;
import com.example.android.popularmoviesapp.utilities.JsonUtils;
import com.example.android.popularmoviesapp.utilities.MyAsyncTaskLoader;
import com.example.android.popularmoviesapp.utilities.NetworkUtils;

import org.json.JSONException;

import java.net.URL;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private AppDatabase mDb;
    private static final String SEARCH_URL_KEY = "search_url";
    private int menuItemThatWasSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO salvar a query que esta sendo utilizada pra não retornar para a url principal

        mDb = AppDatabase.getInstance(getApplicationContext());

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SEARCH_URL_KEY)) {
                Integer queryCallback = savedInstanceState
                        .getInt(SEARCH_URL_KEY);
                if( queryCallback == R.id.action_most_popular ){
                    URL popularMoviesURL = NetworkUtils.buildPopularMoviesUrl();
                    makeMovieDbSearch(popularMoviesURL);
                }

                if( queryCallback == R.id.action_highest_rated ) {
                    URL highestRatedURL = NetworkUtils.buildPopularMovieHighestRated();
                    makeMovieDbSearch(highestRatedURL);
                }

                if (queryCallback == R.id.action_favorite_movies) {
                    final LiveData<List<MovieDB>> favoriteMovies = mDb.movieDao().loadFavoriteMovies();
                    favoriteMovies.observe(this, new Observer<List<MovieDB>>() {
                        @Override
                        public void onChanged(@Nullable List<MovieDB> movieDBS) {
                            favoriteMovies.removeObserver(this);
                            initializeMovieObject(movieDBS);
                        }
                    });
                }
            }
        }else{
            URL popularMoviesURL = NetworkUtils.buildPopularMoviesUrl();
            makeMovieDbSearch(popularMoviesURL);
        }

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
        menuItemThatWasSelected = item.getItemId();
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
            final LiveData<List<MovieDB>> favoriteMovies = mDb.movieDao().loadFavoriteMovies();
            favoriteMovies.observe(this, new Observer<List<MovieDB>>() {
                @Override
                public void onChanged(@Nullable List<MovieDB> movieDBS) {
                    favoriteMovies.removeObserver(this);
                    initializeMovieObject(movieDBS);
                }
            });

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        Log.d("OnSavedInstanceMethod", "OnSavedInstanceMethod");
        outState.putInt(SEARCH_URL_KEY, menuItemThatWasSelected);
    }

}
