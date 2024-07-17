package com.example.igorapp1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.Timestamp
import java.util.*

class Emotion : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    // Declarar variables para email, name y apode
    private var email: String? = null
    private var name: String? = null
    private var apode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emotion)

        // Inicializar variables con los valores del intent
        email = intent.getStringExtra("EMAIL_EXTRA")
        name = intent.getStringExtra("NAME_EXTRA") ?: "Usuario"
        apode = intent.getStringExtra("APODE_EXTRA")

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
    }

    fun mostrarMensaje(view: View) {
        val tag = view.tag as String // Obtener el tag como String

        // Convertir el tag a Integer si es necesario
        val emocionIndex = tag.toIntOrNull() ?: -1 // Convertir a Integer o asignar -1 si falla

        // Verificar el índice obtenido
        when (emocionIndex) {
            0 -> mostrarConfirmacion("Feliz")
            1 -> mostrarConfirmacion("Normal")
            2 -> mostrarConfirmacion("Frustrado")
            3 -> mostrarConfirmacion("Triste")
            4 -> mostrarConfirmacion("Cansado")
            else -> {
                // Acción por defecto en caso de valor no reconocido
                Toast.makeText(this, "Emoción no reconocida", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun mostrarConfirmacion(emocion: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("¿Estás seguro de anotar que estás $emocion?")
            .setPositiveButton("Sí") { dialog, id ->
                // Asegurarse de que email y name no son null antes de guardar en Firestore
                val emailNonNull = email ?: return@setPositiveButton
                val nameNonNull = name ?: return@setPositiveButton

                // Guardar en una subcolección "emociones"
                val emocionData = hashMapOf(
                    "nombre" to nameNonNull,
                    "emocion" to emocion,
                    "timestamp" to Timestamp.now() // Guardar la fecha y hora actuales
                )

                // Crear una nueva entrada en la subcolección "emociones"
                db.collection("usuarios").document(emailNonNull)
                    .collection("emociones")
                    .add(emocionData)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Confirmado: Estás $emocion", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error al registrar emoción: $e", Toast.LENGTH_SHORT).show()
                    }
                // Aquí puedes guardar la emoción en un arreglo o hacer cualquier otra acción
            }
            .setNegativeButton("Cancelar") { dialog, id ->
                // Acción a realizar si el usuario dice "Cancelar"
                dialog.dismiss()  // Cerrar el diálogo sin hacer nada
            }
        builder.create().show()
    }
}
