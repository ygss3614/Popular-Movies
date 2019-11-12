package com.example.android.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesapp.model.MovieDB;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>{


    public interface OnItemClickListener {
        void onItemClick(MovieDB movieDB);
    }

    private final List<MovieDB> mMoviesList;
    private final OnItemClickListener listener;
    private ImageView listItemMoviePoster;
    private TextView listItemMovieTitle;


    public MoviesAdapter (List<MovieDB> moviesList, OnItemClickListener listenerOnItemClickListener) {
        mMoviesList = moviesList;
        listener = listenerOnItemClickListener;

    }
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForMovieItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForMovieItem, viewGroup, false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int i) {
        movieViewHolder.bind(mMoviesList.get(i), listener);
    }


    @Override
    public int getItemCount() {
        return mMoviesList.size();
    }


    class MovieViewHolder extends RecyclerView.ViewHolder {

        MovieViewHolder(View itemView){
            super(itemView);
            listItemMoviePoster =  itemView.findViewById(R.id.iv_movie_poster);
            listItemMovieTitle = itemView.findViewById(R.id.tv_movie_title);
        }

        void bind(final MovieDB movieDB, final OnItemClickListener listener){
            listItemMovieTitle.setText(movieDB.getTitle());
            Picasso.get()
                    .load(movieDB.getPosterPath())
                    .error(R.mipmap.ic_launcher)
                    .into(listItemMoviePoster);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(movieDB);
                }
            });

        }

    }
}
