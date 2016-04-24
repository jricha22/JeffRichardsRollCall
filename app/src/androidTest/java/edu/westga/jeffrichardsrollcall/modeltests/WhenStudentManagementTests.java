package edu.westga.jeffrichardsrollcall.modeltests;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import java.util.ArrayList;

import edu.westga.jeffrichardsrollcall.model.Attendance;
import edu.westga.jeffrichardsrollcall.model.DatabaseHandler;

/**
 * Test cases for managing students in classes
 * Created by Jeff on 4/23/2016.
 */
public class WhenStudentManagementTests extends AndroidTestCase {
    private DatabaseHandler db;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        db = new DatabaseHandler(context);
    }

    @Override
    public void tearDown() throws Exception {
        db.close();
        super.tearDown();
    }

    public void testClassInitiallyEmpty(){
        db.addClass("CS6242-1");
        ArrayList<Attendance> students = db.getStudentsInClass("CS6242-1");
        assertEquals(0, students.size());
    }

    public void testAddingOneStudentGivesClassSize1(){
        db.addClass("CS6242-1");
        db.addStudentToClass("Sam", "CS6242-1");
        ArrayList<Attendance> students = db.getStudentsInClass("CS6242-1");
        assertEquals(1, students.size());
    }

    public void testAddingTwoStudentsDeleting1GivesClassSize1(){
        db.addClass("CS6242-1");
        db.addStudentToClass("Sam", "CS6242-1");
        db.addStudentToClass("Tom", "CS6242-1");
        db.deleteStudentFromClass("Sam", "CS6242-1");
        ArrayList<Attendance> students = db.getStudentsInClass("CS6242-1");
        assertEquals(1, students.size());
    }

    public void testAddingSameStudentToClassFails(){
        db.addClass("CS6242-1");
        db.addStudentToClass("Sam", "CS6242-1");
        boolean res = db.addStudentToClass("Sam", "CS6242-1");
        assertEquals(res, false);
    }

    public void testCanDeleteClassWithStudents(){
        db.addClass("CS6242-1");
        db.addStudentToClass("Sam", "CS6242-1");
        boolean res  = db.deleteClass("CS6242-1");
        assertEquals(res, true);
    }

    public void testAddingTwoStudentsClassHasBothStudentsInAlphaOrder(){
        db.addClass("CS6242-1");
        db.addStudentToClass("Z", "CS6242-1");
        db.addStudentToClass("A", "CS6242-1");
        ArrayList<Attendance> records = db.getStudentsInClass("CS6242-1");
        ArrayList<String> students = new ArrayList<>();
        for (Attendance cur: records) {
            students.add(cur.getName());
        }
        ArrayList<String> expected = new ArrayList<>();
        expected.add("A");
        expected.add("Z");
        assertEquals(expected, students);
    }

}
