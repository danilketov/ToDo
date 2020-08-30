package com.danilketov.todo.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danilketov.todo.R
import com.danilketov.todo.adapter.NoteAdapter
import com.danilketov.todo.viewmodel.NoteViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*

class NoteActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private lateinit var viewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setToolbar()
        val adapter = setRecyclerView()
        setFab()
        checkItems(adapter)
        setViewModel(adapter)
    }

    private fun setViewModel(adapter: NoteAdapter) {
        viewModel = ViewModelProviders.of(this)[NoteViewModel::class.java]
        viewModel.getNotes()?.observe(this, Observer {
            adapter.setItems(it)
        })
    }

    private fun setFab() {
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            NoteDetailsActivity.start(this, null)
        }
    }

    private fun setToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.tasks)
    }

    private fun setRecyclerView(): NoteAdapter {
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView?.layoutManager = LinearLayoutManager(this)

        val adapter = NoteAdapter()
        recyclerView?.adapter = adapter

        return adapter
    }

    private fun checkItems(adapter: NoteAdapter) {
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                checkEmpty()
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                checkEmpty()
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                super.onItemRangeRemoved(positionStart, itemCount)
                checkEmpty()
            }

            fun checkEmpty() {
                empty_list_text_view.visibility =
                    (if (adapter.itemCount == 0) View.VISIBLE else View.GONE)
                empty_list_image_view.visibility =
                    (if (adapter.itemCount == 0) View.VISIBLE else View.GONE)
            }
        })
    }
}
