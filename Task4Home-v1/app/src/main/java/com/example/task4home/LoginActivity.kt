package com.example.task4home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.task4home.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase




class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth


    private val respuestaRegistro = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == RESULT_OK){
            Toast.makeText(this, "Register correctly, use your credentials to login", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Sign up failed", Toast.LENGTH_LONG).show()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.buttonLogin.setOnClickListener {Login()}

        binding.buttonRegister.setOnClickListener {Register()}


    }

    fun Login(){
        val intencion = Intent(this, HomeActivity::class.java)
        var email = binding.emailText.text.toString().trim()
        var password = binding.passwordText.text.toString().trim()
        if (email != "" && password != "") {

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        intencion.putExtra("email", "${email}")
                        startActivity(intencion)
                    } else {
                        Toast.makeText(
                            this,
                            "Email or password incorrect, try again!",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }
        } else {
            Toast.makeText(
                this,
                "Email or password incorrect, try again!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun Register(){
        val intencion = Intent(this, RegisterActivity::class.java)
        respuestaRegistro.launch(intencion)
    }

    override fun onBackPressed() {}
}