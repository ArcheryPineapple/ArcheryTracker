package evans.ben.archerytracker.scoring.round;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import evans.ben.archerytracker.HomeFragment;
import evans.ben.archerytracker.MainActivity;
import evans.ben.archerytracker.R;
import evans.ben.archerytracker.scoring.Round;

public class RoundActivity extends AppCompatActivity {
    private TextView nameTextView;
    private TextView totalScoreTextView;
    private RecyclerView roundRecyclerView;
    private RecyclerView.LayoutManager roundLayoutManager;
    private RoundAdapter roundAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round);
        Intent intent = getIntent();
        Round round = intent.getParcelableExtra("roundSelected");

        /* So we know a round has been started and can change the behaviour of the round button on
        the home fragment */
        SharedPreferences sharedPreferences = this.getSharedPreferences("Scoring", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        // Check if a round is in progress
        boolean roundInProgress = sharedPreferences.getBoolean("roundInProgress", false);
        // Using GSON to save round object
        Gson gson = new Gson();

        if(!roundInProgress) {
            editor.putBoolean("roundInProgress", true);
            String jsonSave = gson.toJson(round);
            editor.putString("round", jsonSave);
            editor.apply();
        }

        String[] roundName;
        int scoringType;
        List<String> distances;
        List<String> arrowsDistance;
        Round roundLoad = null;


        if (roundInProgress) {
            // Setting the values using shared preferences if the round is already in progress
            String jsonLoad = sharedPreferences.getString("round", "");
            roundLoad = gson.fromJson(jsonLoad, Round.class);
            roundName = roundLoad.getRoundName().split("\\(");
            distances = roundLoad.getDistances();
            arrowsDistance = roundLoad.getArrowsDistance();
            scoringType = roundLoad.getScoringType();

        }
        else {
            // Setting values as the round has been started from the round selection activity
            roundName = round.getRoundName().split("\\(");
            distances = round.getDistances();
            arrowsDistance = round.getArrowsDistance();
            scoringType = round.getScoringType();
        }

        nameTextView = findViewById(R.id.round_name);

        // For rounds with extra descriptions in brackets after we drop it
        nameTextView.setText(roundName[0]);

        int maxArrowVal;

        if (scoringType == 0 || scoringType == 2 || scoringType == 3) {
            maxArrowVal = 10;
        }
        else if (scoringType == 1) {
            maxArrowVal = 9;
        }
        else if (scoringType == 4){
            maxArrowVal = 5;
        }
        else {
            maxArrowVal = 0;
            Log.d("Scoring", "Error loading from shared preferences");
            finish();
        }

        totalScoreTextView = findViewById(R.id.round_total_score);
        int totalArrows = 0;
        for (int i = 0; i < arrowsDistance.size(); i++) {
            totalArrows += Integer.parseInt(arrowsDistance.get(i));
        }
        int totalScore = totalArrows * maxArrowVal;
        // 0 is a placeholder until scoring is worked out
        totalScoreTextView.setText(0 + "/" + totalScore);

        // Instantiating values
        roundRecyclerView = findViewById(R.id.round_recyclerview);
        roundLayoutManager = new LinearLayoutManager(this);

        Round roundSend;
        if (roundInProgress) {
            roundSend = roundLoad;
        }
        else {
            roundSend = round;
        }

        // Need set the inputs for the adapter depending on who the activity was started
        roundAdapter = new RoundAdapter(roundSend, maxArrowVal);

        // Connecting the recycler view
        roundRecyclerView.setLayoutManager(roundLayoutManager);
        roundRecyclerView.setAdapter(roundAdapter);

        // Delete button
        FloatingActionButton delFAB = findViewById(R.id.round_delete_button);
        delFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("roundInProgress", false);
                editor.apply();
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.startActivity(new Intent(this, MainActivity.class));
    }
}