package evans.ben.archerytracker.scoring.input;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
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
        // Used to fix the dimensions of the boxes on the scoring table

        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        int dim = 90;
        int dimTotalsAlter = 50;
        int textSize = 20;

        for (int i  = 0; i < rows; i++) {
            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            TableRow.LayoutParams headerLp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            headerLp.span = arrowsEnd;
            row.setLayoutParams(lp);
            if (i == 0) {
                TextView arrowsHeader = new TextView(this);
                arrowsHeader.setLayoutParams(headerLp);
                arrowsHeader.setTextSize(textSize);
                arrowsHeader.setPadding(5,5,5,5);
                arrowsHeader.setBackgroundResource(R.drawable.cell_shape);
                arrowsHeader.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                arrowsHeader.setText("Arrows");
                arrowsHeader.setHeight(dim);
                arrowsHeader.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                row.addView(arrowsHeader);
            }
            else {
                for (int j = 0; j < arrowsEnd; j++) {
                    TextView arrow = new TextView(this);
                    arrow.setHeight(dim);
                    arrow.setWidth(dim);
                    arrow.setTextSize(textSize);
                    arrow.setPadding(5, 5,5,5 );
                    // Setting borders for cells
                    arrow.setBackgroundResource(R.drawable.cell_shape);
                    row.addView(arrow);
            }

            }

            // Setting up the columns at the end of the scoring table
            TextView ET = new TextView(this);
            ET.setTextSize(textSize);
            ET.setPadding(5,5,5,5);
            ET.setBackgroundResource(R.drawable.cell_shape);
            ET.setHeight(dim);
            ET.setWidth(dim + dimTotalsAlter);
            ET.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            row.addView(ET);

            TextView Hits = new TextView(this);
            Hits.setTextSize(textSize);
            Hits.setPadding(5,5,5,5);
            Hits.setBackgroundResource(R.drawable.cell_shape);
            Hits.setHeight(dim);
            Hits.setWidth(dim);
            Hits.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            row.addView(Hits);

            TextView Golds = new TextView(this);
            Golds.setTextSize(textSize);
            Golds.setPadding(5,5,5,5);
            Golds.setBackgroundResource(R.drawable.cell_shape);
            Golds.setHeight(dim);
            Golds.setWidth(dim);
            Golds.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            row.addView(Golds);

            TextView RT = new TextView(this);
            RT.setTextSize(textSize);
            RT.setPadding(5,5,5,5);
            RT.setBackgroundResource(R.drawable.cell_shape);
            RT.setHeight(dim);
            RT.setWidth(dim + dimTotalsAlter);
            RT.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            row.addView(RT);

            // For the first row we want to label the columns and give them a thick bottom border
            if (i == 0) {
                ET.setText("E/T");
                Hits.setText("H");
                Golds.setText("G");
                RT.setText("R/T");
            }
            scoringTable.addView(row);
        }


    }
}