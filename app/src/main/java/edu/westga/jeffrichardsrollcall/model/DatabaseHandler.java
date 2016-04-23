package edu.westga.jeffrichardsrollcall.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Database handler for roll call database
 *
 * Created by Jeff on 4/21/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "rollCall";
    private static final String TABLE_CLASSES = "classes";
    private static final String CLASS_ID = "id";
    private static final String CLASS_ROLL_COUNT = "count";


    private static final String TABLE_STUDENTS = "students";
    private static final String STUDENT_ID = "id";
    private static final String STUDENT_NAME = "name";
    private static final String CLASS_KEY = "class";
    private static final String STUDENT_ROLL_COUNT = "count";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CLASSES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CLASSES + "(" +
                CLASS_ID + " TEXT PRIMARY KEY, " +
                CLASS_ROLL_COUNT + " INTEGER NOT NULL DEFAULT 0 " +
                ")";
        String CREATE_STUDENTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_STUDENTS + "(" +
                STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                STUDENT_NAME + " TEXT NOT NULL, " +
                CLASS_KEY + " TEXT NOT NULL, " +
                STUDENT_ROLL_COUNT + " INTEGER NOT NULL DEFAULT 0, " +
                "FOREIGN KEY(" + CLASS_KEY + ") REFERENCES " + TABLE_CLASSES + "(" + CLASS_ID + ") ON DELETE CASCADE, " +
                "UNIQUE (" + STUDENT_NAME + ", " + CLASS_KEY + ")" +
                ")";
        System.out.println(CREATE_CLASSES_TABLE);
        System.out.println(CREATE_STUDENTS_TABLE);
        db.execSQL(CREATE_CLASSES_TABLE);
        db.execSQL(CREATE_STUDENTS_TABLE);
    }

    @Override
    public void onConfigure(SQLiteDatabase db){
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        onCreate(db);
    }

    public boolean addClass(String newClass) {
        boolean retval = true;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CLASS_ID, newClass);

        // Inserting Row
        try {
            db.insertOrThrow(TABLE_CLASSES, null, values);
        } catch (SQLiteConstraintException exc) {
            retval = false;
        }
        db.close();
        return retval;
    }

    public ArrayList<String> getAllClasses() {
        ArrayList<String> classList = new ArrayList<>();
        String selectQuery = "SELECT id FROM " + TABLE_CLASSES + " ORDER BY " + CLASS_ID;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                classList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        db.close();
        return classList;
    }
    /*
    // Getting single contact
    Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                        KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return contact
        return contact;
    }

    // Updating single contact
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PH_NO, contact.getPhoneNumber());

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
    }
    */

    // Deleting single contact
    public boolean deleteClass(String deletedClass) {
        SQLiteDatabase db = this.getWritableDatabase();
        int count = db.delete(TABLE_CLASSES, CLASS_ID + " = ?", new String[] { deletedClass });
        db.close();
        return count == 1;
    }


    /**
    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
     */

    public ArrayList<String> getStudentsInClass(String theClass) {
        ArrayList<String> studentList = new ArrayList<>();
        String selectQuery = "SELECT " + STUDENT_NAME +
                " FROM " + TABLE_STUDENTS +
                " WHERE " + CLASS_KEY + " = ? " +
                " ORDER BY " + STUDENT_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] { theClass });

        if (cursor.moveToFirst()) {
            do {
                studentList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        db.close();
        return studentList;
    }

    public boolean addStudentToClass(String student, String theClass) {
        boolean retval = true;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(STUDENT_NAME, student);
        values.put(CLASS_KEY, theClass);

        // Inserting Row
        try {
            db.insertOrThrow(TABLE_STUDENTS, null, values);
        } catch (SQLiteConstraintException exc) {
            retval = false;
        }
        db.close();
        return retval;
    }

    public boolean deleteStudentFromClass(String student, String theClass) {
        SQLiteDatabase db = this.getWritableDatabase();
        int count = db.delete(TABLE_STUDENTS, STUDENT_NAME + " = ? and " + CLASS_KEY + " = ?", new String[] { student, theClass });
        db.close();
        return count == 1;
    }
}
