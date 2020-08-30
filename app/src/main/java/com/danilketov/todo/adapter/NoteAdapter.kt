package com.danilketov.todo.adapter

import android.app.Activity
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.danilketov.todo.App
import com.danilketov.todo.R
import com.danilketov.todo.activity.NoteDetailsActivity
import com.danilketov.todo.entity.Note
import kotlinx.android.synthetic.main.item_view_note.view.*

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private var list: SortedList<Note>

    init {
        list = SortedList(Note::class.java, object : SortedList.Callback<Note>() {
            override fun compare(o1: Note, o2: Note): Int {
                if (!o2.done && o1.done) {
                    return 1
                }
                return if (o2.done && !o1.done) {
                    -1
                } else (o2.timestamp - o1.timestamp).toInt()
            }

            override fun onChanged(position: Int, count: Int) {
                notifyItemRangeChanged(position, count)
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.equals(newItem)
            }

            override fun areItemsTheSame(item1: Note, item2: Note): Boolean {
                return item1.id === item2.id
            }

            override fun onInserted(position: Int, count: Int) {
                notifyItemRangeInserted(position, count)
            }

            override fun onRemoved(position: Int, count: Int) {
                notifyItemRangeRemoved(position, count)
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                notifyItemMoved(fromPosition, toPosition)
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_view_note, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size()
    }

    fun setItems(notes: List<Note>?) {
        list.replaceAll(notes!!)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(list.get(position))
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var completed: CheckBox? = null
        var delete: View? = null
        var note: Note? = null
        var update = false

        private val titleTextView = itemView.title_text_view
        private val checkBoxTextView = itemView.check_box
        private val deleteButton = itemView.delete_button

        init {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    NoteDetailsActivity.start((itemView.context as Activity), note)
                }
            }
            deleteButton?.setOnClickListener {
                App.getInstance()?.getNoteDao()?.deleteNotes(note!!)
            }
            checkBoxTextView?.setOnCheckedChangeListener { buttonView, isChecked ->
                if (!update) {
                    note?.done = isChecked
                    App.getInstance()?.getNoteDao()?.updateNotes(note!!)
                }
                updateStroke()
            }
        }

        fun bind(note: Note) {
            this.note = note

            titleTextView?.text = note.text
            updateStroke()

            update = true
            checkBoxTextView?.isChecked = note.done
            update = false
        }

        private fun updateStroke() {
            if (note!!.done) {
                titleTextView.paintFlags = titleTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                titleTextView.setTextColor(Color.parseColor("#d8d8d8"))
            } else {
                titleTextView.paintFlags =
                    titleTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                titleTextView.setTextColor(Color.BLACK)
            }
        }
    }
}