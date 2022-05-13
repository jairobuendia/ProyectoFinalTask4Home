package com.example.task4home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.task4home.databinding.ActivityRegisterBinding
import com.example.task4home.entidades.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private lateinit var binding: ActivityRegisterBinding
private lateinit var auth: FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private var db:AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = AppDatabase.getAppDataBase(this)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonR.setOnClickListener { Registro(it) }
    }

    fun Registro(view: View) {
        var email = binding.emailRText.text.toString().trim()
        var password = binding.passwordRText.text.toString().trim()
        var name = binding.nameRText.text.toString().trim()
        var username = binding.usernameRText.text.toString().trim()

        auth = Firebase.auth
        if (email != "" && password != "") {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    setResult(RESULT_OK)
                    db?.userDao()?.insertUsers(
                        User(0, "${name}", "${email}", "${username}")
                    )
                    finish()
                } else {
                    Snackbar.make(view, "Failed to register, this email is already used!", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }


}
