package com.example.igorapp1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class Mensaje : AppCompatActivity() {

    // Declarar variables para email, name y apode
    private var email: String? = null
    private var name: String? = null
    private var apode: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mensaje)

        // Inicializar variables con los valores del intent
        email = intent.getStringExtra("EMAIL_EXTRA")
        name = intent.getStringExtra("NAME_EXTRA") ?: "Usuario"
        apode = intent.getStringExtra("APODE_EXTRA")

        val textViewSaludo = findViewById<TextView>(R.id.receptor)
        textViewSaludo.text = "$name!"

        val igorButton: ImageView = findViewById(R.id.igor)
        igorButton.setOnClickListener {
            val intent = Intent(this, Menu::class.java)
            intent.putExtra("EMAIL_EXTRA", email)
            intent.putExtra("NAME_EXTRA", name)
            intent.putExtra("APODE_EXTRA", apode)
            startActivity(intent)
        }
    }
}