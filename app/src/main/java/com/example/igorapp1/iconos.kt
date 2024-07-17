package com.example.igorapp1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.TextView
import android.widget.Button
import android.widget.ImageView

class iconos : AppCompatActivity() {

    private lateinit var adminPerfil: Button
    private lateinit var adultPerfil: ImageView
    private lateinit var ninoPerfil: ImageView
    private var nombreApoderado: String? = null
    private var nombreNino: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iconos)
        val email = intent.getStringExtra("EMAIL_EXTRA")

        // Inicialización de botones y vistas
        adminPerfil = findViewById(R.id.adminPerf)
        adultPerfil = findViewById(R.id.perfilAdult)
        ninoPerfil = findViewById(R.id.perfilNino)


        // Actualizar datos
        if (email != null) {
            // Obtener instancia de Firestore
            val db = FirebaseFirestore.getInstance()

            // Consultar los datos del niño y del apoderado
            db.collection("perfiles").document(email)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        nombreApoderado = document.getString("apoderado.nombre")
                        nombreNino = document.getString("nino.nombre")

                        val textViewApode = findViewById<TextView>(R.id.textViewApode)
                        textViewApode.text = nombreApoderado
                        val textViewPeque = findViewById<TextView>(R.id.textViewPeque)
                        textViewPeque.text = nombreNino
                    } else {
                        Toast.makeText(this, "No se encontraron datos para el email especificado", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error al obtener datos: $e", Toast.LENGTH_SHORT).show()
                }
        }

        // Configuración del botón para adminPerfil
        adminPerfil.setOnClickListener {
            val intent = Intent(this, Usuario::class.java)
            intent.putExtra("EMAIL_EXTRA", email)
            intent.putExtra("NAME_APODE", nombreApoderado)
            intent.putExtra("NAME_NINO", nombreNino)
            startActivity(intent)
        }

        // Configuración del botón para adultPerfil
        adultPerfil.setOnClickListener {
            val intent = Intent(this, Menu::class.java)
            intent.putExtra("EMAIL_EXTRA", email)
            intent.putExtra("NAME_EXTRA", nombreApoderado)
            intent.putExtra("NAME_RECEP", nombreNino)
            intent.putExtra("APODE_EXTRA", true)
            startActivity(intent)
        }

        // Configuración del botón para ninoPerfil
        ninoPerfil.setOnClickListener {
            val intent = Intent(this, Menu::class.java)
            intent.putExtra("EMAIL_EXTRA", email)
            intent.putExtra("NAME_EXTRA", nombreNino)
            intent.putExtra("NAME_RECEP", nombreApoderado)
            intent.putExtra("APODE_EXTRA", false)
            startActivity(intent)
        }
    }
}

