package com.danilketov.todo.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.danilketov.todo.App
import com.danilketov.todo.R
import com.danilketov.todo.entity.Note

class NoteDetailsActivity : AppCompatActivity() {

    private var note: Note? = null
    private var editText: EditText? = null

    companion object {
        private const val EXTRA_NOTE = "dk.NoteDetailsActivity"

        fun start(caller: Activity, note: Note?) {
            val intent = Intent(caller, NoteDetailsActivity::class.java)
            if (note != null) {
                intent.putExtra(EXTRA_NOTE, note)
            }
            caller.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_details)

        setToolbar()
        setEditText()
    }

    private fun setToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        title = getString(R.string.note_details_title)
    }

    private fun setEditText() {
        editText = findViewById(R.id.text)

        if (intent.hasExtra(EXTRA_NOTE)) {
            note = intent.getParcelableExtra(EXTRA_NOTE)
            editText?.setText(note?.text)
        } else {
            note = Note()
        }

        // Фокус на EditText
        editText?.requestFocus()
        // Курсор в конец предложения
        editText?.text?.length?.let {
            editText?.setSelection(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_details, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> finish()
            R.id.save -> if (editText!!.text.length > 0) {
                note?.text = editText!!.text.toString()
                note?.done = false
                note?.timestamp = System.currentTimeMillis()
                if (intent.hasExtra(EXTRA_NOTE)) {
                    App.getInstance()?.getNoteDao()?.updateNotes(note!!)
                } else {
                    App.getInstance()?.getNoteDao()?.insertNotes(note!!)
                }
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
