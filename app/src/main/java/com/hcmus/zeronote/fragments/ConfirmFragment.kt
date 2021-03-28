package com.hcmus.zeronote.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.hcmus.zeronote.R

class ConfirmFragment(val msg: String, val onOk: View.OnClickListener?) : DialogFragment() {
	private lateinit var msgText: TextView;
	private lateinit var okButton: Button;
	private lateinit var cancelButton: Button;
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_my_dialog, container)
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		view.apply {
			msgText = findViewById(R.id.dialogMessageText)
			msgText.text = msg
			
			okButton = findViewById(R.id.okButton)
			okButton.setOnClickListener {
				onOk?.onClick(it)
			}
			
			cancelButton = findViewById<Button>(R.id.cancelButton).apply {
				setOnClickListener {
					this@ConfirmFragment.dismiss()
				}
			}
		}
		
	}
	
	companion object {
		fun newInstance(msg: String, onOk: View.OnClickListener?): ConfirmFragment {
			val confirmFragment = ConfirmFragment(msg, onOk);
			
			return confirmFragment
		}
	}
	
}