package evans.ben.archerytracker.coachingnotes;

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


public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    public static class NotesViewHolder extends RecyclerView.ViewHolder {
        // Preparing variables for all the views involved in the recyclerview
        public LinearLayout notesContainerView;
        public TextView dateTextView;
        public TextView titleTextView;

        public NotesViewHolder(View view) {
            super(view);

            // Getting access to the views
            this.notesContainerView = view.findViewById(R.id.notes_row);
            this.dateTextView = view.findViewById(R.id.notes_row_date);
            this.titleTextView = view.findViewById(R.id.notes_row_title);

            // On click listener that sends the necessary information about which note was
            // chosen to the note activity
            this.notesContainerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Note note = (Note) notesContainerView.getTag();
                    Intent intent = new Intent(view.getContext(), NotesActivity.class);
                    intent.putExtra("id", note.id);
                    intent.putExtra("date", note.date);
                    intent.putExtra("title", note.title);
                    intent.putExtra("content", note.content);
                    // Starts the sight mark activity
                    context.startActivity(intent);
                }
            });
        }
    }

    // List to store all notes
    private List<Note> notes = new ArrayList<>();

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_row, parent, false);
        return new NotesAdapter.NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        Note current = notes.get(position);
        holder.notesContainerView.setTag(current);
        holder.dateTextView.setText(current.date);
        holder.titleTextView.setText(current.title);

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void reloadNotes() {
        notes = NotesFragment.notesDatabase.notesDao().getAllNotes();
        notifyDataSetChanged();
    }
}
