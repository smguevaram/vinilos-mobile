package com.example.vinyls_jetpack_application.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks_table")
data class Track (
    @PrimaryKey val id:Int,
    val name:String,
    val duration:String,
    val albumId: Int
)
