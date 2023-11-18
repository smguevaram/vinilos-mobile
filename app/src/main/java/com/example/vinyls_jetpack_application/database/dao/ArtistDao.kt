package com.example.vinyls_jetpack_application.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vinyls_jetpack_application.models.Artist

@Dao
interface ArtistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(artist: Artist)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(artists: List<Artist>)
    @Query("SELECT * FROM artists_table")
    fun getAllArtists(): LiveData<List<Artist>>

    @Query("SELECT * FROM artists_table WHERE id = :artistId")
    fun getArtistById(artistId: Int): LiveData<Artist>
}
