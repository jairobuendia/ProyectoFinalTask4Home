package com.example.task4home.entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Task(@PrimaryKey (autoGenerate = true) val id : Int,
                @ColumnInfo val idUser: String,
                @ColumnInfo val name: String,
                @ColumnInfo val points: Int,
                @ColumnInfo val stars: Int,
                @ColumnInfo val finishDate: String? = null

                ):Serializable
