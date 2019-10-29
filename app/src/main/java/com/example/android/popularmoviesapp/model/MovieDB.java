package com.example.android.popularmoviesapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class MovieDB implements Parcelable{
    private String title, posterPath, size, originalTitle, overview, voteAverage,
            releaseDate, thumbnailPosterPath;

    private List<MovieReviews> reviews;

    private int id;


    public  MovieDB (){}

    public MovieDB(int id, String title, String originalTitle, String overview, String posterPath,
                   String thumbnailPoster, String voteAverage, String releaseDate){
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.size = size;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.thumbnailPosterPath = thumbnailPoster;

    }

    private MovieDB(Parcel in) {
        id = in.readInt();
        title = in.readString();
        posterPath = in.readString();
        size = in.readString();
        originalTitle = in.readString();
        overview = in.readString();
        voteAverage = in.readString();
        releaseDate = in.readString();
        thumbnailPosterPath = in.readString();

    }

    public static final Creator<MovieDB> CREATOR = new Creator<MovieDB>() {
        @Override
        public MovieDB createFromParcel(Parcel in) {
            return new MovieDB(in);
        }

        @Override
        public MovieDB[] newArray(int size) {
            return new MovieDB[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(posterPath);
        parcel.writeString(size);
        parcel.writeString(originalTitle);
        parcel.writeString(overview);
        parcel.writeString(voteAverage);
        parcel.writeString(releaseDate);
        parcel.writeString(thumbnailPosterPath);
    }

    public int getId () { return this.id; }

    public String getTitle(){
        return this.title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath(){ return this.posterPath; }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getThumbnailPosterPath() {
        return thumbnailPosterPath;
    }

    public void setReviews(List<MovieReviews> reviews) {
        this.reviews = reviews;
    }

    public List<MovieReviews> getReviews() {
        return reviews;
    }
}
