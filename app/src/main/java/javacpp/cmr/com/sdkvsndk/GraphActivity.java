package javacpp.cmr.com.sdkvsndk;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

public class GraphActivity extends AppCompatActivity {

    //elementi interfaccia grafica
    private TextView titolo, datiC, datiJava, datiInput, in, c, j;
    private GraphView graph;

    //variabili di utilizzo
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        //prendo gli id dell'interfaccia
        titolo = (TextView) findViewById(R.id.titolo);
        in = (TextView) findViewById(R.id.In);
        c = (TextView) findViewById(R.id.c);
        j = (TextView) findViewById(R.id.j);
        datiInput = (TextView) findViewById(R.id.datiInput);
        datiC = (TextView) findViewById(R.id.datiC);
        datiJava = (TextView) findViewById(R.id.datiJava);
        graph = (GraphView) findViewById(R.id.graph);

        //setto le due textview scrollabili in caso abbia più dati di quelli che ci possano stare
        datiC.setMovementMethod(new ScrollingMovementMethod());
        datiJava.setMovementMethod(new ScrollingMovementMethod());

        //recupero i dati passati dall'intent e setto il titolo
        pos = getIntent().getIntExtra("pos", 0);
        titolo.setText(AlgorithmView.list[pos].getNome());

        //qui faccio la serie che stampera il grafico(dovo prendera dal db)
        //usero la funzione implementata in AlgoritmhView
        LineGraphSeries<DataPoint> seriesC = AlgorithmView.list[pos].getSeriesC(this);
        LineGraphSeries<DataPoint> seriesJava = AlgorithmView.list[pos].getSeriesJava(this);

        //setto alcuni parametri delle serie del c
        //setto il colore per differenziare le due serie
        seriesC.setColor(Color.RED);
        //non sono stati dati i titoli ai grafici con le funzioni della libreria perchè purtroppo
        //spostavano il grafico e non appariva tutto sullo schermo
        seriesC.setDrawDataPoints(true); //evidenziare i punti di interesse(datapoint)
        seriesC.setDataPointsRadius(8); //dimensione del datapoint
        //li facciamo anche cliccabili cosi da stamparci un toast per vedere bene i dati

        //setto alcuni parametri delle serie del java
        //setto il colore per differenziare le due serie
        seriesJava.setColor(Color.BLUE);
        //non sono stati dati i titoli ai grafici con le funzioni della libreria perchè purtroppo
        //spostavano il grafico e non appariva tutto sullo schermo
        seriesJava.setDrawDataPoints(true); //evidenziare i punti di interesse(datapoint)
        seriesJava.setDataPointsRadius(8); //dimensione del datapoint

        //inserisco la serie dei tempi del c
        graph.addSeries(seriesC);

        //inserire la serie dei tempi del java
        graph.addSeries(seriesJava);

        //ora mettiamo la leggenda
        seriesC.setTitle("C");
        seriesJava.setTitle("Java");
        graph.getLegendRenderer().setVisible(true);

        //ora abilitiamo lo zoom e lo scrool del grafico
        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalableY(true);
        graph.getViewport().setScrollableY(true);

        //faccio cliccabili le due serie cosi da poter visualizzare i dati con cui ha fatto la media
        //e stamparli sulla text box sotto
        /*
            * PROBLEMA CON I LISTNER:
            * i listener funzionano ma con i dati sovrapposti anche se clicco una sola volta me lo
            * fa deu volte perchè è come se avessi toccato entrambi i punti quindi volendo basta un
            * solo listner perchè tanto mi basta solo un dato che è quello dell'input
         */
        seriesC.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                int input = (int) dataPoint.getX(); //prendo l'input
                String outc = "", outjava = ""; //stringhe per l'output
                int x[][] = AlgorithmView.list[pos].getData(GraphActivity.this, input);
                //x non dovrebbe essere mai null ma non si sa mai quindi faccio questo if
                //in caso sia null non faccio niente
                if(x != null){
                    for(int i = 0; i < x.length; i++) {
                        outc = outc + x[i][0] + "\n";
                        outjava = outjava + x[i][1] + "\n";
                    }
                }
                in.setText(R.string.Input);
                c.setText(R.string.c);
                j.setText(R.string.java);
                datiInput.setText(Integer.toString(input));
                datiC.setText(outc);
                datiJava.setText(outjava);
            }
        });

        //dovrei implementare anche questo metodo allo stesso modo ma siccome le due linee sono abbastanza
        //vicine non serve mi fara l'output lo stesso
        /*
        seriesJava.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                int input = (int) dataPoint.getX(); //prendo l'input
                String outc = "", outjava = ""; //stringhe per l'output
                int x[][] = AlgorithmView.list[pos].getData(GraphActivity.this, input);
                //x non dovrebbe essere mai null ma non si sa mai quindi faccio questo if
                //in caso sia null non faccio niente
                if(x != null){
                    for(int i = 0; i < x.length; i++) {
                        outc = outc + x[i][0] + "\n";
                        outjava = outjava + x[i][1] + "\n";
                    }
                }
                in.setText(R.string.Input);
                c.setText(R.string.c);
                j.setText(R.string.java);
                datiInput.setText((String) Integer.toString(input));
                datiC.setText(outc);
                datiJava.setText(outjava);
            }
        });
        */

    }

}