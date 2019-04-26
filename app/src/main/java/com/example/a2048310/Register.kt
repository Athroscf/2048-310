package com.example.a2048310

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
//import android.util.Patterns
import android.view.View
//import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.etPasswordRegister
//import java.util.regex.Pattern

class Register : AppCompatActivity() {

//    private val password = Pattern.compile(".{4}")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btnRegister.setOnClickListener {
            progressBar2.visibility = View.VISIBLE
            registrarUsuario()
        }

        tvSignIn.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

    }

    private fun registrarUsuario() {
        val email = etEmailRegister.text.toString()
        val password = etPasswordRegister.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            progressBar2.visibility = View.INVISIBLE
        } else {

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    progressBar2.visibility = View.INVISIBLE
                    if (!it.isSuccessful) return@addOnCompleteListener
                    else {
                        Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
                .addOnFailureListener {
                    progressBar2.visibility = View.INVISIBLE
                    Log.d("Failure", "Failed to create user: ${it.message}")
                    Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show()
                }
        }
    }

//    fun emailValidation(valid: String, tv: TextView): Boolean {
//        return if (!Patterns.EMAIL_ADDRESS.matcher(valid).matches()) {
//            tv.visibility = View.VISIBLE
//            false
//        } else
//            true
//    }
//
//    fun passwordValidation(valid: String, tv: TextView): Boolean {
//        return if (!password.matcher(valid).matches()) {
//            tv.text = R.string.en_minPassword.toString()
//            tv.visibility = View.VISIBLE
//            false
//        } else
//            true
//    }
}
