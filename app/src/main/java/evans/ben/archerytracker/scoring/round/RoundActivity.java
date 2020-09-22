package evans.ben.archerytracker.scoring.round;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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
import java.util.ArrayList;
import java.util.List;

import evans.ben.archerytracker.MainActivity;
import evans.ben.archerytracker.R;
import evans.ben.archerytracker.scoring.Round;

public class RoundActivity extends AppCompatActivity {
    private List<Integer> distanceValues = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round);
        // Calling the method set up to do all things required to display desired information
          setUp();
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
        List<String> arrowsDistance;
        Round roundLoad = null;
        int totalArrowsShot = 0;


        if (roundInProgress) {
            // Setting the values using shared preferences if the round is already in progress
            String jsonLoad = sharedPreferences.getString("round", "");
            roundLoad = gson.fromJson(jsonLoad, Round.class);
            roundName = roundLoad.getRoundName().split("\\(");
            arrowsDistance = roundLoad.getArrowsDistance();
            scoringType = roundLoad.getScoringType();
            List<String> distances = roundLoad.getDistances();
            int arrowsEnd = roundLoad.getArrowsPerEnd();

            // Shared preferences for accessing saved values
            SharedPreferences sharedPreferencesScoring = this.getSharedPreferences("Scoring", Context.MODE_PRIVATE);
            // String to store what we read from shared preference
            String arrowValuesString;
            // Gson to convert the arrowValues to an array
            Gson gsonScoring = new Gson();

            // Getting the values for each distance
            for (int i = 0; i < distances.size(); i++) {
                arrowValuesString = sharedPreferencesScoring.getString(distances.get(i), null);

                // Need to exit the loop for the distances that haven't been filled yet
                if (arrowValuesString == null) {
                    continue;
                }

                // Reading arrowValues into a temporary arrow for addition to be done
                String[][] arrowValues = gsonScoring.fromJson(arrowValuesString, String[][].class);
                /* Starting a new sum each time we go through the for loop so need to add a new
                   element to list to add to */
                int current = 0;
                // Summing up total for arrowValues
                for (String[] arrowValue : arrowValues) {
                    for (int k = 0; k < arrowsEnd; k++) {
                        // Dealing with non numeric scoring values when summing
                        if (arrowValue[k].equals("X")) {
                            current += 10;
                        } else if (arrowValue[k].equals("M") || arrowValue[k].equals("")) {
                            current += 0;
                        } else {
                            current += Integer.parseInt(arrowValue[k]);
                        }

                        // Checking if an arrow has been shot
                        if (!arrowValue[k].equals("")) {
                            totalArrowsShot += 1;
                        }
                    }
                }
                // Load distance values from shared preferences an update values
                String savedDistanceValues = sharedPreferencesScoring.getString("distanceValues", null);
                if (savedDistanceValues == null) {
                    // Setting distance values to zero as there was an error loading from sharedPreferences
                    for (int j = 0; j < distances.size(); j++) {
                        distanceValues.add(0);
                    }
                }
                else {
                    Type distanceValuesType = new TypeToken<ArrayList<Integer>>(){}.getType();
                    distanceValues = gson.fromJson(savedDistanceValues, distanceValuesType);
                    distanceValues.set(i, current);
                }
            }
        }
        else {
            // Setting values as the round has been started from the round selection activity
            assert round != null;
            roundName = round.getRoundName().split("\\(");
            arrowsDistance = round.getArrowsDistance();
            scoringType = round.getScoringType();
            List<String> distances = round.getDistances();

            // Setting distanceValues to zero since there is no information to load from sharedPref
            for (int i = 0; i < distances.size(); i++) {
                distanceValues.add(0);
            }
        }

        Log.d("Ben", "" + distanceValues);
        TextView nameTextView = findViewById(R.id.round_name);

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

        Round roundSend;
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


    }
}