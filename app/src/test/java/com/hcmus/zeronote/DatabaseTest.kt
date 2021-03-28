package com.hcmus.zeronote

import android.content.Context
import com.hcmus.zeronote.entities.Note
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DatabaseTest {
	@Mock
	private lateinit var mockContext: Context
	
	private val notes: List<Note> = listOf(
		Note().apply {
			title = "Thu vũ"
			tag = "poem"
			content = "Thiên cách vân âm thảm bất minh,\n" +
					"Tiêu tiêu thu vũ lạc nhàn đình.\n" +
					"Đoản trường khô thụ chi đầu lệ,\n" +
					"Thư cấp hoàng tiêu diệp thượng thanh.\n" +
					"Ngâm đoạn đê mê thiên lý mộng,\n" +
					"Sầu khiên liêu lạc ngũ canh tình.\n" +
					"Thâm khuê tối khổ như hoa diện,\n" +
					"Nhất phiến sầu dung hoạ bất thành."
		},
		Note().apply {
			title = "Quả mít"
			tag = "poem"
			content = "Thân em như quả mít trên cây,\n" +
					"Da nó xù xì múi nó dày.\n" +
					"Quân tử có yêu xin đóng cọc,\n" +
					"Đừng mân mó nữa nhựa ra tay."
		},
		Note().apply {
			title = "Huấn Hoa chi giáo"
			tag = "cursed text"
			content = "Cần lao vi tiên thủ" +
					"Năng cán dĩ đắc thực" +
					"Vô vi thực đầu b*ồi, thực c*c"
		}
	)
	
	@Test
	fun insertNotes() {
	
	}
}