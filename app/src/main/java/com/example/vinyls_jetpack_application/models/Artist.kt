package com.example.vinyls_jetpack_application.models

data class Artist (
    val id:Int,
    val name:String,
    val cover:String,
    val releaseDate:String,
    val description:String,
    val genre:String,
    val recordLabel:String,
    val tracks: List<Track>,
    var comments: List<Comment>,
)
