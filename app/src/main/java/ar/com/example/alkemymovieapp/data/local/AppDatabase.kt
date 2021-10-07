package ar.com.example.alkemymovieapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ar.com.example.alkemymovieapp.data.models.MovieEntity

@Database(entities = [MovieEntity::class], version = 1)
abstract class AppDatabase:RoomDatabase() {

    abstract fun movieDao():MovieDao

}