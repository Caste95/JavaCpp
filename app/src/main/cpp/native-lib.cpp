#include <jni.h>
#include <string>
#include <stdlib.h>


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
bool flag = false;

extern "C"
JNIEXPORT jlong JNICALL
Java_javacpp_cmr_com_sdkvsndk_MainActivity_Random(JNIEnv* env, jobject, jlong n){
    unsigned long long m = 4294967296L; // = 2^32
    unsigned long long a = 432274426543147L; //numero primo a 15 cifre
    unsigned int c = 42430867; // un altro numero primo molto grande a 8 cifre
    long long x, x0;
    timeval start, stop;
    long long t;
    //inizio algoritmo e prendo primo tempo
    gettimeofday(&start, NULL);
    x0 = (unsigned)time(NULL); //seme
    for(int i = 1; i < n; i++) {
        if(flag) break;
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
Java_javacpp_cmr_com_sdkvsndk_MainActivity_NestedLoops(JNIEnv* env, jobject, jint n){
    int i, j, k, l, r, p; //contatori dei cicli
    timeval start, stop;
    long long t;
    //inizio algoritmo e prendo primo tempo
    gettimeofday(&start, NULL);
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
    //fine algoritmo, prendo il secondo tempo
    gettimeofday(&stop, NULL);
    t = (stop.tv_sec - start.tv_sec) * 1000;
    t += (long long) ((stop.tv_usec - start.tv_usec) / 1000);
    return (jlong) t;
}

void Java_javacpp_cmr_com_sdkvsndk_MainActivity_cancella(JNIEnv *env, jobject obj) {
    flag = true;
}


jboolean Java_javacpp_cmr_com_sdkvsndk_MainActivity_visualizza(JNIEnv *env, jobject obj) {
    return flag;
}

void Java_javacpp_cmr_com_sdkvsndk_MainActivity_setta(JNIEnv *env, jobject obj) {
    flag = false;
}

jint unfibonacci(jint n) {
    if (n==0) return 0;
    if (n==1) return 1;
    if (flag) return 0;
    return unfibonacci(n-1) + unfibonacci(n-2);
}

jlong Java_javacpp_cmr_com_sdkvsndk_MainActivity_fibonacci(JNIEnv *env, jobject obj, jint n) {
    timeval start, stop;
    long long t;
    gettimeofday(&start, NULL);

    unfibonacci(n);

    gettimeofday(&stop, NULL);
    t = (stop.tv_sec-start.tv_sec) * 1000;
    t += (long long) ((stop.tv_usec-start.tv_usec)/1000);
    return (jlong) t;
}

jlong Java_javacpp_cmr_com_sdkvsndk_MainActivity_calcMatr(JNIEnv *env, jobject obj, jint n) {
    timeval start, stop;
    long long t;
    gettimeofday(&start, NULL);

    int fatt1[n][n];
    int fatt2[n][n];
    int ris[n][n];
    for (int i = 0; i < n; i++) {
        if(flag) break;
        for (int j = 0; j < n; j++) {
            fatt1[i][j] = rand()%100;
            fatt2[i][j] = rand()%100;
        }
    }
    for (int j = 0; j < n; j++) {
        if(flag) break;
        for (int i = 0; i < n; i++) {
            for (int l = 0; l < n; l++) {
                ris[i][j] += fatt1[l][j] * fatt2[i][l];
            }
        }
    }

    gettimeofday(&stop, NULL);
    t = (stop.tv_sec-start.tv_sec) * 1000;
    t += (long long) ((stop.tv_usec-start.tv_usec)/1000);
    return (jlong) t;
}

jint unacker(jint m, jint n) {
    if (m == 0) return n + 1;
    if ((m > 0) && (n == 0)) return unacker(m - 1, 1);
    if (flag) return 0;
    else return unacker(m - 1, unacker(m, n - 1));
}

jlong Java_javacpp_cmr_com_sdkvsndk_MainActivity_acker(JNIEnv *env, jobject obj, jint m, jint n) {
    timeval start, stop;
    long long t;
    gettimeofday(&start, NULL);

    unacker(m, n);

    gettimeofday(&stop, NULL);
    t = (stop.tv_sec-start.tv_sec) * 1000;
    t += (long long) ((stop.tv_usec-start.tv_usec)/1000);
    return (jlong) t;
}
