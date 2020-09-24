package evans.ben.archerytracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.os.Bundle;
import android.view.MenuItem;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import evans.ben.archerytracker.coachingnotes.NotesFragment;
import evans.ben.archerytracker.scoring.CompletedRoundsDatabase;
import evans.ben.archerytracker.sightmarks.SightMarksFragment;

public class MainActivity extends AppCompatActivity {
    public static CompletedRoundsDatabase completedRoundsDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

        // Setting up CompletedRoundsDatabase
        completedRoundsDatabase = Room.databaseBuilder(this, CompletedRoundsDatabase.class,
                "CompletedRounds").allowMainThreadQueries().build();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_notes:
                            selectedFragment = new NotesFragment();
                            break;
                        case R.id.nav_sightmarks:
                            selectedFragment = new SightMarksFragment();
                            break;
                    }
                    assert selectedFragment != null;
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                return true;
                }
            };
}