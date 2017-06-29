package javacpp.cmr.com.sdkvsndk;

/*
  Activity iniziale che visualizza la lista degli algoritmi
  La visualizzazzione degli algoritmi è fatta tramite una ListView modificata
  La grafica della list view è datta dal file listactivity_raw.xml in cui ci sono gli ID a cui fa riferimento
  Usiamo un simple adapter per fare questo(si sarebbe potuto usare anche un adapter fatto ad-hoc estendendo
  la classe adapter ma il simple adapter è più semplice da gestire.
  Il SimpleAdapter prende come parametri:
   - Il contesto
   - La sorgente dei dati fatta in questo modo:
       - Un ArrayList che come elementi ha delle HashMap <String, Object>
           - Le stringe sono degli identificativi
           - Mentre gli Object sono i nostri dati che andremo a prendere dagli algoritmi in AlgorithmView
   - Poi prende un arrey di String che sara uguale agli identificatori che abbiamo messo nella HashMap
   - E un arrey di int che saranno gli id del nostro Layout
  Cosi facendo andrà a ricomporre l'interfaccia desiderata per ogni riga della ListView
  Renderemo "toccabile" ogni riga della lista così da poter lanciare un intent per aprire l'interfaccia
  di esecuzione di quell'algoritmo (MainActivity).
  Inoltre distruggeremo questa Activity in modo che quando si ritorna si abbiano i dati aggiornati in automatico
  dal db senza dover fare passaggi di parametri inutili con gli intent
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;
import android.view.View;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //devo fare un arrraylist e riempirla
        ArrayList<AlgorithmView> arr = new ArrayList<>();
        Collections.addAll(arr, AlgorithmView.LIST);

        //ora devo inizializzare una mappa chiave valore per poter mettere i valore nella lista con il simple adapter
        ArrayList<HashMap<String, Object>> dati = new ArrayList<>();
        for(int i = 0; i < arr.size(); i++){
            AlgorithmView a = arr.get(i);
            HashMap<String,Object> map = new HashMap<>();  //creiamo una mappa di valori
            map.put("nome", getString(a.getNome()));       //prendo il nome del algoritmo
            map.put("tipo", getString(a.getTipo()));       //prendo il tipo dell algoritmo
            map.put("c", a.getEseC(this));                 //prendo il tempo di esecuzione in linguaggio c
            map.put("j", a.getEseJ(this));                 //prendo il tempo di esecuzione in linguaggio java
            map.put("input", a.getInput(this));            //prendo l'ultimo dato di input inserito
            dati.add(map);                                 //aggiungiamo la mappa di valori all'ArreyList dati
        }

        //creiamo le due strutture from e to per il simple adapter che mappera i valori tra i due
        String[] from = {"nome", "tipo", "c", "j", "input"};                  //dalla hash mappa
        int[] to = { R.id.nomeA, R.id.tipoA, R.id.c, R.id.j, R.id.inp2 };     //alle id della view

        //ora facciamo l'adapter
        SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), dati, R.layout.listactivity_raw, from, to);

        //ora creo la ListView e ci applico l'adapter
        ListView mylist = (ListView) findViewById(R.id.listView1);
        mylist.setAdapter(adapter);

        //ora faccio la lista cliccabile
        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adattatore, View view, int pos, long id){
                // qui lancero la ExecutinonsActivity con un intent esplicito e passero la posizione
                // che serve per la visualizzazzione della descrizzione
                Intent i = new Intent( view.getContext(), MainActivity.class);
                i.putExtra("pos", pos);
                startActivity(i);
                //dovro distruggere la activity perche se l'utente preme back mi carichera la versione
                //con dati obsoleti(non aggiornera le ultime esecuzioni e l'input)
                //quindi la distruggo e quando l'utente preme back nell'altra activity questa sarà
                //ricreata con un intent
                finish();
            }
        });
    }

}
