package com.hcmus.zeronote.DAOs

import androidx.room.*
import com.hcmus.zeronote.entities.Note

@Dao
interface NoteDao {
	@Query("SELECT * FROM notes ORDER BY date DESC")
	fun getAllNotesSync(): List<Note>
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertNote(note: Note)
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertNoteSync(note: Note)
	
	@Delete
	suspend fun deleteNote(note: Note)
	
	@Delete
	fun deleteNoteSync(note: Note)
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertAll(notes: List<Note>)
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertAllSync(notes: List<Note>)
}