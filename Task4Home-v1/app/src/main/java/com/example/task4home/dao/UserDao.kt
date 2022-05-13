package com.example.task4home.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.task4home.entidades.User

@Dao
interface UserDao {
    @Query("SELECT * FROM USER")
    fun getAllUser() : MutableList<User>

    @Query("SELECT * FROM USER WHERE Email = :email")
    fun getUserByEmail(email:String):User

    @Insert
    fun insertUsers(user: User)


}