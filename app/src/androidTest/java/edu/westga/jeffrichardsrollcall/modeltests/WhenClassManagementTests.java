package edu.westga.jeffrichardsrollcall.modeltests;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import java.util.ArrayList;

import edu.westga.jeffrichardsrollcall.model.DatabaseHandler;

/**
 * Tests for managing classes in database
 * Created by Jeff on 4/23/2016.
 */
public class WhenClassManagementTests extends AndroidTestCase {
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

    public void testInitiallyEmpty(){
        ArrayList<String> classes = db.getAllClasses();
        assertEquals(0, classes.size());
    }

    public void testAddAClassSizeIsOne(){
        db.addClass("CS6242-1");
        ArrayList<String> classes = db.getAllClasses();
        assertEquals(1, classes.size());
    }

    public void testAddAClassGetReturnsThatClass(){
        db.addClass("CS6242-1");
        ArrayList<String> classes = db.getAllClasses();
        assertEquals("CS6242-1", classes.get(0));
    }

    public void testAdd2ClassesGetReturns2Classes(){
        db.addClass("CS6242-1");
        db.addClass("CS6242-2");
        ArrayList<String> classes = db.getAllClasses();
        assertEquals(2, classes.size());
    }

    public void testAddDuplicateClassFails(){
        db.addClass("CS6242-1");
        boolean result = db.addClass("CS6242-1");
        ArrayList<String> classes = db.getAllClasses();
        assertEquals(false, result);
    }

    public void testAdd2ClassesDelete1Returns1Class(){
        db.addClass("CS6242-1");
        db.addClass("CS6242-2");
        db.deleteClass("CS6242-1");
        ArrayList<String> classes = db.getAllClasses();
        assertEquals(1, classes.size());
    }

    public void testAdd2ClassesDelete1stReturns2nClass(){
        db.addClass("CS6242-1");
        db.addClass("CS6242-2");
        db.deleteClass("CS6242-1");
        ArrayList<String> classes = db.getAllClasses();
        assertEquals("CS6242-2", classes.get(0));
    }

    public void testDeleteNonExistentClassFails(){
        db.addClass("CS6242-1");
        boolean result = db.deleteClass("CS6242");
        assertEquals(false, result);
    }
}
