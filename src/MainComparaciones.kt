import java.io.FileWriter
import java.io.IOException
import java.util.*
import java.sql.Time
import kotlin.system.measureNanoTime


internal object MainComparaciones {

    @JvmStatic
    fun main(args: Array<String>) {
        val tamVector = 1000000
        val numRepeticiones = 20 // 10 Veces que se repite un cierto tamaño
        val numMediciones = 5 // 20 Cantidad de diferentes tamaños probados
        var vector: IntArray
        val mediciones = LongArray(numMediciones)
//        var tiempo: Long


        repeat(3) { it0 ->

            mediciones.fill(0) // Resetear las mediciones para el nuevo algoritmo

            for (i in 0..<numMediciones) {
                vector = IntArray(tamVector * (i + 1))
                print("${tamVector * (i + 1)}, ")

                repeat(numRepeticiones) {

                    aleatorio(vector, tamVector * (i + 1))

                    mediciones[i] += when (it0) {
//                        0 -> ordena1(vector, tamVector * (i + 1))
                        1 -> ordena2(vector, tamVector * (i + 1))
                        else -> ordena3(vector, tamVector * (i + 1))
                    }

                }

                mediciones[i] = mediciones[i] / numRepeticiones

            }
            println(mediciones.contentToString())
            println(numRepeticiones)
            escribirCSV(stringACSV(intAStringArray(mediciones)))
        }
    }

    //    }
    private fun escribirCSV(cad: String) {
        try {
            val myWriter = FileWriter("src/mediciones/comparaciones.csv", true)
            myWriter.write(cad + "\n")
            myWriter.close()
            println("Todo bn.")
        } catch (e: IOException) {
            println("ERROR CATASTRÓFICO.")
            e.printStackTrace()
        }
    }


    private fun intAStringArray(intArray: LongArray): Array<String?> {
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


    // Medir el tiempo java
    //import java.time.Duration;
    //import java.time.Instant;
    //...
    //Instant start = Instant.now();
    //// Código que queremos medir
    //Instant finish = Instant.now();
    //long timeElapsed = Duration.between(start, finish).toNanos();
    // A
    private val random: Random = Random()


    // Alg1
    private fun ordena1(v: IntArray, tam: Int): Long {
        var i: Int
        var j: Int
        var temp: Int
        var contadorComparaciones: Long = 0

        i = 1
        j = 2
        while (i < tam) {
            contadorComparaciones++
            if (v[i - 1] <= v[i]) {
                i = j
                j++
            } else {
                temp = v[i - 1]
                v[i - 1] = v[i]
                v[i] = temp
                i--
                if (i == 0) {
                    i = 1
                }
            }
        }
        return contadorComparaciones
    }


    // Alg2
    private fun ordena2(v: IntArray, tam: Int): Long {
        var k: Int
        val n = tam
        var contadorComparaciones: Long = 0

        k = n / 2
        while (k >= 1) {
            contadorComparaciones++
            contadorComparaciones += f(v, k, n) // Suma las asignaciones de f()
            k--
        }
        k = n
        while (k > 1) {
            g(v, 1, k--)
            contadorComparaciones += 0 // Suma las asignaciones de g() (Siempre son 3) (Comparaciones son 0)
            contadorComparaciones += f(v, 1, k) // Suma las asignaciones de f()
        }
        return contadorComparaciones
    }

    private fun f(v: IntArray, k: Int, n: Int): Int {
        var k = k
        var contadorComparaciones: Int = 0

        while (2 * k <= n) {
            var j = 2 * k
            contadorComparaciones++
            if ((j < n) && (v[j - 1] < v[j])) {
                j++
            }
            contadorComparaciones++
            if (v[k - 1] >= v[j - 1]) {
                break
            }
            g(v, k, j)
            k = j
        }
        return contadorComparaciones
    }

    private fun g(v: IntArray, i: Int, j: Int) { // 3 Asignaciones | 0 Comparaciones
        val temp = v[i - 1]
        v[i - 1] = v[j - 1]
        v[j - 1] = temp
    }


    // Alg3
    private fun ordena3(v: IntArray, tam: Int): Long {
        val vectorAuxiliar = h(v, tam) // m es igual al primer elemento del Array
        val m = vectorAuxiliar[0]
        var contadorComparaciones: Long = vectorAuxiliar[1].toLong() // El contador está en la segunda posición del Array
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
        return contadorComparaciones
    }

    private fun h(v: IntArray, tam: Int): IntArray { // TODO seguir con el contador de asignaciones
        var m = intArrayOf(v[0], 0) // Array para pasar el contadorAsignaciones como segundo valor
        var i = 1

        while (i < tam) {
            m[1]++ // Incrementar el contador de comparaciones
            if (v[i] > m[0]) {
                m[0] = v[i]
            }
            i++
        }
        return m
    }


    //Fisher Yates
    private fun aleatorio(v: IntArray, tam: Int) {
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