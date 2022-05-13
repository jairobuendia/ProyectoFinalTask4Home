package com.example.task4home.entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Prize(@PrimaryKey (autoGenerate = true) val id: Int,
                 @ColumnInfo val name : String,
                 @ColumnInfo val points : Int,
                 @ColumnInfo val image : String? = null

                 )
