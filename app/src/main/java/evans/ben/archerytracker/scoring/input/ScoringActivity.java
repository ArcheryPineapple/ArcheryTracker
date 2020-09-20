package evans.ben.archerytracker.scoring.input;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import evans.ben.archerytracker.R;

public class ScoringActivity extends AppCompatActivity {
    private int selectedArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoring);

        TextView distanceTextView = findViewById(R.id.scoring_activity_distance_header);

        Intent intent = getIntent();
        Distance distance = intent.getParcelableExtra("distance");
        assert distance != null;
        distanceTextView.setText(distance.getDistance());

        int arrowsEnd = distance.getArrowsEnd();
        int rows = Integer.parseInt(distance.getArrowsAtDistance()) / arrowsEnd + 1;
        // Making the table and setting up on click listeners for arrow value textViews
        init(arrowsEnd, rows);
        // Setting up so that on opening the first empty arrow textView is selected
        int check = firstSelectedArrow(arrowsEnd, rows);
        // In case all arrow textViews are filled
        if (check == 1) {
            selectedArrow = 10;
        }

    }

    private void init(int arrowsEnd, int rows) {
        TableLayout scoringTable = findViewById(R.id.scoring_table);
        // Used so that table stretches to fit screen
        scoringTable.setStretchAllColumns(true);

        // So that text size can be altered for all textViews easily
        int textSize = 20;
        // So that padding can be altered for all textViews easily
        int padding = 5;

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
                arrowsHeader.setPadding(padding, padding, padding, padding);
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
                    arrow.setPadding(padding, padding, padding, padding );
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
            ET.setPadding(padding, padding, padding, padding);
            ET.setBackgroundResource(R.drawable.cell_shape_thick_sides);
            ET.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            ET.setTypeface(Typeface.DEFAULT_BOLD);
            row.addView(ET);

            TextView RT = new TextView(this);
            RT.setLayoutParams(totalsLp);
            RT.setTextSize(textSize);
            RT.setPadding(padding, padding, padding, padding);
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
}