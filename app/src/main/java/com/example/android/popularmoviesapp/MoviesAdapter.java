package com.example.android.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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

    private final List<MovieDB> mMoviesList;
    private TextView listItemMovieTitle;
    private ImageView listItemMoviePoster;
    

    public MoviesAdapter (List<MovieDB> moviesList) {
        mMoviesList = moviesList;

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
        movieViewHolder.bind(i);
    }


    @Override
    public int getItemCount() {
        return mMoviesList.size();
    }


    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        MovieViewHolder(View itemView){
            super(itemView);
            listItemMovieTitle = itemView.findViewById(R.id.tv_movie_title);
            listItemMoviePoster =  itemView.findViewById(R.id.iv_movie_poster);
            itemView.setOnClickListener(this);
        }

        void bind(int position){

            Picasso.get()
                    .load(mMoviesList.get(position).getPosterPath())
                    .error(R.mipmap.ic_launcher)
                    .into(listItemMoviePoster);

            listItemMovieTitle.setText(mMoviesList.get(position).getTitle());
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            Context context = view.getContext();
            MovieDB movieDB = mMoviesList.get(clickedPosition);
            if (movieDB != null){
                Intent intent = new Intent(context, MoviesDetails.class);
                intent.putExtra(MoviesDetails.EXTRA_MOVIE, movieDB );
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "movieDB object is null", Toast.LENGTH_LONG).show();
            }

        }
    }
}
