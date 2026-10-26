package com.example.sumcalculator

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.math.BigDecimal
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {

    private lateinit var editTextFirst: EditText
    private lateinit var editTextSecond: EditText
    private lateinit var textViewResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        editTextFirst = findViewById(R.id.editTextFirst)
        editTextSecond = findViewById(R.id.editTextSecond)
        textViewResult = findViewById(R.id.textViewResult)

        findViewById<Button>(R.id.buttonAdd).setOnClickListener { calculate('+') }
        findViewById<Button>(R.id.buttonSubtract).setOnClickListener { calculate('-') }
        findViewById<Button>(R.id.buttonMultiply).setOnClickListener { calculate('*') }
        findViewById<Button>(R.id.buttonDivide).setOnClickListener { calculate('/') }
        findViewById<Button>(R.id.buttonClear).setOnClickListener { clearAll() }
        findViewById<Button>(R.id.buttonSwap).setOnClickListener { swapValues() }
    }

    private fun readNumbers(): Pair<BigDecimal, BigDecimal>? {
        val first = editTextFirst.text.toString().toBigDecimalOrNull()
        val second = editTextSecond.text.toString().toBigDecimalOrNull()

        if (first == null || second == null) {
            Toast.makeText(this, R.string.error_invalid_input, Toast.LENGTH_SHORT).show()
            return null
        }
        return Pair(first, second)
    }

    private fun showResult(a: BigDecimal, op: String, b: BigDecimal, result: BigDecimal) {
        val textResult = result.stripTrailingZeros().toPlainString()
        textViewResult.text = getString(
            R.string.result_format,
            a.stripTrailingZeros().toPlainString(),
            op,
            b.stripTrailingZeros().toPlainString(),
            textResult
        )
    }

    private fun calculate(operation: Char) {
        val numbers = readNumbers() ?: return
        val (a, b) = numbers

        Log.d("SumCalculator", "Операция: $operation, a=$a, b=$b")

        when (operation) {
            '+' -> showResult(a, "+", b, a + b)
            '-' -> showResult(a, "-", b, a - b)
            '*' -> showResult(a, "×", b, a * b)
            '/' -> {
                if (b.signum() == 0) {
                    Toast.makeText(this, R.string.error_divide_by_zero, Toast.LENGTH_SHORT).show()
                    Log.d("SumCalculator", "Ошибка: деление на ноль")
                } else {
                    val result = a.divide(b, 10, RoundingMode.HALF_UP)
                    showResult(a, "÷", b, result)
                }
            }
        }
    }

    private fun clearAll() {
        editTextFirst.text.clear()
        editTextSecond.text.clear()
        textViewResult.setText(R.string.result_placeholder)
    }

    private fun swapValues() {
        val firstText = editTextFirst.text.toString()
        val secondText = editTextSecond.text.toString()
        editTextFirst.setText(secondText)
        editTextSecond.setText(firstText)
    }
}