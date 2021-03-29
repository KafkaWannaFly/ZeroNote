package com.hcmus.zeronote.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hcmus.zeronote.R
import com.hcmus.zeronote.activities.NoteActivity
import com.hcmus.zeronote.entities.Note

class NotesAdapter(val context: Context, private val notes: List<Note>) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {
	
	class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		lateinit var view: LinearLayout;
		private lateinit var itemTitle: TextView;
		private lateinit var itemTag: TextView;
		private lateinit var itemContent: TextView;
		private lateinit var itemDate: TextView;
		
		init {
			itemView.apply {
				view = findViewById(R.id.noteItem)
				itemTitle = findViewById(R.id.itemTitle)
				itemTag = findViewById(R.id.itemTag)
				itemContent = findViewById(R.id.itemContent)
				itemDate = findViewById(R.id.itemDate)
			}
		}
		
		fun setNote(note: Note) {
			note.apply {
				itemTitle.text = title
				itemTag.text = tag
				itemContent.text = content
				itemDate.text = date
			}
		}
		
	}
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
		return NoteViewHolder(
			LayoutInflater.from(parent.context).inflate(
				R.layout.note_item,
				parent,
				false
			)
		)
	}
	
	override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
		if (notes.isEmpty()) {
			return
		}
		
		holder.setNote(notes[position]);
		holder.view.setOnClickListener {
			val intent = Intent(context, NoteActivity::class.java)
			intent.putExtra("Note", notes[position]);
			context.startActivity(intent)
		}
	}
	
	override fun getItemCount(): Int {
		return this.notes.size
	}
	
}