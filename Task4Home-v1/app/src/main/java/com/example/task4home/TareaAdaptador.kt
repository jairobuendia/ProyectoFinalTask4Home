package com.example.task4home

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.task4home.databinding.TaskViewBinding
import com.example.task4home.entidades.Task

class TareaAdaptador(var datos : MutableList<Task>, val gestionarPulsacionLarga:(MenuItem, Task) -> Boolean) : RecyclerView.Adapter<TareaAdaptador.TareaContenedor>() {

    override fun getItemCount(): Int = datos.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaContenedor {
        val inflador = LayoutInflater.from(parent.context)
        val binding = TaskViewBinding.inflate(inflador, parent, false)

        return TareaContenedor (binding)
    }

    override fun onBindViewHolder(holder: TareaContenedor, position: Int) {
        holder.bindTarea(datos[position])
    }


    inner class TareaContenedor (val binding: TaskViewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindTarea ( task: Task ){
            //Mostramos el titulo de la tarea
            binding.tareaTitulo.text = task.name

            //Mostramos las estrellas de la tarea
            binding.tareaEstrellas.text = "${task.stars.toString()} estrellas"

            //Mostramos los puntos de la tarea
            binding.tareaPuntos.text = "${task.points.toString()} puntos"

            //Definimos un listener largo
            binding.root.setOnLongClickListener {
                val pop = PopupMenu(binding.root.context, binding.tareaTitulo)
                pop.inflate(R.menu.tareas_context_menu)
                pop.setOnMenuItemClickListener { gestionarPulsacionLarga(it, task) }
                pop.show()

                true
            }
        }
    }
}