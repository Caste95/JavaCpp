package javacpp.cmr.com.sdkvsndk;

/**
 * Classe che contiene tutti gli algoritmi in java su cui svolgiamo i test in questo programma
 * Contiene anche delle variabili di appoggio se servono ai vari algoritmi
 * Misuriamo il tempo dentro ogni algorimo cosi da avere la massima precisione della misurazione
 * Quindi ogni algoritmo ritornera un long (il tempo in ms)
 * Inoltre ritornaranno -1 in caso l'algoritmo venga interrotto (distrutto l'asynctask che lo sta eseguendo)
 * Questo è stato gestito con un flag di cui si verificherà lo stato durante l'esecuzione dell'algoritmo
 * E' stato fatto questo perchè si era notato che anche se l'AsyncTask veniva uccisso se c'erano dei metodi
 * esterni (gli algoritmi) che stavano eseguendo questi non venivano terminati e quindi andavano fino in fondo
 * causando problemi di stabilità del programma in caso ne fossero stati chaimati altri visto che non avevano ancora
 * liberato le risorse
 */

class Algorithm {

    //TODO: chiedere al tutor se è il caso di mettere l'array in strings.xml
    //variabile contenente i numeri primi per l'algoritmo primalitytest
    static final long[] PRIMES = {
            7,
            97,
            773,
            5113,
            54673L,
            633797L,
            9139397L,
            34542467L,
            359454547L,
            2331891997L,
            16333396997L,
            297564326947L,
            2456435675347L,
            37267627626947L,
            726483934563467L,
            9573357564326947L,
            75136938367986197L,
            165678739293946997L,
            1276812967623946997L
    };


    //flag per permettere l'uscita dell'algoritmo in caso in cui sia stato premuto su stop
    private static boolean flag = false;

    //metodi per accedere al flag
    static void cancella(){
        flag = true;
    }

    static void setta(){
        flag = false;
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
            if(flag) return -1;
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
        start = System.currentTimeMillis(); //primo tempo
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
        int i, j, l;
        long start, end;            //per il tempo
        //qui prendiamo anche l'inizializzazzione delle matrici visto che anche questo protebbe essere differente
        start = System.currentTimeMillis(); //primo tempo
        int[][] fatt1 = new int[n][n];
        int[][] fatt2 = new int[n][n];
        int[][] ris = new int[n][n];
        for (i = 0; i < n; i++) {
            for (j = 0; j < n; j++) {
                if (flag) return -1;
                fatt1[i][j] = (int)(Math.random()*100);
                fatt2[i][j] = (int)(Math.random()*100);
            }
        }
        for (j = 0; j < n; j++) {
            for (i = 0; i < n; i++) {
                for (l = 0; l < n; l++) {
                    if (flag) return -1;
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

    //incapsulamento di ackermann per la misura del tempo
    static long acker(long m, long n) {
        long start, end;            //per il tempo
        start = System.currentTimeMillis(); //primo tempo
        unacker(m, n);
        //fine algoritmo
        end = System.currentTimeMillis(); //secondo tempo
        return (end - start);
    }

    //verifica se un numero passato(n) è primo
    static long primalityTest(long n) {
        long s = System.currentTimeMillis();

        boolean prime = true;
        long sr = (long) Math.sqrt(n); //il compilatore java ottimizza già il codice, ma lo rendo esplicito
        for(long i=2; i < sr && prime; i++) {
            if(flag) return -1;
            if (n % i == 0)
                prime = false;
        }
        /*
        * You can verify the correctness of the algorithm by uncomment the code below
        */
        /*
        if(prime)
            Log.i("Java PrimalityTest",n + " is prime");
        else
            Log.i("Java PrimalityTest",n + " is not prime");
        */

        long e = System.currentTimeMillis();
        return e-s;
    }

    //calcola tutti i numeri primi fino a r
    static long eratostene(long r) {
        long s = System.currentTimeMillis();

        int i, j;
        boolean[] v = new boolean[(int) (r+1)]; //boolean vector
        v[0] = v[1] = false;
        for (i = 2; i <= r; i++) v[i] = true;// i is prime by default!
        for (i = 2; i <= r; i++)
            for (j = 2; i * j <= r; j++) {
                if(flag) return -1;
                v[i * j] = false; //disable multiples...
            }
        /*
        * You can verify the correctness of the algorithm by uncomment the code below
        */
        //int c = 0; for(i = 0; i <= r; i++) if(v[i]) c++;
        //Log.d("Java Eratoste\'s sieve", "There are " + c + " prime numbers");

        long e = System.currentTimeMillis();
        return e-s;
    }

    //concatena stringhe
    static long strcat(int n){
        long s = System.currentTimeMillis();

        String st = new String();
        for(int i = 0; (i < n); i++)
            if(flag)
                return -1;
            else
                //Data l'immutabilità degli oggetti String, st viene ricreata e distrutta dal GC
                //il tutto è mooooolto lento
                st += "a";

        long e = System.currentTimeMillis();
        return e - s;
    }
}
