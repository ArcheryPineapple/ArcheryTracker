package evans.ben.archerytracker.scoring.history;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import evans.ben.archerytracker.MainActivity;
import evans.ben.archerytracker.R;

public class RoundHistoryActivity extends AppCompatActivity {
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
}