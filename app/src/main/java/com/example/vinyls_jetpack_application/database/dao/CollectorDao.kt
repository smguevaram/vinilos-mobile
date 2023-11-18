package com.example.vinyls_jetpack_application.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vinyls_jetpack_application.models.Collector

@Dao
interface CollectorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(collectors: List<Collector>)

    @Query("SELECT * FROM collectors_table")
    fun getAllCollectors(): LiveData<List<Collector>>

    @Query("SELECT * FROM collectors_table WHERE id = :collectorId")
    fun getCollectorById(collectorId: Int): LiveData<Collector>
}