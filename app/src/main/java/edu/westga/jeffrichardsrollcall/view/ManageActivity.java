package edu.westga.jeffrichardsrollcall.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextWatcher;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import edu.westga.jeffrichardsrollcall.R;
import edu.westga.jeffrichardsrollcall.model.Attendance;
import edu.westga.jeffrichardsrollcall.model.DatabaseHandler;

public class ManageActivity extends AppCompatActivity {
    public final static String CLASS_SELECTION = "edu.westga.jeffrichardsrollcall.manage.CLASS_SELECTION";

    private ListView classList;
    private Button deleteClass;
    private Button manageStudents;
    private Button addClass;
    private EditText newClassName;
    private ClassListViewAdapter classAdapter;
    private DatabaseHandler myDbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        this.classList = (ListView) findViewById(R.id.lstClasses);
        this.deleteClass = (Button) findViewById(R.id.btnDeleteClass);
        this.manageStudents = (Button) findViewById(R.id.btnManageStudents);
        this.addClass = (Button) findViewById(R.id.btnAddClass);
        this.newClassName = (EditText) findViewById(R.id.txtClassName);
        this.myDbHandler = new DatabaseHandler(this.getApplicationContext());
        ArrayList<HashMap<String, String>> classes = new ArrayList<>();
        this.classAdapter = new ClassListViewAdapter(this, classes);
        this.classList.setAdapter(this.classAdapter);

        this.populateClasses();

        this.classList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                ManageActivity.this.deleteClass.setEnabled(ManageActivity.this.classList.isItemChecked(position));
                ManageActivity.this.manageStudents.setEnabled(ManageActivity.this.classList.isItemChecked(position));
            }
        });

        this.newClassName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    ManageActivity.this.addClass.setEnabled(false);
                } else {
                    ManageActivity.this.addClass.setEnabled(true);
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

    @Override
    public void onResume(){
        super.onResume();
        this.populateClasses();
    }

    private void populateClasses() {
        ArrayList<Attendance> newClasses = this.myDbHandler.getAllClasses();
        this.classAdapter.clear();
        for (Attendance aClass: newClasses) {
            this.classAdapter.add(aClass.getName(), Integer.toString(aClass.getNumClasses()), Integer.toString(aClass.getAttendencePercent()) + "%");
        }
        this.deleteClass.setEnabled(false);
        this.manageStudents.setEnabled(false);
        this.classList.setAdapter(this.classAdapter);
    }

    public void onAddClass(View view) {
        if (this.myDbHandler.addClass(this.newClassName.getText().toString())) {
            this.populateClasses();
        } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Class Already Exists", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 50);
                toast.show();
        }
        this.newClassName.getText().clear();
    }

    @SuppressWarnings("unchecked")
    public void onDeleteClass(View view) {
        HashMap<String, String> item = (HashMap<String, String>)this.classList.getItemAtPosition(this.classList.getCheckedItemPosition());
        String itemToDelete = item.get("first");
        this.myDbHandler.deleteClass(itemToDelete);
        this.populateClasses();
    }

    @SuppressWarnings("unchecked")
    public void onManageStudents(View view) {
        HashMap<String, String> item = (HashMap<String, String>)this.classList.getItemAtPosition(this.classList.getCheckedItemPosition());
        String itemToManage = item.get("first");
        Intent intent = new Intent(this, ManageStudentsActivity.class);
        intent.putExtra(CLASS_SELECTION, itemToManage);
        startActivity(intent);
    }
}
