package edu.westga.jeffrichardsrollcall.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView;

import java.util.ArrayList;

import edu.westga.jeffrichardsrollcall.R;
import edu.westga.jeffrichardsrollcall.model.Attendance;
import edu.westga.jeffrichardsrollcall.model.DatabaseHandler;

public class MainActivity extends AppCompatActivity {
    public final static String CLASS_SELECTION = "edu.westga.jeffrichardsrollcall.main.CLASS_SELECTION";

    private Spinner classSpinner;
    private ArrayAdapter<String> classList;
    private DatabaseHandler myDbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.myDbHandler = new DatabaseHandler(this.getApplicationContext());
        this.classSpinner = (Spinner) findViewById(R.id.spnClasses);
        ArrayList<String> choices = new ArrayList<>();
        this.classList = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, choices);
        this.classList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.classSpinner.setAdapter(this.classList);

        this.classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
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

    @Override
    public void onResume(){
        super.onResume();
        this.populateClasses();
        this.classSpinner.setSelection(0);
    }

    public void onManage(View view) {
        Intent intent = new Intent(this, ManageActivity.class);
        startActivity(intent);
    }

    private void populateClasses() {
        ArrayList<Attendance> attendances = this.myDbHandler.getAllClasses();
        ArrayList<String> newClasses = new ArrayList<>();
        for (Attendance aAttendance : attendances) {
            newClasses.add(aAttendance.getName());
        }
        this.classList.clear();
        this.classList.add(getResources().getString(R.string.select_class));
        for (String aClass: newClasses) {
            this.classList.add(aClass);
        }
    }
}
