package edu.uci.ics.fabflixmobile.ui.singlemovie;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uci.ics.fabflixmobile.R;
import edu.uci.ics.fabflixmobile.data.NetworkManager;
import edu.uci.ics.fabflixmobile.data.model.Movie;

public class SingleMovieActivity extends AppCompatActivity {
    Movie movie;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movielist_row);

        Intent intent = getIntent();
        String movieId = intent.getStringExtra("movieId");

        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        final String host = "10.0.2.2";
        final String port = "8080";
        final String domain = "Fabflix_war";
        final String baseURL = "http://" + host + ":" + port + "/" + domain;

        final StringRequest movieRequest = new StringRequest(
                Request.Method.GET,
                baseURL + "/api/single-movie?id=" + movieId,
                response -> {
                    try {
                        JSONObject movieJson = new JSONObject(response);
                        movie = new Movie(movieJson);
                        displayMovie();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                },
                error -> Log.d("single_movie.error", error.toString()));
        queue.add(movieRequest);
    }

    private void displayMovie()
    {
        TextView title = findViewById(R.id.title);
        TextView subtitle = findViewById(R.id.subtitle);
        TextView director = findViewById(R.id.director);
        TextView rating = findViewById(R.id.rating);
        TextView genres = findViewById(R.id.genres);
        TextView stars = findViewById(R.id.stars);

        title.setText(movie.getName());
        subtitle.setText(movie.getYear() + "");
        director.setText(movie.getDirector());
        rating.setText(movie.getRating() + "");
        genres.setText(movie.getGenresString());
        stars.setText(movie.getStarsString());
    }
}
