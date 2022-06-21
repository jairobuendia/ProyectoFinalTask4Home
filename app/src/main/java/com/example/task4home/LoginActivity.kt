package com.example.task4home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.BoringLayout
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.task4home.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase




class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth


    private val respuestaRegistro = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == RESULT_OK){
            Toast.makeText(this, getString(R.string.register_correctly), Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, getString(R.string.singup_failed), Toast.LENGTH_LONG).show()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        auth = Firebase.auth
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.buttonLogin.setOnClickListener {Login(it)}

        binding.buttonRegister.setOnClickListener {Register()}



        storedUser()


    }

    fun Login(view: View){
        val intencion = Intent(this, HomeActivity::class.java)
        var email = binding.emailText.text.toString().trim()
        var password = binding.passwordText.text.toString().trim()
        if (email != "" && password != "") {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        saveUserLogin(email)
                        intencion.putExtra("email", "${email}")
                        startActivity(intencion)
                    } else {
                        Snackbar.make(
                            view,
                            getString(R.string.wrong_email_password),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }

                }
        } else {
            Snackbar.make(
                view,
                getString(R.string.wrong_email_password),
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    fun Register(){
        val intencion = Intent(this, RegisterActivity::class.java)
        respuestaRegistro.launch(intencion)
    }


    override fun onBackPressed() {}


    private fun saveUserLogin(email: String){
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.apply()
    }

    private fun storedUser(){
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)

        if (email != null){
            val intencion = Intent(this, HomeActivity::class.java)
            intencion.putExtra("email", "${email}")
            startActivity(intencion)

        }
    }
}