package com.github.travelplannerapp.travels.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.WindowManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.github.travelplannerapp.R
import kotlinx.android.synthetic.main.dialog_add_edit_travel.view.*

class AddEditTravelDialog(private val dialogTitle: String) : DialogFragment() {

    companion object {
        const val TAG = "ADD TRAVEL DIALOG"
    }

    lateinit var travelName: EditText
    var onOk: (() -> Unit)? = null
    var onCancel: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity!!.layoutInflater.inflate(R.layout.dialog_add_edit_travel, null)

        travelName = view.editTextTravelNameDialogTravel

        view.textViewDialogTravel.text = dialogTitle

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
