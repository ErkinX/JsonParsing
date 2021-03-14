package com.example.jsonparsing;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private ListView lv;
    List<Contact> contacts;
    ContactDbHelper contactDBHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contacts = new ArrayList<Contact>();
        contactDBHelper = new ContactDbHelper(MainActivity.this);//Create this object in onCreate() method

        db = contactDBHelper.getWritableDatabase();

        lv = (ListView) findViewById(R.id.list);
        new GetContacts().execute();
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            if (!isTableExists("contacts_tab")) {
                String jsonStr = loadJSONFromAsset();

                if (jsonStr != null) {
                    Gson gson = new Gson();
                    Contacts cs = gson.fromJson(jsonStr, Contacts.class);
                    contacts = cs.contacts;
                    ContentValues insertValues = new ContentValues();
                    for (Contact contact : contacts) {
                        Phone phone = contact.getPhone();
                        insertValues.put("name", contact.getName());
                        insertValues.put("email", contact.getEmail());
                        insertValues.put("address", contact.getAddress());
                        insertValues.put("gender", contact.getGender());
                        insertValues.put("mobile", phone.getMobile());
                        insertValues.put("home", phone.getHome());
                        insertValues.put("office", phone.getOffice());

                        db.insert("contacts_tab", null, insertValues);
                    }
                } else {
                    Log.e("TAG", "Couldn't get json.");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Couldn't get json!",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            } else {
                contacts = contactDBHelper.getContactsData();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            MyCustomAdapter dataAdapter = new MyCustomAdapter(MainActivity.this, contacts);
            lv.setAdapter(dataAdapter);
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("actors.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public boolean isTableExists(String tableName) {
        boolean isExist = false;
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                Cursor cursor1 = db.rawQuery("select * from contacts_tab", null);
                if (cursor1.getCount() > 0) {
                    isExist = true;
                }
            }
            cursor.close();
        }
        return isExist;
    }
}