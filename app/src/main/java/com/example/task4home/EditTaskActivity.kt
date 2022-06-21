package com.example.task4home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.task4home.databinding.ActivityEditTaskBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.createtask_dialog.view.*
import java.util.*

class EditTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditTaskBinding
    private lateinit var db: FirebaseFirestore

    private lateinit var taskId: String
    private lateinit var name: String
    private lateinit var points: String
    private lateinit var groupId: String
    private var stars: Int = 0
    private var starsedit: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = FirebaseFirestore.getInstance()

        taskId = intent.extras?.get("taskId").toString().trim()
        name = intent.extras?.get("name").toString().trim()
        points = intent.extras?.get("points").toString().trim()
        stars = intent.extras?.get("stars").toString().trim().toInt()
        groupId = intent.extras?.get("groupId").toString().trim()



        binding.nameEditTaskText.setText(name)
        binding.nameEditTaskStarsText.setText(stars.toString())
        binding.nameEditTaskPointsText.setText(points)

        binding.botonCancelarEditarTarea.setOnClickListener {
            finish()
        }

        binding.botonEditarTarea.setOnClickListener {
            updateTask(groupId, it)
        }
    }

    override fun onBackPressed() {
    }

    private fun updateTask(groupId: String?, view : View) {
        var nameedit = binding.nameEditTaskText.text.toString().trim()
        var pointsedit = binding.nameEditTaskPointsText.text.toString().trim()

        if (binding.nameEditTaskStarsText.text.toString().trim() != "") {
            starsedit = binding.nameEditTaskStarsText.text.toString().trim().toInt()
        }

        if (starsedit >= 5) {
            starsedit = 5
        }
        if (nameedit != "" && pointsedit != "" && starsedit != 0) {
            db.collection("tasks").document(taskId).set(
                hashMapOf(
                    "taskId" to taskId,
                    "name" to nameedit,
                    "points" to pointsedit,
                    "stars" to starsedit,
                    "groupId" to groupId
                )
            ).addOnSuccessListener {
                finish()
            }
        } else {
            Snackbar.make(view, "ERROR AL ACTUALIZAR", Snackbar.LENGTH_LONG).show()
        }

    }

}