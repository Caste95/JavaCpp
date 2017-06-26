#include <jni.h>
#include <string>

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
Java_com_example_davide_javavscc_ExecutionsActivity_Random(JNIEnv* env, jobject, jlong n){
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
Java_com_example_davide_javavscc_ExecutionsActivity_NestedLoops(JNIEnv* env, jobject, jint n){
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

