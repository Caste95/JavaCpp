package javacpp.cmr.com.sdkvsndk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //elementi interfaccia grafica
    private TextView desc, ris1, ris2;
    private Button go, stop, plot;
    private EditText input;

    //varibili di utilizzo
    private int x;
    private long tj, tc;
    private int pos;

    // carico la libreria nativ-lib
    static {
        System.loadLibrary("native-lib");
    }

    //assegno i metodi
    public native long Random(long n);
    public native long NestedLoops(int n);

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

        //recupero i dati passati dall'intent
        pos = getIntent().getIntExtra("pos", 0);
        desc.setText(AlgorithmView.list[pos].getDesc());

        //recupero i dati dal bundle se ci sono(in caso di app distrutta)
        if(savedInstanceState != null){
            String execJava = savedInstanceState.getString("java");
            String execC = savedInstanceState.getString("c");
            if(execJava != null)
                ris1.setText(execJava);
            if(execC != null)
                ris2.setText(execC);
        }

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    x = Integer.parseInt(input.getText().toString());
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
                            //chiamo l'algoritmo di Numeri Primi
                            //dovro farlo con un asyncTask
                            break;
                        case 3:
                            //chiamo l'algoritmo di NestedLoop
                            //dovro farlo con un asyncTask
                            tj = Algorithm.NestedLoops(x);
                            tc = NestedLoops(x);
                            break;
                        case 4:
                            //chiamo l'algoritmo di Numeri Casuali
                            //dovro farlo con un asyncTask
                            int in = (int) (x * Math.pow(10, 6));
                            tj = Algorithm.Random(in);
                            tc = Random(in);
                            break;
                        case 5:
                            //chiamo l'algoritmo di Ackermann
                            //dovro farlo con un asyncTask
                            break;
                        default:
                            Toast.makeText(MainActivity.this, R.string.toastError, Toast.LENGTH_LONG).show();
                            break;
                    }
                    ris1.setText(getString(R.string.resjava) + " " + Long.toString(tj) + " " + getString(R.string.unita));
                    ris2.setText(getString(R.string.resc) + " " + Long.toString(tc) + " " + getString(R.string.unita));

                    //adesso dovro aggiornare il db
                    AlgorithmView.list[pos].updateDB(MainActivity.this, x, tc, tj);
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
    }


    //Siccome se premo back lui mi riporta all' activity precedente ma non ricrea la lista
    //ho bisogno di lanciarla tramite un intent così sarà ricreata e quindi avrà la visualizzazzione aggiornata
    //ovviamente la activity precedente dovra essere stata distrutta
    @Override
    public void onBackPressed() {
        Intent i = new Intent( this, ListActivity.class);
        startActivity(i);
        //scelgo di distruggerla perche se faccio back nell'activiti principale probabilmente
        //voglio chiuderla e non tornare all'activity dell'esecuzione dell'algoritmo
        finish();
    }

    //in caso venga girato lo schermo voglio che l'output venga visualizzato ancora
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        String execJava = ris1.getText().toString();
        String execC = ris2.getText().toString();
        savedInstanceState.putString("java", execJava);
        savedInstanceState.putString("c", execC);
        super.onSaveInstanceState(savedInstanceState);
    }

}
