package de.aatd.a21;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ShowText extends ListActivity {

    private ListAdapter listadapter;
    private ArrayList<String> list;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        String dateipfad = i.getStringExtra("Dateipfad");
        String text = "";
        String[] eintraege = new String[1];

        Path path = Paths.get(dateipfad);
        try{
            List<String> allLines = Files.readAllLines(path, StandardCharsets.UTF_8);

            for(String line : allLines){
                text += line;
            }
            eintraege[0] = text;

        }catch(IOException e){
            e.printStackTrace();

            eintraege[0] = "Datei konnte nicht gelesen werden!";
        }
        listadapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, eintraege);
        setListAdapter(listadapter);
    }
}
