package javacpp.cmr.com.sdkvsndk;

class Algorithm {

    //flag per permettere l'uscita dell'algoritmo in caso in cui sia stato premuto su stop
    private static boolean flag = false;

    //metodi per accedere al flag
    static void cancella(){
        flag = true;
    }

    static void setta(){
        flag = false;
    }

    public static boolean visualizza(){
        return flag;
    }

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
    static long random (long n){
        long m = 4294967296L;       // = 2^32 (L per i long)
        long a = 432274426543147L;  //numero primo a 15 cifre (L per i long)
        int c = 42430867;           // un altro numero primo molto grande a 8 cifre
        long x;                     //dove andra il numero casuale
        long x0;                    //seme
        long start, end;            //per il tempo
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
    static long nestedLoops (int n){
        int i, j, k, l, r, p; //contatori dei cicli
        long start, end;
        //inizio algoritmo e prendo primo tempo
        start = System.currentTimeMillis();//primo tempo
        for(i = 0; i < n; i++){
            for (j = 0; j < n; j++){
                for(k = 0; k < n; k++){
                    for(l = 0; l < n; l++){
                        for(r = 0; r < n; r++){
                            for(p = 0; p < n; p++){
                                //non faccio niente verifico solo il flag
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

    //vero e proprio algoritmo di fibonacci
    private static int unfibonacci(int n) {
        if (n == 0)
            return 0;
        if (n == 1)
            return 1;
        if(flag) return 0;
        else
            return unfibonacci(n-1) + unfibonacci(n-2);
    }

    //incapsulamento di fibonacci per la misura del tempo
    static long fibonacci(int n) {
        long start, end;            //per il tempo
        start = System.currentTimeMillis(); //primo tempo
        unfibonacci(n);
        //fine algoritmo
        end = System.currentTimeMillis(); //secondo tempo
        return (end - start);
    }

    //algoritmo di prodotto tra due matrici
    static long calcMatr(int n) {
        long start, end;            //per il tempo
        //qui prendiamo anche l'inizializzazzione delle matrici visto che anche questo protebbe essere differente
        start = System.currentTimeMillis();//primo tempo
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
        //fine algoritmo
        end = System.currentTimeMillis(); //secondo tempo
        return (end - start);
    }

    //vero e proprio algoritmo di ackerman
    private static long unacker(long m, long n) {
        if (m == 0) return n + 1;
        if ((m > 0) && (n == 0)) return unacker(m - 1, 1);
        if(flag) return 0;
        else return unacker(m - 1, unacker(m, n - 1));
    }

    //incapsulamento di acker per la misura del tempo
    static long acker(long m, long n) {
        long start, end;            //per il tempo
        start = System.currentTimeMillis(); //primo tempo
        unacker(m, n);
        //fine algoritmo
        end = System.currentTimeMillis(); //secondo tempo
        return (end - start);
    }


    static long primalityTest(long x) {
        long s = System.currentTimeMillis();


        long e = System.currentTimeMillis();
        return e-s;
    }

    static long eratostene(long x) {
        long s = System.currentTimeMillis();


        long e = System.currentTimeMillis();
        return e-s;
    }


}
