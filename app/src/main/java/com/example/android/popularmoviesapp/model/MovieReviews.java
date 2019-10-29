package com.example.android.popularmoviesapp.model;

public class MovieReviews {
    private String author, content, url;

    public MovieReviews(){};

    public MovieReviews(String author, String content, String url){
        this.author = author;
        this.content = content;
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }
}
