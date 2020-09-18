package evans.ben.archerytracker.coachingnotes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import evans.ben.archerytracker.R;
import evans.ben.archerytracker.sightmarks.SightMarksActivity;

public class NotesFragment extends Fragment {
    private RecyclerView notesRecyclerView;
    private RecyclerView.LayoutManager notesLayoutManager;
    private NotesAdapter notesAdapter;
    public static NotesDatabase notesDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        // Instantiating values
        notesRecyclerView = view.findViewById(R.id.notes_recyclerview);
        notesLayoutManager = new LinearLayoutManager(view.getContext());
        notesAdapter = new NotesAdapter();

        // Setting values
        notesRecyclerView.setLayoutManager(notesLayoutManager);
        notesRecyclerView.setAdapter(notesAdapter);

        // Setting up the database
        notesDatabase = Room.databaseBuilder(view.getContext(), NotesDatabase.class, "notes")
                .allowMainThreadQueries().build();

        // This is the "add new note" button
        FloatingActionButton sightMarksFloatingActionButton = view.findViewById(R.id.notes_add_button);
        sightMarksFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Getting the current date
                String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MMM-yy"));
                // Creating a new note with the date filled in
                long id = notesDatabase.notesDao().createNote(today);
                // Need to send id and date to notes activity but the other fields can be left blank
                // since its a new note
                Context context = view.getContext();
                Intent intent = new Intent(context, NotesActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("date", today);

                context.startActivity(intent);
            }
        });

        return view;
    }


    // Need to include this so the recyclerView refreshes whenever its opened to reflect any changes
    @Override
    public void onResume() {
        super.onResume();
        notesAdapter.reloadNotes();
    }
}
