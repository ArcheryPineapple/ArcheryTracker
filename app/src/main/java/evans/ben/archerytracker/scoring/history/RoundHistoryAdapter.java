package evans.ben.archerytracker.scoring.history;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import evans.ben.archerytracker.MainActivity;
import evans.ben.archerytracker.R;
import evans.ben.archerytracker.scoring.CompletedRound;
import evans.ben.archerytracker.scoring.Round;
import evans.ben.archerytracker.scoring.round.RoundActivity;
import evans.ben.archerytracker.scoring.scorecard.ScorecardActivity;

public class RoundHistoryAdapter extends RecyclerView.Adapter<RoundHistoryAdapter.RoundHistoryViewHolder> {


    public static class RoundHistoryViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout historyContainerView;
        public TextView roundNameTextView;
        public TextView roundScoreTextView;

        public RoundHistoryViewHolder(@NonNull View view) {
            super(view);

            historyContainerView = view.findViewById(R.id.history_row);
            roundNameTextView = view.findViewById(R.id.history_row_round_name);
            roundScoreTextView = view.findViewById(R.id.history_row_round_score);

            /* On click listener to start the ScorecardActivity and send the id of the
               CompletedRound selected */
            historyContainerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    CompletedRound completedRound = (CompletedRound) historyContainerView.getTag();
                    Intent intent = new Intent(context, ScorecardActivity.class);
                    intent.putExtra("id", completedRound.id);
                    intent.putExtra("fromHistory", true);
                    context.startActivity(intent);
                }
            });
        }
    }

    // List to store all completed rounds that will be displayed
    private List<CompletedRound> completedRoundList = new ArrayList<>();

    @NonNull
    @Override
    public RoundHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.round_history_row,
                parent, false);
        return new RoundHistoryAdapter.RoundHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoundHistoryViewHolder holder, int position) {
        CompletedRound current = completedRoundList.get(position);
        holder.historyContainerView.setTag(current);
        holder.roundNameTextView.setText(current.roundName);

        // Calculate maximum score
        int maxArrowVal;
        // Getting necessary information
        Gson gson = new Gson();
        String serialisedRound = current.round;
        Round round = gson.fromJson(serialisedRound, Round.class);
        int scoringType = round.getScoringType();
        List<String> arrowsDistance = round.getArrowsDistance();

        // Doing the calculations
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

        holder.roundScoreTextView.setText(current.totalScore + "/" + totalScore);
    }

    @Override
    public int getItemCount() {
        return completedRoundList.size();
    }

    public void reloadHistory() {
        completedRoundList = MainActivity.completedRoundsDatabase.completedRoundsDao().getAllRounds();
        notifyDataSetChanged();
    }

}
