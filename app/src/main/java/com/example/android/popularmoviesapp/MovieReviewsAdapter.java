package com.example.android.popularmoviesapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmoviesapp.model.MovieReviews;

import java.util.List;

public class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsAdapter.MovieReviewsViewHolder> {


    private final List<MovieReviews> mMovieReviewsList;
    private TextView listItemReviewContent;
    private TextView listItemReviewAuthor;



    public MovieReviewsAdapter(List<MovieReviews> movieReviewsList) {
        mMovieReviewsList = movieReviewsList;
    }

    @NonNull
    @Override
    public MovieReviewsAdapter.MovieReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForMovieItem = R.layout.movie_review_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForMovieItem, viewGroup, false);
        return new MovieReviewsAdapter.MovieReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieReviewsAdapter.MovieReviewsViewHolder movieViewHolder, int i) {
        movieViewHolder.bind(mMovieReviewsList.get(i));
    }


    @Override
    public int getItemCount() {
        return mMovieReviewsList.size();
    }


    class MovieReviewsViewHolder extends RecyclerView.ViewHolder {

        MovieReviewsViewHolder(View itemView) {
            super(itemView);
            listItemReviewContent = itemView.findViewById(R.id.tv_movie_review_resume);
            listItemReviewAuthor = itemView.findViewById(R.id.tv_movie_review_author);
        }

        void bind(final MovieReviews movieReviews) {
            listItemReviewContent.setText(movieReviews.getContent());
            listItemReviewAuthor.setText(movieReviews.getAuthor());
        }

    }
}
