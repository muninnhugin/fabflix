package edu.uci.ics.fabflixmobile.ui.movielist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.uci.ics.fabflixmobile.R;
import edu.uci.ics.fabflixmobile.data.NetworkManager;
import edu.uci.ics.fabflixmobile.data.model.Movie;

import java.util.ArrayList;

public class MovieListActivity extends AppCompatActivity {
    ArrayList<Movie> movies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movielist);
        // TODO: this should be retrieved from the backend server
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");

        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        final String host = "10.0.2.2";
        final String port = "8080";
        final String domain = "Fabflix_war";
        final String baseURL = "http://" + host + ":" + port + "/" + domain;

        final StringRequest loginRequest = new StringRequest(
                Request.Method.GET,
                baseURL + "/api/movie-list?title=" + title,
                response -> {
                    try {
                        processResponse(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    callAdapter();
                },
                error -> {
                    Log.d("movieList.error", error.toString());
                });
        queue.add(loginRequest);

        for(Movie movie : movies)
        {
            Log.d("MovieListStatus", movie.toString());
        }
    }

    private void processResponse(String response) throws JSONException {
        Log.d("MovieListStatus", "processing json response");
        JSONArray moviesJson = new JSONArray(response);
        for(int i = 0; i < moviesJson.length(); ++i) {
            JSONObject movieJson = moviesJson.getJSONObject(i);
            Movie movie = new Movie(movieJson, 3, 3);
            movies.add(movie);
        }
    }

    private void callAdapter()
    {
        Log.d("MovieListStatus", "calling adapter");
        MovieListViewAdapter adapter = new MovieListViewAdapter(this, movies);
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Movie movie = movies.get(position);
            @SuppressLint("DefaultLocale") String message = String.format("Clicked on position: %d, name: %s, %d", position, movie.getName(), movie.getYear());
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        });
    }
}