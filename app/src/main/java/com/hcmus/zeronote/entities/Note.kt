package com.hcmus.zeronote.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

@Entity(tableName = "notes")
class Note : Serializable {
	@PrimaryKey(autoGenerate = false)
	var id: Long = Instant.now().toEpochMilli();
	
	@ColumnInfo(name = "title")
	var title: String = "title";
	
	@ColumnInfo(name = "tag")
	var tag: String = "tag"
	
	@ColumnInfo(name = "date")
	var date: String = Date().toString()
	
	@ColumnInfo(name = "content")
	var content: String = "content"
	
	override fun toString(): String {
		return "Note(id='$id', title='$title', tag='$tag', date=$date, content='$content')"
	}
	
}