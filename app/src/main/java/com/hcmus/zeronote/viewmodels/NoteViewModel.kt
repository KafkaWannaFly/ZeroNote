package com.hcmus.zeronote.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.hcmus.zeronote.databases.NotesDatabase
import com.hcmus.zeronote.entities.Note

class NoteViewModel {
	var isDeletable = MutableLiveData(false);
	var isSavable = MutableLiveData(false);
	var characterCount = MutableLiveData(0);
	var wordCount = MutableLiveData(0);
	
	suspend fun saveNote(context: Context, note: Note) {
		NotesDatabase.getDatabase(context).noteDao().insertNote(note)
	}
	
	suspend fun getNotes(context: Context): List<Note> {
		return NotesDatabase.getDatabase(context).noteDao().getAllNotesSync()
	}
	
	suspend fun saveNotes(context: Context, notes: List<Note>) {
		NotesDatabase.getDatabase(context).noteDao().insertAll(notes);
	}
	
	suspend fun deleteNote(context: Context, note: Note) {
		NotesDatabase.getDatabase(context).noteDao().deleteNote(note)
	}
	
	fun deleteNoteSync(context: Context, note: Note) {
		NotesDatabase.getDatabase(context).noteDao().deleteNoteSync(note)
	}
}