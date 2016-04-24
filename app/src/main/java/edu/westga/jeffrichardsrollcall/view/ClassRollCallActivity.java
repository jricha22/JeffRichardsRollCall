package edu.westga.jeffrichardsrollcall.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.westga.jeffrichardsrollcall.R;
import edu.westga.jeffrichardsrollcall.model.DatabaseHandler;

public class ClassRollCallActivity extends AppCompatActivity {
    private String className;
    ListView studentList;
    private ArrayAdapter<String> studentAdapter;
    private DatabaseHandler myDbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_roll_call);
        Intent intent = getIntent();
        this.className = intent.getStringExtra(MainActivity.CLASS_SELECTION);
        TextView classLabel = (TextView) findViewById(R.id.lblClassName);
        classLabel.setText(String.format(getResources().getString(R.string.roll_call_label), this.className));
        this.studentList = (ListView) findViewById(R.id.lstStudents);
        this.studentList.setItemsCanFocus(false);
        this.myDbHandler = new DatabaseHandler(this.getApplicationContext());
        ArrayList<String> students = new ArrayList<>();
        this.studentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, students);
        this.studentAdapter.setNotifyOnChange(true);
        this.studentList.setAdapter(this.studentAdapter);

        this.populateStudents();
    }

    private void populateStudents() {
        ArrayList<String> newClasses = this.myDbHandler.getStudentsInClass(this.className);
        this.studentAdapter.clear();
        for (String aClass: newClasses) {
            this.studentAdapter.add(aClass);
        }
    }

    public void onApply(View view) {
        SparseBooleanArray checkedStudents = this.studentList.getCheckedItemPositions();
        ArrayList<String> studentsPresent = new ArrayList<>();
        for (int i = 0; i < checkedStudents.size(); ++i) {
            studentsPresent.add(this.studentList.getItemAtPosition(checkedStudents.keyAt(i)).toString());
        }
        if (this.myDbHandler.updateStudentsPresent(studentsPresent, this.className)) {
            finish();
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Error updating roll call", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 50);
            toast.show();
        }
    }
}
