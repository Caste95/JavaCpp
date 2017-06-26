package javacpp.cmr.com.sdkvsndk;

/*
    * Classe per la gestione della creazione del database
    * Mi crea il db
    * Me lo aggiorna (non credo ci sarannno nuovo versioni)
    * Il db è molto semplice abbiamo solamente una tabella strutturata in:
    *   id autoincrementante
    *   id_alg l'id dell'algoritmo che è la posizione nella lista
    *   input input che è stato usato
    *   timec tempo di esecuzione algoritmo in c
    *   timejava tempo di esecuzione algoritmo in java
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DBOpenHelper extends SQLiteOpenHelper {

    //variabili del db
    private static final String DB_NAME = "javavsccpp";
    private static final int DB_VERSION = 1;
    public static final String TABLE = "algorithm";
    public static final String ALG = "id_alg";
    public static final String INPUT = "input";
    public static final String TIMEC = "c";
    public static final String TIMEJAVA = "java";

    //costruttore
    DBOpenHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    //onCreate
    @Override
    public void onCreate(SQLiteDatabase db){
        String sql = "create table if not exists " + TABLE + "( " +
                BaseColumns._ID + " integer primary key autoincrement, " +
                ALG + " integer not null, " +
                INPUT + " integer not null, " +
                TIMEC + " integer not null, " +
                TIMEJAVA + " integer not null" +
                ");";
        db.execSQL(sql);
    }

    //onUpdate (non penso servira mai)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //non devo fare niente
    }

}

