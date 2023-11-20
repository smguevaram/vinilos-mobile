package com.example.vinyls_jetpack_application.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "collectors_table")
data class Collector(
    @PrimaryKey  val id: Int,
    val email: String,
    val name: String,
    val telephone: String
)