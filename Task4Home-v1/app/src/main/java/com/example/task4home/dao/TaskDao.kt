package com.example.task4home.dao

import androidx.room.*
import com.example.task4home.entidades.Task
import com.example.task4home.entidades.User

@Dao
interface TaskDao {
    @Query("SELECT * FROM Task")
    fun getAllTask(): MutableList<Task>

    @Insert
    fun insertTask(task: Task)

    @Query ("SELECT * FROM Task WHERE idUser = :userID")
    fun getTaskOfUser(userID: String) : MutableList<Task>

    @Delete
    fun delete(task: Task)

    @Update
    fun actualizarTarea(task: Task)


}