package edu.westga.jeffrichardsrollcall.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView;

import java.util.ArrayList;

import edu.westga.jeffrichardsrollcall.R;

public class MainActivity extends AppCompatActivity {
    public final static String CLASS_SELECTION = "edu.westga.betsyjeffwordjumble.CLASS_SELECTION";

    private Spinner classSpinner;
    private boolean ignoreSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.ignoreSelection = true;
        this.classSpinner = (Spinner) findViewById(R.id.spnClasses);
        ArrayList<String> choices = new ArrayList<>();
        // TODO Read class list from DB
        choices.add("Foo");
        choices.add("Bar");
        choices.add("Baz");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, choices);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.classSpinner.setAdapter(adapter);

        this.classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (ignoreSelection) {
                    ignoreSelection = false;
                    return;
                }
                Intent intent = new Intent(MainActivity.this, ClassRollCallActivity.class);
                intent.putExtra(CLASS_SELECTION, (String)parentView.getItemAtPosition(position));
                startActivity(intent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    public void onManage(View view) {

    }
}
