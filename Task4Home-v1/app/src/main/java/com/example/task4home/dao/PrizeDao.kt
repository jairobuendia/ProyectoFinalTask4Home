package com.example.task4home.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.task4home.entidades.Prize

@Dao
interface PrizeDao {
    @Query("SELECT * FROM PRIZE")
    fun getAllPrize(): MutableList<Prize>

    @Insert
    fun insertPrize(vararg prize: Prize)
}