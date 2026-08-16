package com.example.desafiopracticodsm

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.desafiodsm.R
import java.text.DecimalFormat

/*me esta volviendo loca la carrera, el ciclo va a terminar conmigo*/
/*tu haces que yo pierda la cabeza, saquenme de aqui,:(*/
class PromedioActivity : AppCompatActivity() {

    private lateinit var edtNombre: EditText
    private lateinit var edtNota1: EditText
    private lateinit var edtNota2: EditText
    private lateinit var edtNota3: EditText
    private lateinit var edtNota4: EditText
    private lateinit var edtNota5: EditText
    private lateinit var txtResultado: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promedio)

        edtNombre = findViewById(R.id.edtNombre)
        edtNota1 = findViewById(R.id.edtNota1)
        edtNota2 = findViewById(R.id.edtNota2)
        edtNota3 = findViewById(R.id.edtNota3)
        edtNota4 = findViewById(R.id.edtNota4)
        edtNota5 = findViewById(R.id.edtNota5)

        txtResultado = findViewById(R.id.txtResultado)

        val btnCalcular = findViewById<Button>(R.id.btnCalcular)
        val btnRegresar = findViewById<Button>(R.id.btnRegresar)

        btnCalcular.setOnClickListener {
            calcularPromedio()
        }

        btnRegresar.setOnClickListener {
            finish()
        }
    }

    private fun calcularPromedio() {

        val nombre = edtNombre.text.toString().trim()

        if (nombre.isEmpty()) {
            edtNombre.error = getString(R.string.error_nombre)
            edtNombre.requestFocus()
            return
        }

        val notas = obtenerNotas()

        if (notas == null) {
            return
        }

        val ponderacion1 = 8.00
        val ponderacion2 = 0.20
        val ponderacion3 = 5.49
        val ponderacion4 = 6.20
        val ponderacion5 = 10.00

        val promedio = calcularPromedioPonderado(
            notas[0],
            notas[1],
            notas[2],
            notas[3],
            notas[4],
            ponderacion1,
            ponderacion2,
            ponderacion3,
            ponderacion4,
            ponderacion5
        )

        val formato = DecimalFormat("0.00")
        val promedioFormateado = formato.format(promedio)

        /*
         * Cambia 6.0 por la nota mínima de aprobación
         */
        val estado = if (promedio >= 7.0) {
            getString(R.string.aprobado)
        } else {
            getString(R.string.reprobado)
        }

        txtResultado.text = getString(
            R.string.resultado_promedio,
            promedioFormateado,
            estado
        )

        Toast.makeText(
            this,
            "$nombre: $promedioFormateado",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun obtenerNotas(): Array<Double>? {

        val campos = arrayOf(
            edtNota1,
            edtNota2,
            edtNota3,
            edtNota4,
            edtNota5
        )

        val notas = ArrayList<Double>()

        for (campo in campos) {

            val texto = campo.text.toString().trim()

            if (texto.isEmpty()) {
                campo.error = getString(R.string.error_nota)
                campo.requestFocus()
                return null
            }

            val nota = texto.toDoubleOrNull()

            if (nota == null) {
                campo.error = getString(R.string.error_nota)
                campo.requestFocus()
                return null
            }

            if (nota < 0 || nota > 10) {
                campo.error = getString(R.string.error_rango)
                campo.requestFocus()
                return null
            }

            notas.add(nota)
        }

        return notas.toTypedArray()
    }

    private fun calcularPromedioPonderado(
        nota1: Double,
        nota2: Double,
        nota3: Double,
        nota4: Double,
        nota5: Double,
        ponderacion1: Double,
        ponderacion2: Double,
        ponderacion3: Double,
        ponderacion4: Double,
        ponderacion5: Double
    ): Double {

        return (nota1 * ponderacion1) +
                (nota2 * ponderacion2) +
                (nota3 * ponderacion3) +
                (nota4 * ponderacion4) +
                (nota5 * ponderacion5)
    }
}