package com.example.vinyls_jetpack_application.database.dao

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.vinyls_jetpack_application.models.Album
import com.example.vinyls_jetpack_application.models.Artist
import com.example.vinyls_jetpack_application.models.Collector

@Database(entities = [Artist::class, Album::class, Collector::class], version = 3, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    abstract fun artistDao(): ArtistDao
    abstract fun albumDao(): AlbumDao
    abstract fun collectorDao(): CollectorDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vinyls_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
