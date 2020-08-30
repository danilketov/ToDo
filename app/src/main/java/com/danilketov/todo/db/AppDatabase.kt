package com.danilketov.todo.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.danilketov.todo.entity.Note


@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME: String = "note_db"
    }

    abstract fun repositoryDao(): NoteDao
}