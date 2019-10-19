package com.github.travelplannerapp.sharetraveldialog

import android.app.Dialog
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.github.travelplannerapp.ServerApp.datamodels.commonmodel.UserInfo

class ShareTravelDialog(private val dialogTitle: String, private val friends: ArrayList<UserInfo>) : DialogFragment() {

    companion object {
        const val TAG = "SHARE TRAVEL DIALOG"
    }

    var onOk: (() -> Unit)? = null
    var onCancel: (() -> Unit)? = null
    var selectedFriendsId = ArrayList<Int>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var friendsEmails = arrayOfNulls<String>(friends.size)
        val ifFriendsChecked = BooleanArray(friends.size);
        friends.forEachIndexed { i, userInfo ->
            friendsEmails.set(i, userInfo.email)
            ifFriendsChecked.set(i, false)
        }
        val builder = AlertDialog.Builder(context!!)
                .setTitle(dialogTitle)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    friends.forEachIndexed { i, userInfo ->
                        if (ifFriendsChecked[i].equals(true)) {
                            selectedFriendsId.add(userInfo.id)
                        }
                    }
                    onOk?.invoke()
                }
                .setNegativeButton(android.R.string.cancel) { _, _ ->
                    onCancel?.invoke()
                }
                .setMultiChoiceItems(friendsEmails, ifFriendsChecked) { dialog, which, isChecked ->
                    ifFriendsChecked[which] = isChecked
                }

        val dialog = builder.create()
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        return dialog
    }
}