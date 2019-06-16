package de.aatd.a21;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import de.aatd.a21.DatabaseContract.Verspaetung;

public class Database extends SQLiteOpenHelper {

    private SQLiteDatabase db;
    private String pfad = "";

    public static final int DB_VERSION = 1;
    public static final String DB_NAME_VERSPAETUNG = "verspaetung";
    private static final String TYPE = " TEXT";
    private static final String CREATE_TABLE_VERSPAETUNG =
            "CREATE TABLE IF NOT EXISTS " + Verspaetung.TABLE_NAME + " ( " + Verspaetung._ID + " INTEGER PRIMARY KEY," +
                    Verspaetung.COLUMN_ZEITSTEMPEL + TYPE + "," +
                    Verspaetung.COLUMN_TAGESZEIT   + TYPE + "," +
                    Verspaetung.COLUMN_KOMMENTAR   + TYPE + "," +
                    Verspaetung.COLUMN_VERSPAETUNG + TYPE + " )"
            ;
    private static final String DELETE_TABLE_VERSPAETUNG =
            "DROP TABLE IF EXISTS " + Verspaetung.TABLE_NAME;

    public Database(Context context) {
        super(context, DB_NAME_VERSPAETUNG, null, DB_VERSION );
    }

    //Callbacks
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_VERSPAETUNG);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_TABLE_VERSPAETUNG);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    //Aufgabe 3.2
    public void   speichereVerspaetung(String verspaetung, String kommentar, String verspatungstyp){
        //Put values inside

        db = getWritableDatabase();

        ContentValues verspaetungObj = new ContentValues();
        verspaetungObj.put(Verspaetung.COLUMN_VERSPAETUNG, verspaetung);
        verspaetungObj.put(Verspaetung.COLUMN_KOMMENTAR, kommentar);
        verspaetungObj.put(Verspaetung.COLUMN_TAGESZEIT, verspatungstyp);
        verspaetungObj.put(Verspaetung.COLUMN_ZEITSTEMPEL, new Date(System.currentTimeMillis()).toString());

        db.insert(DatabaseContract.Verspaetung.TABLE_NAME, null, verspaetungObj);
    }
    public void   exportiereAlsTextDatei(){
        //Kriege alle Daten von der Datenbank
        getReadableDatabase();
        Cursor cAll = leseVerspaetungenAus(R.id.actionbarAlleVerspaetungen);
        Cursor cM   = leseVerspaetungenAus(R.id.actionbarVerspaetungenMorgens);
        Cursor cA   = leseVerspaetungenAus(R.id.actionbarVerspaetungenAbends);

        //Schreibe drei text dateien
        String verspaetungenAlle    = "Alle Verspaetungen: \n";
        String verspaetungenMorgens = "Alle Verspaetungen Morgens: \n";
        String verspaetungenAbends  = "Alle Verspaetungen Abends: \n";
        while(cAll.moveToNext()){

            verspaetungenAlle += "\n" +
                    "Wann:           " + cAll.getString(1) + "\n" +
                    "Anzahl Minuten: " + cAll.getString(2) + "\n" +
                    "Kommentar:      " + cAll.getString(0) + "\n" +
                    "Zeitstempel:    " + cAll.getString(3) + "\n" +
                    "\n";
        }
        while(cM.moveToNext()){

            verspaetungenMorgens += "\n" +
                    "Anzahl Minuten: " + cM.getString(2) + "\n" +
                    "Kommentar:      " + cM.getString(0) + "\n" +
                    "Zeitstempel:    " + cM.getString(3) + "\n" +
                    "\n";
        }
        while(cA.moveToNext()){

            verspaetungenAbends += "\n" +
                    "Anzahl Minuten: " + cA.getString(2) + "\n" +
                    "Kommentar:      " + cA.getString(0) + "\n" +
                    "Zeitstempel:    " + cA.getString(3) + "\n" +
                    "\n";
        }

        //Erstelle Dateien
        File fileA   = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() +  "/verspaetungenAbends.txt");
        File fileM   = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/verspaetungenMorgens.txt");
        File fileAll = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() +    "/verspaetungenAlle.txt");

        try{
            if(!fileAll.exists()){
                fileAll.createNewFile();
            }
            if(!fileA.exists()) {
                fileA.createNewFile();
            }
            if(!fileM.exists()){
                fileM.createNewFile();
            }

        }catch (IOException e){
            e.printStackTrace();
        }

        pfad = fileAll.getAbsolutePath();

        //In die dateien die erstellten Strings
        try {
            BufferedWriter writerAll     = new BufferedWriter(new FileWriter(fileAll));
            BufferedWriter writerAbends  = new BufferedWriter(new FileWriter(  fileA));
            BufferedWriter writerMorgens = new BufferedWriter(new FileWriter(  fileM));

            writerAll.write(       verspaetungenAlle);
            writerAbends.write(  verspaetungenAbends);
            writerMorgens.write(verspaetungenMorgens);

            writerAbends.close();
            writerAll.close();
            writerMorgens.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Cursor leseVerspaetungenAus(int verspaetungsID){

        //Get DB
        SQLiteDatabase db = getReadableDatabase();

        //Taking table Verspaetung
        String tableName = DatabaseContract.Verspaetung.TABLE_NAME;

        //Projection to all column entries
        String[] projection = {
                DatabaseContract.Verspaetung.COLUMN_KOMMENTAR,
                DatabaseContract.Verspaetung.COLUMN_TAGESZEIT,
                DatabaseContract.Verspaetung.COLUMN_VERSPAETUNG,
                DatabaseContract.Verspaetung.COLUMN_ZEITSTEMPEL
        };

        //Sort ascending
        String sortOrder = DatabaseContract.Verspaetung.COLUMN_ZEITSTEMPEL + " ASC";

        //Where
        String whereClauseMorgens = DatabaseContract.Verspaetung.COLUMN_TAGESZEIT + " = 'Morgens'";
        String whereClauseAbends = DatabaseContract.Verspaetung.COLUMN_TAGESZEIT + " = 'Abends'";

        //Queries
        if(verspaetungsID == R.id.actionbarVerspaetungenMorgens){
            return db.query(tableName, projection, whereClauseMorgens, null, null, null, sortOrder);
        }
        if(verspaetungsID == R.id.actionbarVerspaetungenAbends){
            return db.query(tableName, projection,  whereClauseAbends, null, null, null, sortOrder);
        }
        if(verspaetungsID == R.id.actionbarAlleVerspaetungen){
            return db.query(tableName, projection, null, null, null, null, sortOrder);
        }
        return null;
    }
    public void   loescheDatenbank(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Verspaetung.TABLE_NAME,null,null);
    }

    //Aux
    public String getPfad(){
        return this.pfad;
    }
}












