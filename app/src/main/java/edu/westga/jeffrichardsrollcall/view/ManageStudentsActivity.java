package edu.westga.jeffrichardsrollcall.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.HashMap;

import edu.westga.jeffrichardsrollcall.R;
import edu.westga.jeffrichardsrollcall.model.Attendance;
import edu.westga.jeffrichardsrollcall.model.DatabaseHandler;

public class ManageStudentsActivity extends AppCompatActivity {
    private String className;
    private ListView studentList;
    private Button deleteStudent;
    private Button addStudent;
    private EditText newStudentName;
    private ClassListViewAdapter studentAdapter;
    private DatabaseHandler myDbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_students);
        Intent intent = getIntent();
        this.className = intent.getStringExtra(ManageActivity.CLASS_SELECTION);
        TextView classLabel = (TextView) findViewById(R.id.lblClassName);
        classLabel.setText(String.format(getResources().getString(R.string.class_label), this.className));
        this.studentList = (ListView) findViewById(R.id.lstStudents);
        this.deleteStudent = (Button) findViewById(R.id.btnDeleteStudent);
        this.addStudent = (Button) findViewById(R.id.btnAddStudent);
        this.newStudentName = (EditText) findViewById(R.id.txtStudentName);
        this.myDbHandler = new DatabaseHandler(this.getApplicationContext());
        ArrayList<HashMap<String, String>> classes = new ArrayList<>();
        this.studentAdapter = new ClassListViewAdapter(this, classes);
        this.studentList.setAdapter(this.studentAdapter);

        this.populateStudents();

        this.studentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                ManageStudentsActivity.this.deleteStudent.setEnabled(ManageStudentsActivity.this.studentList.isItemChecked(position));
            }
        });

        this.newStudentName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    ManageStudentsActivity.this.addStudent.setEnabled(false);
                } else {
                    ManageStudentsActivity.this.addStudent.setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // Nothing

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Nothing

            }
        });
    }

    private void populateStudents() {
        ArrayList<Attendance> newStudents = this.myDbHandler.getStudentsInClass(this.className);
        this.studentAdapter.clear();
        for (Attendance aStudent: newStudents) {
            this.studentAdapter.add(aStudent.getName(), Integer.toString(aStudent.getNumClasses()), Integer.toString(aStudent.getAttendencePercent()) + "%");
        }
        this.studentList.setAdapter(this.studentAdapter);
    }

    public void onAddStudent(View view) {
        if (this.myDbHandler.addStudentToClass(this.newStudentName.getText().toString(), this.className)) {
            this.populateStudents();
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Student Already Exists", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 50);
            toast.show();
        }
        this.newStudentName.getText().clear();
    }

    @SuppressWarnings("unchecked")
    public void onDeleteStudent(View view) {
        HashMap<String, String> item = (HashMap<String, String>)this.studentList.getItemAtPosition(this.studentList.getCheckedItemPosition());
        String itemToDelete = item.get("first");
        this.myDbHandler.deleteStudentFromClass(itemToDelete, this.className);
        this.populateStudents();
    }
}
