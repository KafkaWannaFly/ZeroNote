package com.hcmus.zeronote

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
import com.hcmus.zeronote.activities.NoteActivity
import com.hcmus.zeronote.adapters.NotesAdapter
import com.hcmus.zeronote.entities.Note
import com.hcmus.zeronote.viewmodels.NoteViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer

class MainActivity : AppCompatActivity() {
	
	private lateinit var recyclerView: RecyclerView;
	private lateinit var notesAdapter: NotesAdapter;
	private var notes = mutableListOf<Note>()
	private val noteViewModel = NoteViewModel();
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		
		val spinner: Spinner = findViewById(R.id.dropdownMenu);
		ArrayAdapter.createFromResource(this,
			R.array.searchDropdownMenu,
			R.layout.support_simple_spinner_dropdown_item).also {
			spinner.adapter = it
			
			spinner.setSelection(0)
		}
		
		recyclerView = findViewById(R.id.recyclerView);
		notesAdapter = NotesAdapter(this, notes);
		recyclerView.adapter = notesAdapter;
		
		val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
		layoutManager.isAutoMeasureEnabled = true;
		
		recyclerView.layoutManager = layoutManager
		
		
	}
	
	override fun onStart() {
		super.onStart()
		
		CompletableFuture.runAsync {
			runBlocking {
				launch {
					// Run only once for test
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
					
					val dbNotes = noteViewModel.getNotes(this@MainActivity);
					notes.clear()
					notes.addAll(dbNotes)
					Log.d("NoteDB", "Get notes")
				}
				
			}
		}.thenAccept {
			notesAdapter.notifyDataSetChanged()
			recyclerView.smoothScrollToPosition(0)
			
			Log.d("NoteDB", "Notes: $notes")
		}
	}
	
	fun onCreateNote(v: View) {
		startActivity(Intent(this, NoteActivity::class.java))
	}
}