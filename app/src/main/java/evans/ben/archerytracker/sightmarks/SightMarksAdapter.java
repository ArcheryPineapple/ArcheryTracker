package evans.ben.archerytracker.sightmarks;

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

public class SightMarksAdapter extends RecyclerView.Adapter<SightMarksAdapter.SightMarksViewHolder> {
    public static class SightMarksViewHolder extends RecyclerView.ViewHolder {
        // Preparing variables for all the views involved in the recyclerview
        public LinearLayout sightMarksContainerView;
        public TextView distTextView;
        public TextView markTextView;

        public SightMarksViewHolder(View view) {
            super(view);
            // Getting access to the views
            this.sightMarksContainerView = view.findViewById(R.id.sightmarks_row);
            this.distTextView = view.findViewById(R.id.sightmarks_row_dist);
            this.markTextView = view.findViewById(R.id.sightmarks_row_mark);

            // On click listener that sends the necessary information about which sight mark was
            // chosen to the sight marks activity
            this.sightMarksContainerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    SightMark sightMark = (SightMark) sightMarksContainerView.getTag();
                    Intent intent = new Intent(view.getContext(), SightMarksActivity.class);
                    intent.putExtra("id", sightMark.id);
                    intent.putExtra("dist", sightMark.dist);
                    intent.putExtra("unit", sightMark.unit);
                    intent.putExtra("mark", sightMark.mark);
                    // Starts the sight mark activity
                    context.startActivity(intent);
                }
            });
        }

    }

    // List of sight marks
    private List<SightMark> sightMarks = new ArrayList<>();

    // These three methods must be implemented when extending RecyclerView
    @NonNull
    @Override
    public SightMarksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sightmarks_row, parent, false);
        return new SightMarksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SightMarksViewHolder holder, int position) {
        SightMark current = sightMarks.get(position);
        holder.sightMarksContainerView.setTag(current);
        holder.distTextView.setText(current.dist + current.unit);
        holder.markTextView.setText(current.mark);
    }

    @Override
    public int getItemCount() {
        return sightMarks.size();
    }

    public void reloadSightMarks() {
        sightMarks = SightMarksFragment.sightMarksDatabase.sightMarksDao().getAllSightMarks();
        notifyDataSetChanged();
    }
}
