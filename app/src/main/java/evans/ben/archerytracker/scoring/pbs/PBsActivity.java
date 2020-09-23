package evans.ben.archerytracker.scoring.pbs;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import evans.ben.archerytracker.MainActivity;
import evans.ben.archerytracker.R;
import evans.ben.archerytracker.scoring.CompletedRound;
import evans.ben.archerytracker.scoring.Round;

public class PBsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_bs);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Get list of all rounds in database
        List<String> rounds = MainActivity.completedRoundsDatabase.completedRoundsDao().getRounds();
        // Getting access to the linear layout that we'll add textViews to
        LinearLayout linearLayout = findViewById(R.id.pbs_layout);

        // Iterate through all rounds
        for (String round : rounds) {
            int score = MainActivity.completedRoundsDatabase.completedRoundsDao().getPB(round);
            LinearLayout pb = new LinearLayout(this);
            LinearLayout.LayoutParams lpPB = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lpPB.setMargins(0, 0, 0, 20);
            pb.setLayoutParams(lpPB);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            lp.weight = 1;

            TextView roundNameTextView = new TextView(this);
            roundNameTextView.setText(round);
            roundNameTextView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            roundNameTextView.setTextSize(20);
            roundNameTextView.setLayoutParams(lp);
            roundNameTextView.setTypeface(Typeface.DEFAULT_BOLD);
            pb.addView(roundNameTextView);
            // Calculate maximum possible score for this round
            // Calculate maximum score
            int maxArrowVal;
            // Getting necessary information
            Gson gson = new Gson();
            String serialisedRound = MainActivity.completedRoundsDatabase.completedRoundsDao().getRoundObject(round);
            Round roundObject = gson.fromJson(serialisedRound, Round.class);
            int scoringType = roundObject.getScoringType();
            List<String> arrowsDistance = roundObject.getArrowsDistance();
            // Doing the calculation
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
            }
            int totalArrows = 0;
            for (int i = 0; i < arrowsDistance.size(); i++) {
                totalArrows += Integer.parseInt(arrowsDistance.get(i));
            }
            int totalScore = totalArrows * maxArrowVal;
            TextView roundScoreTextView = new TextView(this);
            roundScoreTextView.setText(score + "/" + totalScore);
            roundScoreTextView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            roundScoreTextView.setTextSize(20);
            roundScoreTextView.setLayoutParams(lp);
            pb.addView(roundScoreTextView);
            linearLayout.addView(pb);
        }
    }
}