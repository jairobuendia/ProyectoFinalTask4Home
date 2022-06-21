package com.example.task4home

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.example.task4home.adaptadores.TaskShowAdapter
import com.example.task4home.databinding.FragmentTaskBinding
import com.example.task4home.entidades.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.createtask_dialog.*
import kotlinx.android.synthetic.main.createtask_dialog.view.*
import java.util.*
import kotlin.collections.ArrayList


class TaskFragment : Fragment() {

    private lateinit var db: FirebaseFirestore
    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var taskArrayList: ArrayList<Task>
    private lateinit var taskShowAdapter: TaskShowAdapter

    private lateinit var taskName: String
    private lateinit var taskPoints: String
    private var taskStars: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = FirebaseFirestore.getInstance()

        //Shared preferences para sacar el email
        val prefs =
            activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs?.getString("email", null)
        val groupID = prefs?.getString("group", "")

        auth = Firebase.auth

        taskArrayList = arrayListOf()

        taskShowAdapter = TaskShowAdapter(taskArrayList, activity as Context)
        binding.recyclerViewUserShow.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = taskShowAdapter
        }


        binding.menuButtonTasks.setOnClickListener {
            showPopup(binding.menuButtonTasks)
        }


        binding.floatingAddButton.setOnClickListener {
            //Log.i("TEST", "${email}")

            db.collection("users").document(email as String).get().addOnSuccessListener {
                if (it.get("groupAdmin") == true) {
                    val taskDialog: View =
                        layoutInflater.inflate(R.layout.createtask_dialog, binding.root, false)
                    taskDialog.removeDialog()
                    MaterialAlertDialogBuilder(context as Context, R.style.AlertDialogTheme)
                        .setView(taskDialog)
                        .setTitle(getString(R.string.create_Task))
                        .setIcon(R.drawable.ic_baseline_assignment_24)
                        .setNegativeButton(getString(R.string.cancelar_tarea)) { dialog, which ->
                        }
                        .setPositiveButton(getString(R.string.guardar_Tarea)) { dialog, which ->
                            taskName = taskDialog.taskNameText.text.toString().trim()
                            taskPoints = taskDialog.taskPointsText.text.toString().trim()

                            if (taskDialog.taskStarsText.text.toString().trim() != "") {
                                taskStars = taskDialog.taskStarsText.text.toString().toInt()
                            }

                            createTask(groupID)

                        }
                        .show()

                } else {
                    Snackbar.make(
                        view,
                        getString(R.string.onlyAdminCan),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }

        }
        calldb()
    }


    private fun calldb() {
        db = FirebaseFirestore.getInstance()

        val prefs =
            activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val groupID = prefs?.getString("group", "")

        db.collection("tasks").whereEqualTo("groupId", groupID).addSnapshotListener(object :
            EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.i("Carencias en firestore", error.message.toString())
                    return
                }
                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        var task = dc.document.toObject(Task::class.java)
                        task.taskId = dc.document.id
//                        Log.i("Test32", "${task.taskId}")
                        if (taskArrayList.indexOf(task) == -1) {
                            taskArrayList.add(task)
                            taskShowAdapter.notifyItemInserted(taskArrayList.size)
                        }
                    }
                    //SI SE EDITA UNA TAREA
                    if (dc.type == DocumentChange.Type.MODIFIED){
                        var newTask = dc.document.toObject(Task::class.java)
                        newTask.taskId = dc.document.id

                        var position : Int = 0
                        for (task in taskArrayList) {
                            if (task.taskId == dc.document.id) {
                                position = taskArrayList.indexOf(task)
//                                Log.i("TESTING" , "${taskArrayList} + ${position}")
                                taskArrayList[position] = newTask
//                                Log.i("TESTING2" , "${taskArrayList} + ${position}")
                                taskShowAdapter.notifyItemChanged(position)
                                taskShowAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
            }
        })

    }

    private fun showPopup(view: View) {
        val popup = PopupMenu(activity as Context, view)
        val inflater: MenuInflater = popup.menuInflater
        val prefs =
            activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
                ?.edit()
        inflater.inflate(R.menu.options_menu, popup.menu)
        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.log_out -> {
                    prefs?.clear()
                    prefs?.apply()
                    goLoginPage()
                    auth.signOut()
                    activity?.finish()
                }
            }
            true
        }
        popup.show()
    }

    fun goLoginPage() {
        val intent = Intent(context, LoginActivity::class.java)
        startActivity(intent)

    }

    private fun View.removeDialog() {
        this ?: return
        val parentView = parent as? ViewGroup ?: return
        parentView.removeView(this)
    }


    private fun createTask(groupId: String?) {
        var taskId = UUID.randomUUID().toString()
        if (taskStars >= 5) {
            taskStars = 5
        }
        if (taskName != "" && taskPoints != "" && taskStars != 0) {
            db.collection("tasks").document(taskId).set(
                hashMapOf(
                    "taskId" to taskId,
                    "name" to taskName,
                    "points" to taskPoints,
                    "stars" to taskStars,
                    "groupId" to groupId
                )
            ).addOnSuccessListener {
                Toast.makeText(context, getString(R.string.taskCreateSucces), Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            Toast.makeText(context, getString(R.string.taskCreateFail), Toast.LENGTH_LONG).show()
        }

    }

}