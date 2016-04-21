package edu.westga.jeffrichardsrollcall.view;

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

import java.util.ArrayList;

import edu.westga.jeffrichardsrollcall.R;

public class MainActivity extends AppCompatActivity {

    private Spinner classSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.classSpinner = (Spinner) findViewById(R.id.spnClasses);
        ArrayList<String> choices = new ArrayList<String>();
        choices.add("Foo");
        choices.add("Bar");
        choices.add("Baz");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, choices);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.classSpinner.setAdapter(adapter);
    }

    public void onManage(View view) {

    }
}
