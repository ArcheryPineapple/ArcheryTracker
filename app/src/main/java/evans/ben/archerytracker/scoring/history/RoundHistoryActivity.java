package evans.ben.archerytracker.scoring.history;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import evans.ben.archerytracker.MainActivity;
import evans.ben.archerytracker.R;

public class RoundHistoryActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private RoundHistoryAdapter roundHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_history);

        // Instantiating recycler view
        RecyclerView recyclerView = findViewById(R.id.history_recyclerview);
        RecyclerView.LayoutManager historyLayoutManager = new LinearLayoutManager(this);
        roundHistoryAdapter = new RoundHistoryAdapter();

        // Setting values
        recyclerView.setLayoutManager(historyLayoutManager);
        recyclerView.setAdapter(roundHistoryAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        roundHistoryAdapter.reloadHistory();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // So that it exits to main activity if we entered a round then pressed back
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.round_history_search, menu);
        MenuItem searchItem = menu.findItem(R.id.round_history_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        roundHistoryAdapter.getFilter().filter(newText);
        return false;
    }
}