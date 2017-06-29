package javacpp.cmr.com.sdkvsndk;

/*
    * Classe per la gestione della creazione del database
    * Mi crea il db
    * Me lo aggiorna (non credo ci sarannno nuovo versioni)
    * Il db è molto semplice abbiamo solamente una tabella strutturata in:
    *   id: intero autoincrementante
    *   id_alg: l'id dell'algoritmo che è la posizione nella lista
    *   input: input che è stato usato
    *   timec: tempo di esecuzione algoritmo in c
    *   timejava: tempo di esecuzione algoritmo in java
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

class DBOpenHelper extends SQLiteOpenHelper {

    //variabili del db
    private static final String DB_NAME = "javavsccpp";
    private static final int DB_VERSION = 1;
    static final String TABLE = "algorithm";
    static final String ALG = "id_alg";
    static final String INPUT = "input";
    static final String TIMEC = "c";
    static final String TIMEJAVA = "java";

    //costruttore
    DBOpenHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    //onCreate (creazione della tabella a cui faremo riferimento nel programma)
    @Override
    public void onCreate(SQLiteDatabase db){
        //if not exists potrebbe anche non essere messo ma per sicurezza lo messo lo stesso
        String sql = "create table if not exists " + TABLE + "( " +
                BaseColumns._ID + " integer primary key autoincrement, " +
                ALG + " integer not null, " +
                INPUT + " integer not null, " +
                TIMEC + " integer not null, " +
                TIMEJAVA + " integer not null" +
                ");";
        db.execSQL(sql);
    }

    //onUpdate (sovrascritto perchè è obligatorio ma non lo usiamo visto che non ci sono altre versioni)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //non devo fare niente
    }

}

