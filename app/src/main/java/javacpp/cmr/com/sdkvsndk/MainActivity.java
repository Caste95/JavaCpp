package javacpp.cmr.com.sdkvsndk;

import static javacpp.cmr.com.sdkvsndk.AlgorithmView.primes;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView ris1;
    private TextView ris2;
    private Button go, stop, plot;
    private EditText input, inputm, inputn;
    private ProgressBar prBar;

    //varibili di utilizzo
    private int x;
    private int y;
    private int z;
    private int pos;

    //asynctask
    private Worker w;

    // carico la libreria nativ-lib
    static {
        System.loadLibrary("native-lib");
    }

    //dichiaro i metodi
    public native void cancella();
    public native void setta();
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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //prendo gli id dell'interfaccia
        TextView tit = (TextView) findViewById(R.id.titolo);
        TextView desc = (TextView) findViewById(R.id.desc);
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

        //TODO: Chiedere al tutor perchè la progress bar rimane visibile
        prBar.setVisibility(View.INVISIBLE);
        stop.setVisibility(View.INVISIBLE);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //ackermann richiede due valori di input
                    if (pos == 5) {
                        y = Integer.parseInt(inputm.getText().toString());
                        z = Integer.parseInt(inputn.getText().toString());
                    }
                    else {
                        x = Integer.parseInt(input.getText().toString());
                    }

                    //verifica dell'input
                    switch(pos){
                        case 0:
                            //fibonacci: non da problemi di crash applicativi
                            break;
                        case 1:
                            //prodotto matriciale: non da problemi di crash applicativi
                            break;
                        case 2:
                            //PrimalityTest
                            if ((x > primes.length) || (x < 1)) {
                                throw new Exception();
                            }else Toast.makeText(MainActivity.this,String.valueOf(primes[x-1]),Toast.LENGTH_SHORT);
                            break;
                        case 3:
                            //NestedLoop: non da problemi di crash applicativi
                            break;
                        case 4:
                            //numeri casuali: non da problemi di crash applicativi
                            break;
                        case 5:
                            //Ackermann
                            if(y > 4 || (((y == 4) && z > 0) || z > 10))
                                throw new Exception();
                            break;
                        case 6:
                            //Crivello di Eratostene
                            if(x > 8)
                                throw new Exception();
                            break;
                    }
                    //lanciamo l'asynctask
                    w = new Worker();
                    w.execute(x,  y,  z); //solo ackermann considera gli ultimi due parametri
                }
                catch (Exception e){
                    Toast.makeText(MainActivity.this, R.string.invalid, Toast.LENGTH_LONG).show();
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
                    Toast.makeText(MainActivity.this, R.string.toastPlot, Toast.LENGTH_LONG).show();
                }
            }
        });

        //stoppiamo l'asynctask
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!w.isCancelled())
                    w.terminate();
            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();
        //termina il task
        if(w != null && !w.isCancelled())
            w.terminate();
    }

    //Siccome se premo back lui mi riporta all' activity precedente ma non ricrea la lista
    //ho bisogno di lanciarla tramite un intent così sarà ricreata e quindi avrà la visualizzazzione aggiornata
    //ovviamente la activity precedente dovra essere stata distrutta
    @Override
    public void onBackPressed() {
        //termina il task
        if(w !=null && !w.isCancelled())
            w.terminate();
        Intent i = new Intent( this, ListActivity.class);
        startActivity(i);
        //scelgo di distruggerla perche se faccio back nell'activity principale probabilmente
        //voglio chiuderla e non tornare all'activity dell'esecuzione dell'algoritmo
        finish();
    }

    private class Worker extends AsyncTask<Integer, Void, Long[]>{

        @Override
        protected void onPreExecute(){
            go.setVisibility(View.INVISIBLE);
            plot.setVisibility(View.INVISIBLE);
            prBar.setVisibility(View.VISIBLE);
            stop.setVisibility(View.VISIBLE);
            //setto i flag a false permettendo la normale esecuzione degli algoritmi
            Algorithm.setta();
            setta();
        }

        @Override
        protected Long[] doInBackground(Integer... params) { //Spawn di un thread separato, la UI rimane responsive
            Long[] res = new Long[2];
            int in;

            //scelgo quale algoritmo chiamare
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
                    //chiamo l'algoritmo dei numeri primi
                    res[0] = Algorithm.primalityTest(primes[params[0] -1]);
                    res[1] = primalityTest(primes[params[0] -1]);
                    break;
                case 3:
                    //chiamo l'algoritmo di NestedLoop
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
                    res[0] = Algorithm.acker(params[1], params[2]);
                    res[1] = acker(params[1], params[2]);
                    x = y*10+z;
                    break;
                case 6:
                    //chiamo l'algoritmo di Eratostene
                    in = (int) (Math.pow(10, x));
                    res[0] = Algorithm.eratostene(in);
                    res[1] = eratostene(in);
                    break;
                default: //non verrà mai chiamato
                    break;
            }
            return res;
        }

        @Override
        protected void onPostExecute(Long[] res){ //eseguito dopo che il task è terminato correttamente, sono nel thread UI
            //rendo esplicito il risultato
            long tj = res[0];
            long tc = res[1];

            prBar.setVisibility(View.INVISIBLE);
            go.setVisibility(View.VISIBLE);
            plot.setVisibility(View.VISIBLE);
            stop.setVisibility(View.INVISIBLE);

            ris1.setText(getString(R.string.resjava) + " " + Long.toString(tj) + " " + getString(R.string.unita));
            ris2.setText(getString(R.string.resc) + " " + Long.toString(tc) + " " + getString(R.string.unita));

            //adesso dovro aggiornare il db
            AlgorithmView.list[pos].updateDB(MainActivity.this, x, tc, tj);
        }

        //Eseguito DOPO doInBackground() ---> creo un altro metodo per cancellare il task: terminate()
        @Override
        protected void onCancelled(){ //Rendo visibili i pulsanti go e plotta, e visualizzo un toast di notifica
            Toast.makeText(MainActivity.this, R.string.canc, Toast.LENGTH_LONG).show();
            prBar.setVisibility(View.INVISIBLE);
            stop.setVisibility(View.INVISIBLE);
            go.setVisibility(View.VISIBLE);
            plot.setVisibility(View.VISIBLE);
            ris1.setText(R.string.outputjava);
            ris2.setText(R.string.outputcpp);
        }

        //termina il più velocemente possibile il task
        private void terminate(){
            cancel(true); //cancella il task, non potrà più essere eseguito
            Algorithm.cancella(); //termina java settando il flag a true
            cancella(); //termina c++ settando il flag a true
        }
    }
}
