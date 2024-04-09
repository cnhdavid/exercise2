package at.ac.fhcampuswien.fhmdb.api;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.google.gson.reflect.TypeToken;
import okhttp3.OkHttpClient;
import com.google.gson.Gson;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

// MovieAPI-Klasse für die Kommunikation mit der externen API
public class MovieAPI {
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();
    private final String baseUrl = "https://prog2.fh-campuswien.ac.at/movies";

    // Filme von der externen API abrufen
    public List<Movie> fetchMovies(String searchText, Object genre, String rating, String releaseYearStr) throws IOException {
        // releaseYearStr in einen Integer parsen, wenn es nicht leer ist

        String genreString = "";
        if (genre != null) {
            genreString = (genre instanceof String) ? (String) genre : ((Genre) genre).getName();
        }

        String url = constructUrl(searchText, genreString, rating, releaseYearStr);

        // Anfordern der URL protokollieren (für Debugging-Zwecke)
        System.out.println("Request URL: " + url);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "http.agent")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response + " with body " + response.body().string());
            }
            System.out.println("Response: " + response);
            String jsonResponse = response.body().string();
            System.out.println("Response JSON: " + jsonResponse);

            Type listType = new TypeToken<ArrayList<Movie>>() {}.getType();
            ArrayList<Movie> movies = gson.fromJson(jsonResponse, listType);
            System.out.println(movies);

            return movies != null ? movies : new ArrayList<>();
        }
    }

    // URL für die Anfrage konstruieren
    public String constructUrl(String searchText, String genre, String rating, String releaseYear) throws IOException {
        searchText = searchText != null ? searchText : "";
        genre = genre != null ? genre : "";

        // URL-Encoding
        String encodedSearchText = URLEncoder.encode(searchText, "UTF-8");
        String encodedGenre = URLEncoder.encode(genre, "UTF-8");

        // URL konstruieren
        String url = baseUrl;
        List<String> params = new ArrayList<>();
        if (!searchText.isEmpty()) {
            params.add("searchText=" + encodedSearchText);
        }
        if (!genre.isEmpty()) {
            params.add("genre=" + encodedGenre);
        }
        if (!rating.isEmpty()) {
            params.add("rating=" + rating);
        }
        if (!releaseYear.isEmpty()) {
            params.add("releaseYear=" + releaseYear);
        }
        if (!params.isEmpty()) {
            url += "?" + String.join("&", params);
        }

        return url;
    }
}
