package evans.ben.archerytracker.scoring.round;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import evans.ben.archerytracker.R;
import evans.ben.archerytracker.scoring.Round;
import evans.ben.archerytracker.scoring.input.Distance;
import evans.ben.archerytracker.scoring.input.ScoringActivity;

public class RoundAdapter extends RecyclerView.Adapter<RoundAdapter.RoundViewHolder> {
    private int maxArrowValue;
    private List<Distance> distances = new ArrayList<>();

    public static class RoundViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout roundContainerView;
        public TextView roundDistance;
        public TextView roundScore;

        public RoundViewHolder(View view) {
            super(view);

            this.roundContainerView = view.findViewById(R.id.round_row);
            this.roundDistance = view.findViewById(R.id.round_row_dist);
            this.roundScore = view.findViewById(R.id.round_row_score);

            this.roundContainerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Distance distance = (Distance) roundContainerView.getTag();
                    Intent intent = new Intent(view.getContext(), ScoringActivity.class);
                    intent.putExtra("distance", distance);
                    context.startActivity(intent);
                }
            });

        }
    }

    // Constructor to allow list of distances for the round selected to be passed to the adapter
    public RoundAdapter(Round round, int maxArrowVal, List<Integer> distanceValues) {
        // Trying to pass the data set from the RoundActivity using a constructor
        List<String> distancesList = round.getDistances();
        List<String> arrowsDistanceList = round.getArrowsDistance();
        maxArrowValue = maxArrowVal;

        for (int i = 0; i < distancesList.size(); i++) {
            distances.add(new Distance(distancesList.get(i), arrowsDistanceList.get(i), round.getScoringType(), round.getArrowsPerEnd(), distanceValues.get(i)));
        }
    }




    @NonNull
    @Override
    public RoundAdapter.RoundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.round_row, parent, false);
        return new RoundAdapter.RoundViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RoundAdapter.RoundViewHolder holder, int position) {
        Distance current = distances.get(position);
        holder.roundContainerView.setTag(current);
        holder.roundDistance.setText(current.getDistance());
        int arrowsPerDistance = Integer.parseInt(current.getArrowsAtDistance());
        int totalDistance = arrowsPerDistance * maxArrowValue;
        String totalDistanceString = Integer.toString(totalDistance);
        // 0 is a placeholder which filled with score for this distance once I get round to scoring
        holder.roundScore.setText(current.getDistanceValue() + "/" + totalDistanceString);
    }

    @Override
    public int getItemCount() {
        return distances.size();
    }
}