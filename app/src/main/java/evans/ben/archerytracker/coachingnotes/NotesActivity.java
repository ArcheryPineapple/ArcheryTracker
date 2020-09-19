package evans.ben.archerytracker.coachingnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import evans.ben.archerytracker.R;


public class NotesActivity extends AppCompatActivity {
    private EditText notesTitleEditText;
    private EditText notesContentEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        // Getting the information that started this activity
        Intent intent = getIntent();

        // Accessing the views in the activity
        TextView notesDateTextView = findViewById(R.id.notes_date);
        notesTitleEditText = findViewById(R.id.notes_title);
        notesContentEditText = findViewById(R.id.notes_content);

        // Setting the views
        notesDateTextView.setText(intent.getStringExtra("date"));
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        notesTitleEditText.setText(title);
        notesContentEditText.setText(content);

        // Getting the keyboard to appear only if a field is empty
        if (title == null || title.equals("")) {
            notesTitleEditText.requestFocus();
        }
        else if (content == null || content.equals("")) {
            notesContentEditText.requestFocus();
        }

        // Save button
        FloatingActionButton save = findViewById(R.id.notes_save_button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveOrDel();
                finish();
            }
        });

        // Delete button
        FloatingActionButton del = findViewById(R.id.notes_delete_button);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_del = getIntent();
                long id_del = intent_del.getLongExtra("id", 0);
                NotesFragment.notesDatabase.notesDao().deleteNote(id_del);
                finish();
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        saveOrDel();
        /* Added this so if the user exits the app via the multitask button next time the app is
           opened it will go to the coaching notes fragment not let the user edit a note that
           won't be saved. */
        finish();
    }



    /* This method detects if the title is empty and if it is then it deletes the note and informs
       the user. Otherwise it saves the note to the SQL table. */
    public void saveOrDel () {
        String title = notesTitleEditText.getText().toString();
        String content = notesContentEditText.getText().toString();

        Intent intent = getIntent();
        long id = intent.getLongExtra("id", 0);

        // Automatically delete a note if the title is empty
        if (title.equals("")) {
            NotesFragment.notesDatabase.notesDao().deleteNote(id);
            Toast toast = Toast.makeText(getApplicationContext(),
                    "The note was incomplete, it has been deleted.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        else {
            NotesFragment.notesDatabase.notesDao().saveNote(id, title, content);
        }
    }
}