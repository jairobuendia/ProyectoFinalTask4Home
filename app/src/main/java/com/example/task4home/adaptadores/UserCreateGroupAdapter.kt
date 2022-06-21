package com.example.task4home.adaptadores

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.task4home.R
import com.example.task4home.databinding.UsergroupViewBinding
import com.example.task4home.entidades.User
import com.google.android.material.switchmaterial.SwitchMaterial

class UserCreateGroupAdapter(private val userList: ArrayList<User>, private val context: Context, private val currentUserEmail: String):
    RecyclerView.Adapter<UserCreateGroupAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.usergroup_view,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserCreateGroupAdapter.ViewHolder, position: Int) {
        val user = userList[position]

        holder.name.text = user.fullname
        holder.username.text = user.username
        holder.switch.setOnClickListener {
            if (holder.switch.isChecked) {
                user.onGroup = true
            } else {
                user.onGroup = false
            }
            //Log.i("test-user", "${user.onGroup}")

        }
        if (currentUserEmail == user.email){
            holder.switch.visibility = View.GONE
        }

        Glide.with(context)
            .load(user.imgUrl)
            .skipMemoryCache(true)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return  userList.size;
    }


    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        var binding = UsergroupViewBinding.bind(view)

        val name: TextView = binding.userName
        val username : TextView = binding.userNickname
        val switch : SwitchMaterial = binding.switchAdd
        val imageView : ImageView = binding.imageView


    }

}
