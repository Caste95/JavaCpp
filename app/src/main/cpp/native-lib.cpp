#include <jni.h>
#include <string>
#include <stdlib.h>
#include <cmath>

/**
 * Libreria che contiene tutti gli algoritmi scritti in c su cui svolgiamo i test in questo programma
 * Misuriamo il tempo dentro ogni algorimo cosi da avere la massima precisione della misurazione
 * Quindi ogni algoritmo ritornera un Jlong (il tempo in ms)
 * Inoltre ritornaranno -1 in caso l'algoritmo venga interrotto (distrutto l'asynctask che lo sta eseguendo)
 * Questo è stato gestito con un flag di cui si verificherà lo stato durante l'esecuzione dell'algoritmo
 * E' stato fatto questo perchè si era notato che anche se l'AsyncTask veniva uccisso se c'erano dei metodi
 * esterni (gli algoritmi) che stavano eseguendo questi non venivano terminati e quindi andavano fino in fondo
 * causando problemi di stabilità del programma in caso ne fossero stati chaimati altri visto che non avevano ancora
 * liberato le risorse
 */

//variabile per stoppare l'algoritmo nell asynctask
bool flag = false;

extern "C" {

    //setta il flag per interrompere i cicli
    void Java_javacpp_cmr_com_sdkvsndk_MainActivity_cancella(JNIEnv *env, jobject obj) {
        flag = true;
    }

    //ripristina il flag
    void Java_javacpp_cmr_com_sdkvsndk_MainActivity_setta(JNIEnv *env, jobject obj) {
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
    jlong Java_javacpp_cmr_com_sdkvsndk_MainActivity_random(JNIEnv *env, jobject, jlong n) {
        unsigned long long m = 4294967296L; // = 2^32
        unsigned long long a = 432274426543147L; //numero primo a 15 cifre
        unsigned int c = 42430867; // un altro numero primo molto grande a 8 cifre
        long long x, x0;
        timeval start, stop;
        long long t;
        //inizio algoritmo e prendo primo tempo
        gettimeofday(&start, NULL);
        x0 = (unsigned) time(NULL); //seme
        for (int i = 1; i < n; i++) {
            if (flag) return -1;
            x = ((a * x0) + c) % m;
            x0 = x;
        }
        //fine algoritmo, prendo il secondo tempo
        gettimeofday(&stop, NULL);
        t = (stop.tv_sec - start.tv_sec) * 1000;
        t += (long long) ((stop.tv_usec - start.tv_usec) / 1000);
        return (jlong) t;
    }

    /*
     * Algoritmo Nested Loops
     * Un algoritmo molto semplice prende come parametro di ingresso un numero intero n
     * e fa sei cicli uno dentro l'altro con ognuno n iterazioni
    */
    jlong Java_javacpp_cmr_com_sdkvsndk_MainActivity_nestedLoops(JNIEnv *env, jobject, jint n) {
        int i, j, k, l, r, p; //contatori dei cicli
        timeval start, stop;
        long long t;
        //inizio algoritmo e prendo primo tempo
        gettimeofday(&start, NULL);
        for (i = 0; i < n; i++) {
            for (j = 0; j < n; j++) {
                for (k = 0; k < n; k++) {
                    for (l = 0; l < n; l++) {
                        for (r = 0; r < n; r++) {
                            for (p = 0; p < n; p++) {
                                //non faccio niente verifico solo il flag
                                if (flag) return -1;
                            }
                        }
                    }
                }
            }
        }
        //fine algoritmo, prendo il secondo tempo
        gettimeofday(&stop, NULL);
        t = (stop.tv_sec - start.tv_sec) * 1000;
        t += (long long) ((stop.tv_usec - start.tv_usec) / 1000);
        return (jlong) t;
    }

    //vero e proprio algoritmo di fibonacci
    jint unfibonacci(jint n) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        if (flag) return 0;
        return unfibonacci(n - 1) + unfibonacci(n - 2);
    }

    //algoritmo per misurare il tempo di fibonacci(al suo interno chiama fibonacci)
    jlong Java_javacpp_cmr_com_sdkvsndk_MainActivity_fibonacci(JNIEnv *env, jobject obj, jint n) {
        timeval start, stop;
        long long t;
        gettimeofday(&start, NULL);
        unfibonacci(n);
        gettimeofday(&stop, NULL);
        t = (stop.tv_sec - start.tv_sec) * 1000;
        t += (long long) ((stop.tv_usec - start.tv_usec) / 1000);
        return (jlong) t;
    }

    //algoritmo di prodotto di due matrici
    jlong Java_javacpp_cmr_com_sdkvsndk_MainActivity_calcMatr(JNIEnv *env, jobject obj, jint n) {
        timeval start, stop;
        long long t;
        int i, j, l;
        double **mat1;
        double **mat2;
        double **ris;
        //qui prendiamo anche l'inizializzazzione delle matrici visto che anche questo potrebbe essere differente
        gettimeofday(&start, NULL);
        //usiamo la malloc perche con l'inizializzazzione classica android fa un po' di casino con la dimensione
        //delle variabili e quindi non alloca tutta la matrice
        mat1 = (double**) malloc(n * sizeof(double));
        mat2 = (double**) malloc(n * sizeof(double));
        ris = (double**) malloc(n * sizeof(double));
        for(i = 0; i < n; i++){
            mat1 [i] = ( double *) malloc ( n * sizeof ( double ));
            mat2 [i] = ( double *) malloc ( n * sizeof ( double ));
            ris [i] = ( double *) malloc ( n * sizeof ( double ));
        }
        srand((unsigned int) time(NULL));
        for (i = 0; i < (long)n; i++) {
            for (j = 0; j < (long)n; j++) {
                if(flag) return -1;
                mat1[i][j] = (rand()/RAND_MAX * 100);
                mat2[i][j] = (rand()/RAND_MAX * 100);
            }
        }
        for (j = 0; j < (long)n; j++) {
            for (i = 0; i < (long)n; i++) {
                for (l = 0; l < (long)n; l++) {
                    if (flag) return -1;
                    ris[i][j] += mat1[l][j] * mat2[i][l];
                }
            }
        }
        free(mat1);
        free(mat2);
        free(ris);
        //fine algoritmo e prendiamo secondo tempo
        gettimeofday(&stop, NULL);
        t = (stop.tv_sec - start.tv_sec) * 1000;
        t += (long long) ((stop.tv_usec - start.tv_usec) / 1000);
        return (jlong) t;
    }

    //vero e proprio algoritmo di ackerman
    jlong unacker(jlong m, jlong n) {
        if (m == 0) return n + 1;
        if ((m > 0) && (n == 0)) return unacker(m - 1, 1);
        if (flag) return 0;
        else return unacker(m - 1, unacker(m, n - 1));
    }

    //misurazione del tempo di esecuzione di ackerman
    jlong Java_javacpp_cmr_com_sdkvsndk_MainActivity_acker(JNIEnv *env, jobject obj, jlong m, jlong n) {
        timeval start, stop;
        long long t;
        gettimeofday(&start, NULL);
        unacker(m, n);
        gettimeofday(&stop, NULL);
        t = (stop.tv_sec - start.tv_sec) * 1000;
        t += (long long) ((stop.tv_usec - start.tv_usec) / 1000);
        return (jlong) t;
    }

    //calcola tutti i numeri primi fino a r
    jlong Java_javacpp_cmr_com_sdkvsndk_MainActivity_eratostene(JNIEnv *env, jobject obj, jlong n) {
        timeval start, stop;
        long long t;
        gettimeofday(&start, NULL);

        int i, j, r = (int) n;
        char *v; //boolean vector
        v = (char *) malloc((size_t) (r + 1));
        v[0] = v[1] = 0;
        for (i = 2; i <= r; i++) v[i] = 1;// i is prime by default!
        for (i = 2; i <= r; i++)
            for (j = 2; i * j <= r; j++) {
                if (flag) {
                    free(v);
                    return -1;
                }
                v[i * j] = 0; //disable multiples...
            }
        /*
         * You can verify the correctness of the algorithm by uncomment the code below
         * and adding #include <android/log.h>
        */
        //int c = 0; for(int i = 0; i <= r; i++) if(v[i]) c++;
        //__android_log_print(ANDROID_LOG_INFO,"C++ Eratostene\'s sieve","There are %d prime numbers",c);

        free(v);

        gettimeofday(&stop, NULL);
        t = (stop.tv_sec - start.tv_sec) * 1000;
        t += (long long) ((stop.tv_usec - start.tv_usec) / 1000);
        return (jlong) t;
    }

    //verifica se un numero passato(n) è primo
    jlong Java_javacpp_cmr_com_sdkvsndk_MainActivity_primalityTest(JNIEnv *env, jobject obj, jlong r) {
        timeval start, stop;
        long long t;
        gettimeofday(&start, NULL);

        bool prime = true;
        //il compilatore non riesce a ottimizzare il codice, allora lo faccio io
        // veniva calcolata la sqrt ad ogni iterazione che è un overhead MICIDIALE
        unsigned long long sr = (unsigned long long) sqrt(r);
        for (long long i = 2; i <   sr && prime; i++) {
            if (flag) return -1;
            if (r % i == 0) prime = false;
        }

        /*
         * You can verify the correctness of the algorithm by uncomment the code below
         * and adding #include <android/log.h>
        */
    /*
        if (prime) { //TODO il debugger mi mostra r negativo ma logcat me lo stampa correttamente
            __android_log_print(ANDROID_LOG_INFO, "C++ PrimalityTest", "%llu is prime", (unsigned long long) r);
        } else {
            __android_log_print(ANDROID_LOG_INFO, "C++ PrimalityTest", "%llu is not prime", (unsigned long long)r);
        }
    */

        gettimeofday(&stop, NULL);
        t = (stop.tv_sec - start.tv_sec) * 1000;
        t += (long long) ((stop.tv_usec - start.tv_usec) / 1000);
        return (jlong) t;
    }

    //concatena stringhe
    jlong Java_javacpp_cmr_com_sdkvsndk_MainActivity_strcat(JNIEnv *env, jobject obj, jint n) {
        timeval start, stop;
        long long t;
        gettimeofday(&start, NULL);

        std::string st;
        for(int i = 0; (i < n); i++)
            if(flag)
                return -1;
            else
                st += "a";

        gettimeofday(&stop, NULL);
        t = (stop.tv_sec - start.tv_sec) * 1000;
        t += (long long) ((stop.tv_usec - start.tv_usec) / 1000);
        return (jlong) t;
    }

}//chiusura extern "c"