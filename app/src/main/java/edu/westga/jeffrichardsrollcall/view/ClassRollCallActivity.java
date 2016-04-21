package edu.westga.jeffrichardsrollcall.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import edu.westga.jeffrichardsrollcall.R;

public class ClassRollCallActivity extends AppCompatActivity {
    private TextView selectedClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_roll_call);
        this.selectedClass = (TextView) findViewById(R.id.lblText);
        Intent intent = getIntent();
        String myClass = intent.getStringExtra(MainActivity.CLASS_SELECTION);
        this.selectedClass.setText(myClass);
    }

}
