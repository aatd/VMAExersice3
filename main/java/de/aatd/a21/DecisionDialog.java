package de.aatd.a21;


import android.app.*;
import android.content.*;
import android.os.Bundle;
import android.widget.TextView;

public class DecisionDialog extends Activity {
    static final int JA_NEIN = 1;
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        showDialog(JA_NEIN);

    }
    protected Dialog onCreateDialog(int id, Bundle args) {

        AlertDialog.Builder dialogBuilder;
        AlertDialog dialog;
        Intent result = new Intent();

        switch (id) {
            case JA_NEIN:
                dialogBuilder = new AlertDialog.Builder(this);
                dialogBuilder.setMessage("Verspätungen zurücksetzen?");
                dialogBuilder.setCancelable(true);

                dialogBuilder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        TextView anzahlAbends = (TextView) findViewById(R.id.verspaetungAbends);
                        anzahlAbends.setText("0");
                        TextView anzahlMorgens = (TextView) findViewById(R.id.verspaetungMorgens);
                        anzahlMorgens.setText("0");
                        TextView summeAbends = (TextView) findViewById(R.id.gesamtAbends);
                        summeAbends.setText("0");
                        TextView summeMorgens = (TextView) findViewById(R.id.gesamtMorgens);
                        summeMorgens.setText("0");
                        TextView durchschnittAbends = (TextView) findViewById(R.id.durchschnittAbends);
                        durchschnittAbends.setText("0");
                        TextView durchschnittMorgens = (TextView) findViewById(R.id.durchschnittMorgens);
                        durchschnittMorgens.setText("0");
                        Database db = new Database(getBaseContext());
                        db.loescheDatenbank();
                    }
                });
                dialogBuilder.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                dialog = dialogBuilder.create();

                return dialog;
            default:
                return null;
        }
    }
}

