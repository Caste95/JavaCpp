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
            if(flag) break;
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
                                if(flag) return -1;
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

    protected static boolean flag = false;

    public static int unfibonacci(int n) {

        if (n == 0)
            return 0;
        if (n == 1)
            return 1;
        if(flag) return 0;
        else
            return unfibonacci(n-1) + unfibonacci(n-2);
    }

    public static long fibonacci(int n) {
        long s = System.currentTimeMillis();
        unfibonacci(n);
        long e = System.currentTimeMillis();
        return (e - s);
    }

    public static void calcMatr(int n) {
        int[][] fatt1 = new int[n][n];
        int[][] fatt2 = new int[n][n];
        int[][] ris = new int[n][n];
        for (int i = 0; i < n; i++) {
            if (flag) break;
            for (int j = 0; j < n; j++) {
                fatt1[i][j] = (int)(Math.random()*100);
                fatt2[i][j] = (int)(Math.random()*100);
            }
        }
        for (int j = 0; j < n; j++) {
            if (flag) break;
            for (int i = 0; i < n; i++) {
                for (int l = 0; l < n; l++) {
                    ris[i][j] += fatt1[l][j] * fatt2[i][l];
                }
            }
        }
    }

    public static int acker(int m, int n) {
        if (m == 0) return n + 1;
        if ((m > 0) && (n == 0)) return acker(m - 1, 1);
        if(flag) return 0;
        else return acker(m - 1, acker(m, n - 1));
    }
}
