package com.example.android.popularmoviesapp.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

@Entity(tableName = "favorite")
public class MovieDB implements Parcelable{
    @PrimaryKey
    private int id;
    public String title;

    private String posterPath;

    private String size;

    private String originalTitle;

    private String overview;

    private String voteAverage;

    private String releaseDate;

    private String thumbnailPosterPath;
    @Ignore
    private List<MovieReviews> reviews;

    @Ignore
    public  MovieDB (){}
    @Ignore
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
    public MovieDB(int id, String title){
        this.id = id;
        this.title = title;
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

    public void setId(int id) {
        this.id = id;
    }

    public int getId () { return this.id; }

    public String getTitle(){
        return this.title;
    }

    public String getSize() { return size; }

    public void setSize(String size) { this.size = size; }


    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }


    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getOverview() {
        return overview;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getPosterPath(){ return this.posterPath; }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setThumbnailPosterPath(String thumbnailPosterPath) {
        this.thumbnailPosterPath = thumbnailPosterPath;
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
