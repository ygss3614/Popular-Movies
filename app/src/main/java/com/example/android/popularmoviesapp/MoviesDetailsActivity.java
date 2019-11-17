package com.example.android.popularmoviesapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesapp.model.AppDatabase;
import com.example.android.popularmoviesapp.model.MovieDB;
import com.example.android.popularmoviesapp.model.MovieReviews;
import com.example.android.popularmoviesapp.model.MovieVideos;
import com.example.android.popularmoviesapp.utilities.JsonUtils;
import com.example.android.popularmoviesapp.utilities.MyAsyncTaskLoader;
import com.example.android.popularmoviesapp.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.net.URL;
import java.util.List;
import java.util.Objects;


public class MoviesDetailsActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIE = "movie";
    private static final MovieDB DEFAULT_MOVIE = new MovieDB();

    private TextView mOriginalTitleTextView;
    private TextView mOverviewTextView;
    private TextView mReleaseDateTextView;
    private TextView mVoteAverageTextView;
    private TextView mReviewsLabel;
    private TextView mVideosLabel;
    private ImageView mThumbnailImageView;
    private Button mFavoriteMovieButton;

    private AppDatabase mDb;
    private MovieDB currentMovie;

    private LiveData<MovieDB> savedMovieLD;
    private MovieDB savedMovie;
    private static final String LOG_TAG = AppDatabase.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

        initViews();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        if (intent == null) {
            closeOnError();
        }

        currentMovie = Objects.requireNonNull(intent).getParcelableExtra(EXTRA_MOVIE);

        if (currentMovie == DEFAULT_MOVIE) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        mDb = AppDatabase.getInstance(getApplicationContext());
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                savedMovie = mDb.movieDao().getMovieById(currentMovie.getId());
            }
        });

        populateUI(currentMovie);
    }

    private void initViews() {
        mOriginalTitleTextView = findViewById(R.id.original_title_tv);
        mOverviewTextView = findViewById(R.id.overview_tv);
        mReleaseDateTextView = findViewById(R.id.release_tv);
        mVoteAverageTextView = findViewById(R.id.vote_average_tv);
        mThumbnailImageView = findViewById(R.id.thumbnail_iv);
        mFavoriteMovieButton = findViewById(R.id.favorite_bt);
        mReviewsLabel = findViewById(R.id.reviews_label);
        mVideosLabel = findViewById(R.id.videos_label_tv);
        mFavoriteMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddFavoriteButtonClicked();
            }
        });
    }

    private void closeOnError() {
        finish();
    }


    public void initializeMovieReviewObject(String searchResultReview) {
        try {
            RecyclerView mMovieReviewsList = findViewById(R.id.rv_movie_review_list);
            GridLayoutManager layoutManager = new GridLayoutManager(MoviesDetailsActivity.this, 1);
            mMovieReviewsList.setLayoutManager(layoutManager);
            final List<MovieReviews> reviews = JsonUtils.parseMovieReviewsJson(searchResultReview);

            MovieReviewsAdapter mAdapter = new MovieReviewsAdapter(reviews);
            mMovieReviewsList.setAdapter(mAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void fetchMovieReviews(int movieID) {
        URL movieReviewsURL = NetworkUtils.builPopularMovieReviews(movieID);
        LoaderManager loaderManager = getSupportLoaderManager();
        new MyAsyncTaskLoader(
                new MyAsyncTaskLoader.AsyncResponse() {
                    @Override
                    public void processFinish(String output) {
                        if (output != null){
                            initializeMovieReviewObject(output);
                        } else {
                            mFavoriteMovieButton.setVisibility(View.INVISIBLE);
                            mVideosLabel.setVisibility(View.INVISIBLE);
                            mReviewsLabel.setVisibility(View.INVISIBLE);
                        }

                    }
                }, this
        ).startAsyncTaskLoader(movieReviewsURL, loaderManager, MyAsyncTaskLoader.MOVIE_REVIEWS_LOADER);

    }

    public void playVideo(String videoKey) {
        String youtube_base = "http://www.youtube.com/watch?v=%s";
        Uri videoUri = Uri.parse(String.format(youtube_base, videoKey));

        Intent intentMovieVideo = new Intent(Intent.ACTION_VIEW);
        intentMovieVideo.setData(videoUri);

        if (intentMovieVideo.resolveActivity(getPackageManager()) != null) {
            startActivity(intentMovieVideo);
        } else {
            startActivity(intentMovieVideo);
        }
    }

    public void initializeMovieObject(String searchResultVideos) {
        try {
            RecyclerView mMovieVideoList = findViewById(R.id.rv_movie_video_list);
            GridLayoutManager layoutManager = new GridLayoutManager(MoviesDetailsActivity.this,
                    1);
            mMovieVideoList.setLayoutManager(layoutManager);
            final List<MovieVideos> videos = JsonUtils.parseMovieVideosJson(searchResultVideos);
            MovieVideosAdapter mAdapter = new MovieVideosAdapter(videos, new MovieVideosAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(MovieVideos movieVideos) {
                    Context context = MoviesDetailsActivity.this;
                    if (movieVideos != null) {
                        playVideo(movieVideos.getKey());
                    } else {
                        Toast.makeText(context, "movieVideo object is null",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
            mMovieVideoList.setAdapter(mAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void fetchMovieVideos(int movieId) {
        URL movieVideosURL = NetworkUtils.builPopularMovieVideos(movieId);
        LoaderManager loaderManager = getSupportLoaderManager();
        new MyAsyncTaskLoader(
                new MyAsyncTaskLoader.AsyncResponse() {
                    @Override
                    public void processFinish(String output) {
                        if (output != null){
                            initializeMovieObject(output);
                        } else {
                            mFavoriteMovieButton.setVisibility(View.INVISIBLE);
                            mVideosLabel.setVisibility(View.INVISIBLE);
                            mReviewsLabel.setVisibility(View.INVISIBLE);

                        }
                    }
                }, this
        ).startAsyncTaskLoader(movieVideosURL, loaderManager, MyAsyncTaskLoader.MOVIE_VIDEOS_LOADER);
    }

    private void populateUI(MovieDB movieDBObject) {
        mOriginalTitleTextView.setText(movieDBObject.getOriginalTitle());
        mOverviewTextView.setText(movieDBObject.getOverview());
        mReleaseDateTextView.setText(movieDBObject.getReleaseDate());
        mVoteAverageTextView.setText(movieDBObject.getVoteAverage());

        Picasso.get()
                .load(movieDBObject.getThumbnailPosterPath())
                .error(R.mipmap.ic_launcher)
                .into(mThumbnailImageView);


        fetchMovieVideos(movieDBObject.getId());
        fetchMovieReviews(movieDBObject.getId());

        if (savedMovie != null) {
            mFavoriteMovieButton.setBackgroundResource(android.R.drawable.btn_star_big_on);
        } else {
            mFavoriteMovieButton.setBackgroundResource(android.R.drawable.btn_star_big_off);
        }
    }

    // onAddFavoriteButtonClicked is called when "favorite" button is clicked
    public void onAddFavoriteButtonClicked() {

        if (savedMovie != null) {
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.movieDao().deleteMovie(savedMovie);
                    mFavoriteMovieButton.setBackgroundResource(android.R.drawable.btn_star_big_off);
                }
            });

        } else {
            final MovieDB favoriteMovie = new MovieDB(currentMovie.getId(), currentMovie.getTitle(),
                    currentMovie.getOriginalTitle(), currentMovie.getOverview(),
                    currentMovie.getPosterPath(),
                    currentMovie.getThumbnailPosterPath(), currentMovie.getVoteAverage(),
                    currentMovie.getReleaseDate());
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.movieDao().insertMovie(favoriteMovie);
                    mFavoriteMovieButton.setBackgroundResource(android.R.drawable.btn_star_big_on);
                }
            });
        }
    }
}