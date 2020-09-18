package evans.ben.archerytracker.scoring.round;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import evans.ben.archerytracker.R;

public class RoundAdapter extends RecyclerView.Adapter<RoundAdapter.RoundViewHolder> {
    // Trying to pass the data set from the RoundActivity using a constructor
    private List<String> distancesList;
    private List<String> arrowsDistanceList;
    private int maxArrowValue;

    public static class RoundViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout roundContainerView;
        public TextView roundDistance;
        public TextView roundScore;

        public RoundViewHolder(View view) {
            super(view);

            this.roundContainerView = view.findViewById(R.id.round_row);
            this.roundDistance = view.findViewById(R.id.round_row_dist);
            this.roundScore = view.findViewById(R.id.round_row_score);

            // Put on click listener here later

        }
    }

    // Constructor to allow list of distances for the round selected to be passed to the adapter
    public RoundAdapter(List<String> distances, List<String> arrowsDistance, int maxArrowVal) {
        distancesList = distances;
        arrowsDistanceList = arrowsDistance;
        maxArrowValue = maxArrowVal;
    }

    @NonNull
    @Override
    public RoundAdapter.RoundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.round_row, parent, false);
        return new RoundAdapter.RoundViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoundAdapter.RoundViewHolder holder, int position) {
        String current = distancesList.get(position);
        holder.roundContainerView.setTag(current);
        holder.roundDistance.setText(distancesList.get(position));
        int arrowsPerDistance = Integer.parseInt(arrowsDistanceList.get(position));
        int totalDistance = arrowsPerDistance * maxArrowValue;
        String totalDistanceString = Integer.toString(totalDistance);
        // 0 is a placeholder which filled with score for this distance once I get round to scoring
        holder.roundScore.setText(0 + "/" + totalDistanceString);
    }

    @Override
    public int getItemCount() {
        return distancesList.size();
    }
}