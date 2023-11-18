package com.example.vinyls_jetpack_application.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "collectors_table")
data class Collector(
    @PrimaryKey  val id: Int,
    val collectorAlbums: List<CollectorAlbum>,
    val comments: List<Comment>,
    val email: String,
    val favoritePerformers: List<Performer>,
    val name: String,
    val telephone: String
)