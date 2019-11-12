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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
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
    private int actionSelected;
    private TextView currentPageTitle;
    private TextView connectionError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDb = AppDatabase.getInstance(getApplicationContext());

        currentPageTitle = findViewById(R.id.current_page_tv);
        connectionError = findViewById(R.id.connection_error_tv);
        connectionError.setVisibility(View.GONE);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SEARCH_URL_KEY)) {
                Integer queryCallback = savedInstanceState
                        .getInt(SEARCH_URL_KEY);

                if( queryCallback == R.id.action_most_popular ){
                    actionSelected = R.id.action_most_popular;
                    currentPageTitle.setText("Most Popular");
                    URL popularMoviesURL = NetworkUtils.buildPopularMoviesUrl();
                    makeMovieDbSearch(popularMoviesURL);
                }

                if( queryCallback == R.id.action_highest_rated ) {
                    actionSelected = R.id.action_highest_rated;
                    currentPageTitle.setText("Highest Rated");
                    URL highestRatedURL = NetworkUtils.buildPopularMovieHighestRated();
                    makeMovieDbSearch(highestRatedURL);
                }

                if (queryCallback == R.id.action_favorite_movies) {
                    actionSelected = R.id.action_favorite_movies;
                    currentPageTitle.setText("Favorite Movies");
                    final LiveData<List<MovieDB>> favoriteMovies =
                            mDb.movieDao().loadFavoriteMovies();
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
            currentPageTitle.setText("Most Popular");
            actionSelected = R.id.action_most_popular;
            URL popularMoviesURL = NetworkUtils.buildPopularMoviesUrl();
            makeMovieDbSearch(popularMoviesURL);
        }

    }

    public void makeMovieDbSearch(URL popularMoviesURL){
        LoaderManager loaderManager = getSupportLoaderManager();

            new MyAsyncTaskLoader(
                    new MyAsyncTaskLoader.AsyncResponse() {
                        @Override
                        public void processFinish(String output) {
                            try {
                                if( output != null){
                                    List<MovieDB> movieDBList = JsonUtils.parseMovieDBJson(output);
                                    initializeMovieObject(movieDBList);
                                }else{
                                    connectionError.setVisibility(View.VISIBLE);
                                    connectionError.setText("Unable to load the movie list! \n" +
                                            "Please connect to internet \n" +
                                            "to get the movie list");

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, this
            ).startAsyncTaskLoader(popularMoviesURL, loaderManager, MyAsyncTaskLoader.MOVIEDB_SEARCH_LOADER);



    }

    public void initializeMovieObject(List<MovieDB> movieDBList){
        if (movieDBList.size() == 0) {
            connectionError.setVisibility(View.VISIBLE);
            connectionError.setText("You don't have favorite movies! \n" +
                    "Tap the star button in movie details \n" +
                    "to save yours ");
        }
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
        connectionError.setVisibility(View.GONE);
        int menuItemThatWasSelected = item.getItemId();
        actionSelected = menuItemThatWasSelected;
        if( menuItemThatWasSelected == R.id.action_most_popular ){
            currentPageTitle.setText("Most Popular");
            URL popularMoviesURL = NetworkUtils.buildPopularMoviesUrl();
            makeMovieDbSearch(popularMoviesURL);
            return true;
        }

        if( menuItemThatWasSelected == R.id.action_highest_rated ) {
            currentPageTitle.setText("Highest Rated");
            URL highestRatedURL = NetworkUtils.buildPopularMovieHighestRated();
            makeMovieDbSearch(highestRatedURL);
            return true;
        }

        if (menuItemThatWasSelected == R.id.action_favorite_movies) {
            currentPageTitle.setText("Favorite Movies");
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
        outState.putInt(SEARCH_URL_KEY, actionSelected);
    }

}
