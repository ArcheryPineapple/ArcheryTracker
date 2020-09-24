package evans.ben.archerytracker.scoring.scorecard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

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
        archerSig = findViewById(R.id.scorecard_archer_sig);
        scorerSig = findViewById(R.id.scorecard_scorer_sig);
        TextView roundNameTextView = findViewById(R.id.scorecard_name);
        TextView roundDateTextView = findViewById(R.id.scorecard_date);
        roundNameTextView.setText(roundName);
        roundDateTextView.setText(date);
        archerSig.setText(current.archerString);
        scorerSig.setText(current.scorerString);

        // List of distances
        List<String> distances = round.getDistances();
        // List of number of arrows at each distance
        List<String> arrowsDistance = round.getArrowsDistance();
        int arrowsEnd = round.getArrowsPerEnd();
        // Variable to store ET values
        int ETVal;
        // Variable for storing previous ends RT
        int prevRTVal = 0;
        // Array for storing number of hits at each arrow value where 0 = M and 11 = X
        int[] hitsAtValue = new int[12];
        // Variable for calculating number of hits
        int hits = 0;

        // Layout parameters for different views that will be added programmatically
        TableRow.LayoutParams lpArrows = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        lpArrows.weight = 1;
        TableRow.LayoutParams headerLp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        headerLp.span = arrowsEnd;
        TableRow.LayoutParams totalsLp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        totalsLp.weight = (float) 1.5;
        TableLayout.LayoutParams lpTable = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lpTable.setMargins(0,0,0, 20);


        for (int i = 0; i < distances.size(); i++) {
            TextView distanceHeader = new TextView(this);
            distanceHeader.setText(distances.get(i));
            distanceHeader.setTextSize(15);
            distanceHeader.setTypeface(Typeface.DEFAULT_BOLD);
            linearLayoutDistanceTables.addView(distanceHeader);
            // Get arrow values for this distance
            String[][] distanceArrowValues = arrowValues.get(i);
            // Get the number of ends at this distance
            int ends = Integer.parseInt(arrowsDistance.get(i)) / arrowsEnd;

            // Create table view for this distance
            TableLayout tableLayout = new TableLayout(this);
            tableLayout.setStretchAllColumns(true);
            tableLayout.setBackgroundResource(R.drawable.cell_shape_thick);
            tableLayout.setLayoutParams(lpTable);
            // Iterate through ends with 1 added for the header row
            for (int j = 0; j < ends + 1; j++) {
                // Create table row for this row
                TableRow tableRow = new TableRow(this);

                /* Setting up the columns at the end of the scoring table, note we don't add these
                   to the row until after the other elements have been added */
                TextView ET = new TextView(this);
                ET.setLayoutParams(totalsLp);
                ET.setTextSize(15);
                ET.setBackgroundResource(R.drawable.cell_shape_thick_sides);
                ET.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                ET.setTypeface(Typeface.DEFAULT_BOLD);

                TextView RT = new TextView(this);
                RT.setLayoutParams(totalsLp);
                RT.setTextSize(15);
                RT.setBackgroundResource(R.drawable.cell_shape);
                RT.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                RT.setTypeface(Typeface.DEFAULT_BOLD);

                // Handle header row vs regular rows
                if (j == 0) {
                    // Setting up the arrows header
                    TextView arrowsHeader = new TextView(this);
                    arrowsHeader.setLayoutParams(headerLp);
                    arrowsHeader.setTextSize(15);
                    arrowsHeader.setTypeface(Typeface.DEFAULT_BOLD);
                    arrowsHeader.setBackgroundResource(R.drawable.cell_shape_thick_bottom);
                    arrowsHeader.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    arrowsHeader.setText(R.string.arrows);
                    tableRow.addView(arrowsHeader);

                    // Setting up ET and RT header
                    ET.setText(R.string.end_total);
                    ET.setBackgroundResource(R.drawable.cell_shape_thick_bottom_sides);
                    tableRow.addView(ET);
                    RT.setText(R.string.running_total);
                    RT.setBackgroundResource(R.drawable.cell_shape_thick_bottom);
                    tableRow.addView(RT);

                    // prevRTVal is zero since no previous ends
                    prevRTVal = 0;
                }
                else {
                    // Counter for this ends ET
                    ETVal = 0;
                    // Iterate through number of arrows each end
                    for (int k = 0; k < arrowsEnd; k++) {
                        TextView arrow = new TextView(this);
                        arrow.setLayoutParams(lpArrows);
                        arrow.setTextSize(15);
                        arrow.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        // Setting borders for cells
                        arrow.setBackgroundResource(R.drawable.cell_shape);
                        String arrowValue = distanceArrowValues[j - 1][k];
                        // Inputting arrow values
                        arrow.setText(arrowValue);
                        // Deal with different arrow values and adding them to ET
                        if (arrowValue.equals("X")) {
                            ETVal += 10;
                            hits += 1;
                            hitsAtValue[11] += 1;
                        }
                        else if (arrowValue.equals("M") || arrowValue.equals("")) {
                            ETVal += 0;
                            hitsAtValue[0] += 1;
                        }
                        else {
                            int value = Integer.parseInt(arrowValue);
                            ETVal += value;
                            hits += 1;
                            hitsAtValue[value] += 1;
                        }
                        tableRow.addView(arrow);
                    }
                    // Fill in ET and RT columns with correct numbers
                    ET.setText(Integer.toString(ETVal));
                    tableRow.addView(ET);
                    int RTVal = prevRTVal + ETVal;
                    RT.setText(Integer.toString(RTVal));
                    tableRow.addView(RT);

                    // Update prevRTVal
                    prevRTVal = RTVal;

                }
                // Adding the row to the tableLayout
                tableLayout.addView(tableRow);
            }
            // Adding the table to the linearLayout
            linearLayoutDistanceTables.addView(tableLayout);
        }

        // Summary table

        TableLayout summaryTable = findViewById(R.id.scorecard_table_for_summary);
        summaryTable.setBackgroundResource(R.drawable.cell_shape_thick);

        // Calculating the values we need
        int totalArrows = 0;
        for (String i : round.getArrowsDistance()) {
            totalArrows += Integer.parseInt(i);
        }
        float average =  (float) current.totalScore / totalArrows;

        int scoringType = round.getScoringType();

        TableRow firstRow = findViewById(R.id.scorecard_summary_upper);
        TableRow secondRow = findViewById(R.id.scorecard_summary_lower);

        if (scoringType == 0) {
            // Metric outdoors
            // Arrow values
            for (int i = 0; i < 12; i++) {
                // Setting up TextView formats
                TextView arrowValue = new TextView(this);
                TextView hitsValue = new TextView(this);
                arrowValue.setLayoutParams(lpArrows);
                hitsValue.setLayoutParams(lpArrows);
                arrowValue.setBackgroundResource(R.drawable.cell_shape_thick_bottom);
                hitsValue.setBackgroundResource(R.drawable.cell_shape);
                arrowValue.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                hitsValue.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                arrowValue.setTypeface(Typeface.DEFAULT_BOLD);
                arrowValue.setTextSize(15);
                hitsValue.setTextSize(15);

                // Calculating which arrow value we are working with
                int arrowScore = 11 - i;
                hitsValue.setText(Integer.toString(hitsAtValue[arrowScore]));
                if (arrowScore == 11) {
                    arrowValue.setText("X");
                }
                else if (arrowScore == 0) {
                    arrowValue.setText("M");
                }
                else {
                    arrowValue.setText(Integer.toString(arrowScore));
                }
                firstRow.addView(arrowValue);
                secondRow.addView(hitsValue);
            }
        }
        else if (scoringType == 1){
            // Imperial outdoors
            // Arrow values
            for (int i = 0; i < 6; i++) {
                // Setting up TextView formats
                TextView arrowValue = new TextView(this);
                TextView hitsValue = new TextView(this);
                arrowValue.setLayoutParams(lpArrows);
                hitsValue.setLayoutParams(lpArrows);
                arrowValue.setBackgroundResource(R.drawable.cell_shape_thick_bottom);
                hitsValue.setBackgroundResource(R.drawable.cell_shape);
                arrowValue.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                hitsValue.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                arrowValue.setTypeface(Typeface.DEFAULT_BOLD);
                arrowValue.setTextSize(15);
                hitsValue.setTextSize(15);

                // Calculating which arrow value we are working with
                int arrowScore = 9 - (2 * i);

                if (arrowScore == -1) {
                    arrowValue.setText("M");
                    hitsValue.setText(Integer.toString(hitsAtValue[0]));
                }
                else {
                    arrowValue.setText(Integer.toString(arrowScore));
                    hitsValue.setText(Integer.toString(hitsAtValue[arrowScore]));
                }
                firstRow.addView(arrowValue);
                secondRow.addView(hitsValue);
            }
        }
        else if (scoringType == 2) {
            // Indoors full
            // Arrow values
            for (int i = 0; i < 11; i++) {
                // Setting up TextView formats
                TextView arrowValue = new TextView(this);
                TextView hitsValue = new TextView(this);
                arrowValue.setLayoutParams(lpArrows);
                hitsValue.setLayoutParams(lpArrows);
                arrowValue.setBackgroundResource(R.drawable.cell_shape_thick_bottom);
                hitsValue.setBackgroundResource(R.drawable.cell_shape);
                arrowValue.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                hitsValue.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                arrowValue.setTypeface(Typeface.DEFAULT_BOLD);
                arrowValue.setTextSize(15);
                hitsValue.setTextSize(15);

                // Calculating which arrow value we are working with
                int arrowScore = 10 - i;
                hitsValue.setText(Integer.toString(hitsAtValue[arrowScore]));
                if (arrowScore == 0) {
                    arrowValue.setText("M");
                }
                else {
                    arrowValue.setText(Integer.toString(arrowScore));
                }
                firstRow.addView(arrowValue);
                secondRow.addView(hitsValue);
            }
        }
        else if (scoringType == 3) {
            // Indoors 3 spot
            // Arrow values
            for (int i = 0; i < 6; i++) {
                // Setting up TextView formats
                TextView arrowValue = new TextView(this);
                TextView hitsValue = new TextView(this);
                arrowValue.setLayoutParams(lpArrows);
                hitsValue.setLayoutParams(lpArrows);
                arrowValue.setBackgroundResource(R.drawable.cell_shape_thick_bottom);
                hitsValue.setBackgroundResource(R.drawable.cell_shape);
                arrowValue.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                hitsValue.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                arrowValue.setTypeface(Typeface.DEFAULT_BOLD);
                arrowValue.setTextSize(15);
                hitsValue.setTextSize(15);

                // Calculating which arrow value we are working with
                int arrowScore = 10 - i;
                if (arrowScore == 4) {
                    arrowValue.setText("M");
                    hitsValue.setText(Integer.toString(hitsAtValue[0]));
                }
                else {
                    arrowValue.setText(Integer.toString(arrowScore));
                    hitsValue.setText(Integer.toString(hitsAtValue[arrowScore]));
                }
                firstRow.addView(arrowValue);
                secondRow.addView(hitsValue);
            }
        }
        else {
            // Worcester
            // Arrow values
            for (int i = 0; i < 6; i++) {
                // Setting up TextView formats
                TextView arrowValue = new TextView(this);
                TextView hitsValue = new TextView(this);
                arrowValue.setLayoutParams(lpArrows);
                hitsValue.setLayoutParams(lpArrows);
                arrowValue.setBackgroundResource(R.drawable.cell_shape_thick_bottom);
                hitsValue.setBackgroundResource(R.drawable.cell_shape);
                arrowValue.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                arrowValue.setTypeface(Typeface.DEFAULT_BOLD);
                hitsValue.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                arrowValue.setTextSize(15);
                hitsValue.setTextSize(15);

                // Calculating which arrow value we are working with
                int arrowScore = 5 - i;
                hitsValue.setText(Integer.toString(hitsAtValue[arrowScore]));
                if (arrowScore == 0) {
                    arrowValue.setText("M");
                }
                else {
                    arrowValue.setText(Integer.toString(arrowScore));
                }
                firstRow.addView(arrowValue);
                secondRow.addView(hitsValue);


            }
        }

        // hits and average
        // Names for first row
        TextView hitsNameTextView = new TextView(this);
        TextView averageNameTextView = new TextView(this);
        hitsNameTextView.setLayoutParams(totalsLp);
        averageNameTextView.setLayoutParams(totalsLp);
        hitsNameTextView.setBackgroundResource(R.drawable.cell_shape_thick_bottom_sides);
        averageNameTextView.setBackgroundResource(R.drawable.cell_shape_thick_bottom);
        hitsNameTextView.setTextSize(15);
        averageNameTextView.setTextSize(15);
        hitsNameTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        averageNameTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        hitsNameTextView.setTypeface(Typeface.DEFAULT_BOLD);
        averageNameTextView.setTypeface(Typeface.DEFAULT_BOLD);
        hitsNameTextView.setText("Hits");
        averageNameTextView.setText("Average");
        firstRow.addView(hitsNameTextView);
        firstRow.addView(averageNameTextView);

        // Values for second row
        TextView hitsTextView = new TextView(this);
        TextView averageTextView = new TextView(this);
        hitsTextView.setLayoutParams(totalsLp);
        averageTextView.setLayoutParams(totalsLp);
        hitsTextView.setBackgroundResource(R.drawable.cell_shape_thick_sides);
        averageTextView.setBackgroundResource(R.drawable.cell_shape);
        hitsTextView.setTextSize(15);
        averageTextView.setTextSize(15);
        hitsTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        averageTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        String hitsContent = Integer.toString(hits) + "/" + Integer.toString(totalArrows);
        hitsTextView.setText(hitsContent);
        averageTextView.setText(String.format("%.2f", average));
        secondRow.addView(hitsTextView);
        secondRow.addView(averageTextView);

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
        Intent intent;
        if (fromHistory) {
            intent = new Intent(this, RoundHistoryActivity.class);
        }
        else {
            intent = new Intent(this, MainActivity.class);
        }

        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}