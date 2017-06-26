package javacpp.cmr.com.sdkvsndk;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //elementi interfaccia grafica
    private TextView desc, ris1, ris2;
    private Button go, stop, plot;
    private EditText input;
    private ProgressBar prBar;
    //varibili di utilizzo
    private int x;
    private long tj, tc;
    private int pos;

    Worker w;

    // carico la libreria nativ-lib
    static {
        System.loadLibrary("native-lib");
    }

    //dichiaro i metodi
    public native void cancella();
    public native void setta();
    public native boolean visualizza();
    public native long fibonacci(int n);
    public native void calcMatr(int n);
    public native long acker(int m, int n);
    public native long random(long n);
    public native long nestedLoops(int n);
    public native long eratostene(int n);
    public native long primalityTest(long n);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //prendo gli id dell'interfaccia
        desc = (TextView) findViewById(R.id.desc);
        ris1 = (TextView) findViewById(R.id.resultsjava);
        ris2 = (TextView) findViewById(R.id.resultscpp);
        go = (Button) findViewById(R.id.buttonGo);
        stop = (Button) findViewById(R.id.buttonStop);
        plot = (Button) findViewById(R.id.buttonPlot);
        input = (EditText) findViewById(R.id.input);
        prBar = (ProgressBar) findViewById(R.id.bar);

        //recupero i dati passati dall'intent
        pos = getIntent().getIntExtra("pos", 0);
        desc.setText(AlgorithmView.list[pos].getDesc());

        //La progress bar rimane visibile (PERCHÈ???)
        prBar.setVisibility(View.INVISIBLE);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    x = Integer.parseInt(input.getText().toString());
                    w = new Worker();
                    w.execute((long)x);

                }
                catch (Exception e){
                    Toast.makeText(MainActivity.this, R.string.toast, Toast.LENGTH_LONG).show();
                    ris1.setText(R.string.outputjava);
                    ris2.setText(R.string.outputcpp);
                }
            }
        });

        //dentro al tasto plot lancero l'activity per la creazione del grafico
        plot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //lancero l'activity solo se avro un certo numero di dati da plottare
                //perche se no il grafico viene male e potrebbero essere lanciate delle eccezioni
                //in questo caso scegliamo di visualizzare solo se abbiamo piu di 5 input diversi
                if(AlgorithmView.list[pos].getNumData(MainActivity.this) >= 5) {
                    Intent i = new Intent(MainActivity.this, GraphActivity.class);
                    //gli passo la posizione che mi identifichera l'algoritmo che dovro plottare
                    i.putExtra("pos", pos);
                    startActivity(i);
                }
                else{
                    //altrimenti stampo un toast di errore
                    Toast.makeText(MainActivity.this, R.string.toastPlot, Toast.LENGTH_LONG).show();
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!w.isCancelled())
                    w.terminate();
            }
        });
    }


    //TODO: onPause()
    //onCreate() vs onStart() per definire i listener dei bottoni

    //Siccome se premo back lui mi riporta all' activity precedente ma non ricrea la lista
    //ho bisogno di lanciarla tramite un intent così sarà ricreata e quindi avrà la visualizzazzione aggiornata
    //ovviamente la activity precedente dovra essere stata distrutta
    @Override
    public void onBackPressed() {
        if(w !=null && !w.isCancelled())
            w.terminate();
        Intent i = new Intent( this, ListActivity.class);
        startActivity(i);
        //scelgo di distruggerla perche se faccio back nell'activiti principale probabilmente
        //voglio chiuderla e non tornare all'activity dell'esecuzione dell'algoritmo
        finish();
    }

    private class Worker extends AsyncTask<Long, Void, Long[]>{

        @Override
        protected void onPreExecute(){
            prBar.setVisibility(View.VISIBLE);
            Algorithm.setta();
            setta();
        }

        @Override
        protected Long[] doInBackground(Long... params) {
            Long[] res = new Long[2];
            switch (pos) {
                case 0:
                    //chiamo l'algoritmo di Fibonacci
                    //dovro farlo con un asyncTask
                    break;
                case 1:
                    //chiamo l'algoritmo di Prodotto Matriciale
                    //dovro farlo con un asyncTask
                    break;
                case 2:
                    //chiamo l'algoritmo di PrimalityTest
                    res[0] = Algorithm.primalityTest(x);
                    res[1] = primalityTest(x);
                    break;
                case 3:
                    //chiamo l'algoritmo di NestedLoop
                    //dovro farlo con un asyncTask
                    res[0] = Algorithm.nestedLoops(x);
                    res[1] = nestedLoops(x);
                    break;
                case 4:
                    //chiamo l'algoritmo di Numeri Casuali
                    int in = (int) (x * Math.pow(10, 6));
                    res[0] = Algorithm.random(in);
                    res[1] = random(in);
                    break;
                case 5:
                    //chiamo l'algoritmo di Ackermann
                    //dovro farlo con un asyncTask
                    break;
                case 6:
                    //chiamo l'algoritmo di Eratostene
                    res[0] = Algorithm.eratostene(x);
                    res[1] = eratostene(x);
                    break;
                default:
                    Toast.makeText(MainActivity.this, R.string.toastError, Toast.LENGTH_LONG).show();
                    break;
            }
            return res;
        }

        @Override
        protected void onPostExecute(Long[] res){
            tj = res[0];
            tc = res[1];
            prBar.setVisibility(View.INVISIBLE);
            ris1.setText(getString(R.string.resjava) + " " + Long.toString(tj) + " " + getString(R.string.unita));
            ris2.setText(getString(R.string.resc) + " " + Long.toString(tc) + " " + getString(R.string.unita));

            //adesso dovro aggiornare il db
            AlgorithmView.list[pos].updateDB(MainActivity.this, x, tc, tj);
        }

        //Eseguito DOPO doInBackground() ---> creo un altro metodo per cancellare il task
        @Override
        protected void onCancelled(){
            prBar.setVisibility(View.INVISIBLE);
        }

        //termina il più velocemente possibile il task
        public void terminate(){
            cancel(true);
            Algorithm.cancella();
            cancella();
        }
    }
}
