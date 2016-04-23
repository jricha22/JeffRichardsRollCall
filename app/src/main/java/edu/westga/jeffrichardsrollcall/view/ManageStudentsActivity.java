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

public class ManageStudentsActivity extends AppCompatActivity {
    private TextView selectedClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_students);
        this.selectedClass = (TextView) findViewById(R.id.lblClass);
        Intent intent = getIntent();
        String myClass = intent.getStringExtra(ManageActivity.CLASS_SELECTION);
        this.selectedClass.setText(myClass);
    }
}
