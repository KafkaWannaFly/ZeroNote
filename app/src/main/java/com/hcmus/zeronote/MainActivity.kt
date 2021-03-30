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
//							title = "Thu vũ"
//							tag = "poem"
//							content = "Thiên cách vân âm thảm bất minh,\n" +
//									"Tiêu tiêu thu vũ lạc nhàn đình.\n" +
//									"Đoản trường khô thụ chi đầu lệ,\n" +
//									"Thư cấp hoàng tiêu diệp thượng thanh.\n" +
//									"Ngâm đoạn đê mê thiên lý mộng,\n" +
//									"Sầu khiên liêu lạc ngũ canh tình.\n" +
//									"Thâm khuê tối khổ như hoa diện,\n" +
//									"Nhất phiến sầu dung hoạ bất thành."
//						},
//						Note().apply {
//							title = "Quả mít"
//							tag = "poem"
//							content = "Thân em như quả mít trên cây,\n" +
//									"Da nó xù xì múi nó dày.\n" +
//									"Quân tử có yêu xin đóng cọc,\n" +
//									"Đừng mân mó nữa nhựa ra tay."
//						},
//						Note().apply {
//							title = "Huấn Hoa chi giáo"
//							tag = "cursed text"
//							content = "Cần lao vi tiên thủ" +
//									"Năng cán dĩ đắc thực" +
//									"Vô vi thực đầu b*ồi, thực c*c"
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