package ie.bookfast.bookfast;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Cathal on 06/11/2017.
 */

public class GoogleBooksAPI {
    private static final String URI_ENDPOINT = "https://www.googleapis.com/books/v1/volumes?q=";

    public GoogleBooksAPI(){};

    // returns raw JSON String
    public Book searchForISBN(String isbn_string){
        String isbn_encoded, full_query;

        // encode the isbn and build the full query
        try {
            isbn_encoded = java.net.URLEncoder.encode(isbn_string, "UTF-8");
            full_query = URI_ENDPOINT + isbn_encoded + "&maxResults=1";  //maxResults;
        } catch (UnsupportedEncodingException e) {
            return null;
        }

        // stores json result string
        String results = null;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {
            // Build the URL
            Uri builtUri = Uri.parse(full_query).buildUpon().build();
            URL url = new URL(builtUri.toString());


            // Establish HTTP connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();

            // If no results
            if (inputStream == null) return null;

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            if (buffer.length() == 0) {
                results = null;
            }
            results = buffer.toString();
        } catch (IOException v) {
            results = null;
        } finally {
            // close the URL connection
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return parseJSON(results);
    }

    private Book parseJSON(String json_string){
        Log.e("BOOK_JSON",json_string);
        if (json_string != null) {
            try {
                // Convert the results from String to JSONObject
                JSONObject jsonObject = new JSONObject(json_string);
                // Get the items node (Books) from the JSONObject
                JSONArray resultsArray = jsonObject.getJSONArray("items");

                // Book Object to be returned
                Book mBook = null;

                if(resultsArray.length() >= 1){
                    // Get the Book record
                    JSONObject bookRecord = resultsArray.getJSONObject(0);

                    // Get the volume info node from the Book record
                    JSONObject bookVolumeInfo = bookRecord.getJSONObject("volumeInfo");

                    String title = bookVolumeInfo.getString("title");
                    String description = bookVolumeInfo.getString("description");
                    String publisher = bookVolumeInfo.getString("publisher");
                    String publishedDate = bookVolumeInfo.getString("publishedDate");

                    // Some books don't have an authors node, use try/catch to prevent null pointers
                    JSONArray bookAuthors = null;
                    try {
                        bookAuthors = bookVolumeInfo.getJSONArray("authors");
                    } catch (JSONException ignored) {
                    }

                    // Convert the authors to a string
                    ArrayList<String> authorsArray = new ArrayList<>();
                    // If the author is empty, set it as "Unknown"
                    if (bookAuthors == null) {
                        authorsArray.add(0,"Unknown");
                    } else {
                        int countAuthors = bookAuthors.length();
                        for (int e = 0; e < countAuthors; e++) {
                            authorsArray.add(e,bookAuthors.getString(e));
                        }
                    }


                    JSONObject bookImageLinks = null;
                    try {
                        bookImageLinks = bookVolumeInfo.getJSONObject("imageLinks");
                    } catch (JSONException ignored) {
                    }

                    // Convert the image link to a string
                    String thumbnail = "";
                    if ( bookImageLinks == null){
                        thumbnail = "null";
                    }else{
                        thumbnail  = bookImageLinks.getString("thumbnail");
                    }
                    // Create a Book object
                    mBook = new Book(title,authorsArray,description,publisher,publishedDate,thumbnail);

                }
                return mBook;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }




}
