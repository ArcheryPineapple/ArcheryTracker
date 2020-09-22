package evans.ben.archerytracker.scoring.input;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;


import java.lang.reflect.Array;
import java.util.Locale;

import evans.ben.archerytracker.R;

public class ScoringActivity extends AppCompatActivity {
    private int selectedArrow;
    private int arrowsEnd;
    private int rows;
    private Distance distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoring);

        TextView distanceTextView = findViewById(R.id.scoring_activity_distance_header);

        Intent intent = getIntent();
        distance = intent.getParcelableExtra("distance");
        assert distance != null;
        distanceTextView.setText(distance.getDistance());
        arrowsEnd = distance.getArrowsEnd();

        int arrowsEnd = distance.getArrowsEnd();
        rows = (Integer.parseInt(distance.getArrowsAtDistance()) / arrowsEnd) + 1;

        // Making the table and setting up on click listeners for arrow value textViews
        init(arrowsEnd, rows);

        //Loading arrows
        loadArrows();

        // Setting up so that on opening the first empty arrow textView is selected
        int check = firstSelectedArrow(arrowsEnd, rows);
        // In case all arrow textViews are filled
        if (check == 1) {
            selectedArrow = 10;
        }

        initKeyboard(distance.getScoringStyle());

        FloatingActionButton backspaceButton = findViewById(R.id.scoring_del_button);
        backspaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView current = findViewById(selectedArrow);
                current.setText("");
                current.setBackgroundResource(R.drawable.cell_shape_selected);
                updateTotals(selectedArrow / 10);
            }
        });

    }

    // This methods creates the table for the arrow values to go in
    private void init(int arrowsEnd, int rows) {
        TableLayout scoringTable = findViewById(R.id.scoring_table);
        // Used so that table stretches to fit screen
        scoringTable.setStretchAllColumns(true);

        // So that text size can be altered for all textViews easily
        int textSize = 20;

        for (int i  = 0; i < rows; i++) {
            TableRow row = new TableRow(this);

            /* The different LayoutParams are to alter the weight to get totals columns wider than
                arrow columns. The WRAP_CONTENT is so the height of the rows are right size for the
                text */
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            lp.weight = 1;
            TableRow.LayoutParams headerLp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            headerLp.span = arrowsEnd;
            TableRow.LayoutParams totalsLp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            totalsLp.weight = (float) 1.5;
            row.setLayoutParams(lp);


            if (i == 0) {
                TextView arrowsHeader = new TextView(this);
                arrowsHeader.setLayoutParams(headerLp);
                arrowsHeader.setTextSize(textSize);
                arrowsHeader.setTypeface(Typeface.DEFAULT_BOLD);
                arrowsHeader.setBackgroundResource(R.drawable.cell_shape_thick_bottom);
                arrowsHeader.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                arrowsHeader.setText(R.string.arrows);
                row.addView(arrowsHeader);
            }
            else {
                for (int j = 0; j < arrowsEnd; j++) {
                    final TextView arrow = new TextView(this);
                    arrow.setLayoutParams(lp);
                    arrow.setTextSize(textSize);
                    // Setting borders for cells
                    arrow.setBackgroundResource(R.drawable.cell_shape);

                    // Giving each arrow textView an ID based on its position in the table
                    String id = String.format("%s%s",i, j);
                    arrow.setId(Integer.parseInt(id));

                    arrow.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    arrow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            /* Need to defined selected on create of scoring activity to avoid
                               referencing a null id. This code ensures only one arrow textView will
                                have the selected appearance at any time */
                            TextView selected = findViewById(selectedArrow);
                            selected.setBackgroundResource(R.drawable.cell_shape);
                            arrow.setBackgroundResource(R.drawable.cell_shape_selected);
                            selectedArrow = arrow.getId();
                        }
                    });
                    row.addView(arrow);
            }

            }

            // Setting up the columns at the end of the scoring table
            TextView ET = new TextView(this);
            ET.setLayoutParams(totalsLp);
            ET.setTextSize(textSize);
            ET.setBackgroundResource(R.drawable.cell_shape_thick_sides);
            ET.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            ET.setTypeface(Typeface.DEFAULT_BOLD);
            row.addView(ET);

            TextView RT = new TextView(this);
            RT.setLayoutParams(totalsLp);
            RT.setTextSize(textSize);
            RT.setBackgroundResource(R.drawable.cell_shape);
            RT.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            RT.setTypeface(Typeface.DEFAULT_BOLD);
            row.addView(RT);

            // For the first row we want to label the columns and give them a thick bottom border
            if (i == 0) {
                ET.setText(R.string.end_total);
                ET.setBackgroundResource(R.drawable.cell_shape_thick_bottom_sides);
                RT.setText(R.string.running_total);
                RT.setBackgroundResource(R.drawable.cell_shape_thick_bottom);
            }
            else {
                /* Giving each ET and RT textView an ID based on its row and then ET
                   have 20 in front of the row and RT has 30 */
                String idET = String.format("20%s", i);
                String idRT = String.format("30%s", i);
                ET.setId(Integer.parseInt(idET));
                RT.setId(Integer.parseInt(idRT));
            }
            scoringTable.addView(row);
        }
    }

    // This method is used to select the first empty arrow textView
    private int firstSelectedArrow(int arrowsEnd, int ends) {
        for (int i = 1; i < ends; i++) {
            for (int j = 0; j < arrowsEnd; j++) {
                String currentID = String.format("%s%s", i, j);
                TextView current = findViewById(Integer.parseInt(currentID));
                if (current.getText() == null || current.getText().equals("")) {
                    current.setBackgroundResource(R.drawable.cell_shape_selected);
                    selectedArrow = Integer.parseInt(currentID);
                    return 0;
                }
            }
        }
        return 1;
    }

    // This method makes the keyboard depending on the scoring type
    private void initKeyboard(int scoringType) {
        TableLayout keyboard = findViewById(R.id.scoring_keyboard_table);
        keyboard.setStretchAllColumns(true);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        lp.weight = 1;
        lp.setMargins(10,10,10,10);

        if (scoringType == 0) {
            TableRow tableRow1 = new TableRow(this);
            tableRow1.setLayoutParams(lp);
            // Adding all buttons
            TextView button = makeButton("X", Color.YELLOW, lp);
            tableRow1.addView(button);
            button = makeButton("10", Color.YELLOW, lp);
            tableRow1.addView(button);
            button = makeButton("9", Color.YELLOW, lp);
            tableRow1.addView(button);
            button = makeButton("8", Color.RED, lp);
            tableRow1.addView(button);
            button = makeButton("7", Color.RED, lp);
            tableRow1.addView(button);
            button = makeButton("6", Color.BLUE, lp);
            tableRow1.addView(button);
            keyboard.addView(tableRow1);

            // New row
            TableRow tableRow2 = new TableRow(this);
            tableRow2.setLayoutParams(lp);
            button = makeButton("5", Color.BLUE, lp);
            tableRow2.addView(button);
            button = makeButton("4", Color.BLACK, lp);
            tableRow2.addView(button);
            button = makeButton("3", Color.BLACK, lp);
            tableRow2.addView(button);
            button = makeButton("2", Color.WHITE, lp);
            tableRow2.addView(button);
            button = makeButton("1", Color.WHITE, lp);
            tableRow2.addView(button);
            button = makeButton("M", Color.GREEN, lp);
            tableRow2.addView(button);
            keyboard.addView(tableRow2);
        }
        else if (scoringType == 1) {
            TableRow tableRow1 = new TableRow(this);
            tableRow1.setLayoutParams(lp);
            // Adding all buttons
            TextView button = makeButton("9", Color.YELLOW, lp);
            tableRow1.addView(button);
            button = makeButton("7", Color.RED, lp);
            tableRow1.addView(button);
            button = makeButton("5", Color.BLUE, lp);
            tableRow1.addView(button);
            button = makeButton("3", Color.BLACK, lp);
            tableRow1.addView(button);
            button = makeButton("1", Color.WHITE, lp);
            tableRow1.addView(button);
            button = makeButton("M", Color.GREEN, lp);
            tableRow1.addView(button);
            keyboard.addView(tableRow1);
        }
        else if (scoringType == 2) {
            TableRow tableRow1 = new TableRow(this);
            tableRow1.setLayoutParams(lp);
            // Adding all buttons
            TextView button = makeButton("10", Color.YELLOW, lp);
            tableRow1.addView(button);
            button = makeButton("9", Color.YELLOW, lp);
            tableRow1.addView(button);
            button = makeButton("8", Color.RED, lp);
            tableRow1.addView(button);
            button = makeButton("7", Color.RED, lp);
            tableRow1.addView(button);
            button = makeButton("6", Color.BLUE, lp);
            tableRow1.addView(button);
            keyboard.addView(tableRow1);

            // New row
            TableRow tableRow2 = new TableRow(this);
            tableRow2.setLayoutParams(lp);
            button = makeButton("5", Color.BLUE, lp);
            tableRow2.addView(button);
            button = makeButton("4", Color.BLACK, lp);
            tableRow2.addView(button);
            button = makeButton("3", Color.BLACK, lp);
            tableRow2.addView(button);
            button = makeButton("2", Color.WHITE, lp);
            tableRow2.addView(button);
            button = makeButton("1", Color.WHITE, lp);
            tableRow2.addView(button);
            button = makeButton("M", Color.GREEN, lp);
            tableRow2.addView(button);
            keyboard.addView(tableRow2);
        }
        else if (scoringType == 3) {
            TableRow tableRow1 = new TableRow(this);
            tableRow1.setLayoutParams(lp);
            // Adding all buttons
            TextView button = makeButton("10", Color.YELLOW, lp);
            tableRow1.addView(button);
            button = makeButton("9", Color.YELLOW, lp);
            tableRow1.addView(button);
            button = makeButton("8", Color.RED, lp);
            tableRow1.addView(button);
            button = makeButton("7", Color.RED, lp);
            tableRow1.addView(button);
            button = makeButton("6", Color.BLUE, lp);
            tableRow1.addView(button);
            button = makeButton("M", Color.GREEN, lp);
            tableRow1.addView(button);
            keyboard.addView(tableRow1);
        }
        else {
            TableRow tableRow1 = new TableRow(this);
            tableRow1.setLayoutParams(lp);
            // Adding all buttons
            TextView button = makeButton("5", Color.WHITE, lp);
            tableRow1.addView(button);
            button = makeButton("4", Color.BLACK, lp);
            tableRow1.addView(button);
            button = makeButton("3", Color.BLACK, lp);
            tableRow1.addView(button);
            button = makeButton("2", Color.BLACK, lp);
            tableRow1.addView(button);
            button = makeButton("1", Color.BLACK, lp);
            tableRow1.addView(button);
            button = makeButton("M", Color.GREEN, lp);
            tableRow1.addView(button);
            keyboard.addView(tableRow1);
        }
    }

    /* This method makes a keyboard button, taking the value, colour of the button on input and
       layout parameter on input */
    private TextView makeButton(final String value, final int colour, TableRow.LayoutParams layoutParams) {
        // Button appearance
        Button button = new Button(this);
        button.setLayoutParams(layoutParams);
        button.setTextSize(20);
        button.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        button.setText(value);
        button.setBackgroundColor(colour);
        if (colour == Color.BLACK || colour == Color.BLUE) {
            button.setTextColor(Color.WHITE);
        }

        // On click listener for scoring
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView currentArrow = findViewById(selectedArrow);
                currentArrow.setText(value);
                //currentArrow.setBackgroundColor(colour);

                // Moving which arrow is selected
                currentArrow.setBackgroundResource(R.drawable.cell_shape);

                // Calculating where we are on the table
                int col = selectedArrow % 10;
                int row = selectedArrow / 10;

                if (col == arrowsEnd - 1 && row == rows - 1) {
                    selectedArrow = 10;
                }
                else if (col == arrowsEnd - 1) {
                    // For concatenating next selectedArrow value
                    String newSelectedArrow = String.format("%s0", row + 1);
                    selectedArrow = Integer.parseInt(newSelectedArrow);
                    currentArrow = findViewById(selectedArrow);
                    currentArrow.setBackgroundResource(R.drawable.cell_shape_selected);
                }
                else {
                    selectedArrow += 1;
                    currentArrow = findViewById(selectedArrow);
                    currentArrow.setBackgroundResource(R.drawable.cell_shape_selected);
                }

                // Updating ET and RT
                updateTotals(row);
            }
        });
        return button;
    }

    // Updating ET and RT values
    private void updateTotals(int row) {
        for (int i = row; i < rows; i++) {
            String ETID = String.format("20%s", i);
            String RTID = String.format("30%s", i);
            TextView currentET = findViewById(Integer.parseInt(ETID));
            TextView currentRT = findViewById(Integer.parseInt(RTID));
            // For the case where someone enters a miss
            boolean miss = false;
            // Calculating ET for this row
            int rowET = 0;
            for (int j = 0; j < arrowsEnd; j++) {
                // Getting current arrow
                String arrowID = String.format("%s%s", i, j);
                TextView arrow = findViewById(Integer.parseInt(arrowID));
                if (arrow == null || arrow.getText().equals("")) {
                    // Don't add anything to rowTotal
                }
                else if (arrow.getText().equals("M")) {
                    miss = true;
                }
                else if (arrow.getText().equals("X")) {
                    rowET += 10;
                }
                else {
                    rowET += Integer.parseInt((String) arrow.getText());
                }
            }

            // Determining what to put in ET for this row
            if (rowET != 0 || miss) {
                currentET.setText(Integer.toString(rowET));
            }
            else {
                currentET.setText("");
            }

            // RT calculations
            if (i == 1) {
                currentRT.setText(currentET.getText());
            }
            else {
                String prevRTID = String.format("30%s", i - 1);
                TextView prevRT = findViewById(Integer.parseInt(prevRTID));
                int prevRTVal;
                if (prevRT.getText().equals("")) {
                     prevRTVal = 0;
                }
                else {
                    prevRTVal = Integer.parseInt((String) prevRT.getText());
                }
                int RT = rowET + prevRTVal;

                if (rowET != 0 || miss) {
                    currentRT.setText(Integer.toString(RT));
                }
                else {
                    currentRT.setText("");
                }
            }
        }
    }

    /* Method for loading in arrow values from shared preferences, returns 1 if there is an error
       loading from sharedPreferences */
    private int loadArrows() {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = this.getSharedPreferences("Scoring", Context.MODE_PRIVATE);
        String savedArrows = sharedPreferences.getString(distance.getDistance(), null);
        // Checking if what we read was correct
        if (savedArrows == null) {
            return 1;
        }

        // Converting stored arrows into an array to be used to fill table
        String[][] arrowValues = gson.fromJson(savedArrows, String[][].class);

        // Filling table with saved values
        for (int i = 1; i < rows; i++) {
            for (int j = 0; j < arrowsEnd; j++) {
                String currentID = String.format(Locale.getDefault(), "%s%s", i, j);
                TextView current = findViewById(Integer.parseInt(currentID));
                current.setText(arrowValues[i - 1][j]);
            }
        }
        // Updating totals
        updateTotals(1);
        return 0;
    }

    // Overriding onPause in order to save arrow values
    @Override
    protected void onPause() {
        super.onPause();
        // Array to store arrow values in
        String[][] arrowValues = new String[rows - 1][arrowsEnd];

        // Iterate over all arrow textViews and save to the array
        for (int i = 1; i < rows; i++) {
            for (int j = 0; j < arrowsEnd; j++) {
                String currentID = String.format(Locale.getDefault(), "%s%s", i, j);
                TextView current = findViewById(Integer.parseInt(currentID));
                String content = (String) current.getText();
                arrowValues[i - 1][j] = content;
            }
        }
        // Need Gson in order to store the array as a string
        Gson gson = new Gson();
        String savedArrowValues = gson.toJson(arrowValues);

        // Setting up shared preference to store arrow values
        SharedPreferences sharedPreferences = this.getSharedPreferences("Scoring", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // We to save this with reference to the distance so we use that as the name for shared pref
        editor.putString(distance.getDistance(), savedArrowValues);
        editor.apply();
    }
}