package com.example.android.popularmoviesapp.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

@SuppressWarnings("SpellCheckingInspection")
public class NetworkUtils {

    private final static String THE_MOVIE_DB_POPULAR_BASE_URL =
            "https://api.themoviedb.org/3/movie/popular";
    private final static String THE_MOVIE_DB_HIGHEST_RATED_URL =
            "https://api.themoviedb.org/3/discover/movie";
    private final static String THE_MOVIE_DB_IMAGE_BASE_URL =
            "https://image.tmdb.org/t/p/";
    private final static String SORT_BY = "sort_by";
    private final static String SORT_BY_PARAM = "vote_average.desc";
    private final static String API_KEY = "api_key";
    @SuppressWarnings("SpellCheckingInspection")
    private final static String YOUR_API_KEY = "<your_key>";

    public static URL buildPopularMoviesUrl() {
        Uri builtUri  = Uri.parse(THE_MOVIE_DB_POPULAR_BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY, YOUR_API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    public static URL buildPopularMovieImageUrl(String imagePath, String imageSize){
        final String THE_MOVIE_DB_IMAGE_DEFAULT_BASE_URL =
                THE_MOVIE_DB_IMAGE_BASE_URL + imageSize + imagePath;

        Uri buildUri = Uri.parse(THE_MOVIE_DB_IMAGE_DEFAULT_BASE_URL).buildUpon()
            .build();


        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e){
            e.printStackTrace();
        }

        return url;
    }


    public static URL buildHighestRated(){
        Uri builtUri  = Uri.parse(THE_MOVIE_DB_HIGHEST_RATED_URL).buildUpon()
                .appendQueryParameter(API_KEY, YOUR_API_KEY)
                .appendQueryParameter(SORT_BY, SORT_BY_PARAM )
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }


}
