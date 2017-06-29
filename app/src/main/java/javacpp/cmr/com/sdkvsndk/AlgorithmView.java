package javacpp.cmr.com.sdkvsndk;

/*
  * Classe per la gestione del passaggio di paramtri tra le varie Activity, per l'interfacciamentro con il db
  * e per la gestione e creazione dei dati da plottare su grafico
  * Serve per interfacciarsi con l'adapter della ListView nella ListActivity dando i valori da mettere nelle righe
  * della ListView e prendendo gli ultimi dati aggiornati(input, tempo esecuzione c e java)
  * Serve come tramite tra le 3 varie Activity visto che contiene la struttura base (LIST in fondo alla classe)
  * così facendo basta la posizione come parametro di passaggio tra le varie activity che sarà l'indice a cui
  * si accede a LIST così da prendere i vari parametri(Titolo, descrzioni, dati, ecc...)
  * Serve anche per interfacciarsi con il db sempre per la visualizzazzione delle ultime esecuzioni nella
  * ListActivity, mentre serve anche per prendere i dati nel db per metterli dentro alle Series che saranno
  * poi plottate nella GraphActivity
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

class AlgorithmView {

    //variabile statica per l'incremento degli id
    private static int id_counter = 0;

    //variabili
    private final int id;   //ID che usero per identificare l'algoritmo nel db
    private final int nome; //nome algoritmo
    private final int tipo; //tipo (memory bound o cpu bound)
    private final int desc; //descrizione dell'algoritmo
    private int input = 0;  //ultimo input dato
    private long esec = 0;  //ultimo tempo esecuzione cpp
    private long esej = 0;  //ultimo tempo esecuzione java

    //variabili per il db
    private SQLiteDatabase db;
    private DBOpenHelper oh;

    //variabili per plottare il grafico
    private LineGraphSeries<DataPoint> seriesC;
    private LineGraphSeries<DataPoint> seriesJava;

    //costruttore private perchè inizializzo gli elementi solo all'interno di questa classe
    private AlgorithmView(int nome, int tipo, int desc){
        id = id_counter++;
        this.nome = nome;
        this.tipo = tipo;
        this.desc = desc;
    }

    //inizio metodi

    //ritorna l'id(privato lo utilizzo solo qua dentro)
    private int getId() {
        return id;
    }

    //restituisce la descrzione
    public int getDesc() {
        return desc;
    }

    //quando nella listActivity andrò a chiamare questo metodo allora in quel caso prendero
    //l'ultimo tempo dell'esecuzione in c con un interrogazione al db
    long getEseC(Context c) {
        oh = new DBOpenHelper(c);
        db = oh.getWritableDatabase();
        //creo la query che mi dara l'ultima esecuzione in c fatta
        String sql = "select " + DBOpenHelper.TIMEC +
                " from " + DBOpenHelper.TABLE +
                " where " + DBOpenHelper.ALG + " = " + getId() +
                " and " + BaseColumns._ID + " = (" +
                " select max(" + BaseColumns._ID + ")" +
                " from " + DBOpenHelper.TABLE +
                " where " + DBOpenHelper.ALG + " = " + getId() + " );";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            int n = cursor.getCount();
            if(n == 0){
                cursor.close();
                oh.close();
                return esec; //in caso non sia ancora niente ritorna zero visto che lo abbiamo inizializzato a zero
            }
            cursor.moveToFirst();
            esec = cursor.getLong(0);
            cursor.close();
            oh.close();
        }
        return esec;
    }

    //quando nella listActivity andrò a chiamare questo metodo allora in quel caso prendero
    //l'ultimo tempo dell'esecuzione in java con un interrogazione al db
    long getEseJ(Context c) {
        oh = new DBOpenHelper(c);
        db = oh.getWritableDatabase();
        //creo la query che mi dara l'ultima esecuzione in java fatta
        String sql = "select " + DBOpenHelper.TIMEJAVA +
                " from " + DBOpenHelper.TABLE +
                " where " + DBOpenHelper.ALG + " = " + getId() +
                " and " + BaseColumns._ID + " = (" +
                " select max(" + BaseColumns._ID + ")" +
                " from " + DBOpenHelper.TABLE +
                " where " + DBOpenHelper.ALG + " = " + getId() + " );";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            int n = cursor.getCount();
            if(n == 0){
                cursor.close();
                oh.close();
                return esej; //in caso non sia ancora niente ritorna zero visto che lo abbiamo inizializzato a zero
            }
            cursor.moveToFirst();
            esej = cursor.getLong(0);
            cursor.close();
            oh.close();
        }
        return esej;
    }

    //restituisce il tipo
    int getTipo() {
        return tipo;
    }

    //restituisce il nome
    int getNome() {
        return nome;
    }

    //quando nella listActivity andrò a chiamare questo metodo allora in quel caso prendero
    //l'ultimo input usato con un interrogazione al db
    int getInput(Context c) {
        oh = new DBOpenHelper(c);
        db = oh.getWritableDatabase();
        //creo la query che mi dara l'ultimo input usato
        String sql = "select " + DBOpenHelper.INPUT +
                " from " + DBOpenHelper.TABLE +
                " where " + DBOpenHelper.ALG + " = " + getId() +
                " and " + BaseColumns._ID + " = (" +
                " select max(" + BaseColumns._ID + ")" +
                " from " + DBOpenHelper.TABLE +
                " where " + DBOpenHelper.ALG + " = " + getId() + " );";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            int n = cursor.getCount();
            if(n == 0){
                cursor.close();
                oh.close();
                return input; //in caso non sia ancora niente ritorna zero visto che lo abbiamo inizializzato a zero
            }
            cursor.moveToFirst();
            input = cursor.getInt(0);
            cursor.close();
            oh.close();
        }
        return input;
    }

    //funzione per l'aggiornamento dei dati nel db
    void updateDB(Context c, int input, long tc, long tj){
        oh = new DBOpenHelper(c);
        db = oh.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(DBOpenHelper.ALG, this.id);
        v.put(DBOpenHelper.INPUT, input);
        v.put(DBOpenHelper.TIMEC, tc);
        v.put(DBOpenHelper.TIMEJAVA, tj);
        db.insert(DBOpenHelper.TABLE, null, v);
        oh.close();
    }

    //funzione per creare la LineGraphSeries del c che serve per plottare il grafico dell'algoritmo
    LineGraphSeries<DataPoint> getSeriesC(Context c) {
        oh = new DBOpenHelper(c);
        db = oh.getWritableDatabase();
        //creo la query che mi dara tutti i dati per fare il grafico
        //per gli stessi imput faro la media
        String sql = "select " + DBOpenHelper.INPUT + ", avg(" + DBOpenHelper.TIMEC + ") " +
                " from " + DBOpenHelper.TABLE +
                " where " + DBOpenHelper.ALG + " = " + getId() +
                " group by " + DBOpenHelper.INPUT + ";";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null){
            int n = cursor.getCount();
            if(n == 0){
                cursor.close();
                oh.close();
                return null;
            }
            DataPoint data[] = new DataPoint[n];
            cursor.moveToFirst();
            for(int i = 0; i < n; i++) {
                int input = cursor.getInt(0);
                float timec = cursor.getFloat(1);
                data[i] = new DataPoint(input, timec);
                cursor.moveToNext();
            }
            cursor.close();
            oh.close();
            seriesC = new LineGraphSeries<>(data);
        }
        return seriesC;
    }

    //funzione per creare la LineGraphSeries del java che serve per plottare il grafico dell'algoritmo
    LineGraphSeries<DataPoint> getSeriesJava(Context c) {
        oh = new DBOpenHelper(c);
        db = oh.getWritableDatabase();
        //creo la query che mi dara tutti i dati per fare il grafico
        //per gli stessi imput faro la media
        String sql = "select " + DBOpenHelper.INPUT + ", avg(" + DBOpenHelper.TIMEJAVA + ") " +
                " from " + DBOpenHelper.TABLE +
                " where " + DBOpenHelper.ALG + " = " + getId() +
                " group by " + DBOpenHelper.INPUT + ";";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null){
            int n = cursor.getCount();
            if(n == 0){
                return null;
            }
            DataPoint data[] = new DataPoint[n];
            cursor.moveToFirst();
            for(int i = 0; i < n; i++) {
                int input = cursor.getInt(0);
                float timejava = cursor.getFloat(1);
                data[i] = new DataPoint(input, timejava);
                cursor.moveToNext();
            }
            cursor.close();
            seriesJava = new LineGraphSeries<>(data);
        }
        return seriesJava;
    }

    //funzione che mi resitituisce quanti righe di input diverse ho che mi serve per fare il controllo
    //prima di fare lanciare la GraphActivity
    //in teoria non servirebbe fare anche la media ma messa per sicurezza
    int getNumData(Context c){
        int n;
        oh = new DBOpenHelper(c);
        db = oh.getWritableDatabase();
        //creo la query che mi dara tutti i dati per fare il grafico
        //per gli stessi imput faro la media
        String sql = "select " + DBOpenHelper.INPUT + ", avg(" + DBOpenHelper.TIMEJAVA + ") " + ", avg(" + DBOpenHelper.TIMEC + ")" +
                " from " + DBOpenHelper.TABLE +
                " where " + DBOpenHelper.ALG + " = " + getId() +
                " group by " + DBOpenHelper.INPUT + ";";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null){
            n = cursor.getCount();
            cursor.close();
            oh.close();
            return n;
        }
        return 0; //in caso di errore del cursor
    }

    //funzione che dato in ingresso il contesto e il numero di input mi trova tutti i dati
    //prodotti da quell'input per quel specifico algoritmo
    //mi restituisce una matrice con la prima colonna i dati in c e la seconda in java
    int[][] getDataByInput(Context c, int input){
        int n;
        int x[][] = null;
        oh = new DBOpenHelper(c);
        db = oh.getWritableDatabase();
        //creo la query che mi dara tutti i dati con quello specifico input per quell'algoritmo
        String sql = "select " + DBOpenHelper.INPUT + ", " + DBOpenHelper.TIMEC + ", " + DBOpenHelper.TIMEJAVA +
                " from " + DBOpenHelper.TABLE +
                " where " + DBOpenHelper.ALG + " = " + getId() +
                " and " + DBOpenHelper.INPUT + " = " + input + ";";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null){
            n = cursor.getCount(); //impossibile che n sia = 0
            cursor.moveToFirst();
            x = new int[n][2];  //perchè ho sia i dati c che java per ogni riga
            for(int i = 0; i < n; i++) {
                x[i][0] = cursor.getInt(1); //seconda colonna (tempo del c)
                x[i][1] = cursor.getInt(2); //terza colonna (tempo del java)
                cursor.moveToNext();
            }
            cursor.close();
            oh.close();
        }
        return x;
    }

    //lista degli algoritmi
    static final AlgorithmView[] LIST = new AlgorithmView[]{
            new AlgorithmView(R.string.fib, R.string.cpu, R.string.fibd),
            new AlgorithmView(R.string.mat, R.string.memory, R.string.matd),
            new AlgorithmView(R.string.prim, R.string.cpu, R.string.primd),
            new AlgorithmView(R.string.loop, R.string.cpu, R.string.loopd),
            new AlgorithmView(R.string.cas, R.string.cpu, R.string.casd),
            new AlgorithmView(R.string.ack, R.string.cpu, R.string.ackd),
            new AlgorithmView(R.string.era, R.string.cpu, R.string.arad)
    };


}