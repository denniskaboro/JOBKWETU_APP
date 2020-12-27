package com.e.jobkwetu.Helper;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.e.jobkwetu.Model.Category;
import com.e.jobkwetu.Model.SelectedCategory;
import com.e.jobkwetu.Model.Subcounty;
import com.e.jobkwetu.Model.Taskers;
import com.e.jobkwetu.Model.Tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "categories_db";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create categores table
        db.execSQL(Category.CREATE_TABLE);
        db.execSQL(Tasks.CREATE_TABLE);
        db.execSQL(Subcounty.CREATE_TABLE);
        db.execSQL(SelectedCategory.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Category.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Tasks.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Subcounty.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SelectedCategory.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertSelectedCategory(String note) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(SelectedCategory.COLUMN_CATEGORY, note);

        // insert row
        long id = db.insert(SelectedCategory.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public long insertCategory(HashMap<String, String> category) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Category.COLUMN_ID, category.get("id"));
        values.put(Category.COLUMN_CATEGORY,category.get("category"));

        // insert row
        long id = db.insertWithOnConflict(Category.TABLE_NAME, null, values,SQLiteDatabase.CONFLICT_REPLACE);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }
    public long insertTasks(HashMap<String, String> tasks) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Tasks.COLUMN_ID, tasks.get("id"));
        values.put(Tasks.COLUMN_CATEGORY, tasks.get("tasker"));

        // insert row
        long id = db.insertWithOnConflict(Tasks.TABLE_NAME, null, values,SQLiteDatabase.CONFLICT_REPLACE);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }
    public long insertSubcounty(HashMap<String, String> subcounty) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Subcounty.COLUMN_ID, subcounty.get("id"));
        values.put(Subcounty.COLUMN_SUBCOUNTY, subcounty.get("subcounty"));

        // insert row
        long id = db.insertWithOnConflict(Subcounty.TABLE_NAME, null, values,SQLiteDatabase.CONFLICT_REPLACE);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }
    public SelectedCategory getSelectedCategory(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(SelectedCategory.TABLE_NAME,
                new String[]{SelectedCategory.COLUMN_ID, SelectedCategory.COLUMN_CATEGORY, SelectedCategory.COLUMN_TIMESTAMP},
                SelectedCategory.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        SelectedCategory note = new SelectedCategory(
                cursor.getInt(cursor.getColumnIndex(SelectedCategory.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(SelectedCategory.COLUMN_CATEGORY)),
                cursor.getString(cursor.getColumnIndex(SelectedCategory.COLUMN_TIMESTAMP)));

        // close the db connection
        cursor.close();

        return note;
    }

    public List<SelectedCategory> getAllSELECTEDCATEGORY() {
        List<SelectedCategory> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + SelectedCategory.TABLE_NAME + " ORDER BY " +
                SelectedCategory.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SelectedCategory note = new SelectedCategory();
                note.setId(cursor.getInt(cursor.getColumnIndex(SelectedCategory.COLUMN_ID)));
                note.setNote(cursor.getString(cursor.getColumnIndex(SelectedCategory.COLUMN_CATEGORY)));
                note.setTimestamp(cursor.getString(cursor.getColumnIndex(SelectedCategory.COLUMN_TIMESTAMP)));

                notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }

    public int getSelectedCategoryCount() {
        String countQuery = "SELECT  * FROM " + SelectedCategory.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public Category getCategory(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Category.TABLE_NAME,
                new String[]{Category.COLUMN_ID, Category.COLUMN_CATEGORY,Category.COLUMN_TIMESTAMP},
                Category.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        Category category = new Category(
                cursor.getInt(cursor.getColumnIndex(Category.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Category.COLUMN_CATEGORY)),
                cursor.getString(cursor.getColumnIndex(Category.COLUMN_TIMESTAMP)));

        // close the db connection
        cursor.close();

        return category;
    }

    public Subcounty getSubcounty(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Subcounty.TABLE_NAME,
                new String[]{Subcounty.COLUMN_ID, Subcounty.COLUMN_SUBCOUNTY,Subcounty.COLUMN_TIMESTAMP},
                Subcounty.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        Subcounty subcounty = new Subcounty(
                cursor.getInt(cursor.getColumnIndex(Subcounty.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Subcounty.COLUMN_SUBCOUNTY)),
                cursor.getString(cursor.getColumnIndex(Subcounty.COLUMN_TIMESTAMP)));

        // close the db connection
        cursor.close();

        return subcounty;
    }
    public Tasks getTasks(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Tasks.TABLE_NAME,
                new String[]{Tasks.COLUMN_ID, Tasks.COLUMN_CATEGORY,Tasks.COLUMN_TIMESTAMP},
                Tasks.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        Tasks tasks = new Tasks(
                cursor.getInt(cursor.getColumnIndex(Tasks.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Tasks.COLUMN_CATEGORY)),
                cursor.getString(cursor.getColumnIndex(Tasks.COLUMN_TIMESTAMP)));

        // close the db connection
        cursor.close();

        return tasks;
    }
    public List<String> getAllSelected() {
        List<String> labels = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM " + SelectedCategory.TABLE_NAME + " ORDER BY " +
                SelectedCategory.COLUMN_TIMESTAMP + " DESC";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            do {
                labels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return labels;

    }

    public List<String> getAllNotes() {
        List<String> labels = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM " + Category.TABLE_NAME + " ORDER BY " +
                Category.COLUMN_TIMESTAMP + " DESC";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            do {
                labels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return labels;

    }
    public List<String> getAllSubcounty() {
        List<String> labels = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM " + Subcounty.TABLE_NAME + " ORDER BY " +
                Subcounty.COLUMN_TIMESTAMP + " DESC";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            do {
                labels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return labels;
    }
    public List<String> getAllTasks() {
        List<String> labels = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM " + Tasks.TABLE_NAME + " ORDER BY " +
                Category.COLUMN_TIMESTAMP + " DESC";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            do {
                labels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return labels;
    }

    public int getCategoryCount() {
        String countQuery = "SELECT  * FROM " + Category.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }
    public int getSubcountyCount() {
        String countQuery = "SELECT  * FROM " + Subcounty.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }
    public int getTasksCount() {
        String countQuery = "SELECT  * FROM " + Tasks.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int updateCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Category.COLUMN_CATEGORY, category.getNote());

        // updating row
        return db.update(Category.TABLE_NAME, values, Category.COLUMN_ID + " = ?",
                new String[]{String.valueOf(category.getId())});
    }
    public int updateSubcounty(Subcounty subcounty) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Subcounty.COLUMN_SUBCOUNTY, subcounty.getSubcounty());

        // updating row
        return db.update(Subcounty.TABLE_NAME, values, Category.COLUMN_ID + " = ?",
                new String[]{String.valueOf(subcounty.getId())});
    }
    public int updateTasks(Tasks tasks) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Tasks.COLUMN_CATEGORY, tasks.getNote());

        // updating row
        return db.update(Tasks.TABLE_NAME, values, Tasks.COLUMN_ID + " = ?",
                new String[]{String.valueOf(tasks.getId())});
    }
    public int updateNote(SelectedCategory note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SelectedCategory.COLUMN_CATEGORY, note.getNote());

        // updating row
        return db.update(SelectedCategory.TABLE_NAME, values, SelectedCategory.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }

    public void deleteCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Category.TABLE_NAME, Category.COLUMN_ID + " = ?",
                new String[]{String.valueOf(category.getId())});
        db.close();
    }
    public void deleteTasks(Tasks tasks) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Tasks.TABLE_NAME, Tasks.COLUMN_ID + " = ?",
                new String[]{String.valueOf(tasks.getId())});
        db.close();
    }
}
