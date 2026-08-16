package com.example.desafiopracticodsm

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat
import com.example.desafiodsm.R

class CalculadoraActivity : AppCompatActivity() {

    private lateinit var edtNumero1: EditText
    private lateinit var edtNumero2: EditText
    private lateinit var txtResultado: TextView

    private val formato = DecimalFormat("0.########")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculadora)

        edtNumero1 = findViewById(R.id.edtNumero1)
        edtNumero2 = findViewById(R.id.edtNumero2)
        txtResultado = findViewById(R.id.txtResultadoCalculadora)

        val btnSuma = findViewById<Button>(R.id.btnSuma)
        val btnResta = findViewById<Button>(R.id.btnResta)
        val btnMultiplicacion = findViewById<Button>(R.id.btnMultiplicacion)
        val btnDivision = findViewById<Button>(R.id.btnDivision)
        val btnExponente = findViewById<Button>(R.id.btnExponente)
        val btnRaiz = findViewById<Button>(R.id.btnRaiz)
        val btnHistorial = findViewById<Button>(R.id.btnHistorial)
        val btnRegresar = findViewById<Button>(R.id.btnRegresarCalculadora)

        btnSuma.setOnClickListener {
            realizarOperacion("+")
        }

        btnResta.setOnClickListener {
            realizarOperacion("-")
        }

        btnMultiplicacion.setOnClickListener {
            realizarOperacion("*")
        }

        btnDivision.setOnClickListener {
            realizarOperacion("/")
        }

        btnExponente.setOnClickListener {
            realizarOperacion("^")
        }

        btnRaiz.setOnClickListener {
            calcularRaiz()
        }

        btnHistorial.setOnClickListener {
            mostrarHistorial()
        }

        btnRegresar.setOnClickListener {
            finish()
        }
    }

    private fun obtenerNumero1(): Double? {

        val texto = edtNumero1.text.toString().trim()

        if (texto.isEmpty()) {
            edtNumero1.error = getString(R.string.error_numero)
            edtNumero1.requestFocus()
            return null
        }

        val numero = texto.toDoubleOrNull()

        if (numero == null) {
            edtNumero1.error = getString(R.string.error_numero)
            edtNumero1.requestFocus()
            return null
        }

        return numero
    }

    private fun obtenerNumero2(): Double? {

        val texto = edtNumero2.text.toString().trim()

        if (texto.isEmpty()) {
            edtNumero2.error = getString(R.string.error_numero)
            edtNumero2.requestFocus()
            return null
        }

        val numero = texto.toDoubleOrNull()

        if (numero == null) {
            edtNumero2.error = getString(R.string.error_numero)
            edtNumero2.requestFocus()
            return null
        }

        return numero
    }

    private fun realizarOperacion(operacion: String) {

        val numero1 = obtenerNumero1() ?: return
        val numero2 = obtenerNumero2() ?: return

        if (operacion == "/" && numero2 == 0.0) {

            edtNumero2.error =
                getString(R.string.error_division_cero)

            edtNumero2.requestFocus()

            Toast.makeText(
                this,
                getString(R.string.error_division_cero),
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val resultado = when (operacion) {

            "+" -> numero1 + numero2

            "-" -> numero1 - numero2

            "*" -> numero1 * numero2

            "/" -> numero1 / numero2

            "^" -> Math.pow(numero1, numero2)

            else -> return
        }

        mostrarResultado(resultado)

        val operacionTexto =
            "${formato.format(numero1)} $operacion ${formato.format(numero2)}"

        guardarHistorial(
            operacionTexto,
            resultado
        )
    }

    private fun calcularRaiz() {

        val numero = obtenerNumero1() ?: return

        if (numero < 0) {

            edtNumero1.error =
                getString(R.string.error_raiz_negativa)

            edtNumero1.requestFocus()

            Toast.makeText(
                this,
                getString(R.string.error_raiz_negativa),
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val resultado = Math.sqrt(numero)

        mostrarResultado(resultado)

        val operacionTexto =
            "√${formato.format(numero)}"

        guardarHistorial(
            operacionTexto,
            resultado
        )
    }

    private fun mostrarResultado(resultado: Double) {

        txtResultado.text = getString(
            R.string.resultado,
            formato.format(resultado)
        )
    }

    private fun guardarHistorial(
        operacion: String,
        resultado: Double
    ) {

        val texto = getString(
            R.string.operacion,
            operacion,
            formato.format(resultado)
        )

        openFileOutput(
            "historial.txt",
            MODE_APPEND
        ).use { archivo ->

            archivo.write(
                "$texto\n".toByteArray()
            )
        }
    }

    private fun obtenerHistorial(): String {

        return try {

            openFileInput("historial.txt")
                .bufferedReader()
                .use {
                    it.readText()
                }

        } catch (e: Exception) {

            ""
        }
    }

    private fun mostrarHistorial() {

        val historial = obtenerHistorial()

        if (historial.isEmpty()) {

            AlertDialog.Builder(this)
                .setTitle(getString(R.string.ver_historial))
                .setMessage(getString(R.string.historial_vacio))
                .setPositiveButton("OK", null)
                .show()

            return
        }

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.ver_historial))
            .setMessage(historial)
            .setPositiveButton("OK", null)
            .show()
    }
}