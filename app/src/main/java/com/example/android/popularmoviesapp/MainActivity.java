package com.example.android.popularmoviesapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    private static final String KEY_INSTANCE_STATE_RV_POSITION = "state_rv_position";
    private int actionSelected;
    private TextView currentPageTitle;
    private TextView connectionError;
    private GridLayoutManager mLayoutManager;
    private Parcelable mLayoutManagerSavedState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDb = AppDatabase.getInstance(getApplicationContext());
        mLayoutManager = new GridLayoutManager(MainActivity.this, 2);

        currentPageTitle = findViewById(R.id.current_page_tv);
        connectionError = findViewById(R.id.connection_error_tv);
        connectionError.setVisibility(View.GONE);


        if (savedInstanceState != null) {

            if (savedInstanceState.containsKey(KEY_INSTANCE_STATE_RV_POSITION)) {
                mLayoutManagerSavedState = savedInstanceState
                        .getParcelable(KEY_INSTANCE_STATE_RV_POSITION);

                if (savedInstanceState.containsKey(SEARCH_URL_KEY)) {
                    Integer queryCallback = savedInstanceState
                            .getInt(SEARCH_URL_KEY);

                    if( queryCallback == R.id.action_most_popular ){
                        actionSelected = R.id.action_most_popular;
                        currentPageTitle.setText(R.string.most_popular);
                        URL popularMoviesURL = NetworkUtils.buildPopularMoviesUrl();
                        makeMovieDbSearch(popularMoviesURL);
                    }

                    if( queryCallback == R.id.action_highest_rated ) {
                        actionSelected = R.id.action_highest_rated;
                        currentPageTitle.setText(R.string.highest_rated);
                        URL highestRatedURL = NetworkUtils.buildPopularMovieHighestRated();
                        makeMovieDbSearch(highestRatedURL);
                    }

                    if (queryCallback == R.id.action_favorite_movies) {
                        actionSelected = R.id.action_favorite_movies;
                        currentPageTitle.setText(R.string.favorite_movies);
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

                if (mLayoutManagerSavedState != null){
                    mLayoutManager.onRestoreInstanceState(mLayoutManagerSavedState);
                }
            }

        }else{
            currentPageTitle.setText(R.string.most_popular);
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
                                    connectionError.setText(R.string.no_internet_connection);

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
            connectionError.setText(R.string.no_favorite_movies);
        }
        RecyclerView mMoviesList = findViewById(R.id.rv_movie_list);

        mMoviesList.setLayoutManager(mLayoutManager);

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
        actionSelected = item.getItemId();


        if( actionSelected == R.id.action_most_popular ){
            Log.d("MENU",   "MOST POPULAR");
            currentPageTitle.setText(R.string.most_popular);
            URL popularMoviesURL = NetworkUtils.buildPopularMoviesUrl();
            makeMovieDbSearch(popularMoviesURL);
            return true;
        }

        if( actionSelected == R.id.action_highest_rated ) {
            Log.d("MENU",   "HIGHEST RATED");
            currentPageTitle.setText(R.string.highest_rated);
            URL highestRatedURL = NetworkUtils.buildPopularMovieHighestRated();
            makeMovieDbSearch(highestRatedURL);
            return true;
        }

        if (actionSelected == R.id.action_favorite_movies) {
            Log.d("MENU",   "FAVORITE MOVIES");
            currentPageTitle.setText(R.string.favorite_movies);
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
        outState.putParcelable(KEY_INSTANCE_STATE_RV_POSITION, mLayoutManager.onSaveInstanceState());
    }

}
