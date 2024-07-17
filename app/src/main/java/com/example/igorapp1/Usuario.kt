package com.example.igorapp1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.Button
import android.widget.Toast
import android.widget.TextView

class Usuario : AppCompatActivity() {

    private lateinit var saveButtom: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usuario)

        val email = intent.getStringExtra("EMAIL_EXTRA")
        val apode = intent.getStringExtra("NAME_APODE")
        val nino = intent.getStringExtra("NAME_NINO")

        val textViewApode = findViewById<TextView>(R.id.textViewAdult)
        textViewApode.text = apode
        val textViewPeque = findViewById<TextView>(R.id.textViewKid)
        textViewPeque.text = nino

        saveButtom = findViewById(R.id.save)

        saveButtom.setOnClickListener {
            Toast.makeText(this, "Cambios guardados correctamente", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, iconos::class.java)
            intent.putExtra("EMAIL_EXTRA", email)
            startActivity(intent)
        }
    }
}