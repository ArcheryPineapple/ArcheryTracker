package evans.ben.archerytracker.sightmarks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import evans.ben.archerytracker.R;


public class SightMarksFragment extends Fragment {
    private SightMarksAdapter sightMarksAdapter;
    public  static SightMarksDatabase sightMarksDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sightmarks, container, false);

        // Instantiating values
        RecyclerView sightMarksRecyclerView = view.findViewById(R.id.sightmarks_recyclerview);
        RecyclerView.LayoutManager sightMarksLayoutManager = new LinearLayoutManager(view.getContext());
        sightMarksAdapter = new SightMarksAdapter();

        // Setting values
        sightMarksRecyclerView.setLayoutManager(sightMarksLayoutManager);
        sightMarksRecyclerView.setAdapter(sightMarksAdapter);

        // Setting up the database
        sightMarksDatabase = Room.databaseBuilder(view.getContext(), SightMarksDatabase.class, "sightmarks")
                .allowMainThreadQueries().build();

        // This is the "add new sight mark" button
        FloatingActionButton sightMarksFloatingActionButton = view.findViewById(R.id.sightmarks_add_button);
        sightMarksFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long id = sightMarksDatabase.sightMarksDao().createSightMark();
                // Need to send id to sight marks activity but the other fields can be left blank
                // since its a new sight mark
                Context context = view.getContext();
                Intent intent = new Intent(context, SightMarksActivity.class);
                intent.putExtra("id", id);

                context.startActivity(intent);
            }
        });


        return view;
    }

    // Need to include this so the recyclerView refreshes whenever its opened to reflect any changes
    @Override
    public void onResume() {
        super.onResume();
        sightMarksAdapter.reloadSightMarks();
    }
}
