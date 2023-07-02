package com.example.notezz.callback

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.notezz.R
import com.example.notezz.adapter.ArchiveAdapter
import com.example.notezz.adapter.MainAdapter
import com.example.notezz.model.note_model.ArchiveModelDB
import com.example.notezz.viewmodels.NoteViewModel
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


class ArchiveSwipeToDeleteCallback(private val adapter: ArchiveAdapter, private val noteViewModel: NoteViewModel, private val context: Context) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        // Not needed for swipe action, so return false
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val note = adapter.currentList.get(position)
//        noteViewModel.deletePendingNote(note)
//        noteViewModel.createArchiveNote(ArchiveModelDB(note.noteId,note.name,note.text,note.color,false))
        if(direction == ItemTouchHelper.LEFT) {
            //Note update as delete = true

            println("swiped Left")
            noteViewModel.deleteWaitingArchiveNote(note)
        }
        else{
            //Note update as Create at note_database and delete in archive database
            noteViewModel.createNote(note.name,note.text,note.color)
            noteViewModel.deleteArchiveNote(note)
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        RecyclerViewSwipeDecorator.Builder(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
            .addSwipeLeftBackgroundColor(ContextCompat.getColor(context, R.color.light_red))
            .addSwipeRightBackgroundColor(ContextCompat.getColor(context,R.color.light_green))
            .addSwipeLeftActionIcon(R.drawable.ic_delete)
            .addSwipeRightActionIcon(R.drawable.ic_unarchive)
            .create()
            .decorate()
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}