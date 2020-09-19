package evans.ben.archerytracker.scoring.input;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import evans.ben.archerytracker.R;

public class ScoringActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoring);

        TextView distanceTextView = findViewById(R.id.scoring_activity_distance_header);

        Intent intent = getIntent();
        Distance distance = intent.getParcelableExtra("distance");
        distanceTextView.setText(distance.getDistance());

        int arrowsEnd = distance.getArrowsEnd();
        int rows = Integer.parseInt(distance.getArrowsAtDistance()) / arrowsEnd + 1;
        init(arrowsEnd, rows);
    }

    public void init(int arrowsEnd, int rows) {
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
                arrowsHeader.setBackgroundResource(R.drawable.cell_shape);
                arrowsHeader.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                arrowsHeader.setText("Arrows");
                row.addView(arrowsHeader);
            }
            else {
                for (int j = 0; j < arrowsEnd; j++) {
                    TextView arrow = new TextView(this);
                    arrow.setLayoutParams(lp);
                    arrow.setTextSize(textSize);
                    arrow.setPadding(padding, padding, padding, padding );
                    // Setting borders for cells
                    arrow.setBackgroundResource(R.drawable.cell_shape);
                    arrow.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    row.addView(arrow);
            }

            }

            // Setting up the columns at the end of the scoring table
            TextView ET = new TextView(this);
            ET.setLayoutParams(totalsLp);
            ET.setTextSize(textSize);
            ET.setPadding(padding, padding, padding, padding);
            ET.setBackgroundResource(R.drawable.cell_shape);
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
                ET.setText("E/T");
                RT.setText("R/T");
            }
            scoringTable.addView(row);
        }


    }
}