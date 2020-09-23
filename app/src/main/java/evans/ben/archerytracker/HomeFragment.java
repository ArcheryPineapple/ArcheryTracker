package evans.ben.archerytracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import evans.ben.archerytracker.arrowcounter.ArrowCounterActivity;
import evans.ben.archerytracker.scoring.history.RoundHistoryActivity;
import evans.ben.archerytracker.scoring.pbs.PBsActivity;
import evans.ben.archerytracker.scoring.round.RoundActivity;
import evans.ben.archerytracker.scoring.roundselection.RoundSelectionActivity;

public class HomeFragment extends Fragment {
    private TextView dailyTextView;
    private TextView weeklyTextView;
    private int dailyCount;
    private int weekVol;
    private SharedPreferences sharedPreferencesArrowCounter;
    private SharedPreferences sharedPreferencesScoring;
    private SharedPreferences.Editor editor;
    private boolean roundInProgress;
    private Button scoringButton;
    private Intent intentScoring;
    private String lastResetWeek;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        context = view.getContext();

        // Getting access to the text views displaying arrow counts
        dailyTextView = view.findViewById(R.id.daily_count_view);
        weeklyTextView = view.findViewById(R.id.weekly_count_view);

        /* Arrow counter button opens the arrow counter activity, sending the current value of
           dailyCount which the activity will alter */
        Button arrowVolumeButton = view.findViewById(R.id.arrow_counter_button);
        arrowVolumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentArrowVolume = new Intent(context, ArrowCounterActivity.class);
                intentArrowVolume.putExtra("count", dailyCount);
                context.startActivity(intentArrowVolume);
            }
        });

        // Round history button opens the round history activity
        Button roundHistoryButton = view.findViewById(R.id.history_button);
        roundHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentHistory = new Intent(context, RoundHistoryActivity.class);
                context.startActivity(intentHistory);
            }
        });

        // PBs button opens the PB activity
        Button pbsButton = view.findViewById(R.id.PB_button);
        pbsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPB = new Intent(context, PBsActivity.class);
                context.startActivity(intentPB);
            }
        });

        /* Scoring button, text will change depending on if a round is in process or not. If no
           round in process this will open the round selection activity, if there is a round in
           process it will open the round activity (not the scoring activity). */
        scoringButton = view.findViewById(R.id.scoring_button);
        /* Intent and context for on click listener need to be up here so that both branches of the
           if statement can assign their values. */
        sharedPreferencesScoring = Objects.requireNonNull(getActivity()).getSharedPreferences("Scoring", Context.MODE_PRIVATE);
        roundInProgress = sharedPreferencesScoring.getBoolean("roundInProgress", false);

        // Determines what text to display on button and which activity to start
        if (!roundInProgress) {
            scoringButton.setText(R.string.new_round);
            intentScoring = new Intent(context, RoundSelectionActivity.class);
        }
        else {
            scoringButton.setText(R.string.continue_round);
            intentScoring = new Intent(context, RoundActivity.class);
        }

        // Opening the activity depending on state of round in progress
        scoringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(intentScoring);
            }
        });

        return view;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onResume() {
        super.onResume();
        // sharedPreferences set up
        sharedPreferencesArrowCounter = Objects.requireNonNull(getActivity()).getSharedPreferences("ArrowCounter", Context.MODE_PRIVATE);
        editor = sharedPreferencesArrowCounter.edit();

        // Getting daily counter values and last reset day/week values from shared preferences
        dailyCount = sharedPreferencesArrowCounter.getInt("counter", 0);
        weekVol = sharedPreferencesArrowCounter.getInt("weekVol", 0);
        String lastResetDay = sharedPreferencesArrowCounter.getString("resetDay", null);
        lastResetWeek = sharedPreferencesArrowCounter.getString("resetWeek", null);

        // Get today's date as a string for easy comparison to lastResetDay
        String today = LocalDate.now().toString();

        // For first set up of the app
        if (lastResetDay == null) {
            // Need to initialise weekVol when app is first used
            weekVol = 0;
            lastResetDay = today;
            editor.putString("resetDay", lastResetDay);
            editor.apply();
        }

        /* Checking to see if we've already reset daily counter today by comparing the stored date
           for last reset with today's date */
        if (!lastResetDay.equals(today)) {
            // Updating weekVol to include arrows shot in the last day
            weekVol += dailyCount;
            // Reseting dailyCount
            dailyCount = 0;
            // Setting the lastResetDay to today as we've just performed a reset
            lastResetDay = today;
            editor.putString("resetDay", lastResetDay);
            editor.apply();
        }

        // Getting today's date as a LocalDate to allow use of LocalDate methods
        LocalDate todayLocalDate = LocalDate.now();
        LocalDate isMonday;
        // This is for first time setup of the app or if an error has occurred in retrieving lastResetWeek
        if (lastResetWeek == null) {
            // Setting the lastResetWeek to the closest Monday before today
            isMonday = todayLocalDate;
            while (true) {
                isMonday = isMonday.minusDays(1);
                if (isMonday.getDayOfWeek() == DayOfWeek.MONDAY) {
                    lastResetWeek = isMonday.toString();
                    break;
                }
            }
        }

        /* Need lastResetWeek as local date for checking if last weekly reset was more
           than 7  days ago */
        LocalDate lastResetWeekLocalDate = LocalDate.parse(lastResetWeek, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        /* For weekly reset if the app is opened on a Monday, this needs to be after daily reset
           otherwise arrows from Sunday are added to weeklyVol after weekly reset. The else if handles
            the case when the user doesn't open the app on a Monday. */
        if (!lastResetWeek.equals(today) && todayLocalDate.getDayOfWeek() == DayOfWeek.MONDAY) {
            weekVol = 0;
            lastResetWeek = today;
            editor.putString("resetWeek", lastResetWeek);
            editor.apply();
        }
        else if(lastResetWeekLocalDate.isBefore(todayLocalDate.minusDays(7))) {
            weekVol = 0;
            isMonday = todayLocalDate;
            while (true) {
                isMonday = isMonday.minusDays(1);
                if (isMonday.getDayOfWeek() == DayOfWeek.MONDAY) {
                    lastResetWeek = isMonday.toString();
                    editor.putString("resetWeek", lastResetWeek);
                    editor.apply();
                    break;
                }
            }
        }

        // Setting weeklyCount
        int weeklyCount = weekVol + dailyCount;

        // Updating text views whenever the view resumes
        dailyTextView.setText(String.format("%03d", dailyCount));
        weeklyTextView.setText(String.format("%03d", weeklyCount));

        // Including this in onResume() to fix button not changing
        roundInProgress = sharedPreferencesScoring.getBoolean("roundInProgress", false);
        // Determines what text to display on button and which activity to start
        if (!roundInProgress) {
            scoringButton.setText(R.string.new_round);
            intentScoring = new Intent(context, RoundSelectionActivity.class);
        }
        else {
            scoringButton.setText(R.string.continue_round);
            intentScoring = new Intent(context, RoundActivity.class);
        }

        // Opening the activity depending on state of round in progress
        scoringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(intentScoring);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        // sharedPreferences set up
        sharedPreferencesArrowCounter = Objects.requireNonNull(getActivity()).getSharedPreferences("ArrowCounter", Context.MODE_PRIVATE);
        editor = sharedPreferencesArrowCounter.edit();
        /* Saving weekVol, dailyCount and resetWeek. If resetWeek isn't here sometimes the app
           doesn't reset the week correctly if a monday is skipped not sure why yet. */
        editor.putInt("weekVol", weekVol);
        editor.putInt("counter", dailyCount);
        editor.putString("resetWeek", lastResetWeek);
        editor.apply();
    }
}
