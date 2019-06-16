package de.aatd.a21;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.content.Intent;

import java.util.ArrayList;

public class VerspaetungsListe extends ListActivity {

    private ListAdapter listadapter;
    private ArrayList<String> list;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Database db = new Database(this);

        db.getReadableDatabase();

        Intent i = getIntent();
        int id = i.getIntExtra("Verspaetung", R.id.actionbarAlleVerspaetungen);
        Cursor c = db.leseVerspaetungenAus(id);

        list =new ArrayList<String>();

        while(c.moveToNext()){
            list.add(
                    "Wann:\n   " + c.getString(1) + "\n" +
                    "Anzahl Minuten:\n   " + c.getString(2) + "\n" +
                    "Kommentar:\n   " + c.getString(0) + "\n" +
                    "Zeitstempel:\n   " + c.getString(3) + "\n"
            );
        }

        String[] eintraege =  list.toArray(new String[0]);

        listadapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, eintraege);

        setListAdapter(listadapter);
    }
}