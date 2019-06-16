package de.aatd.a21;

import android.provider.BaseColumns;

public final class DatabaseContract {
    public DatabaseContract (){}

    public static abstract class Verspaetung implements BaseColumns {
        public static final String TABLE_NAME         = "Verspeatung";
        public static final String COLUMN_VERSPAETUNG =     "Minuten";
        public static final String COLUMN_KOMMENTAR   =   "Kommentar";
        public static final String COLUMN_TAGESZEIT   =   "Tageszeit";
        public static final String COLUMN_ZEITSTEMPEL = "ZEITSTEMPEL";
    }
}














