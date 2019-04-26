package com.example.a2048310

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.etPasswordRegister

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            login()
        }

        tvSignUp.setOnClickListener {
            finish()
        }

    }

    private fun login() {
        val email = etUsernameLogin.text.toString()
        val password = etPasswordRegister.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            progressBar.visibility = View.INVISIBLE
        } else {

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    progressBar.visibility = View.INVISIBLE
                    if (!it.isSuccessful) {
                        Toast.makeText(this, "Username/Email or Password are incorrect", Toast.LENGTH_SHORT).show()
                        return@addOnCompleteListener
                    } else {
                        progressBar.visibility = View.INVISIBLE
                        Toast.makeText(this, "Login Complete", Toast.LENGTH_SHORT).show()
                        Log.d("Login", "Login to user $email")
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
        }
    }
}
