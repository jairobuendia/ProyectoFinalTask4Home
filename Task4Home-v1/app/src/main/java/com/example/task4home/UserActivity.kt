package com.example.task4home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.task4home.databinding.ActivityUserBinding
import com.google.android.material.snackbar.Snackbar

class UserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserBinding
    private var db:AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = AppDatabase.getAppDataBase(this)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val datos = db!!.userDao().getAllUser()
        val adaptador = UserAdaptador (datos) {
            Snackbar.make(binding.root, "Datos del usuario -> [Id: ${it.id}, Email: ${it.email}, Name: ${it.name}, Username: ${it.username}]", Snackbar.LENGTH_LONG).show()
        }
        binding.recyclerViewUser.adapter = adaptador
        binding.recyclerViewUser.setHasFixedSize(true)

        binding.imageButton2.setOnClickListener { finish() }
    }

    override fun onBackPressed() {}
}