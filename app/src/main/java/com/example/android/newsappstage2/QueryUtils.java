package com.example.android.newsappstage2;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving news data from Gaurdians.
 */
public class QueryUtils {

    private QueryUtils() {
    }

    /**
     * Query the Gaurdians dataset and return a list of {@link News} objects.
     */
    public static List<News> fetchNewsData(String requestUrl) {
        // Create URL object
        URL url = createUrl( requestUrl );

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest( url );
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Extract relevant fields from the JSON response and create a list of {@link News}
        List<News> newsList = extractFeatureFromJson( jsonResponse );

        // Return the list of {@link News}
        return newsList;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL( stringUrl );
        } catch (MalformedURLException e) {

            e.printStackTrace();
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout( 10000 /* milliseconds */ );
            urlConnection.setConnectTimeout( 15000 /* milliseconds */ );
            urlConnection.setRequestMethod( "GET" );
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream( inputStream );
            } else {
                Log.d( "Error response code: ", String.valueOf( urlConnection.getResponseCode() ) );
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader( inputStream, Charset.forName( "UTF-8" ) );
            BufferedReader reader = new BufferedReader( inputStreamReader );
            String line = reader.readLine();
            while (line != null) {
                output.append( line );
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link News} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<News> extractFeatureFromJson(String newsJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty( newsJSON )) {
            return null;
        }

        // Create an empty ArrayList that we can start adding news to
        List<News> newsList = new ArrayList<>();


        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject( newsJSON );
            JSONObject response = baseJsonResponse.getJSONObject( "response" );
            JSONArray resultsArray = response.getJSONArray( "results" );

            // For each news in the newsArray, create an {@link News} object
            for (int i = 0; i < resultsArray.length(); i++) {

                // Get a single news at position i within the list of news
                JSONObject currentResults = resultsArray.getJSONObject( i );

                String Title = currentResults.getString( "webTitle" );
                String category = currentResults.getString( "sectionName" );
                String date = currentResults.getString( "webPublicationDate" );
                String url = currentResults.getString( "webUrl" );

                JSONArray tagsauthor = currentResults.getJSONArray( "tags" );
                String author = "";
                if (tagsauthor.length() != 0) {
                    JSONObject currenttagsauthor = tagsauthor.getJSONObject( 0 );
                    author = currenttagsauthor.getString( "webTitle" );
                } else {
                    author = "No Author ..";
                }


                // Create a new {@link News} object with the magnitude, location, time,
                // and url from the JSON response.
                News news = new News( Title, category, date, url, author );

                // Add the new {@link News} to the list of news.
                newsList.add( news );
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            e.printStackTrace();
        }

        return newsList;
    }

}
