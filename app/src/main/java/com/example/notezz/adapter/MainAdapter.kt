package com.example.notezz.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notezz.R
import com.example.notezz.callback.ItemClickListener
import com.example.notezz.model.note_model.NoteModelDB

class MainAdapter(private val listener: ItemClickListener) : ListAdapter<NoteModelDB, MainAdapter.MainViewHolder>(DiffUtilCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_sample_layout, parent ,false)
        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, listener)
    }
    class MainViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val noteTitle = view.findViewById<TextView>(R.id.tv_note_title);
        private val noteText = view.findViewById<TextView>(R.id.tv_note_text);
        private val parentLayout = view.findViewById<ConstraintLayout>(R.id.note_sample_parent_layout);
        private val backgroundDrawable = parentLayout.background as GradientDrawable
        fun bind(item: NoteModelDB, listener: ItemClickListener) {
            noteTitle.text = item.name
            noteText.text = item.text
            backgroundDrawable.setColor(Color.parseColor(item.color))
            // Set click listener
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
            // Set long click listener
            itemView.setOnLongClickListener {
                listener.onItemLongClick(adapterPosition)
                true // Return true to consume the event
            }
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