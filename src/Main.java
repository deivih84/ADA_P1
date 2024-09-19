import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.time.Duration;
import java.time.Instant;


class Main {
    public static void main(String[] args) {

        final int TAMVECTOR = 10000;
        int[] vector;
        double[] mediciones = new double[20];
        Instant start, finish;


//        String cadenaCSV = stringACSV(intAStringArray(a));


//        for (int nAlgoritmo = 0; nAlgoritmo < 3; nAlgoritmo++) {

            for (int i = 1; i < 10; i++) {
                vector = new int[TAMVECTOR * i];
                System.out.println(vector.length);

                for (int j = 0; j < 20; j++) {
                    aleatorio(vector, TAMVECTOR);

                    start = Instant.now();

                    ordena3(vector, TAMVECTOR);

                    finish = Instant.now();
                    mediciones[j] = (double) Duration.between(start, finish).toNanos() / 1000000000;
                }

                System.out.println(Arrays.toString(mediciones));
                escribirCSV(stringACSV(intAStringArray(mediciones)), 1);

            }
        }
//    }

    private static void escribirCSV(String cad, int archivo) {
        try {
            FileWriter myWriter = new FileWriter("src/algoritmo" + archivo + ".csv", true);
            myWriter.write(cad + "\n");
            myWriter.close();
            System.out.println("Todo bn.");
        } catch (IOException e) {
            System.out.println("ERROR CATASTRÓFICO.");
            e.printStackTrace();
        }
    }


    private static String[] intAStringArray(double[] intArray) {
        String[] nuevoArray = new String[intArray.length];

        for (int i = 0; i < intArray.length; i++) {
            nuevoArray[i] = String.valueOf(intArray[i]);
        }
        return nuevoArray;
    }

    private static String stringACSV(String[] array) {
        return String.join(",", array);
    }

    private static void ordenarSegunAlgoritmo(int[] vector, int tam, int algoritmo) {
        if (algoritmo == 0) {
            ordena1(vector, algoritmo);
        } else if (algoritmo == 1) {
            ordena2(vector, algoritmo);
        } else if (algoritmo == 2) {
            ordena3(vector, algoritmo);
        }
    }

// Medir el tiempo
//import java.time.Duration;
//import java.time.Instant;
//...
//Instant start = Instant.now();
//// Código que queremos medir
//Instant finish = Instant.now();
//long timeElapsed = Duration.between(start, finish).toNanos();


    // A
    static Random random = new Random();


    // Alg1
    public static void ordena1(int v[], int tam) {
        int i, j, temp;
        i = 1;
        j = 2;
        while (i < tam) {
            if (v[i - 1] <= v[i]) {
                i = j;
                j = j + 1;
            } else {
                temp = v[i - 1];
                v[i - 1] = v[i];
                v[i] = temp;
                i = i - 1;
                if (i == 0) {
                    i = 1;
                }
            }
        }
    }


    // Alg2
    public static void ordena2(int[] v, int tam) {
        int k;
        int n = tam;
        for (k = n / 2; k >= 1; k--) {
            f(v, k, n);
        }
        k = n;
        while (k > 1) {
            g(v, 1, k--);
            f(v, 1, k);
        }
    }

    private static void f(int[] v, int k, int n) {
        while (2 * k <= n) {
            int j = 2 * k;
            if ((j < n) && (v[j - 1] < v[j])) {
                j++;
            }
            if (v[k - 1] >= v[j - 1]) {
                break;
            }
            g(v, k, j);
            k = j;
        }
    }

    private static void g(int[] v, int i, int j) {
        int temp = v[i - 1];
        v[i - 1] = v[j - 1];
        v[j - 1] = temp;
    }


    // Alg3
    public static void ordena3(int[] v, int tam) {
        int m = h(v, tam);
        int[] c = new int[m + 1];
        int[] w = new int[tam];
        for (int i = 0; i < tam; i++) {
            c[v[i]] = c[v[i]] + 1;
        }
        for (int i = 1; i < m + 1; i++) {
            c[i] = c[i] + c[i - 1];
        }
        for (int i = tam - 1; i >= 0; i--) {
            w[c[v[i]] - 1] = v[i];
            c[v[i]] = c[v[i]] - 1;
        }
        for (int i = 0; i < tam; i++) {
            v[i] = w[i];
        }
    }

    private static int h(int[] v, int tam) {
        int i;
        int m = v[0];
        for (i = 1; i < tam; i++) {
            if (v[i] > m) {
                m = v[i];
            }
        }
        return m;
    }


//Fisher Yates

    public static void aleatorio(int[] v, int tam) {
        int i;
        for (i = 0; i < tam; i++) {
            v[i] = i;
        }
        for (i = tam - 1; i > 0; i--) {
// random.nextInt(k) devuelve un entero pseudoaleatorio distribuido
// uniformemente entre 0 (incluido) y el valor especificado k (excluido)
            int j = random.nextInt(i + 1);
            int temp = v[i];
            v[i] = v[j];
            v[j] = temp;
        }
    }

}

