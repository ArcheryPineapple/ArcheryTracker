package evans.ben.archerytracker.scoring.scorecard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import evans.ben.archerytracker.MainActivity;
import evans.ben.archerytracker.R;
import evans.ben.archerytracker.scoring.CompletedRound;
import evans.ben.archerytracker.scoring.Round;
import evans.ben.archerytracker.scoring.history.RoundHistoryActivity;
import evans.ben.archerytracker.scoring.input.ScoringActivity;
import evans.ben.archerytracker.scoring.round.RoundActivity;

public class ScorecardActivity extends AppCompatActivity {
    private boolean fromHistory;
    private EditText archerSig;
    private EditText scorerSig;
    private Long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scorecard);

        // Getting the data from the intent so we know which CompletedRound to get from the database
        Intent intent = getIntent();
        id = intent.getLongExtra("id", 0);
        fromHistory = intent.getBooleanExtra("fromHistory", false);

        // Getting the current CompletedRound object from the database
        CompletedRound current = MainActivity.completedRoundsDatabase.completedRoundsDao().getRound(id);
        // Gson for reading serialised data
        Gson gson = new Gson();
        // Getting all the fields
        String date = current.date;
        String roundName = current.roundName;
        // getting the round serialised object
        String serialisedRound = current.round;
        Round round = gson.fromJson(serialisedRound, Round.class);
        // Getting the list of arrow values organised by distance serialised object.
        String serialisedArrowValues = current.arrowValues;
        // Getting the type of List<String[][]> from gson to use
        Type arrowValuesType = new TypeToken<List<String[][]>>(){}.getType();
        List<String[][]> arrowValues = gson.fromJson(serialisedArrowValues, arrowValuesType);

        // Accessing all the views
        LinearLayout linearLayoutDistanceTables = findViewById(R.id.scorecard_layout_for_distance_tables);
        TableLayout tableLayoutSummary = findViewById(R.id.scorecard_table_for_summary);
        archerSig = findViewById(R.id.scorecard_archer_sig);
        scorerSig = findViewById(R.id.scorecard_scorer_sig);
        archerSig.setText(current.archerString);
        scorerSig.setText(current.scorerString);

        // To do:
        // Create the tables for each distance for the arrow values to go in
        // Fill the tables for each distance, with arrow values and calculate the ETs and RTs
        // Calculate: number of arrows for each scoring value, number of hits, average
        // Populate summary table with above information


    }

    @Override
    protected void onPause() {
        super.onPause();
        // Updating signatures on exit from this activity in the database
        String archerString =  archerSig.getText().toString();
        String scorerString = scorerSig.getText().toString();
        MainActivity.completedRoundsDatabase.completedRoundsDao().updateSignatures(id, archerString, scorerString);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Overriding onBackPressed to send to the correct activity depending on usage
        Intent intent;
        if (fromHistory) {
            intent = new Intent(this, RoundHistoryActivity.class);
        }
        else {
            intent = new Intent(this, MainActivity.class);
        }

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.scorecard_delete, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int ItemID = item.getItemId();

        if (ItemID == R.id.scorecard_action_delete) {
            MainActivity.completedRoundsDatabase.completedRoundsDao().deleteRound(id);
        }
        finish();
        return super.onOptionsItemSelected(item);
    }
}