package com.example.a2048310

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_high_scores.*

abstract class HighScores : AppCompatActivity() {

//    lateinit var tvUser: TextView
//    lateinit var tvLevel: TextView
//    lateinit var tvPoints: TextView
//    lateinit var tvTime: TextView
//    lateinit var tvMoves: TextView

    private val scores: MutableList<HighScoresLoad> = mutableListOf()
    private lateinit var adapter: HighScoreAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_high_scores)

        val listOfScores=  findViewById<RecyclerView>(R.id.rvHighScores)
        val layoutManager = LinearLayoutManager(this)

        val ref = FirebaseDatabase.getInstance().getReference("scores").orderByChild("moves")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
//                Toast.makeText(this, "No carga nada", Toast.LENGTH_SHORT).show()
            }

            // Si se cargan los datos
            override fun onDataChange(p0: DataSnapshot) {
                // Limpiamos las listas donde se almacean los datos del RecyclerView
                scores.clear()
                // Realizamos un ciclo for para poder recorrer todos los subnodos o nodos hijos
                // que dieron como resultado según la referencia hecha al inicio (filtrado)
                for (h in p0.children) {
                    // En la siguiente variable almacenaremos el objeto que
                    // trae todos los datos de Firebase y después se agregan en la Lista Mutable creada anteriormente
                    val historial = h.getValue(HighScoresLoad::class.java)
                    if (historial != null) {
                        scores.add(historial)
                    }
                }
                // Llamos al adaptador para hacer el enlace entre la Base de Datos en Firebase y
                // el template de nuestro RecyclerView
//                adapter = HighScoreAdapter(scores, object : InterfaceHighScoresinterface {
//
//                })

                listOfScores.setHasFixedSize(true)
                listOfScores.layoutManager = layoutManager
                listOfScores.itemAnimator = DefaultItemAnimator()
                listOfScores.adapter = adapter
            }
        })
    }
}

//    private fun initScores() {
//        val scoreListener = object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//
//                dataSnapshot.children.mapNotNullTo(rvHighScores) { it.getValue<HighScoresLoad>(HighScores::class.java) }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                println("loadPost:onCancelled ${databaseError.toException()}")
//            }
//        }
//        firebaseData.child("scores").addListenerForSingleValueEvent(scoresListener)
//    }
//
//    private val menu: MutableList<HighScoresLoad> = mutableListOf()

