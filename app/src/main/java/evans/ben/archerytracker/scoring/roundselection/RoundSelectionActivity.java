package evans.ben.archerytracker.scoring.roundselection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import evans.ben.archerytracker.R;

public class RoundSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_selection);

        // Instantiating values
        RecyclerView roundSelectionRecyclerView = findViewById(R.id.round_selection_recyclerview);
        RecyclerView.LayoutManager roundSelectionLayoutManager = new LinearLayoutManager(this);
        RoundSelectionAdapter roundSelectionAdapter = new RoundSelectionAdapter();

        // Connecting the recycler view
        roundSelectionRecyclerView.setLayoutManager(roundSelectionLayoutManager);
        roundSelectionRecyclerView.setAdapter(roundSelectionAdapter);

    }
}