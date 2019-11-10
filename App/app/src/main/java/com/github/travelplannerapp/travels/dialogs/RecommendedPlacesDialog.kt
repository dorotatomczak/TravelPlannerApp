package com.github.travelplannerapp.travels.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.github.travelplannerapp.R
import kotlinx.android.synthetic.main.dialog_recommended_places.view.*

class RecommendedPlacesDialog(private val places: List<String>, private val dialogTitle: String): DialogFragment() {

    companion object {
        const val TAG = "RECOMMENDED PLACES DIALOG"
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity?.layoutInflater?.inflate(R.layout.dialog_recommended_places, null)
        view?.listViewRecommendedPlaces?.adapter = ArrayAdapter<String>(context!!, R.layout.item_recommended_place, places)
        view?.textViewRecommendedPlacesDialogTitle?.text = dialogTitle

        val builder = AlertDialog.Builder(activity!!).setView(view)

        return builder.create()
    }

}