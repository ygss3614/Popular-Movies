package com.example.android.popularmoviesapp.utilities;

import android.net.Uri;

import com.example.android.popularmoviesapp.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

@SuppressWarnings("SpellCheckingInspection")
public class NetworkUtils {
    private final static String API_KEY = "api_key";
    private final static String YOUR_API_KEY = BuildConfig.API_KEY;
    private final static String THE_MOVIE_DB_BASE_URL =
            "https://api.themoviedb.org/3/movie/%s";
    private final static String THE_MOVIE_DB_POPULAR_URL =
            String.format(THE_MOVIE_DB_BASE_URL, "popular");
    private final static String THE_MOVIE_DB_HIGHEST_RATED_URL =
            String.format(THE_MOVIE_DB_BASE_URL, "top_rated");
    private static String THE_MOVIE_DB_VIDEOS_URL =
            String.format(THE_MOVIE_DB_BASE_URL, "%s/videos");
    private static String THE_MOVIE_DB_REVIEWS_URL =
            String.format(THE_MOVIE_DB_BASE_URL, "%s/reviews");
    private final static String THE_MOVIE_DB_IMAGE_BASE_URL =
            "https://image.tmdb.org/t/p/";

    private final static String SORT_BY = "sort_by";
    private final static String SORT_BY_PARAM = "vote_average.desc";
    @SuppressWarnings("SpellCheckingInspection")


    public static URL buildPopularMoviesUrl() {
        Uri builtUri  = Uri.parse(THE_MOVIE_DB_POPULAR_URL).buildUpon()
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


    public static URL buildPopularMovieHighestRated(){
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


    public static URL builPopularMovieVideos(int movieID){
        Uri builtUri  = Uri.parse(String.format(THE_MOVIE_DB_VIDEOS_URL, movieID)).buildUpon()
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

    public static URL builPopularMovieReviews(int movieID){
        Uri builtUri  = Uri.parse(String.format(THE_MOVIE_DB_REVIEWS_URL, movieID)).buildUpon()
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
