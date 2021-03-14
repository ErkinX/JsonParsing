package com.example.jsonparsing;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class ContactDbHelper extends SQLiteOpenHelper {

    public ContactDbHelper(Context context) {
        super(context, "abc.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_CONTACTS =
                "create table IF NOT EXISTS contacts_tab(" +
                        "id text primary key, " +
                        "name text NOT NULL, " +
                        "email text NOT NULL, " +
                        "address text NOT NULL, " +
                        "gender text NOT NULL, " +
                        "mobile text NOT NULL, " +
                        "home text NOT NULL, " +
                        "office text NOT NULL " +
                        ")";
        db.execSQL(CREATE_TABLE_CONTACTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<Contact> getContactsData() {
        List<Contact> data = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from contacts_tab", null);

        while (cursor.moveToNext()) {
            String id, name, email, address, gender, mobile, home, office;
            id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
            name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
            gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
            mobile = cursor.getString(cursor.getColumnIndexOrThrow("mobile"));
            home = cursor.getString(cursor.getColumnIndexOrThrow("home"));
            office = cursor.getString(cursor.getColumnIndexOrThrow("office"));
            Contact dataModel = new Contact(id, name, email, address, gender, new Phone(mobile, home, office));
            data.add(dataModel);
        }
        return data;
    }
}