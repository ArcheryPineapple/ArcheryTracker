package evans.ben.archerytracker.arrowcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import evans.ben.archerytracker.R;

public class ArrowCounterActivity extends AppCompatActivity {
    private TextView arrowCounterTextView;
    private int arrowCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrow_counter);

        arrowCounterTextView = findViewById(R.id.arrow_counter);

        Intent intent = getIntent();
        arrowCount = intent.getIntExtra("count", 0);

        arrowCounterTextView.setText(String.format("%03d", arrowCount));

        Button arrowCountAddButton = findViewById(R.id.arrow_counter_add);
        arrowCountAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrowCount += 1;
                arrowCounterTextView.setText(String.format("%03d", arrowCount));
            }
        });

        Button arrowCountMinusButton = findViewById(R.id.arrow_counter_minus);
        arrowCountMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (arrowCount != 0) {
                    arrowCount -= 1;
                    arrowCounterTextView.setText(String.format("%03d", arrowCount));
                }
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        // Sending arrow count to home
        Context context = this;
        SharedPreferences sharedPreferences = context.getSharedPreferences("ArrowCounter", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("counter", arrowCount);
        editor.commit();
    }
}