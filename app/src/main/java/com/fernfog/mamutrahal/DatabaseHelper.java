package com.fernfog.mamutrahal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "posts.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_POSTS = "favourites";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_JSON = "json";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_POSTS_TABLE = "CREATE TABLE " + TABLE_POSTS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_JSON + " TEXT UNIQUE)";
        db.execSQL(CREATE_POSTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTS);
        onCreate(db);
    }

    public void addPost(String json) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_JSON, json);

        db.insert(TABLE_POSTS, null, values);
        db.close();
    }

    public List<data> getAllPosts() {
        List<data> posts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_POSTS, null, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String content = cursor.getString(cursor.getColumnIndex(COLUMN_JSON));

                posts.add(new data(id, content));
            }
            cursor.close();
        }

        db.close();
        return posts;
    }

    public void deletePost(int postId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_POSTS, COLUMN_ID + " = ?", new String[]{String.valueOf(postId)});
        db.close();
    }
}

