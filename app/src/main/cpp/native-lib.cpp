#include <jni.h>
#include <string>
#include <stdlib.h>
#include <cmath>

//variabile per stoppare l'algoritmo nell asynctask
bool flag = false;

extern "C" {
//metodi per accedere alla viariabile booleana
void Java_javacpp_cmr_com_sdkvsndk_MainActivity_cancella(JNIEnv *env, jobject obj) {
    flag = true;
}

jboolean Java_javacpp_cmr_com_sdkvsndk_MainActivity_visualizza(JNIEnv *env, jobject obj) {
    return (jboolean) flag;
}
extern "C"
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
extern "C"
JNIEXPORT jlong JNICALL
Java_javacpp_cmr_com_sdkvsndk_MainActivity_random(JNIEnv *env, jobject, jlong n) {
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

/*Algoritmo Nested Loops
* Un algoritmo molto semplice prende come parametro di ingresso un numero intero n
* e fa sei cicli uno dentro l'altro con ognuno n iterazioni
*/
extern "C"
JNIEXPORT jlong JNICALL
Java_javacpp_cmr_com_sdkvsndk_MainActivity_nestedLoops(JNIEnv *env, jobject, jint n) {
    int i, j, k, l, r, p; //contatori dei cicli
    timeval start, stop;
    long long t;
    char *v = (char*) malloc(3*n*n);
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
    free(v);
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
extern "C"
JNIEXPORT jlong JNICALL
Java_javacpp_cmr_com_sdkvsndk_MainActivity_fibonacci(JNIEnv *env, jobject obj, jint n) {
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

JNIEXPORT jlong JNICALL
Java_javacpp_cmr_com_sdkvsndk_MainActivity_calcMatr(JNIEnv *env, jobject obj, jint n) {
    timeval start, stop;
    long long t;
    //qui prendiamo anche l'inizializzazzione delle matrici visto che anche questo protebbe essere differente
    gettimeofday(&start, NULL);
    int fatt1[n][n];
    int fatt2[n][n];
    int ris[n][n];
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            if (flag) return -1;
            fatt1[i][j] = rand() % 100;
            fatt2[i][j] = rand() % 100;
        }
    }
    for (int j = 0; j < n; j++) {
        for (int i = 0; i < n; i++) {
            for (int l = 0; l < n; l++) {
                if (flag) return -1;
                ris[i][j] += fatt1[l][j] * fatt2[i][l];
            }
        }
    }
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
extern "C"
JNIEXPORT jlong JNICALL
Java_javacpp_cmr_com_sdkvsndk_MainActivity_acker(JNIEnv *env, jobject obj, jlong m, jlong n) {
    timeval start, stop;
    long long t;
    gettimeofday(&start, NULL);
    unacker(m, n);
    gettimeofday(&stop, NULL);
    t = (stop.tv_sec - start.tv_sec) * 1000;
    t += (long long) ((stop.tv_usec - start.tv_usec) / 1000);
    return (jlong) t;
}

extern "C"
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
    */
    //int c = 0; for(int i = 0; i <= r; i++) if(v[i]) c++;
    //__android_log_print(ANDROID_LOG_INFO,"C++ Eratostene\'s sieve","There are %d prime numbers",c);

    free(v);

    gettimeofday(&stop, NULL);
    t = (stop.tv_sec - start.tv_sec) * 1000;
    t += (long long) ((stop.tv_usec - start.tv_usec) / 1000);
    return (jlong) t;
}

extern "C"
jlong Java_javacpp_cmr_com_sdkvsndk_MainActivity_primalityTest(JNIEnv *env, jobject obj, jlong r) {
    timeval start, stop;
    long long t;
    gettimeofday(&start, NULL);

    bool prime = true;
    for (jlong i = 2; i < sqrt(r) && prime; i++) {
        if (flag) return -1;
        if (r % i == 0) prime = false;
    }

    /*
     * You can verify the correctness of the algorithm by uncomment the code below
    */
    /*
    if (prime) { //TODO: Non stampa long long -.-
        __android_log_print(ANDROID_LOG_INFO, "C++ PrimalityTest", "%ld is prime", (long long) r);
    } else {
        __android_log_print(ANDROID_LOG_INFO, "C++ PrimalityTest", "%ld is not prime", (long long) r);
    }
    */

    gettimeofday(&stop, NULL);
    t = (stop.tv_sec - start.tv_sec) * 1000;
    t += (long long) ((stop.tv_usec - start.tv_usec) / 1000);
    return (jlong) t;
}
}