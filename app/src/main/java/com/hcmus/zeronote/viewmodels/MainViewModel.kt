package com.hcmus.zeronote.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.hcmus.zeronote.databases.NotesDatabase
import com.hcmus.zeronote.entities.Note
import kotlinx.coroutines.*

class MainViewModel {
	
	fun getNotes(context: Context): List<Note> {
		var dbNotes: List<Note> = listOf();
		runBlocking {
			launch {
				dbNotes = NotesDatabase.getDatabase(context).noteDao().getAllNotesAsync()
				
			}.join()
		}
		
		return dbNotes
	}
}