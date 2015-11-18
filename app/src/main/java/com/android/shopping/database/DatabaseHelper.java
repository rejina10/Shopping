package com.android.shopping.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.shopping.settings.ApplicationSettings;

/**
 * Created by rejina on 11/15/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static int VERSION = 1;
    public static final String TABLE_ITEMS = "tbl_items";

    public static final String CATEGORY = "category";
    public static final String CREATED_AT = "created_at";
    public static final String NAME = "name";
    public static final String UPDATED_AT = "updated_at";
    public static final String USER_ID = "user_id";
    public static final String ITEM_ID = "item_id";


    public static final String CREATE_TBL_ITEMS =
            "CREATE TABLE IF NOT EXISTS " + TABLE_ITEMS
                    + "( " + ITEM_ID + " INTEGER UNIQUE, "
                    + CATEGORY + " TEXT,"
                    + CREATED_AT + " TEXT, "
                    + NAME + " TEXT, "
                    + UPDATED_AT + " TEXT, "
                    + USER_ID + " NUMERIC);";

    public DatabaseHelper(Context context) {
        super(context, ApplicationSettings.APP_DATABASE_PATH, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TBL_ITEMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(sqLiteDatabase);
    }
}
