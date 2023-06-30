package com.example.notezz.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notezz.R
import com.example.notezz.model.note_model.NoteModelDB

class MainAdapter : ListAdapter<NoteModelDB, MainAdapter.MainViewHolder>(DiffUtilCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_sample_layout, parent ,false)
        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
    class MainViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val noteTitle = view.findViewById<TextView>(R.id.tv_note_title);
        val noteText = view.findViewById<TextView>(R.id.tv_note_text);
        fun bind(item: NoteModelDB) {
            noteTitle.text = item.name
            noteText.text = item.text
        }
    }

    class DiffUtilCallback: DiffUtil.ItemCallback<NoteModelDB>(){
        override fun areItemsTheSame(oldItem: NoteModelDB, newItem: NoteModelDB): Boolean {
            return oldItem.noteId == newItem.noteId
        }

        override fun areContentsTheSame(oldItem: NoteModelDB, newItem: NoteModelDB): Boolean {
            return oldItem == newItem
        }
    }
}