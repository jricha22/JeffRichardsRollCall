package edu.westga.jeffrichardsrollcall.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextWatcher;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import edu.westga.jeffrichardsrollcall.R;

public class ManageActivity extends AppCompatActivity {
    private ListView classList;
    private Button deleteClass;
    private Button manageStudents;
    private Button addClass;
    private EditText newClassName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        this.classList = (ListView) findViewById(R.id.lstClasses);
        this.deleteClass = (Button) findViewById(R.id.btnDeleteClass);
        this.manageStudents = (Button) findViewById(R.id.btnManageStudents);
        this.addClass = (Button) findViewById(R.id.btnAddClass);
        this.newClassName = (EditText) findViewById(R.id.txtClassName);
        ArrayList<String> classes = new ArrayList<>();
        classes.add("CS6242-1");
        classes.add("CS6242-2");
        classes.add("CS6242-3");
        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.class_listview, classes);
        this.classList.setAdapter(adapter);

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
}
