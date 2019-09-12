package com.example.android.popularmoviesapp.utilities;

import com.example.android.popularmoviesapp.model.MovieDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class JsonUtils {

    public static final String KEY_PAGE = "page";
    public static final String KEY_TOTAL_RESULTS = "total_results";
    public static final String KEY_TOTAL_PAGES = "total_pages";
    private static final String KEY_RESULTS = "results";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ORIGINAL_TITLE = "original_title";
    public static final String KEY_MOVIE_THUMBNAIL = "title";
    private static final String KEY_POSTER_PATH = "poster_path";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_VOTE_AVERAGE = "vote_average";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String POSTER_SIZE = "w780/";
    private static final String THUMBNAIL_SIZE = "w154/";

    private static String movieThumbnail;

    public static List<MovieDB> parseMovieDBJson(String json) throws JSONException {
        List<MovieDB> results = new ArrayList<>();

        JSONObject resultsJson = new JSONObject(json);
        JSONArray moviesList = resultsJson.getJSONArray(KEY_RESULTS);

        for (int i=0; i < moviesList.length(); i++){
            JSONObject movieObject = new JSONObject(moviesList.get(i).toString());
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

            if (movieObject.getString(KEY_RELEASE_DATE) != null && movieObject.getString(KEY_RELEASE_DATE) == "") {
                String movieReleaseDate = movieObject.getString(KEY_RELEASE_DATE).replace("-", "");

                humanMovieReleaseDate = LocalDate.parse(
                        movieReleaseDate,
                        DateTimeFormatter.BASIC_ISO_DATE
                ).format(
                        DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
                                .withLocale(Locale.US)
                );


            }


            MovieDB movieDB = new MovieDB(movieTitle, movieOriginalTitle, movieOverview,
                    moviePosterUrlString, movieThumbnailUrlString, movieVoteAverage, humanMovieReleaseDate);
            results.add(movieDB);
        }

        return results;
    }
}

