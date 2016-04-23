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

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "rollCall";
    private static final String TABLE_CLASSES = "classes";
    private static final String CLASS_ID = "id";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CLASSES_TABLE = "CREATE TABLE IF NOT EXISTS" + TABLE_CLASSES + "(" + CLASS_ID + " TEXT PRIMARY KEY)";
        db.execSQL(CREATE_CLASSES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASSES);
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

    // Getting All Contacts
    public ArrayList<String> getAllClasses() {
        ArrayList<String> classList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT id FROM " + TABLE_CLASSES + " ORDER BY id";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                classList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
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
    public void deleteClass(String deletedClass) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CLASSES, CLASS_ID + " = ?", new String[] { deletedClass });
        db.close();
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

}
