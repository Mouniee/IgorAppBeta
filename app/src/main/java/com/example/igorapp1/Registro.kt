package com.example.igorapp1

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*
import android.app.DatePickerDialog
import android.text.TextWatcher
import android.text.Editable
import com.google.firebase.firestore.FirebaseFirestore

class Registro : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    private lateinit var registroButton: Button
    private lateinit var usernameEditText: EditText
    private lateinit var fnacimientoEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var password2EditText: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        registroButton = findViewById(R.id.button_accepted)
        usernameEditText = findViewById(R.id.edit_text_username)
        fnacimientoEditText = findViewById(R.id.edit_text_fnacimiento)
        emailEditText = findViewById(R.id.edit_text_mail)
        passwordEditText = findViewById(R.id.edit_text_password)
        password2EditText = findViewById(R.id.edit_text_password2)

        val volverLogin: TextView = findViewById(R.id.volverLogin)
        volverLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        setup()
        setupDatePicker()
    }

    private fun setup() {
        registroButton.setOnClickListener {
            when {
                areFieldsEmpty() -> {
                    Toast.makeText(this, "Faltan campos por llenar", Toast.LENGTH_SHORT).show()
                }
                !arePasswordsEqual() -> {
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        emailEditText.text.toString(),
                        passwordEditText.text.toString()
                    ).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val email = emailEditText.text.toString()
                            val nombre = usernameEditText.text.toString()
                            val fnaci = fnacimientoEditText.text.toString()

                            db.collection("usuarios").document(email).set(
                                hashMapOf("nombre" to nombre,
                                          "fnaci" to fnaci
                                    )
                            )

                            val datosApoderado = hashMapOf(
                                "nombre" to nombre,
                                "apoderado" to true
                            )

                            val datosNino = hashMapOf(
                                "nombre" to "Niño/a",
                                "apoderado" to false
                            )

                            db.collection("perfiles").document(email)
                                .set(
                                    mapOf(
                                        "apoderado" to datosApoderado,
                                        "nino" to datosNino
                                    )
                                )

                            Toast.makeText(this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show()
                            showAlert("Usuario registrado con éxito") {

                                // Navegar a MainActivity
                                val intent = Intent(this@Registro, MainActivity::class.java)
                                startActivity(intent)
                                finish() // Finalizar la actividad actual si no se desea volver a ella
                            }
                        } else {
                            val errorMessage = when (task.exception?.message) {
                                "The email address is badly formatted." -> "El correo electrónico es inválido."
                                "The given password is invalid. [ Password should be at least 6 characters ]" -> "La contraseña es demasiado débil." +
                                        " Esta tiene que contener una mayucula y al menos 6 caracteres"
                                "The email address is already in use by another account." -> "El correo electrónico ya está en uso."
                                else -> "Error al registrar el usuario: ${task.exception?.message}"
                            }
                            showAlert(errorMessage)
                        }
                    }
                }
            }
        }
    }

    private fun areFieldsEmpty(): Boolean {
        return usernameEditText.text.isEmpty() ||
                fnacimientoEditText.text.isEmpty() ||
                emailEditText.text.isEmpty() ||
                passwordEditText.text.isEmpty() ||
                password2EditText.text.isEmpty()
    }

    private fun arePasswordsEqual(): Boolean {
        return passwordEditText.text.toString() == password2EditText.text.toString()
    }

    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlert(message: String, onDismiss: () -> Unit = {}) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Éxito")
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar") { dialog, _ ->
            dialog.dismiss()
            onDismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun setupDatePicker() {
        fnacimientoEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                fnacimientoEditText.setText(dateFormat.format(selectedDate.time))
            }, year, month, day)

            datePickerDialog.show()
        }
    }

    private fun setupTextWatcher() {
        fnacimientoEditText.addTextChangedListener(object : TextWatcher {
            private var current = ""
            private val dateFormat = "ddMMyyyy"
            private val cal = Calendar.getInstance()

            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString() != current) {
                    val userInput = s.toString().replace(Regex("[^\\d.]"), "")
                    if (userInput.length == 8) {
                        val day = userInput.substring(0, 2)
                        val month = userInput.substring(2, 4)
                        val year = userInput.substring(4, 8)

                        val date = day + month + year
                        try {
                            cal.time = SimpleDateFormat(dateFormat, Locale.getDefault()).parse(date)!!
                        } catch (e: Exception) {
                            fnacimientoEditText.error = "Fecha incorrecta"
                        }

                        val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(cal.time)
                        current = formattedDate
                        fnacimientoEditText.setText(formattedDate)
                        fnacimientoEditText.setSelection(formattedDate.length)
                    }
                }
            }
        })
    }
}


