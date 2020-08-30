package com.danilketov.todo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.danilketov.todo.App
import com.danilketov.todo.entity.Note

class NoteViewModel : ViewModel() {
    private val notes: LiveData<List<Note>>? =
        App.getInstance()?.getNoteDao()?.getNotes()

    fun getNotes(): LiveData<List<Note>>? {
        return notes
    }
}