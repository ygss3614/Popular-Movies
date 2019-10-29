package com.example.android.popularmoviesapp.model;

public class MovieVideos {
    private String id, iso_639_1, iso_3166_1, key, name, site, type;
    private int size;

    public MovieVideos () {}

    public MovieVideos (String id, String key, String name,
                        String site, String type) {
        this.id=id;
        this.iso_639_1 = iso_639_1;
        this.iso_3166_1 = iso_3166_1;
        this.key = key;
        this.name = name;
        this.site = site;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }
}
