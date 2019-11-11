package com.example.android.popularmoviesapp.model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MovieDao {

    @Insert
    void insertMovie(MovieDB movieDB);

    @Delete
    void deleteMovie(MovieDB movieDB);

    @Query("SELECT * FROM favorite ORDER BY title")
    LiveData<List<MovieDB>> loadFavoriteMovies();

    @Query("SELECT * FROM favorite WHERE id = :movieId")
    MovieDB getMovieById(int movieId);

}
