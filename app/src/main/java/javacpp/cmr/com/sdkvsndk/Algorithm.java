package javacpp.cmr.com.sdkvsndk;

public class Algorithm {

    /*Generazione di sequenze di numeri casuali
    * usero il linear conguential generetor come algoritmo che e' semplice ed efficace
    * mi bastano poche variabili
    * m = per il modulo
    * a = numero primo grande da moltiplicare
    * c = numero casuale da sommare
    * n = input della dimensione dei numeri casuali che ci dara' l'utente
    * useremo come seme il tempo con current time millis e sara sempre il
    * primo elemento della lista
    */
    public static long Random (long n){
        long m = 4294967296L; // = 2^32 (L per i long)
        long a = 432274426543147L; //numero primo a 15 cifre (L per i long)
        int c = 42430867; // un altro numero primo molto grande a 8 cifre
        long x; //dove andra il numero casuale
        long x0;//seme
        long start, end; //per il tempo
        //inizio algoritmo e prendo primo tempo
        start = System.currentTimeMillis(); //primo tempo
        x0 = System.currentTimeMillis(); //inizializzazzione seme
        for(int i = 1; i < n; i++){
            x = ((a * x0) + c) % m;
            x0 = x;
        }
        //fine algoritmo
        end = System.currentTimeMillis(); //secondo tempo
        return (end - start);
    }
    /*Algoritmo Nested Loops
    * Un algoritmo molto semplice prende come parametro di ingresso un numero intero n
    * e fa sei cicli uno dentro l'altro con ognuno n iterazioni
     */
    public static long NestedLoops (int n){
        int i, j, k, l, r, p; //contatori dei cicli
        long start, end;
        //inizio algoritmo e prendo primo tempo
        start = System.currentTimeMillis(); //primo tempo
        for(i = 0; i < n; i++){
            for (j = 0; j < n; j++){
                for(k = 0; k < n; k++){
                    for(l = 0; l < n; l++){
                        for(r = 0; r < n; r++){
                            for(p = 0; p < n; p++){
                                //non faccio niente
                            }
                        }
                    }
                }
            }
        }
        //fine algoritmo
        end = System.currentTimeMillis(); //secondo tempo
        return (end - start);
    }
}
