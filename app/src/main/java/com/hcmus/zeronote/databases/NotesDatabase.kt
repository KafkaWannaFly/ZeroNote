package com.hcmus.zeronote.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hcmus.zeronote.DAOs.NoteDao
import com.hcmus.zeronote.entities.Note

// Declare array like this help printing useful error notifications when some of our db goes wrong
@Suppress("ReplaceArrayOfWithLiteral")
@Database(entities = arrayOf(Note::class), version = 1, exportSchema = false)
abstract class NotesDatabase: RoomDatabase() {
	companion object {
		@Volatile
		private var notesDatabase: NotesDatabase? = null;
		
		fun getDatabase(context: Context) : NotesDatabase {
			return notesDatabase ?: Room.databaseBuilder(
				context,
				NotesDatabase::class.java,
				"notes_db"
			).build()
		}
	}
	
	abstract fun noteDao(): NoteDao;
}