package com.github.travelplannerapp.addtravel

import android.app.Dialog
import android.os.Bundle
import android.view.WindowManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.github.travelplannerapp.R

class AddTravelDialog : DialogFragment() {
    companion object {
        fun newInstance(): AddTravelDialog {
            val dialog = AddTravelDialog()
            return dialog
        }
    }

    lateinit var editText: EditText
    var onOk: (() -> Unit)? = null
    var onCancel: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity!!.layoutInflater.inflate(R.layout.activity_add_travel, null)

        editText = view.findViewById(R.id.editTextTravelName)

        val builder = AlertDialog.Builder(context!!)
                .setView(view)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    onOk?.invoke()
                }
                .setNegativeButton(android.R.string.cancel) { _, _ ->
                    onCancel?.invoke()
                }
        val dialog = builder.create()

        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

        return dialog
    }
}