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

import edu.westga.jeffrichardsrollcall.R;
import edu.westga.jeffrichardsrollcall.model.DatabaseHandler;

public class ManageStudentsActivity extends AppCompatActivity {
    private String className;
    private ListView studentList;
    private Button deleteStudent;
    private Button addStudent;
    private EditText newStudentName;
    private ArrayAdapter<String> studentAdapter;
    private DatabaseHandler myDbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_students);
        Intent intent = getIntent();
        this.className = intent.getStringExtra(ManageActivity.CLASS_SELECTION);
        TextView classLabel = (TextView) findViewById(R.id.lblClassName);
        classLabel.setText(getResources().getString(R.string.class_label) + " " + this.className);
        this.studentList = (ListView) findViewById(R.id.lstStudents);
        this.deleteStudent = (Button) findViewById(R.id.btnDeleteStudent);
        this.addStudent = (Button) findViewById(R.id.btnAddStudent);
        this.newStudentName = (EditText) findViewById(R.id.txtStudentName);
        this.myDbHandler = new DatabaseHandler(this.getApplicationContext());
        ArrayList<String> students = new ArrayList<>();
        this.studentAdapter = new ArrayAdapter<>(this, R.layout.class_listview, students);
        this.studentAdapter.setNotifyOnChange(true);
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
        ArrayList<String> newClasses = this.myDbHandler.getStudentsInClass(this.className);
        this.studentAdapter.clear();
        for (String aClass: newClasses) {
            this.studentAdapter.add(aClass);
        }
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

    public void onDeleteStudent(View view) {
        String itemToDelete = this.studentList.getItemAtPosition(this.studentList.getCheckedItemPosition()).toString();
        this.myDbHandler.deleteStudentFromClass(itemToDelete, this.className);
        this.populateStudents();
    }
}
