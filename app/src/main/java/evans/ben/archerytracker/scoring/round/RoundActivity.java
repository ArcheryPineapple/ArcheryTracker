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
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import evans.ben.archerytracker.MainActivity;
import evans.ben.archerytracker.R;
import evans.ben.archerytracker.scoring.Round;
import evans.ben.archerytracker.scoring.scorecard.ScorecardActivity;

public class RoundActivity extends AppCompatActivity {
    private List<Integer> distanceValues = new ArrayList<>();
    // For combining arrays of arrow values for each distance into one list
    private List<String[][]> arrowValuesList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Calling the method set up to do all things required to display desired information
        setUp();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Saving the distanceValues list
        Gson gson = new Gson();
        String savedDistanceValues = gson.toJson(distanceValues);
        SharedPreferences sharedPreferences = this.getSharedPreferences("Scoring", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("distanceValues", savedDistanceValues);
        editor.apply();

    }

    private void setUp() {
        Intent intent = getIntent();
        final Round round = intent.getParcelableExtra("roundSelected");

        /* So we know a round has been started and can change the behaviour of the round button on
        the home fragment */
        SharedPreferences sharedPreferences = this.getSharedPreferences("Scoring", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        // Check if a round is in progress
        boolean roundInProgress = sharedPreferences.getBoolean("roundInProgress", false);
        // Using GSON to save round object
        final Gson gson = new Gson();

        if(!roundInProgress) {
            editor.putBoolean("roundInProgress", true);
            String jsonSave = gson.toJson(round);
            editor.putString("round", jsonSave);
            editor.apply();
        }

        String roundName;
        int scoringType;
        List<String> arrowsDistance;
        Round roundLoad = null;
        int totalArrowsShot = 0;


        List<String> distances;
        int arrowsEnd;
        if (roundInProgress) {
            // Setting the values using shared preferences if the round is already in progress
            String jsonLoad = sharedPreferences.getString("round", "");
            roundLoad = gson.fromJson(jsonLoad, Round.class);
            roundName = roundLoad.getRoundName();
            arrowsDistance = roundLoad.getArrowsDistance();
            scoringType = roundLoad.getScoringType();
            distances = roundLoad.getDistances();
            arrowsEnd = roundLoad.getArrowsPerEnd();
             /* Load distance values from shared preferences and update values need this outside for loop
           to avoid issue of overwriting changes each loop */
            String savedDistanceValues = sharedPreferences.getString("distanceValues", null);
            // Needs to look at the unpacked version NOT the string from sharedPref
            // Getting the data from the gson file
            Type distanceValuesType = new TypeToken<List<Integer>>(){}.getType();
            distanceValues = gson.fromJson(savedDistanceValues, distanceValuesType);
        }
        else {
            // Setting values as the round has been started from the round selection activity
            assert round != null;
            roundName = round.getRoundName();
            arrowsDistance = round.getArrowsDistance();
            scoringType = round.getScoringType();
            distances = round.getDistances();
            arrowsEnd = round.getArrowsPerEnd();

            // Setting distanceValues to zero since there is no information to load from sharedPref
            for (int i = 0; i < distances.size(); i++) {
                distanceValues.add(0);
            }
        }

        // String to store what we read from shared preference
        String arrowValuesString;

        // Creating a placeholder array to fill arrowValuesList with
        String[][] placeholder = new String[1][];
            /* Creating the distanceValues array which is full of zeroes to be updated, this
               handles the case when a distance hasn't been shot yet hence the loop exits on the
               first if condition */
        for (int j = 0; j < distances.size(); j++) {
            assert distanceValues != null;
            distanceValues.add(0);
            // Creating the arrowValues list with correct dimensions
            if (arrowValuesList == null || arrowValuesList.size() < distances.size()) {
                assert arrowValuesList != null;
                arrowValuesList.add(placeholder);
            }
        }


        // Getting the values for each distance
        for (int i = 0; i < distances.size(); i++) {
            arrowValuesString = sharedPreferences.getString(distances.get(i), null);

            // Need to exit this loop for the distances that haven't been filled yet
            if (!(arrowValuesString == null)) {
                // Reading arrowValues into a temporary array for addition to be done
                String[][] arrowValues = gson.fromJson(arrowValuesString, String[][].class);

                arrowValuesList.set(i, arrowValues);
                // Starting a new sum each time we go through the for loop so need to reset current
                int current = 0;
                // Calculating number of rows for this distance
                int rows = Integer.parseInt(arrowsDistance.get(i)) / arrowsEnd;

                // Summing up total for arrowValues
                for (int j = 0; j < rows; j++) {
                    for (int k = 0; k < arrowsEnd; k++) {
                        // Dealing with non numeric scoring values when summing
                        if (arrowValues[j][k].equals("X")) {
                            current += 10;
                        } else if (arrowValues[j][k].equals("M") || arrowValues[j][k].equals("")) {
                            current += 0;
                        } else {
                            current += Integer.parseInt(arrowValues[j][k]);
                        }
                        /* Checking if an arrow has been shot for the purpose of when to display
                           save button */
                        if (!arrowValues[j][k].equals("")) {
                            totalArrowsShot += 1;
                        }
                    }
                }
                assert distanceValues != null;
                distanceValues.set(i, current);
            }
        }

        TextView nameTextView = findViewById(R.id.round_name);

        // For rounds with extra descriptions in brackets after we drop it
        nameTextView.setText(roundName);

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

        TextView totalScoreTextView = findViewById(R.id.round_total_score);
        int totalArrows = 0;
        for (int i = 0; i < arrowsDistance.size(); i++) {
            totalArrows += Integer.parseInt(arrowsDistance.get(i));
        }
        int totalScore = totalArrows * maxArrowVal;

        // Calculating round total score
        int currentScore = 0;

        if (distanceValues != null) {
            for (int i = 0; i < distanceValues.size(); i++) {
                currentScore += distanceValues.get(i);
            }
        }
        String totalScoreString = currentScore + "/" + totalScore;
        totalScoreTextView.setText(totalScoreString);

        // Instantiating values
        RecyclerView roundRecyclerView = findViewById(R.id.round_recyclerview);
        RecyclerView.LayoutManager roundLayoutManager = new LinearLayoutManager(this);

        final Round roundSend;
        if (roundInProgress) {
            roundSend = roundLoad;
        }
        else {
            roundSend = round;
        }

        // Need set the inputs for the adapter depending on who the activity was started
        RoundAdapter roundAdapter = new RoundAdapter(roundSend, maxArrowVal, distanceValues);

        // Connecting the recycler view
        roundRecyclerView.setLayoutManager(roundLayoutManager);
        roundRecyclerView.setAdapter(roundAdapter);

        // Delete button
        FloatingActionButton delFAB = findViewById(R.id.round_delete_button);
        delFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.clear();
                editor.putBoolean("roundInProgress", false);
                editor.apply();
                finish();
            }
        });

        // Save button
        FloatingActionButton saveFAB = findViewById(R.id.round_save_button);

        // The save button only appears when the round has been completed
        if (totalArrowsShot == totalArrows) {
            saveFAB.setVisibility(View.VISIBLE);
        }
        else {
            saveFAB.setVisibility(View.GONE);
        }

        // On click listener for save button so it can save rounds and start to score card activity
        final int finalCurrentScore = currentScore;
        saveFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Getting the current date
                String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MMM-yy"));
                // Using gson to serialise the round object
                String serialisedRound = gson.toJson(roundSend);
                // Using gson to serialise list of arrow values
                String serialisedArrowValues = gson.toJson(arrowValuesList);
                // Adding completed round to database
                Long id = MainActivity.completedRoundsDatabase.completedRoundsDao().saveCompletedRound(today, roundSend.getRoundName(), serialisedRound,serialisedArrowValues, finalCurrentScore, "", "");

                // Resetting roundInProgress information
                editor.clear();
                editor.putBoolean("roundInProgress", false);
                editor.apply();

                Context context = view.getContext();
                Intent intentSave = new Intent(context, ScorecardActivity.class);
                intentSave.putExtra("id", id);
                context.startActivity(intentSave);
            }
        });

    }
}