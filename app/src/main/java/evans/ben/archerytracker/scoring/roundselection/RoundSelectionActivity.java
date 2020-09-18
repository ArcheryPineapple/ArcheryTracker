package evans.ben.archerytracker.scoring.roundselection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import evans.ben.archerytracker.R;

public class RoundSelectionActivity extends AppCompatActivity {
    private RecyclerView roundSelectionRecyclerView;
    private RecyclerView.LayoutManager roundSelectionLayoutManager;
    private RoundSelectionAdapter roundSelectionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_selection);

        // Instantiating values
        roundSelectionRecyclerView = findViewById(R.id.round_selection_recyclerview);
        roundSelectionLayoutManager = new LinearLayoutManager(this);
        roundSelectionAdapter = new RoundSelectionAdapter();

        // Connecting the recycler view
        roundSelectionRecyclerView.setLayoutManager(roundSelectionLayoutManager);
        roundSelectionRecyclerView.setAdapter(roundSelectionAdapter);

    }
}