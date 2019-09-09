package com.example.android.popularmoviesapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.popularmoviesapp.model.MovieDB;
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class MoviesDetails extends AppCompatActivity {
    public static final String EXTRA_MOVIE = "movie";
    private static final MovieDB DEFAULT_MOVIE = new MovieDB();

    private TextView mOriginalTitleTextView;
    private TextView mOverviewTextView;
    private TextView mReleaseDateTextView;
    private TextView mVoteAverageTextView;
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

        Intent intent = getIntent();

        if(intent == null){
            closeOnError();
        }

        MovieDB movieDB = Objects.requireNonNull(intent).getParcelableExtra(EXTRA_MOVIE);

        if (movieDB == DEFAULT_MOVIE) {
            // EXTRA_POSITION not fo und in intent
            closeOnError();
            return;
        }

        populateUI(movieDB);
    }

    private void closeOnError(){
        finish();
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

    }

}