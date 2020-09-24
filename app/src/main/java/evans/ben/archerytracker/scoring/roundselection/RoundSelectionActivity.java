package evans.ben.archerytracker.scoring.roundselection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.SearchView;

import evans.ben.archerytracker.R;

public class RoundSelectionActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private RoundSelectionAdapter roundSelectionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_selection);

        // Instantiating values
        RecyclerView roundSelectionRecyclerView = findViewById(R.id.round_selection_recyclerview);
        RecyclerView.LayoutManager roundSelectionLayoutManager = new LinearLayoutManager(this);
        roundSelectionAdapter = new RoundSelectionAdapter();

        // Connecting the recycler view
        roundSelectionRecyclerView.setLayoutManager(roundSelectionLayoutManager);
        roundSelectionRecyclerView.setAdapter(roundSelectionAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.round_select_search, menu);
        MenuItem searchItem = menu.findItem(R.id.round_select_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        roundSelectionAdapter.getFilter().filter(s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        roundSelectionAdapter.getFilter().filter(s);
        return false;
    }
}