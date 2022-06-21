package com.example.task4home.adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.task4home.R
import com.example.task4home.databinding.UserViewBinding
import com.example.task4home.entidades.User

class UserShowAdapter(private val userList: ArrayList<User>, private val context: Context): RecyclerView.Adapter<UserShowAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.user_view,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserShowAdapter.ViewHolder, position: Int) {
        val user = userList[position]

        holder.name.text = user.fullname
        holder.username.text = user.username

        Glide.with(context)
            .load(user.imgUrl)
            .skipMemoryCache(true)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        var binding = UserViewBinding.bind(view)

        val name: TextView = binding.completeName
        val username : TextView = binding.nicknameUser
        val imageView : ImageView = binding.imagenUser


    }

}