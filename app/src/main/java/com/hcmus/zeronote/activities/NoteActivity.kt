package com.hcmus.zeronote.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.hcmus.zeronote.R
import com.hcmus.zeronote.entities.Note
import com.hcmus.zeronote.fragments.ConfirmFragment
import com.hcmus.zeronote.viewmodels.NoteViewModel
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*
import java.util.concurrent.CompletableFuture

class NoteActivity : AppCompatActivity() {
	private var note: Note? = null;
	private val noteViewModel = NoteViewModel();
	
	private lateinit var titleText: EditText
	private lateinit var tagText: EditText
	private lateinit var contentText: EditText
	private lateinit var customInfoText: TextView
	
	private lateinit var date: String;
	
	@SuppressLint("ResourceType")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_note)
		
		this.note = this.intent.getSerializableExtra("Note") as Note?;
		
		this.date = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault()).format(Date())
		
		val saveButton: ImageView = findViewById(R.id.saveButton);
		val deleteButton: ImageView = findViewById(R.id.deleteNoteButton);
		
		titleText = findViewById(R.id.titleText)
		tagText = findViewById(R.id.tagText)
		contentText = findViewById(R.id.contentText)
		customInfoText = findViewById(R.id.customInfoText)
		
		note?.apply {
			titleText.setText(title)
			tagText.setText(tag)
			contentText.setText(content)
			
			val str = "${this@NoteActivity.date} | " +
					"${content.trim().split(" ").size} words | " +
					"${noteViewModel.characterCount.value} characters";
			customInfoText.text = str;
		}
		
		// Require to fill out 3 fields to save or delete
		noteViewModel.apply {
			isDeletable.observe(this@NoteActivity, Observer { enabled ->
				deleteButton.isEnabled = enabled;
				if (enabled) {
					deleteButton.setColorFilter(ContextCompat.getColor(this@NoteActivity,
						R.color.red))
				}
				else {
					deleteButton.setColorFilter(ContextCompat.getColor(this@NoteActivity,
						R.color.gray))
				}
			});
			
			isSavable.observe(this@NoteActivity, Observer { enabled ->
				saveButton.isEnabled = enabled;
				if (enabled) {
					saveButton.setColorFilter(ContextCompat.getColor(this@NoteActivity,
						R.color.blue))
				}
				else {
					saveButton.setColorFilter(ContextCompat.getColor(this@NoteActivity,
						R.color.gray))
				}
			})
		}
		
		titleText.addTextChangedListener(onTextChanged = { text, start, before, count ->
			run {
				if (text != null) {
//					noteViewModel.isSavable.value =
//							(tagText.text.isNotEmpty()) && (text.isNotEmpty()) && (contentText.text.isNotEmpty())
//
//					noteViewModel.isDeletable.value =
//							(tagText.text.isNotEmpty()) && (text.isNotEmpty()) && (contentText.text.isNotEmpty())
					
					noteViewModel.isSavable.value = text.trim().isNotEmpty()
				}
				
				
			}
		})
		
		tagText.addTextChangedListener(onTextChanged = { text, start, before, count ->
			run {
				if (text != null) {
					noteViewModel.isSavable.value = titleText.text.trim().isNotEmpty()
				}
			}
		})
		
		contentText.addTextChangedListener(onTextChanged = { text, start, before, count ->
			run {
				if (text != null) {
					noteViewModel.isSavable.value = titleText.text.trim().isNotEmpty()
//
//					noteViewModel.isDeletable.value =
//							(tagText.text.isNotEmpty()) && (text.isNotEmpty()) && (contentText.text.isNotEmpty())
					
					// Character and word count
					noteViewModel.characterCount.value = text.length;
					noteViewModel.wordCount.value = text.trim().split(" ").size;
				}
			}
		})
		
		
		
		noteViewModel.characterCount.observe(this, Observer { count ->
			val str = "$date | ${noteViewModel.wordCount} words | $count characters";
			customInfoText.text = str;
		})
		
		noteViewModel.wordCount.observe(this, Observer { count ->
			val str = "$date | $count words | ${noteViewModel.characterCount.value} characters";
			customInfoText.text = str;
		})
		
	}
	
	private fun goBack(v: View) {
		onBackPressed()
	}
	
	fun onSaveButtonClick(v: View) {
		if(note == null) {
			note = Note();
		}
		
		note?.apply {
			title = titleText.text.toString().trim();
			tag = tagText.text.toString().trim();
			content = contentText.text.toString().trim();
			date = Date().toString()
		}
		
		val insertJob = GlobalScope.launch {
			note?.let { noteViewModel.saveNote(this@NoteActivity.applicationContext, it) };
		}
		
		this.goBack(v);
	}
	
	fun onDeleteButtonClick(v: View) {
		val confirmDelete = ConfirmFragment.newInstance("Are you sure want to delete?",
			View.OnClickListener {
				deleteNote()
				goBack(v)
			})
		
		confirmDelete.show(supportFragmentManager, "confirm_fragment")
	}
	
	private fun deleteNote() {
		note?.let {
			runBlocking {
				noteViewModel.deleteNote(this@NoteActivity, it)
			}
		}
		
	}
	
	fun onBackButtonClick(v: View) {
		if (noteViewModel.isSavable.value == true) {
			val confirmDelete = ConfirmFragment.newInstance("Save note before leave?",
				View.OnClickListener {
					onSaveButtonClick(v)
				})
			
			confirmDelete.show(supportFragmentManager, "confirm_fragment")
		} else {
			goBack(v)
		}
	}
	
}