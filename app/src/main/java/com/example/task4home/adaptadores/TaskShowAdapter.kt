package com.example.task4home.adaptadores

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.task4home.EditTaskActivity
import com.example.task4home.HomeActivity
import com.example.task4home.R
import com.example.task4home.databinding.TaskViewBinding
import com.example.task4home.entidades.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore

class TaskShowAdapter(private val taskList: ArrayList<Task>, private val context: Context) :
    RecyclerView.Adapter<TaskShowAdapter.ViewHolder>() {



    private lateinit var db: FirebaseFirestore
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.task_view, parent, false)
        return ViewHolder(view)

    }


    override fun onBindViewHolder(holder: TaskShowAdapter.ViewHolder, position: Int) {
        //Log.i("TEST", "${position}")

        val task = taskList[position]
        val stringPoints = context.getString(R.string.points_text)
        val stringPoint = context.getString(R.string.task_point)
        db = FirebaseFirestore.getInstance()
        holder.setIsRecyclable(false)



        holder.name.text = task.name

        holder.stars.rating = task.stars!!.toFloat()

        if (task.points == "1") {
            holder.points.text = task.points + " ${stringPoint}"
        } else {
            holder.points.text = task.points + " ${stringPoints}"
        }



//        if (task.stars == 1) {
//            holder.stars.text = task.stars.toString() + " ${stringStar}"
//        } else {
//            holder.stars.text = task.stars.toString() + " ${stringStars}"
//        }

        holder.itemView.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(view: View?): Boolean {

                val showMenu = PopupMenu(context, holder.binding.tareaTitulo)
                showMenu.inflate(R.menu.tareas_context_menu)
                showMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.taskEditMenu -> {
                            val intencion = Intent(view!!.context, EditTaskActivity::class.java)
                            intencion.putExtra("taskId", task.taskId)
                            intencion.putExtra("name", task.name)
                            intencion.putExtra("points", task.points)
                            intencion.putExtra("stars" , task.stars)
                            intencion.putExtra("groupId", task.groupId)
                            intencion.putExtra("position", position)
                            context.startActivity(intencion)
                        }
                        R.id.taskDeleteMenu -> {
                            db.collection("tasks").document(task.taskId).delete()
                            taskList.remove(task)
                            notifyItemRemoved(position)
                            notifyDataSetChanged()

                            Snackbar.make(
                                holder.binding.root,
                                context.getString(R.string.deleteTask),
                                Snackbar.LENGTH_SHORT).show()
                        }
                    }
                    return@setOnMenuItemClickListener true
                }
                showMenu.show()
                return true
            }
        })

    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding = TaskViewBinding.bind(view)

        val name: TextView = binding.tareaTitulo
        val stars: RatingBar = binding.ratingBar
        val points: TextView = binding.tareaPuntos
    }
}