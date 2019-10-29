package com.example.android.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesapp.model.MovieDB;
import com.example.android.popularmoviesapp.model.MovieReviews;
import com.example.android.popularmoviesapp.model.MovieVideos;
import com.example.android.popularmoviesapp.utilities.JsonUtils;
import com.example.android.popularmoviesapp.utilities.MyQueryTask;
import com.example.android.popularmoviesapp.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.net.URL;
import java.util.List;
import java.util.Objects;


public class MoviesDetails extends AppCompatActivity {
    public static final String EXTRA_MOVIE = "movie";
    private static final MovieDB DEFAULT_MOVIE = new MovieDB();

    private TextView mOriginalTitleTextView;
    private TextView mOverviewTextView;
    private TextView mReleaseDateTextView;
    private TextView mVoteAverageTextView;
    private TextView mMovieReviewTextView;
    private ImageView mThumbnailImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);
        LinearLayout ll = findViewById(R.id.movie_details_linear_layout);
        ll.setBackgroundColor(Color.DKGRAY);

        mOriginalTitleTextView = findViewById(R.id.original_title_tv);
        mOverviewTextView = findViewById(R.id.overview_tv);
        mReleaseDateTextView = findViewById(R.id.release_tv);
        mVoteAverageTextView = findViewById(R.id.vote_average_tv);
        mThumbnailImageView = findViewById(R.id.thumbnail_iv);
        mMovieReviewTextView = findViewById(R.id.movie_review_content_tv);


        Intent intent = getIntent();

        if(intent == null){
            closeOnError();
        }

        MovieDB movieDB = Objects.requireNonNull(intent).getParcelableExtra(EXTRA_MOVIE);

        if (movieDB == DEFAULT_MOVIE) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }


        populateUI(movieDB);
    }

    private void closeOnError(){
        finish();
    }

    private void fetchMovieReviews (int movieDB){
        URL movieReviewsURL = NetworkUtils.builPopularMovieReviews(movieDB);
        new MyQueryTask(new MyQueryTask.AsyncResponse(){

            @Override
            public void processFinish(String output){
                updateMovieReviews(output);
            }
        }).execute(movieReviewsURL);

    }

    private void fetchMovieVideos(int movieDB){
        URL movieVideosURL = NetworkUtils.builPopularMovieVideos(movieDB);
        new MyQueryTask(new MyQueryTask.AsyncResponse(){

            @Override
            public void processFinish(String output){
                initializeMovieObject(output);
            }
        }).execute(movieVideosURL);

    }

    private void updateMovieReviews(String stringReviews){
        try {
            List<MovieReviews> reviews =  JsonUtils.parseMovieReviewsJson(stringReviews);
            for (int i = 0; i < reviews.size(); i ++) {
                mMovieReviewTextView.append(reviews.get(i).getContent() + "\n");
            }

        } catch (JSONException e){
            e.printStackTrace();
        }

    }

    public void initializeMovieObject(String searchResultVideos){
        try {
            RecyclerView mMovieVideoList = findViewById(R.id.rv_movie_video_list);
            GridLayoutManager layoutManager = new GridLayoutManager(MoviesDetails.this, 1);
            mMovieVideoList.setLayoutManager(layoutManager);
            final List<MovieVideos> videos =  JsonUtils.parseMovieVideosJson(searchResultVideos);
            Toast.makeText(MoviesDetails.this, "test 1", Toast.LENGTH_LONG).show();
            MovieVideosAdapter mAdapter = new MovieVideosAdapter(videos, new MovieVideosAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(MovieVideos movieVideos) {
                    Context context = MoviesDetails.this;
                    if (movieVideos != null) {
                        playVideo(movieVideos.getKey());
                    } else {
                        Toast.makeText(context, "movieVideo object is null", Toast.LENGTH_LONG).show();
                    }
                }
            });
            mMovieVideoList.setAdapter(mAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void playVideo(String videoKey){
        Toast.makeText(MoviesDetails.this, "play button pressed", Toast.LENGTH_LONG).show();
        String youtube_base = "http://www.youtube.com/watch?v=%s";
        Uri videoUri = Uri.parse(String.format(youtube_base, videoKey));

        Intent intentMovieVideo = new Intent(Intent.ACTION_VIEW);
        intentMovieVideo.setData(videoUri);

        if (intentMovieVideo.resolveActivity(getPackageManager()) != null) {
            startActivity(intentMovieVideo);
        }else{
            startActivity(intentMovieVideo);
        }
    }


    private void populateUI(MovieDB movieDBObject){
        mOriginalTitleTextView.setText(movieDBObject.getOriginalTitle());
        mOverviewTextView.setText(movieDBObject.getOverview());
        mReleaseDateTextView.setText(movieDBObject.getReleaseDate());
        mVoteAverageTextView.setText(movieDBObject.getVoteAverage());

        Picasso.get()
                .load(movieDBObject.getThumbnailPosterPath())
                .error(R.mipmap.ic_launcher)
                .into(mThumbnailImageView);

        fetchMovieReviews(movieDBObject.getId());
        fetchMovieVideos(movieDBObject.getId());


    }

}