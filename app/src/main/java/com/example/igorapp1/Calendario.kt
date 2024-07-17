package com.example.igorapp1

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Calendario : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    // Declarar variables para email, name y apode
    private var email: String? = null
    private var name: String? = null
    private var apode: String? = null

    private lateinit var EditTextDia: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendario)

        // Inicializar variables con los valores del intent
        email = intent.getStringExtra("EMAIL_EXTRA")
        name = intent.getStringExtra("NAME_EXTRA") ?: "Usuario"
        apode = intent.getStringExtra("APODE_EXTRA")

        EditTextDia = findViewById(R.id.editTextDia)

        val textViewSaludo = findViewById<TextView>(R.id.salEmotion)
        textViewSaludo.text = "¡Hola $name!"

        val igorButton: ImageView = findViewById(R.id.igor)
        igorButton.setOnClickListener {
            val intent = Intent(this, Menu::class.java)
            intent.putExtra("EMAIL_EXTRA", email)
            intent.putExtra("NAME_EXTRA", name)
            intent.putExtra("APODE_EXTRA", apode)
            startActivity(intent)
        }

        val calButton: Button = findViewById(R.id.calcu)
        calButton.setOnClickListener {

        }

        setupDatePicker()
    }

    private fun setupDatePicker() {
        EditTextDia.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                EditTextDia.setText(dateFormat.format(selectedDate.time))
            }, year, month, day)

            datePickerDialog.show()
        }
    }
}