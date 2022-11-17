package edu.uci.ics.fabflixmobile.ui.movielist;

import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

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
import edu.uci.ics.fabflixmobile.ui.singlemovie.SingleMovieActivity;

import java.util.ArrayList;

public class MovieListActivity extends AppCompatActivity {
    ArrayList<Movie> movies = null;
    int pageNumber = 0;
    Button prevBtn;
    Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movielist);

        getMovieList();

        prevBtn = findViewById(R.id.prev_btn);
        nextBtn = findViewById(R.id.next_btn);

        prevBtn.setOnClickListener(view -> {
            if(pageNumber <= 0) return;
            --pageNumber;
            getMovieList();
        });

        nextBtn.setOnClickListener(view -> {
            if(movies != null && movies.size() <= 0) return;
            ++pageNumber;
            getMovieList();
        });

    }

    private void getMovieList()
    {
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");

        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        final String host = "10.0.2.2";
        final String port = "8080";
        final String domain = "Fabflix_war";
        final String baseURL = "http://" + host + ":" + port + "/" + domain;
        final int entriesPerPage = 20;

        final StringRequest loginRequest = new StringRequest(
                Request.Method.GET,
                baseURL + "/api/movie-list?title=" + title +
                        "&page_number=" + pageNumber +
                        "&records_per_page=" + entriesPerPage,
                response -> {
                    try {
                        processResponse(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    callAdapter();
                },
                error -> Log.d("movieList.error", error.toString()));
        queue.add(loginRequest);
    }

    private void processResponse(String response) throws JSONException {
        Log.d("MovieListStatus", "processing json response");
        movies = new ArrayList<>();
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
            Intent SingleMoviePage = new Intent(MovieListActivity.this, SingleMovieActivity.class);
            SingleMoviePage.putExtra("movieId", movie.getId());
            startActivity(SingleMoviePage);
        });
    }
}