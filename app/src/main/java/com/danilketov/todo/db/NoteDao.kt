package com.danilketov.todo.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.danilketov.todo.entity.Note

@Dao
interface NoteDao {

    @Query("SELECT * FROM Note")
    fun getNotes(): LiveData<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotes(note: Note)

    @Update
    fun updateNotes(note: Note)

    @Delete
    fun deleteNotes(note: Note)
}