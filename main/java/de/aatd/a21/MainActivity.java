package de.aatd.a21;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.SharedPreferences.*;
import static android.widget.SeekBar.*;

public class MainActivity extends AppCompatActivity {

    private ActionBar actionbar = null;

    private int zaehlerMorgens      = 0;
    private int zaehlerAbends       = 0;
    private int summeAbends         = 0;
    private int summeMorgens        = 0;
    private int durchschnittAbends  = 0;
    private int durchschnittMorgens = 0;
    private int heutigeVerspaetung  = 0;
    private String verspaetungsTyp  = "";
    private String kommentarText    = "";

    private Database db;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Verspaetungszähler");

        //Augabe 3.1
        zeigeLetzteVerspaetung();
        trageStartwerteEin();

        //Aufgabe 3.2
        db = new Database(this);

        //Aufgabe 2.2
        final SeekBar s = (SeekBar) findViewById(R.id.verspaetung);
        final RadioGroup verspaetungRadio = findViewById(R.id.morgensOderAbends);
        final int morgigeVerspaetung    = R.id.zaehlMorgens;
        final int abendlicheVerspaetung = R.id.zaehlAbends;
        int jetzigeVerspaetung    = verspaetungRadio.getCheckedRadioButtonId();
        s.setOnSeekBarChangeListener( new OnSeekBarChangeListener() {

                                                                       @Override
                                                                       public void onStopTrackingTouch(SeekBar seekBar) {
                                                                           // TODO Auto-generated method stub
                                                                       }

                                                                       @Override
                                                                       public void onStartTrackingTouch(SeekBar seekBar) {
                                                                           // TODO Auto-generated method stub
                                                                       }

                                                                       @Override
                                                                       public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                                                           // TODO Auto-generated method stub
                                                                           int jetzigeVerspaetung    = verspaetungRadio.getCheckedRadioButtonId();
                                                                           TextView morgens = (TextView) findViewById(R.id.verspaetungMorgens);
                                                                           TextView abends = (TextView) findViewById(R.id.verspaetungAbends);

                                                                           if (jetzigeVerspaetung == morgigeVerspaetung) {
                                                                               morgens.setText(progress + "");
                                                                               abends.setText("0");
                                                                           } else if (jetzigeVerspaetung == abendlicheVerspaetung) {
                                                                               abends.setText(progress + "");
                                                                               morgens.setText("0");
                                                                           }
                                                                       }
                                                                   });
    }
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater mi = new MenuInflater(getBaseContext());
        mi.inflate(R.menu.actionbar, menu);

        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item){

        String listActivity     = "de.aatd.a21.VerspaetungsListe";
        String showTextActivity =    "de.aatd.a21.ZeigeTextDatei";

        if(item.getItemId() == R.id.actionbarAlleVerspaetungen){
            Intent i= new Intent(listActivity);
            i.putExtra("Verspaetung", R.id.actionbarAlleVerspaetungen);
            startActivity(i);
        }
        if(item.getItemId() == R.id.actionbarVerspaetungenMorgens){
            Intent i= new Intent(listActivity);
            i.putExtra("Verspaetung", R.id.actionbarVerspaetungenMorgens);
            startActivity(i);
        }
        if(item.getItemId() == R.id.actionbarVerspaetungenAbends){
            Intent i= new Intent(listActivity);
            i.putExtra("Verspaetung", R.id.actionbarVerspaetungenAbends);
            startActivity(i);
        }
        if(item.getItemId() == R.id.actionbarExport){
            db.exportiereAlsTextDatei();
        }
        if(item.getItemId() == R.id.actionbarShowTextFile){
            Intent i= new Intent(showTextActivity);
            i.putExtra("Dateipfad", db.getPfad());
            startActivity(i);
        }
        return true;
    }
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    //Methoden die in den Callback-Methoden aufgerufen werden!
    //--------------------------------------------------------
    //Aufgabe 2.2
    public void buttonTrageVerspaetungEin(View v){
        registriereverspaetung();
        speichereLetzeVerspaetung();
        db.speichereVerspaetung(heutigeVerspaetung+"", kommentarText, verspaetungsTyp);
    }
    public void buttonSetzeVerspaetungenZurueck(View v){
        Intent i = new Intent(this, DecisionDialog.class);
        startActivity(i);
    }
    private void registriereverspaetung(){
        //Get Radiobuttongroup flags
        RadioGroup verspaetungRadio = (RadioGroup) findViewById(R.id.morgensOderAbends);

        int morgigeVerspaetung    = R.id.zaehlMorgens;
        int abendlicheVerspaetung = R.id.zaehlAbends;
        int jetzigeVerspaetung    = verspaetungRadio.getCheckedRadioButtonId();
        kommentarText = ((EditText)findViewById(R.id.kommentarText)).getText().toString();


        if(jetzigeVerspaetung == morgigeVerspaetung){
            //Get Seekbar value
            SeekBar minutenSeekbar = (SeekBar) findViewById(R.id.verspaetung);
            heutigeVerspaetung = minutenSeekbar.getProgress();
            verspaetungsTyp = "Morgens";

            //Do the math
            this.summeMorgens += heutigeVerspaetung;//Summe
            zaehlerMorgens++;
            this.durchschnittMorgens = summeMorgens/zaehlerMorgens;

            //Set views
            TextView jetzt = (TextView) findViewById(R.id.verspaetungMorgens);
            TextView summe = (TextView) findViewById(R.id.gesamtMorgens);
            TextView durchschnitt = (TextView) findViewById(R.id.durchschnittMorgens);

            jetzt.setText(heutigeVerspaetung+"");
            summe.setText(zaehlerMorgens+"");
            durchschnitt.setText(durchschnittMorgens+"");
        }

        else if(jetzigeVerspaetung == abendlicheVerspaetung){
            //Get Seekbar value
            SeekBar minutenSeekbar = (SeekBar) findViewById(R.id.verspaetung);
            heutigeVerspaetung = minutenSeekbar.getProgress();
            verspaetungsTyp = "Abends";

            //Do the math
            this.summeAbends += heutigeVerspaetung;//Summe
            zaehlerAbends++;
            this.durchschnittAbends = summeAbends/zaehlerAbends;

            //Set views
            TextView jetzt = (TextView) findViewById(R.id.verspaetungAbends);
            TextView summe = (TextView) findViewById(R.id.gesamtAbends);
            TextView durchschnitt = (TextView) findViewById(R.id.durchschnittAbends);

            jetzt.setText(heutigeVerspaetung + "");
            summe.setText(zaehlerAbends+"");
            durchschnitt.setText(durchschnittAbends+"");
        }
    }

    //Aufgabe 3.1
    private void speichereLetzeVerspaetung(){
        //Prequesites
        RadioGroup verspaetungRadio = (RadioGroup) findViewById(R.id.morgensOderAbends);
        int morgigeVerspaetung    = R.id.zaehlMorgens;
        int jetzigeVerspaetung    = verspaetungRadio.getCheckedRadioButtonId();

        //Set text, type and duration
        Boolean morgensOderAbends = morgigeVerspaetung == jetzigeVerspaetung ? true:false;
        int heutigeVerspaetung = ((SeekBar) findViewById(R.id.verspaetung)).getProgress();

        //Create shared prefs
        SharedPreferences sp = getSharedPreferences("LetzeVerspaetung", 0);

        //Edit shared prefs
        Editor editor = sp.edit();
        editor.putString(          "Kommentartext",kommentarText);
        editor.putInt(         "Verspaetung", heutigeVerspaetung);
        editor.putBoolean("MorgensOderAbends", morgensOderAbends);

        //Speichere globale Daten
        editor.putString("AnzahlMorgens",            zaehlerMorgens+"");
        editor.putString("AnzahlAbends",              zaehlerAbends+"");
        editor.putString("DurchschnittMorgens", durchschnittMorgens+"");
        editor.putString("DurchschnittAbends",   durchschnittAbends+"");
        editor.commit();
    }
    private void zeigeLetzteVerspaetung(){
        // Get text, type, and duration
        SharedPreferences sp = getSharedPreferences("LetzeVerspaetung", 0);

        String text = sp.getString("Kommentartext", "");
        String morgensOderAbends = sp.getBoolean("MorgensOderAbends",false) ? "Morgige Verspätung" : "Abendliche Verspätung";
        String verspaetung = sp.getInt("Verspaetung", 0) + "";

        // make formatted toast with data
        String toastText =
                morgensOderAbends       + "\n" +
                "Dauer: " + verspaetung + "\n" +
                "Kommentar:\n" + text;

        Toast.makeText(this, toastText, Toast.LENGTH_LONG).show();
    }
    private void trageStartwerteEin(){
        SharedPreferences sp = getSharedPreferences("LetzeVerspaetung", 0);

        zaehlerMorgens = Integer.parseInt(sp.getString("AnzahlMorgens","0"));
        zaehlerAbends  = Integer.parseInt(sp.getString( "AnzahlAbends","0"));
        durchschnittMorgens = Integer.parseInt(sp.getString("DurchschnittMorgens", "0"));
        durchschnittAbends  = Integer.parseInt(sp.getString( "DurchschnittAbends", "0"));

        kommentarText = sp.getString("Kommentartext", "...");
        ((EditText)findViewById(R.id.kommentarText)).setText(kommentarText);
    }
}