package evans.ben.archerytracker.sightmarks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import evans.ben.archerytracker.R;

public class SightMarksActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText sightMarksDistEditText;
    private EditText sightMarksMarkEditText;
    private String unit;
    private int int_dist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sight_marks);

        // Getting the information which started this activity
        Intent intent = getIntent();

        // Accessing the views in the activity
        sightMarksDistEditText = findViewById(R.id.sightmarks_dist_edit_text);
        sightMarksMarkEditText = findViewById(R.id.sightmarks_mark_edit_text);

        /* Getting the distance from the SQL table as a string, getting it as a string to protect
           against users entering non numeric distances. */
        String dist = Integer.toString(intent.getIntExtra("dist", 0));

        /* If distance equals 0 its either the default value or the distance was entered in error
           so need to replace it with null so that cursor starts there */
        if (dist.equals("0")) {
            dist = null;
        }

        // Setting the textViews
        sightMarksDistEditText.setText(dist);
        sightMarksMarkEditText.setText(intent.getStringExtra("mark"));

        // Getting the unit from the SQL table
        unit = intent.getStringExtra("unit");

        // Setting focus on distance if empty and sight mark otherwise
        if (dist == null || dist.equals("")) {
            sightMarksDistEditText.requestFocus();
        }
        else {
            sightMarksMarkEditText.requestFocus();
        }

        // Setting up spinner for unit selection
        Spinner sightMarksSpinner = (Spinner) findViewById(R.id.sightmarks_unit_spinner);
        ArrayAdapter<CharSequence> sightMarksSpinnerAdapter = ArrayAdapter
                .createFromResource(this,
                R.array.sightmark_unit_options, android.R.layout.simple_spinner_item);
        sightMarksSpinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sightMarksSpinner.setAdapter(sightMarksSpinnerAdapter);

        // Setting spinner to show the saved unit
        int unit_selected = 0;
        if (unit != null) {
            if (unit.equalsIgnoreCase("m")) {
                unit_selected = 1;
            } else if (unit.equalsIgnoreCase("yd")) {
                unit_selected = 2;
            }
        }
        sightMarksSpinner.setSelection(unit_selected);
        // Calling the the selection method for the spinner so it updates
        sightMarksSpinner.setOnItemSelectedListener(this);

        // Save button
        FloatingActionButton save = findViewById(R.id.sightmarks_save_button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveOrDel();
                finish();
            }
        });

        // Delete button
        FloatingActionButton del = findViewById(R.id.sightmarks_delete_button);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_del = getIntent();
                long id_del = intent_del.getLongExtra("id", 0);
                SightMarksFragment.sightMarksDatabase.sightMarksDao().deleteSightMark(id_del);
                finish();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        saveOrDel();
        /* Added this so if the user exits the app via the multitask button next time the app is
           opened it will go to the sight marks fragment not let the user edit a sight mark that
           won't be saved. */
        finish();
    }

    // Used by spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        unit = (String) adapterView.getItemAtPosition(i);
    }

    // Necessary for spinner
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /* If the user doesn't input a numerical distance set int_dist to 0, in normal use the user
       shouldn't be able to enter in non numeric characters so this is slight redundant. I'm keeping
        it just in case a user manages to enter a non numeric character somehow */
    public void distToInt (String dist) {
        if (dist == null) {
            int_dist = 0;
        }
        try {
            int_dist = Integer.parseInt(dist);
        }
        catch (NumberFormatException nfe) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "The distance you entered was not a number it has been set to 0.",
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            int_dist = 0;
        }
    }

    /* This method detects if any fields are empty and if there are any empty fields it deletes the
       sight mark and informs the user. Otherwise if the sight mark is fully completed in saves the
       sight mark to the SQL table. */
    public void saveOrDel () {
        String dist = sightMarksDistEditText.getText().toString();
        String mark = sightMarksMarkEditText.getText().toString();

        Intent intent = getIntent();
        long id = intent.getLongExtra("id", 0);

        // Automatically delete a sight mark with any field empty
        if (dist.equals("") || mark.equals("") || unit.equals("Choose unit")) {
            SightMarksFragment.sightMarksDatabase.sightMarksDao().deleteSightMark(id);
            Toast toast = Toast.makeText(getApplicationContext(),
                    "The sight mark was incomplete, it has been deleted.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        else {
            distToInt(dist);
            SightMarksFragment.sightMarksDatabase.sightMarksDao().saveSightMark(id, int_dist, unit,
                    mark);
        }
    }
}