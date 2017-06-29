package javacpp.cmr.com.sdkvsndk;

/*
  Classe Principale in cui verranno eseguiti gli algoritmi in AsyncTask
  Qua gestiamo l'AsyncTask su cui si eseguono gli algoritmi in BackGround così da rendere l'nterfaccia
  dell'utente in grado di rispondere ancora ai comandi e l'utente è abilitato per mezzo di un pulsante di
  stop di fermare l'esecuzione dell'algoritmo.
  Mentre l'algoritmo viene eseguito l'utente vedrà una progressBar e saranno disabilitati i pulsanti
  go e plotta per evitare "incidenti"
  Il risultato sarà il tempo di esecuzione per i due algoritmi in c e java in millisecondi
  Facciamo anche un controllo sull'input che viene inserito dall'utente in modo da non causare crash
  inattesi del programma.
  Per l'algoritmo di Ackerman l'interfaccia è lievemente modificata in modo da far capire meglio come
  funziona infatti prende due dati in input le decine e le unita
       ESEMPIO: se l'utente inserisce 1 e 5 -> sarebbe come 15!
  Abbiamo anche disabilitato che si blocchi lo schermo così l'algoritmo non verrà stoppato dal metodo onPause
  Abbiamo anche settato che l'unico orientamento disponibile è il portrait
  Nel metodo onPause viene stoppato sempre l'algoritmo se sta eseguendo così da mantenere una certa consistenza
  dei tempi generati
 */

import static javacpp.cmr.com.sdkvsndk.Algorithm.PRIMES;
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

    //variabili dell'interfaccia
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

    //dichiaro i metodi nativi
    public native void cancella();
    public native void setta();
    public native long fibonacci(int n);
    public native long calcMatr(int n);
    public native long acker(long m, long n);
    public native long random(long n);
    public native long nestedLoops(int n);
    public native long eratostene(int n);
    public native long primalityTest(long n);
    public native long strcat(int n);

    //metodo onCreate
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
        desc.setText(AlgorithmView.LIST[pos].getDesc());
        tit.setText(AlgorithmView.LIST[pos].getNome());

        //setto le visibilita delle EditText
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
                            if (x > 1000)
                                throw new Exception();
                            break;
                        case 2:
                            //PrimalityTest
                            if ((x > PRIMES.length) || (x < 1)) {
                                throw new Exception();
                            }
                            else
                                Toast.makeText(MainActivity.this,getString(R.string.primeinfo)+" "+String.valueOf(PRIMES[x-1]),Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
                            //NestedLoop: non da problemi di crash applicativi
                            break;
                        case 4:
                            //numeri casuali: non da problemi di crash applicativi
                            break;
                        case 5:
                            //Ackermann
                            if(y > 4 || ((y == 4) && z > 0) || z >= 10)
                                throw new Exception();
                            break;
                        case 6:
                            //Crivello di Eratostene
                            if(x > 8)
                                throw new Exception();
                            break;
                        case 7:
                            break;
                        default:
                            //algoritmo presente in lista ma non implementato
                            Toast.makeText(MainActivity.this, R.string.notimpl, Toast.LENGTH_SHORT).show();
                            break;
                    }
                    //lanciamo l'asynctask
                    w = new Worker();
                    w.execute(x,  y,  z); //solo ackermann considera gli ultimi due parametri
                }
                catch (Exception e){
                    //in caso di problemi lanciamo un toast e settiamo di nuovo le TextView del risultato
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
                if(AlgorithmView.LIST[pos].getNumData(MainActivity.this) >= 5) {
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

    //metodo onPause: dovremo stoppare l'AsyncTask
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

    //AsyncTask
    private class Worker extends AsyncTask<Integer, Void, Long[]>{

        //eseguito prima dell'esecuzione degli algoritmi, setto le visibilita
        @Override
        protected void onPreExecute(){
            go.setVisibility(View.INVISIBLE);
            plot.setVisibility(View.INVISIBLE);
            prBar.setVisibility(View.VISIBLE);
            stop.setVisibility(View.VISIBLE);
            ris1.setText(R.string.outputjava);
            ris2.setText(R.string.outputcpp);

            if(pos == 5){
                inputm.setEnabled(false);
                inputn.setEnabled(false);
            }
            else
                input.setEnabled(false);
            //setto i flag a false permettendo la normale esecuzione degli algoritmi
            Algorithm.setta();
            setta();
        }

        //qui verranno eseguiti gli algoritmi
        @Override
        protected Long[] doInBackground(Integer... params) { //Spawn di un thread separato, la UI rimane responsive
            Long[] res = new Long[2];
            int in;

            //scelgo quale algoritmo chiamare
            switch (pos) {
                case 0:
                    //chiamo l'algoritmo di Fibonacci
                    res[0] = Algorithm.fibonacci(params[0]);
                    res[1] = fibonacci(params[0]);
                    break;
                case 1:
                    //chiamo l'algoritmo di calcolo matriciale
                    res[0] = Algorithm.calcMatr(params[0]);
                    res[1] = calcMatr(params[0]);
                    break;
                case 2:
                    //chiamo l'algoritmo dei numeri primi
                    res[0] = Algorithm.primalityTest(PRIMES[params[0] -1]);
                    res[1] = primalityTest(PRIMES[params[0] -1]);
                    break;
                case 3:
                    //chiamo l'algoritmo di NestedLoop
                    res[0] = Algorithm.nestedLoops(params[0]);
                    res[1] = nestedLoops(params[0]);
                    break;
                case 4:
                    //chiamo l'algoritmo di Numeri Casuali
                    in = (int) (params[0] * Math.pow(10, 6));
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
                    in = (int) (Math.pow(10, params[0]));
                    res[0] = Algorithm.eratostene(in);
                    res[1] = eratostene(in);
                    break;
                case 7:
                    //chiamo l'algoritmo strcat
                    res[0] = Algorithm.strcat(params[0] * 1000);
                    res[1] = strcat(params[0] * 1000);
                    break;
                default:
                    //algoritmo presente in lista ma non implementato
                    res[0] = res[1] = -1L;
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

            if(pos == 5){
                inputm.setEnabled(true);
                inputn.setEnabled(true);
            }
            else
                input.setEnabled(true);

            ris1.setText(getString(R.string.resjava) + " " + Long.toString(tj) + " " + getString(R.string.unita));
            ris2.setText(getString(R.string.resc) + " " + Long.toString(tc) + " " + getString(R.string.unita));

            //adesso dovro aggiornare il db
            //N.B.:x rimane il numero inserito dall'utente e NON quello passato all'algoritmo
            AlgorithmView.LIST[pos].updateDB(MainActivity.this, x, tc, tj);
        }

        //Eseguito DOPO doInBackground() ---> creo un altro metodo per cancellare il task: terminate()
        @Override
        protected void onCancelled(){ //Rendo visibili i pulsanti go e plotta, e visualizzo un toast di notifica
            Toast.makeText(MainActivity.this, R.string.canc, Toast.LENGTH_LONG).show();
            prBar.setVisibility(View.INVISIBLE);
            stop.setVisibility(View.INVISIBLE);
            go.setVisibility(View.VISIBLE);
            plot.setVisibility(View.VISIBLE);
            if(pos == 5){
                inputm.setEnabled(true);
                inputn.setEnabled(true);
            }
            else
                input.setEnabled(true);
        }

        //termina il più velocemente possibile il task
        private void terminate(){
            cancel(true); //cancella il task, non potrà più essere eseguito
            Algorithm.cancella(); //termina java settando il flag a true
            cancella(); //termina c++ settando il flag a true
        }
    }
}
