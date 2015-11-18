package com.android.shopping.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.provider.ContactsContract;

import com.android.shopping.ItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rejina on 11/15/2015.
 */
public class DatabaseQueryManager {

    public static DatabaseQueryManager DB_INSTANCE;

    private SQLiteDatabase db;

    public static DatabaseQueryManager getDbInstance(Context context) {

        if (DB_INSTANCE == null) {
            DB_INSTANCE = new DatabaseQueryManager(context);
        }
        return DB_INSTANCE;
    }

    public DatabaseQueryManager(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();
    }

    public void storeItems(List<ItemModel> itemsList) {
        String sql = "INSERT OR REPLACE INTO " + DatabaseHelper.TABLE_ITEMS
                + " (" + DatabaseHelper.ITEM_ID + ","
                + DatabaseHelper.CATEGORY + "," +
                DatabaseHelper.NAME + "," +
                DatabaseHelper.CREATED_AT + ","
                + DatabaseHelper.UPDATED_AT + ","
                + DatabaseHelper.USER_ID + ") VALUES(?,?,?,?,?,?)";

        db.beginTransaction();
        SQLiteStatement sqLiteStatement = db.compileStatement(sql);

        for (ItemModel item : itemsList) {
            sqLiteStatement.bindLong(1, item.getId());
            sqLiteStatement.bindString(2, item.getCategory());
            sqLiteStatement.bindString(3, item.getName());
            sqLiteStatement.bindString(4, item.getCreated_at());
            sqLiteStatement.bindString(5, item.getUpdated_at());
            sqLiteStatement.bindLong(6, item.getUser_id());

            sqLiteStatement.execute();
            sqLiteStatement.clearBindings();
        }

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void deleteItems(String id) {
        String sql = "DELETE FROM " + DatabaseHelper.TABLE_ITEMS + " WHERE"
                + "(" + DatabaseHelper.ITEM_ID + "=" + id + ")";

        db.beginTransaction();
        db.compileStatement(sql);
        db.setTransactionSuccessful();
        db.endTransaction();
    }



    public ArrayList<ItemModel> getItemsList() {
        ArrayList<ItemModel> itemModelArrayList = new ArrayList<>();
        String sql = "SELECT * FROM " + DatabaseHelper.TABLE_ITEMS;
        Cursor cursor = db.rawQuery(sql, null);
        ItemModel itemModel;
        while (cursor.moveToNext()) {
            itemModel = new ItemModel();
            itemModel.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ITEM_ID)));
            itemModel.setCategory(cursor.getString(cursor.getColumnIndex(DatabaseHelper.CATEGORY)));
            itemModel.setName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NAME)));
            itemModel.setCreated_at(cursor.getString(cursor.getColumnIndex(DatabaseHelper.CREATED_AT)));
            itemModel.setUpdated_at(cursor.getString(cursor.getColumnIndex(DatabaseHelper.UPDATED_AT)));
            itemModel.setUser_id(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.USER_ID)));
            itemModelArrayList.add(itemModel);
        }
        return itemModelArrayList;
    }
}
