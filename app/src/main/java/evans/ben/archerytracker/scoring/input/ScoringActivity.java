package evans.ben.archerytracker.scoring.input;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
    }
}