package edu.uci.ics.fabflixmobile.ui.movie_search;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import edu.uci.ics.fabflixmobile.R;
import edu.uci.ics.fabflixmobile.data.NetworkManager;
import edu.uci.ics.fabflixmobile.ui.movielist.MovieListActivity;

public class MovieSearchActivity extends AppCompatActivity {
    private EditText searchBar;
    private TextView searchMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moviesearch);

        searchBar = findViewById(R.id.search_bar);
        Button searchButton = findViewById(R.id.search_button);
        searchMessage = findViewById(R.id.search_message);

        searchButton.setOnClickListener(view -> search());
    }

    @SuppressLint("SetTextI18n")
    public void search() {
        searchMessage.setText("Searching for movie title");
        Log.d("search.success", "search");
        finish();
        Intent MovieListPage = new Intent(MovieSearchActivity.this, MovieListActivity.class);
        MovieListPage.putExtra("title", searchBar.getText().toString());
        startActivity(MovieListPage);
    }
}
