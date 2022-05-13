package com.example.task4home.entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class User(@PrimaryKey (autoGenerate = true) val id: Int,
                @ColumnInfo val name: String,
                @ColumnInfo val email: String,
                @ColumnInfo val username: String,
                @ColumnInfo val image: String? = null)

