package com.example.android.popularmoviesapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesapp.model.MovieVideos;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieVideosAdapter extends RecyclerView.Adapter<MovieVideosAdapter.MovieVideosViewHolder> {


    public interface OnItemClickListener {
        void onItemClick(MovieVideos movieVideos);
    }

    private final List<MovieVideos> mMovieVideosList;
    private final MovieVideosAdapter.OnItemClickListener listener;
    private TextView listItemMovieLabel;
    private Button listItemMovieButton;


    public MovieVideosAdapter(List<MovieVideos> movieVideosList, MovieVideosAdapter.OnItemClickListener listenerOnItemClickListener) {
        mMovieVideosList = movieVideosList;
        listener = listenerOnItemClickListener;

    }

    @NonNull
    @Override
    public MovieVideosAdapter.MovieVideosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForMovieItem = R.layout.movie_video_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForMovieItem, viewGroup, false);

        return new MovieVideosAdapter.MovieVideosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieVideosAdapter.MovieVideosViewHolder movieViewHolder, int i) {
        movieViewHolder.bind(mMovieVideosList.get(i), listener);
    }


    @Override
    public int getItemCount() {
        return mMovieVideosList.size();
    }


    class MovieVideosViewHolder extends RecyclerView.ViewHolder {

        MovieVideosViewHolder(View itemView) {
            super(itemView);
            listItemMovieLabel = itemView.findViewById(R.id.tv_movie_video_label);
            listItemMovieButton = itemView.findViewById(R.id.bt_movie_video_player);
        }

        void bind(final MovieVideos movieVideos, final MovieVideosAdapter.OnItemClickListener listener) {

            listItemMovieLabel.setText(movieVideos.getName().toString());
            listItemMovieButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(movieVideos);
                }
            });

        }

    }
}
