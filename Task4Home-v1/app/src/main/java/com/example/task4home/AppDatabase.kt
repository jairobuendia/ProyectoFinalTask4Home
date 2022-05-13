package com.example.task4home

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.task4home.dao.PrizeDao
import com.example.task4home.dao.TaskDao
import com.example.task4home.dao.UserDao
import com.example.task4home.entidades.Prize
import com.example.task4home.entidades.Task
import com.example.task4home.entidades.User
import java.security.AccessControlContext

@Database(entities = arrayOf(User::class, Task::class, Prize::class), version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao() : UserDao
    abstract fun taskDao() : TaskDao
    abstract fun prizeDao() : PrizeDao

    companion object {
        private var instancia: AppDatabase? = null

        @Synchronized
        fun getAppDataBase(context: Context): AppDatabase {
            return instancia?: synchronized(this){
                Room.databaseBuilder(context, AppDatabase::class.java, "task-manager-bd")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                    .also { instancia = it }
            }
        }
    }

}