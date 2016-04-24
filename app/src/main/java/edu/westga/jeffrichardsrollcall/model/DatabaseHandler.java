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

    private static final int DATABASE_VERSION = 4;
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

    public ArrayList<Attendance> getAllClasses() {
        ArrayList<Attendance> classList = new ArrayList<>();
        String selectQuery = "SELECT " + CLASS_ID + ", " + CLASS_ROLL_COUNT + " FROM " + TABLE_CLASSES + " ORDER BY " + CLASS_ID;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                int count = cursor.getInt(1);
                int numStudents = 0;
                int studentTotal = 0;

                String studentCountQuery = "SELECT count(" + STUDENT_NAME + "), " + "SUM(" + STUDENT_ROLL_COUNT + ") " +
                        " FROM " + TABLE_STUDENTS +
                        " WHERE " + CLASS_KEY + " = '" + name + "'";

                Cursor cursor2 = db.rawQuery(studentCountQuery, null);
                if(cursor2.moveToFirst())
                {
                    numStudents = cursor2.getInt(0);
                    studentTotal = cursor2.getInt(1);
                }
                cursor2.close();
                double percentage = 0;
                if (numStudents > 0 && count > 0) {
                    percentage = (double)(studentTotal) / (numStudents * count) * 100.0;
                }
                Attendance aAttendance = new Attendance(name, count, (int)(percentage + 0.5));
                classList.add(aAttendance);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return classList;
    }

    public boolean updateStudentsPresent(ArrayList<String> studentsPresent, String theClass) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean retval = true;

        try {
            db.beginTransaction();
            for (String student : studentsPresent) {
                db.execSQL("UPDATE " + TABLE_STUDENTS + " SET "
                    + STUDENT_ROLL_COUNT + " = " + STUDENT_ROLL_COUNT + " +1 WHERE "
                    + STUDENT_NAME + " = '" + student + "' AND " + CLASS_KEY + " = '" + theClass + "'");
            }
            db.execSQL("UPDATE " + TABLE_CLASSES + " SET "
                + CLASS_ROLL_COUNT + " = " + CLASS_ROLL_COUNT + " +1 WHERE "
                + CLASS_ID + " = '" + theClass + "'");
            db.setTransactionSuccessful();
        } catch (Exception exc) {
            retval = false;
        } finally {
            db.endTransaction();
        }

        db.close();
        return retval;
    }

    // Deleting single contact
    public boolean deleteClass(String deletedClass) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STUDENTS, CLASS_KEY + " = ?", new String[] { deletedClass });
        int count = db.delete(TABLE_CLASSES, CLASS_ID + " = ?", new String[] { deletedClass });
        db.close();
        return count == 1;
    }

    public ArrayList<Attendance> getStudentsInClass(String theClass) {
        ArrayList<Attendance> studentList = new ArrayList<>();
        String selectQuery = "SELECT " + STUDENT_NAME + ", " + STUDENT_ROLL_COUNT +
                " FROM " + TABLE_STUDENTS +
                " WHERE " + CLASS_KEY + " = ? " +
                " ORDER BY " + STUDENT_NAME;

        SQLiteDatabase db = this.getWritableDatabase();

        String classCountQuery = "SELECT " + CLASS_ROLL_COUNT +
                " FROM " + TABLE_CLASSES +
                " WHERE " + CLASS_ID + " = '" + theClass + "'";
        Cursor cursor2 = db.rawQuery(classCountQuery, null);
        int classCount = 0;
        if(cursor2.moveToFirst())
        {
            classCount = cursor2.getInt(0);
        }
        cursor2.close();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { theClass });

        if (cursor.moveToFirst()) {
            do {

                String name = cursor.getString(0);
                int numAttended = cursor.getInt(1);
                double percentage = (double)numAttended / classCount * 100.0;

                Attendance student = new Attendance(name, numAttended, (int)(percentage + 0.5));
                studentList.add(student);
            } while (cursor.moveToNext());
        }
        cursor.close();
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
