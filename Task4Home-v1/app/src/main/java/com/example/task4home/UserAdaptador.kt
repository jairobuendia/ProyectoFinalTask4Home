package com.example.task4home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.task4home.databinding.UserViewBinding
import com.example.task4home.entidades.User

class UserAdaptador(var datos : MutableList<User>, val gestionarPulsacionCorta : (User) -> Unit) : RecyclerView.Adapter<UserAdaptador.UserContenedor>() {

    override fun getItemCount(): Int = datos.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserContenedor {
        val inflador = LayoutInflater.from(parent.context)
        val binding = UserViewBinding.inflate(inflador, parent, false)

        return UserContenedor (binding)
    }

    override fun onBindViewHolder(holder: UserContenedor, position: Int) {
        holder.bindUser(datos[position])
    }


    inner class UserContenedor (val binding: UserViewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindUser (user: User){
            //Mostramos el nombre del usuario
            binding.userName.text = user.name

            //Mostramos el nickname del usuario
            binding.userNickname.text = user.username

            //Escuchador
            binding.root.setOnClickListener { gestionarPulsacionCorta(user) }

        }
    }
}