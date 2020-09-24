package evans.ben.archerytracker.scoring.roundselection;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import evans.ben.archerytracker.R;
import evans.ben.archerytracker.scoring.Round;
import evans.ben.archerytracker.scoring.round.RoundActivity;

public class RoundSelectionAdapter extends RecyclerView.Adapter<RoundSelectionAdapter.RoundSelectionViewHolder> implements Filterable {
    // Class variable to store filtered list of rounds
    private List<Round> filtered = new ArrayList<>();

    // Filter for searching
    @Override
    public Filter getFilter() {
        return new RoundsFilter();
    }

    private class RoundsFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence searchTerm) {
            List<Round> filteredRounds = new ArrayList<>();
            // Search algorithm

            for (Round i : roundsList) {
                if (i.getRoundName().toLowerCase().contains(searchTerm.toString().toLowerCase())) {
                    filteredRounds.add(i);
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredRounds;
            results.count = filteredRounds.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            filtered = (List<Round>) filterResults.values;
            notifyDataSetChanged();
        }
    }

    public static class RoundSelectionViewHolder extends RecyclerView.ViewHolder {
        // Preparing variables for all the views involved in the recyclerview
        public LinearLayout roundSelectionContainerView;
        public TextView nameTextView;


        public RoundSelectionViewHolder(View view) {
            super(view);

            // Get access to the views
            this.roundSelectionContainerView = view.findViewById(R.id.round_selection_row);
            this.nameTextView = view.findViewById(R.id.round_selection_row_name);


            // To send the information on which round was chosen to the round activity
            this.roundSelectionContainerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Setting up the start of the new activity
                    Context context = view.getContext();
                    Round round = (Round) roundSelectionContainerView.getTag();
                    Intent intent = new Intent(view.getContext(), RoundActivity.class);
                    intent.putExtra("roundSelected", round);
                    context.startActivity(intent);
                }
            });
        }
    }

    // The list of rounds that can be chosen, rounds are alphabetical order with indoor rounds first
    private List<Round> roundsList = Arrays.asList(
            new Round("Portsmouth", 2,
                    new ArrayList<String>() {{ add("First half"); add("Second half"); }},
                    new ArrayList<String>() {{ add("30"); add("30");  }},
                    3),
            new Round("Portsmouth (Half)", 2,
                    new ArrayList<String>() {{ add("18m"); }},
                    new ArrayList<String>() {{ add("30"); }},
                    3),
            new Round("WA18 (3 Spot)", 3,
                    new ArrayList<String>() {{ add("First half"); add("Second half"); }},
                    new ArrayList<String>() {{ add("30"); add("30");  }},
                    3),
            new Round("WA18 (3 Spot, Half)", 3,
                    new ArrayList<String>() {{ add("18m"); }},
                    new ArrayList<String>() {{ add("30"); }},
                    3),
            new Round("WA18 (Full Face)", 2,
                    new ArrayList<String>() {{ add("First half"); add("Second half"); }},
                    new ArrayList<String>() {{ add("30"); add("30");  }},
                    3),
            new Round("WA18 (Full Face, Half)", 2,
                    new ArrayList<String>() {{ add("18m"); }},
                    new ArrayList<String>() {{ add("30"); }},
                    3),
            new Round("Worcester", 4,
                    new ArrayList<String>() {{ add("First half"); add("Second half"); }},
                    new ArrayList<String>() {{ add("30"); add("30"); }},
                    5),
            new Round ("Albion", 1,
                    new ArrayList<String>() {{ add("80yd"); add("60yd"); add("50yd"); }},
                    new ArrayList<String>() {{ add("36"); add("36"); add("36"); }},
                    6),
            new Round("Hereford", 1,
                    new ArrayList<String>() {{ add("80yd"); add("60yd"); add("50yd"); }},
                    new ArrayList<String>() {{ add("72"); add("48"); add("24"); }},
                    6),
            new Round("Long Metric (Gents)", 0,
                    new ArrayList<String>() {{ add("90m"); add("70m"); }},
                    new ArrayList<String>() {{ add("36"); add("36"); }},
                    6),
            new Round("Long Metric (Ladies)", 0,
                    new ArrayList<String>() {{ add("70m"); add("60m"); }},
                    new ArrayList<String>() {{ add("36"); add("36"); }},
                    6),
            new Round("National", 1,
                    new ArrayList<String>() {{ add("60yd"); add("50yd"); }},
                    new ArrayList<String>() {{ add("48"); add("24"); }},
                    6),
            new Round("Short Metric 1", 0,
                    new ArrayList<String>() {{ add("50m"); add("30m"); }},
                    new ArrayList<String>() {{ add("36"); add("36"); }},
                    6),
            new Round ("St George", 1,
                    new ArrayList<String>() {{ add("100yd"); add("80yd"); add("60yd"); }},
                    new ArrayList<String>() {{ add("36"); add("36"); add("36"); }},
                    6),
            new Round("WA1440 (Gents)", 0,
                  new ArrayList<String>() {{ add("90m"); add("70m"); add("50m"); add("30m"); }},
                  new ArrayList<String>() {{ add("36"); add("36"); add("36"); add("36"); }},
                  6),
            new Round("WA1440 (Ladies)", 0,
                    new ArrayList<String>() {{ add("70m"); add("60m"); add("50m"); add("30m"); }},
                    new ArrayList<String>() {{ add("36"); add("36"); add("36"); add("36"); }},
                    6),
            new Round("WA 70", 0,
                    new ArrayList<String>() {{ add("First half"); add("Second half"); }},
                    new ArrayList<String>() {{ add("36"); add("36"); }},
                    6),
            new Round("York", 1,
                    new ArrayList<String>() {{ add("100yd"); add("80yd"); add("60yd"); }},
                    new ArrayList<String>() {{ add("72"); add("48"); add("24"); }},
                    6)
    );


    @NonNull
    @Override
    public RoundSelectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.round_selection_row, parent, false);
        return new RoundSelectionAdapter.RoundSelectionViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RoundSelectionViewHolder holder, int position) {
        Round current;
        if (filtered == null || filtered.size() == 0) {
            current = roundsList.get(position);
        }
        else {
            current = filtered.get(position);
        }
        holder.roundSelectionContainerView.setTag(current);
        holder.nameTextView.setText(current.getRoundName());
    }

    @Override
    public int getItemCount() {
        if (filtered == null || filtered.size() == 0) {
            return roundsList.size();
        }
        else {
            return filtered.size();
        }
    }
}
