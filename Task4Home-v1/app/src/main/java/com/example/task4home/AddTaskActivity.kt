package com.example.task4home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.task4home.databinding.ActivityAddTaskBinding
import com.example.task4home.entidades.Task

class AddTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTaskBinding
    private var db:AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = AppDatabase.getAppDataBase(this)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var userID = intent.extras?.get("idUser")
//        var edit = false

        //Comprobamos si es una tarea editada
//        if (taskFiltrerID != null){
//            Log.i("xxx", "Tarea: ${taskFiltrerID} Usuario: ${userID}")
//            binding.textView4.text = "Edit Task"
//            edit = true
//
//        }


        binding.botonTareaCancelar.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

        binding.botonTareaGuardar.setOnClickListener {
            var name = binding.nameTaskText.editableText.toString().trim()
            var points =  binding.pointsTaskText.text.toString().trim().toInt()
            var stars = binding.starsTaskText.text.toString().trim().toInt()

                //CREADO
                if (stars >= 1 && stars <= 5) {
                    db?.taskDao()?.insertTask(
                        Task(0, "${userID}", "${name}", points, stars )
                    )
                    setResult(RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(this, "Error, Stars only accepts numbers (1-5), try again!", Toast.LENGTH_LONG).show()
                }
            }

        }
    override fun onBackPressed() {}
    }
