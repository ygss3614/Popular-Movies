package com.example.android.popularmoviesapp.utilities;

import com.example.android.popularmoviesapp.model.MovieDB;
import com.example.android.popularmoviesapp.model.MovieReviews;
import com.example.android.popularmoviesapp.model.MovieVideos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.SoftReference;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class JsonUtils {
    private static final String KEY_ID = "id";
    private static final String KEY_KEY = "key";
    private static final String KEY_NAME = "name";
    private static final String KEY_SITE = "site";
    private static final String KEY_TYPE = "type";
    private static final String KEY_RESULTS = "results";
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_URL = "url";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ORIGINAL_TITLE = "original_title";
    private static final String KEY_POSTER_PATH = "poster_path";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_VOTE_AVERAGE = "vote_average";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String POSTER_SIZE = "w780/";
    private static final String THUMBNAIL_SIZE = "w342/";


    public static List<MovieReviews> parseMovieReviewsJson (String stringReviews) throws JSONException {
        List<MovieReviews> reviews = new ArrayList<>();
        JSONObject reviewsJson = new JSONObject(stringReviews);
        JSONArray reviewsList = reviewsJson.getJSONArray(KEY_RESULTS);
        for (int i = 0; i < reviewsList.length(); i++) {
            JSONObject reviewObject = new JSONObject(reviewsList.get(i).toString());
            String author = reviewObject.getString(KEY_AUTHOR);
            String content = reviewObject.getString(KEY_CONTENT);
            String url = reviewObject.getString(KEY_URL);
            MovieReviews movieReviews = new MovieReviews(author, content, url);
            reviews.add(movieReviews);

        }
        return reviews;

    }

    public static List<MovieVideos> parseMovieVideosJson (String stringVideos) throws JSONException {
        List<MovieVideos> videos = new ArrayList<>();
        JSONObject videosJson = new JSONObject(stringVideos);
        JSONArray videosList = videosJson.getJSONArray(KEY_RESULTS);
        for (int i = 0; i < videosList.length(); i++) {
            JSONObject videoObject = new JSONObject(videosList.get(i).toString());
            String id = videoObject.getString(KEY_ID);
            String key = videoObject.getString(KEY_KEY);
            String name = videoObject.getString(KEY_NAME);
            String type = videoObject.getString(KEY_TYPE);
            String site = videoObject.getString(KEY_SITE);
            MovieVideos movieVideos = new MovieVideos(id, key, name, site, type);
            videos.add(movieVideos);

        }
        return videos;

    }

    public static List<MovieDB> parseMovieDBJson(String json) throws JSONException {
        List<MovieDB> results = new ArrayList<>();

        JSONObject resultsJson = new JSONObject(json);
        JSONArray moviesList = resultsJson.getJSONArray(KEY_RESULTS);

        for (int i=0; i < moviesList.length(); i++){
            JSONObject movieObject = new JSONObject(moviesList.get(i).toString());
            int movieId = movieObject.getInt(KEY_ID);
            String movieTitle = movieObject.getString(KEY_TITLE);
            String movieOriginalTitle = movieObject.getString(KEY_ORIGINAL_TITLE);
            String movieOverview = movieObject.getString(KEY_OVERVIEW);
            String moviePosterPath = movieObject.getString(KEY_POSTER_PATH);
            URL moviePosterUrl = NetworkUtils.buildPopularMovieImageUrl(moviePosterPath, POSTER_SIZE);
            String moviePosterUrlString = moviePosterUrl.toString();
            URL movieThumbnailUrl = NetworkUtils.buildPopularMovieImageUrl(moviePosterPath, THUMBNAIL_SIZE);
            String movieThumbnailUrlString = movieThumbnailUrl.toString();
            String movieVoteAverage = movieObject.getString(KEY_VOTE_AVERAGE);
            String humanMovieReleaseDate = "";

            if (movieObject.getString(KEY_RELEASE_DATE) != null && movieObject.getString(KEY_RELEASE_DATE) != "") {
                String movieReleaseDate = movieObject.getString(KEY_RELEASE_DATE).replace("-", "");

                humanMovieReleaseDate = LocalDate.parse(
                        movieReleaseDate,
                        DateTimeFormatter.BASIC_ISO_DATE
                ).format(
                        DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
                                .withLocale(Locale.US)
                );

            }

            MovieDB movieDB = new MovieDB(movieId, movieTitle, movieOriginalTitle, movieOverview,
                    moviePosterUrlString, movieThumbnailUrlString, movieVoteAverage, humanMovieReleaseDate);
            results.add(movieDB);
        }

        return results;
    }
}

