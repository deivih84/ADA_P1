import java.io.FileWriter
import java.io.IOException
import java.sql.Time
import java.time.Duration
import java.time.Instant
import java.util.*
import kotlin.concurrent.timer
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis

internal object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val TAMVECTOR = 10000
        var vector: IntArray
        val mediciones = DoubleArray(10)
        var tiempo: Long


        repeat(3) { it0 ->

//        for (int nAlgoritmo = 0; nAlgoritmo < 3; nAlgoritmo++) {
            for (i in 0..9) {
                vector = IntArray(TAMVECTOR * (i + 1))
                println(vector.size)

                repeat(20) {
                    aleatorio(vector, TAMVECTOR)

                    tiempo = when (it0) {
                        0 -> measureNanoTime {
                            ordena1(vector, TAMVECTOR)
                        }

                        1 -> measureNanoTime {
                            ordena2(vector, TAMVECTOR)
                        }

                        else -> measureNanoTime {
                            ordena3(vector, TAMVECTOR)
                        }
                    }


                    mediciones[i] += tiempo.toDouble()
                }

                mediciones[i] = mediciones[i] / 20

            }
            println(mediciones.contentToString())
            escribirCSV(stringACSV(intAStringArray(mediciones)), 0)
        }
    }

    //    }
    private fun escribirCSV(cad: String, archivo: Int) {
        try {
            val myWriter = FileWriter("src/tiempos/algoritmo$archivo.csv", true)
            myWriter.write(cad + "\n")
            myWriter.close()
//            println("Todo bn.")
        } catch (e: IOException) {
            println("ERROR CATASTRÓFICO.")
            e.printStackTrace()
        }
    }


    private fun intAStringArray(intArray: DoubleArray): Array<String?> {
        val nuevoArray = arrayOfNulls<String>(intArray.size)

        for (i in intArray.indices) {
            nuevoArray[i] = intArray[i].toString()
        }
        return nuevoArray
    }

    private fun stringACSV(array: Array<String?>): String {
        return java.lang.String.join(",", *array)
    }

    private fun ordenarSegunAlgoritmo(vector: IntArray, tam: Int, algoritmo: Int) {
        if (algoritmo == 0) {
            ordena1(vector, algoritmo)
        } else if (algoritmo == 1) {
            ordena2(vector, algoritmo)
        } else if (algoritmo == 2) {
            ordena3(vector, algoritmo)
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
    var random: Random = Random()


    // Alg1
    fun ordena1(v: IntArray, tam: Int) {
        var i: Int
        var j: Int
        var temp: Int
        i = 1
        j = 2
        while (i < tam) {
            if (v[i - 1] <= v[i]) {
                i = j
                j = j + 1
            } else {
                temp = v[i - 1]
                v[i - 1] = v[i]
                v[i] = temp
                i = i - 1
                if (i == 0) {
                    i = 1
                }
            }
        }
    }


    // Alg2
    fun ordena2(v: IntArray, tam: Int) {
        var k: Int
        val n = tam
        k = n / 2
        while (k >= 1) {
            f(v, k, n)
            k--
        }
        k = n
        while (k > 1) {
            g(v, 1, k--)
            f(v, 1, k)
        }
    }

    private fun f(v: IntArray, k: Int, n: Int) {
        var k = k
        while (2 * k <= n) {
            var j = 2 * k
            if ((j < n) && (v[j - 1] < v[j])) {
                j++
            }
            if (v[k - 1] >= v[j - 1]) {
                break
            }
            g(v, k, j)
            k = j
        }
    }

    private fun g(v: IntArray, i: Int, j: Int) {
        val temp = v[i - 1]
        v[i - 1] = v[j - 1]
        v[j - 1] = temp
    }


    // Alg3
    fun ordena3(v: IntArray, tam: Int) {
        val m = h(v, tam)
        val c = IntArray(m + 1)
        val w = IntArray(tam)
        for (i in 0 until tam) {
            c[v[i]] = c[v[i]] + 1
        }
        for (i in 1 until m + 1) {
            c[i] = c[i] + c[i - 1]
        }
        for (i in tam - 1 downTo 0) {
            w[c[v[i]] - 1] = v[i]
            c[v[i]] = c[v[i]] - 1
        }
        for (i in 0 until tam) {
            v[i] = w[i]
        }
    }

    private fun h(v: IntArray, tam: Int): Int {
        var m = v[0]
        var i = 1
        while (i < tam) {
            if (v[i] > m) {
                m = v[i]
            }
            i++
        }
        return m
    }


    //Fisher Yates
    fun aleatorio(v: IntArray, tam: Int) {
        var i = 0
        while (i < tam) {
            v[i] = i
            i++
        }
        i = tam - 1
        while (i > 0) {
            // random.nextInt(k) devuelve un entero pseudoaleatorio distribuido
// uniformemente entre 0 (incluido) y el valor especificado k (excluido)
            val j = random.nextInt(i + 1)
            val temp = v[i]
            v[i] = v[j]
            v[j] = temp
            i--
        }
    }
}

