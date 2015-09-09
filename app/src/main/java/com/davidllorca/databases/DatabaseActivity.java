package com.davidllorca.databases;

import android.app.Activity;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class DatabaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        DBAdapter db = new DBAdapter(this);
        /*
            Get database from assets on design time
         */
        /*
        try {
            String destPath = "/data/data/" + getPackageName() + "/databases";
            File file = new File(destPath);
            if (!file.exists()) {
                file.mkdirs();
                file.createNewFile();

                // Copy database from assets folder into databases folder
                copyDb(getBaseContext().getAssets().open("mydb"), new FileOutputStream(destPath + "/MyDB"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/



       /*
           Add news contact
       */
        db.open();
        long id = db.insertContact("David Llorca", "davidllorcabaron@gmail.com");
        checkResult(id);
        id = db.insertContact("Chewbacca", "chewbacca@chewbacca.com");
        checkResult(id);
        db.close();

       /*
            Get all contacts
         */
        db.open();
        Cursor cursor = db.getAllContacts();
        if (cursor.moveToFirst()) { // If it's true there are 1 entry at least
            do {
                displayContact(cursor);
            } while (cursor.moveToNext()); // Until there are no entries
        }
        db.close();

       /*
            Get a contact
         */
        db.open();
        cursor = db.getContact(2);
        if (cursor.moveToFirst()) {
            displayContact(cursor);
        } else {
            displayText("No contact found");
        }
        db.close();

        /*
            Update a contact
         */
        db.open();
        if (db.updateContact(1, "David Llorca Baron", "davidllorcabaron@gmail.com")) {
            displayText("Update successful");
        } else {
            displayText("Update failed");
        }
        db.close();

        /*
            Delete a contact
         */
        db.open();
        if (db.deleteContact(1)) {
            displayText("Delete successful");
        } else {
            displayText("Delete failed");
        }
        db.close();
    }

    /**
     * Copy database file into outputstream.
     *
     * @param inputStream
     * @param outputStream
     */
    private void copyDb(InputStream inputStream, OutputStream outputStream) {
        byte[] buffer = new byte[1024];
        int length;
        try {
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            // Close streams
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if operation on database has been successful. Displauy toast with result information.
     *
     * @param id
     */
    public void checkResult(long id) {
        if (id < 0) {
            displayText("Operation failed");
        } else {
            displayText("Operation successful");
        }
    }

    /**
     * Display Toast.
     *
     * @param text
     */
    private void displayText(String text) {
        Toast.makeText(getBaseContext(), text, Toast.LENGTH_SHORT).show();
    }

    /**
     * Display contact information.
     *
     * @param c
     */
    private void displayContact(Cursor c) {
        displayText(
                "id: " + c.getString(0) + "\n"
                        + "Name: " + c.getString(1) + "\n"
                        + "Email: " + c.getString(2));
    }
}
