package ie.bookfast.bookfast;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Cathal on 16/11/2017.
 */

public class DBConnection extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "bookfastDB";

    // Favourites table name
    private static final String TABLE_FAVS = "favourite_books";

    // Fav table columns
    private static final String ISBN = "isbn";
    private static final List<String> COLS =
            new ArrayList<>(Arrays.asList("title", "authorsString", "description", "thumbnailURL", "category"));

    public DBConnection(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public void addFavBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ISBN, book.isbn); // ISBN
        values.put(COLS.get(0), book.title);
        values.put(COLS.get(1), book.authorsString);
        values.put(COLS.get(2), book.description);
        values.put(COLS.get(3), book.thumbnailURL);
        values.put(COLS.get(4), book.category);

        // Inserting Row
        db.insert(TABLE_FAVS, null, values);
        db.close(); // Closing database connection
    }

    public List<Book> getAllFavBooks() {
        List<Book> bookList = new ArrayList<Book>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FAVS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Book book = new Book(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5)
                );
                bookList.add(book);
            } while (cursor.moveToNext());
        }

        // return contact list
        return bookList;
    }


    // checks if book exists in table
    public Boolean isBookInFavourites (String isbn) {
        String selectQuery = "SELECT isbn FROM " + TABLE_FAVS + " WHERE isbn = " + isbn;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // if no results
        if(cursor.getCount() < 1){
            return false;
        }else{
            return true;
        }
    }

    public void removeFavBook(String isbn) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVS, ISBN + " = ?",
                new String[] { isbn });
        db.close();
    }

    private String generateColCreateString(){
        String STR = "";
        for(int i=0;i<COLS.size();i++){
            STR += COLS.get(i) + " TEXT";
            if(i != COLS.size()-1){
                STR += ",";
            }
        }
        return STR;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FAVS_TABLE = "CREATE TABLE " + TABLE_FAVS + "("
                + ISBN + " INTEGER PRIMARY KEY,"
                + generateColCreateString() +
                ")";
        db.execSQL(CREATE_FAVS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVS);

        // Create tables again
        onCreate(db);
    }
}
