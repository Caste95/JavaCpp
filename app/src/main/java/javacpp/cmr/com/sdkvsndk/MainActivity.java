package javacpp.cmr.com.sdkvsndk;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import static android.widget.Toast.makeText;

public class MainActivity extends AppCompatActivity {

    //elementi interfaccia grafica
    private TextView tit, desc, ris1, ris2;
    private Button go, stop, plot;
    private EditText input, inputm, inputn;
    private ProgressBar prBar;
    //varibili di utilizzo
    private int x, y, z;
    private long tj, tc;
    private int pos;

    private Worker w;

    // carico la libreria nativ-lib
    static {
        System.loadLibrary("native-lib");
    }

    //dichiaro i metodi
    public native void cancella();
    public native void setta();
    public native boolean visualizza();
    public native long fibonacci(int n);
    public native long calcMatr(int n);
    public native long acker(long m, long n);
    public native long random(long n);
    public native long nestedLoops(int n);
    public native long eratostene(int n);
    public native long primalityTest(long n);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //prendo gli id dell'interfaccia
        tit = (TextView) findViewById(R.id.titolo);
        desc = (TextView) findViewById(R.id.desc);
        ris1 = (TextView) findViewById(R.id.resultsjava);
        ris2 = (TextView) findViewById(R.id.resultscpp);
        go = (Button) findViewById(R.id.buttonGo);
        stop = (Button) findViewById(R.id.buttonStop);
        plot = (Button) findViewById(R.id.buttonPlot);
        input = (EditText) findViewById(R.id.input);
        inputm = (EditText) findViewById(R.id.inputm);
        inputn = (EditText) findViewById(R.id.inputn);
        prBar = (ProgressBar) findViewById(R.id.bar);

        //recupero i dati passati dall'intent
        pos = getIntent().getIntExtra("pos", 0);
        desc.setText(AlgorithmView.list[pos].getDesc());
        tit.setText(AlgorithmView.list[pos].getNome());

        input.setVisibility(View.VISIBLE);
        inputm.setVisibility(View.GONE);
        inputn.setVisibility(View.GONE);

        //se scelgo ackermann utilizzo due edittext per i due parametri
        if (pos == 5) {
            input.setVisibility(View.GONE);
            inputm.setVisibility(View.VISIBLE);
            inputn.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams param =               //setto il peso della progress bar per poterla vedere in caso ci siano 2 input
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.4f);
            prBar.setLayoutParams(param);
        }

        //La progress bar rimane visibile (PERCHÈ???)
        prBar.setVisibility(View.INVISIBLE);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (pos != 5) {
                        x = Integer.parseInt(input.getText().toString());
                    }
                    else {
                        y = Integer.parseInt(inputm.getText().toString());
                        z = Integer.parseInt(inputn.getText().toString());
                    }
                    w = new Worker();
                    w.execute((long)x, (long)y, (long)z);

                }
                catch (Exception e){
                    makeText(MainActivity.this, R.string.toast, Toast.LENGTH_LONG).show();
                    ris1.setText(R.string.outputjava);
                    ris2.setText(R.string.outputcpp);
                }
            }
        });

        //dentro al tasto plot lancerò l'activity per la creazione del grafico
        plot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //lancerò l'activity solo se avrò un certo numero di dati da plottare
                //perche se no il grafico viene male e potrebbero essere lanciate delle eccezioni
                //in questo caso scegliamo di visualizzare il grafico solo se abbiamo piu di 5 input diversi
                if(AlgorithmView.list[pos].getNumData(MainActivity.this) >= 5) {
                    Intent i = new Intent(MainActivity.this, GraphActivity.class);
                    //gli passo la posizione che mi identificherà l'algoritmo che dovrò plottare
                    i.putExtra("pos", pos);
                    startActivity(i);
                }
                else{
                    //altrimenti stampo un toast di errore
                    makeText(MainActivity.this, R.string.toastPlot, Toast.LENGTH_LONG).show();
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
            go.setVisibility(View.INVISIBLE);
            plot.setVisibility(View.INVISIBLE);
            prBar.setVisibility(View.VISIBLE);
            Algorithm.setta();
            setta();
        }

        @Override
        protected Long[] doInBackground(Long... params) {
            Long[] res = new Long[2];
            int in;
            switch (pos) {
                case 0:
                    //chiamo l'algoritmo di Fibonacci
                    res[0] = Algorithm.fibonacci(x);
                    res[1] = fibonacci(x);
                    break;
                case 1:
                    //chiamo l'algoritmo di calcolo matriciale
                    res[0] = Algorithm.calcMatr(x);
                    res[1] = calcMatr(x);
                    break;
                case 2:
                    //chiamo l'algoritmo di PrimalityTest
                    long[] primes = {7,97,773,5113,54673L,633797L,4563467L,9139397L,34542467L,359454547L,
                            2331891997L,16333396997L,297564326947L,2456435675347L,37267627626947L,726483934563467L,9573357564326947L,75136938367986197L,1276812967623946997L};
                    if(x < primes.length && x >= 1){
                        res[0] = Algorithm.primalityTest(primes[x - 1]);
                        res[1] = primalityTest(primes[x - 1]);
                    }
                    else {
                        Toast.makeText(MainActivity.this, R.string.invalid, Toast.LENGTH_SHORT);
                        //terminate();
                    }
                    break;
                case 3:
                    //chiamo l'algoritmo di NestedLoop
                    //dovro farlo con un asyncTask
                    res[0] = Algorithm.nestedLoops(x);
                    res[1] = nestedLoops(x);
                    break;
                case 4:
                    //chiamo l'algoritmo di Numeri Casuali
                    in = (int) (x * Math.pow(10, 6));
                    res[0] = Algorithm.random(in);
                    res[1] = random(in);
                    break;
                case 5:
                    //chiamo l'algoritmo di Ackermann
                    //genero la prima variabile, le decine dell'input
                    res[0] = Algorithm.acker(params[1], params[2]);
                    res[1] = acker(params[1], params[2]);
                    break;
                case 6:
                    //chiamo l'algoritmo di Eratostene
                    in = (int) (Math.pow(10,x));
                    res[0] = Algorithm.eratostene(in);
                    res[1] = eratostene(in);
                    break;
                default:
                    makeText(MainActivity.this, R.string.toastError, Toast.LENGTH_LONG).show();
                    break;
            }
            return res;
        }

        @Override
        protected void onPostExecute(Long[] res){
            tj = res[0];
            tc = res[1];

            prBar.setVisibility(View.INVISIBLE);
            go.setVisibility(View.VISIBLE);
            plot.setVisibility(View.VISIBLE);

            ris1.setText(getString(R.string.resjava) + " " + Long.toString(tj) + " " + getString(R.string.unita));
            ris2.setText(getString(R.string.resc) + " " + Long.toString(tc) + " " + getString(R.string.unita));

            //adesso dovro aggiornare il db
            AlgorithmView.list[pos].updateDB(MainActivity.this, x, tc, tj);
        }

        //Eseguito DOPO doInBackground() ---> creo un altro metodo per cancellare il task
        @Override
        protected void onCancelled(){
            prBar.setVisibility(View.INVISIBLE);
            go.setVisibility(View.INVISIBLE);
            plot.setVisibility(View.INVISIBLE);
            Toast.makeText(MainActivity.this, R.string.canc, Toast.LENGTH_SHORT).show();
            go.setVisibility(View.VISIBLE);
            plot.setVisibility(View.VISIBLE);
        }

        //termina il più velocemente possibile il task
        public void terminate(){
            cancel(true);
            Algorithm.cancella();
            cancella();
        }
    }
}
