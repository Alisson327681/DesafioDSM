package com.example.desafiopracticodsm

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.VibratorManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat
import com.example.desafiodsm.R

class SalarioActivity : AppCompatActivity() {

    private lateinit var edtNombreEmpleado: EditText
    private lateinit var edtSalarioBase: EditText
    private lateinit var txtResultadoSalario: TextView

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_salario)

        edtNombreEmpleado = findViewById(R.id.edtNombreEmpleado)
        edtSalarioBase = findViewById(R.id.edtSalarioBase)
        txtResultadoSalario = findViewById(R.id.txtResultadoSalario)

        val btnCalcularSalario =
            findViewById<Button>(R.id.btnCalcularSalario)

        val btnRegresarSalario =
            findViewById<Button>(R.id.btnRegresarSalario)

        btnCalcularSalario.setOnClickListener {
            calcularSalario()
        }

        btnRegresarSalario.setOnClickListener {
            finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun calcularSalario() {

        val nombre = edtNombreEmpleado.text.toString().trim()
        val salarioTexto = edtSalarioBase.text.toString().trim()

        // Validar nombre
        if (nombre.isEmpty()) {
            edtNombreEmpleado.error =
                getString(R.string.error_nombre_empleado)

            edtNombreEmpleado.requestFocus()
            return
        }

        // Validar que el salario no esté vacío
        if (salarioTexto.isEmpty()) {
            mostrarErrorSalario()
            return
        }

        // Convertir salario a número
        val salarioBase = salarioTexto.toDoubleOrNull()

        if (salarioBase == null) {
            mostrarErrorSalario()
            return
        }

        // Validar que sea positivo
        if (salarioBase <= 0) {
            mostrarErrorSalario()
            return
        }

        // Calcular descuentos
        val afp = calcularAFP(salarioBase)
        val isss = calcularISSS(salarioBase)
        val renta = calcularRenta(salarioBase)

        // Calcular total de descuentos
        val totalDescuentos = afp + isss + renta

        // Calcular salario neto
        val salarioNeto = salarioBase - totalDescuentos

        // Formato de dos decimales
        val formato = DecimalFormat("0.00")

        // Mostrar resultados
        txtResultadoSalario.text = getString(
            R.string.resultado_salario,
            nombre,
            formato.format(salarioBase),
            formato.format(renta),
            formato.format(afp),
            formato.format(isss),
            formato.format(totalDescuentos),
            formato.format(salarioNeto)
        )

        // Diferenciar visualmente el resultado
        txtResultadoSalario.setTextColor(
            getColor(R.color.salario_neto)
        )
    }

    /*
     * AFP = 7.25%
     */
    private fun calcularAFP(salarioBase: Double): Double {

        return salarioBase * 0.0725
    }

    /*
     * ISSS = 3%
     */
    private fun calcularISSS(salarioBase: Double): Double {

        return salarioBase * 0.03
    }

    /*
     * Cálculo de Renta.
     *

     */
    private fun calcularRenta(salarioBase: Double): Double {

        return 0.0
    }

    /*
     * Muestra el error y hace vibrar el teléfono.
     */
    @RequiresApi(Build.VERSION_CODES.S)
    private fun mostrarErrorSalario() {

        edtSalarioBase.error =
            getString(R.string.error_salario_positivo)

        edtSalarioBase.requestFocus()

        vibrarTelefono()
    }

    /*
     * Vibración del dispositivo.
     */
    @RequiresApi(Build.VERSION_CODES.S)
    private fun vibrarTelefono() {

        val vibratorManager =
            getSystemService(Context.VIBRATOR_MANAGER_SERVICE)
                    as VibratorManager

        val vibrator = vibratorManager.defaultVibrator

        vibrator.vibrate(
            VibrationEffect.createOneShot(
                300,
                VibrationEffect.DEFAULT_AMPLITUDE
            )
        )
    }
}