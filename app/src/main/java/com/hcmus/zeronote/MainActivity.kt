package com.hcmus.zeronote

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.hcmus.zeronote.activities.NoteActivity
import com.hcmus.zeronote.adapters.NotesAdapter
import com.hcmus.zeronote.entities.Note
import com.hcmus.zeronote.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {
	
	private lateinit var recyclerView: RecyclerView;
	private lateinit var notesAdapter: NotesAdapter;
	private lateinit var searchText: EditText;
	
	private var displayNotes = mutableListOf<Note>()
	private var dbNotes = listOf<Note>()
	private val mainViewModel = MainViewModel()
	
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		
		recyclerView = findViewById(R.id.recyclerView);
		
		val spinner: Spinner = findViewById(R.id.dropdownMenu);
		ArrayAdapter.createFromResource(this,
			R.array.searchDropdownMenu,
			R.layout.support_simple_spinner_dropdown_item).also {
			spinner.adapter = it
			
			spinner.setSelection(0)
		}
		
		notesAdapter = NotesAdapter(this, displayNotes)
		recyclerView.adapter = notesAdapter;
		
		val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
		layoutManager.isAutoMeasureEnabled = true;
		
		recyclerView.layoutManager = layoutManager
		
		searchText = findViewById(R.id.searchText)
		searchText.addTextChangedListener(onTextChanged = {text, start, before, count ->
			if (!text.isNullOrEmpty()) {
				val searchBy = spinner.selectedItemPosition
				val filteredNotes: List<Note>;
				
				if (searchBy == 0) {
					filteredNotes = dbNotes.filter {
						it.title.contains(text, true) || it.content.contains(text, true)
					}
				}
				else {
					filteredNotes = dbNotes.filter {
						it.tag.contains(text, true)
					}
				}
				
				Log.d("SearchResult", "Search ${filteredNotes.size} notes")
				
				displayNotes.clear()
				displayNotes.addAll(filteredNotes)
			}
			else {
				displayNotes.clear()
				displayNotes.addAll(dbNotes)
			}
			
			notesAdapter.notifyDataSetChanged()
		})
		
		spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
			override fun onItemSelected(
				parent: AdapterView<*>?,
				view: View?,
				position: Int,
				id: Long,
			) {
				// Just want to notify search text that it should filter again
				searchText.text.insert(searchText.text.length, " ")
				searchText.text.delete(searchText.text.length - 1, searchText.text.length)
			}
			
			override fun onNothingSelected(parent: AdapterView<*>?) {
				/* no-op */
			}
			
			
		}
	}
	
	override fun onStart() {
		super.onStart()
		//					val testNotes: List<Note> = listOf(
//						Note().apply {
//							title = "Thu v??"
//							tag = "poem"
//							content = "Thi??n c??ch v??n ??m th???m b???t minh,\n" +
//									"Ti??u ti??u thu v?? l???c nh??n ????nh.\n" +
//									"??o???n tr?????ng kh?? th??? chi ?????u l???,\n" +
//									"Th?? c???p ho??ng ti??u di???p th?????ng thanh.\n" +
//									"Ng??m ??o???n ???? m?? thi??n l?? m???ng,\n" +
//									"S???u khi??n li??u l???c ng?? canh t??nh.\n" +
//									"Th??m khu?? t???i kh??? nh?? hoa di???n,\n" +
//									"Nh???t phi???n s???u dung ho??? b???t th??nh."
//						},
//						Note().apply {
//							title = "Qu??? m??t"
//							tag = "poem"
//							content = "Th??n em nh?? qu??? m??t tr??n c??y,\n" +
//									"Da n?? x?? x?? m??i n?? d??y.\n" +
//									"Qu??n t??? c?? y??u xin ????ng c???c,\n" +
//									"?????ng m??n m?? n???a nh???a ra tay."
//						},
//						Note().apply {
//							title = "Hu???n Hoa chi gi??o"
//							tag = "cursed text"
//							content = "C???n lao vi ti??n th???" +
//									"N??ng c??n d?? ?????c th???c" +
//									"V?? vi th???c ?????u b*???i, th???c c*c"
//						}
//					)
//					noteViewModel.saveNotes(this@MainActivity, testNotes)
		this.dbNotes = mainViewModel.getNotes(this)
		
		displayNotes.clear()
		displayNotes.addAll(dbNotes)
		
		notesAdapter.notifyDataSetChanged()
		
		Log.d("NoteDB", "Founds ${
			displayNotes.size
		} notes")
		
	}
	
	fun onCreateNote(v: View) {
		startActivity(Intent(this, NoteActivity::class.java))
	}
}