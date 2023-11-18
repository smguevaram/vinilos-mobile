package com.example.vinyls_jetpack_application.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vinyls_jetpack_application.models.Album

@Dao
interface AlbumDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(albums: List<Album>)

    @Query("SELECT * FROM albums_table")
    fun getAllAlbums(): LiveData<List<Album>>

    @Query("SELECT * FROM albums_table WHERE id = :albumId")
    fun getAlbumById(albumId: Int): LiveData<Album>
}
