package com.example.task4home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.task4home.databinding.ActivityHomeBinding
import com.example.task4home.entidades.Prize
import com.example.task4home.entidades.Task
import com.example.task4home.entidades.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth: FirebaseAuth
    private var db:AppDatabase? = null
    private lateinit var adaptador : TareaAdaptador
    lateinit var datos: MutableList<Task>


    private val respuestaAddTarea = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == RESULT_OK){
            Toast.makeText(this, "Task created successfully, logout for see new tasks", Toast.LENGTH_LONG).show()
//            adaptador.notifyDataSetChanged()
        } else {
            Toast.makeText(this, "Error, the task has not been created", Toast.LENGTH_LONG).show()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        db = AppDatabase.getAppDataBase(this)

        var email = intent.extras?.get("email")
        var usuarioLogueado = db?.userDao()?.getUserByEmail(email as String)
        var usuarioLogueadoString = usuarioLogueado?.id.toString().trim()
        Log.i("xxx", "TESTING")
//        var recibirTareasUsuario = db?.taskDao()?.getTaskOfUser(usuarioLogueadoString)

        val datos = db!!.taskDao().getTaskOfUser(usuarioLogueadoString)


        //Definimos la funcion pulsacion larga
        val gestionarPulsacionLarga: (MenuItem,Task) -> Boolean = { item,task ->
            when(item.itemId){
                R.id.taskEditMenu -> {
                    var intencion = Intent(this, AddTaskActivity::class.java)
                    intencion.putExtra("taskEdit", "${task.id}")
                    startActivity(intencion)
                    true}
                R.id.taskDeleteMenu -> {
                    db?.taskDao()?.delete(task)
                    Toast.makeText(this, "Delete correctly Task: ${task.name}!!", Toast.LENGTH_LONG).show()
                    val deleteTask = datos.indexOfFirst { it.equals(task) }
                    datos.removeAt(deleteTask)
                    adaptador.notifyItemRemoved(deleteTask)
                    true}
            }
            false
        }

        //Mi forma
        adaptador = TareaAdaptador(datos, gestionarPulsacionLarga)
        binding.recyclerView.adapter = adaptador
        binding.recyclerView.setHasFixedSize(true)

        //startDataBase()

        binding.userHome.setText(usuarioLogueado?.username.toString().trim())

        binding.botonAddTarea.setOnClickListener { goCreateTask(usuarioLogueadoString) }

        binding.imageButton.setOnClickListener { goUsers() }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intencion = Intent(this, LoginActivity::class.java)
        return when (item.itemId) {
            R.id.log_out -> { auth.signOut(); startActivity(intencion);true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {}

    fun goCreateTask(idUsuario: String){
        val intencion = Intent(this, AddTaskActivity::class.java)
        intencion.putExtra("idUser",idUsuario )
        respuestaAddTarea.launch(intencion)
    }

    fun goUsers(){
        val intencion = Intent(this, UserActivity::class.java)
        startActivity(intencion)

    }

    fun startDataBase(){
//        db?.userDao()?.insertUsers(
//            User(1, "Jairo", "jairo@gmail.com", "ExoKone")
//        )
        db?.taskDao()?.insertTask(
            Task(1, "1", "Limpiar el salón", 200, 3)
        )
//        db?.prizeDao()?.insertPrize(
//            Prize(1, "20€ Paysafecard", 2000)
//        )
    }
}