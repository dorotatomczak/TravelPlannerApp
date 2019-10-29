package com.github.travelplannerapp.searchfriend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.recyclerview.widget.RecyclerView
import com.github.travelplannerapp.R
import com.github.travelplannerapp.deleteactionmode.DeleteActionModeToolbar
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_friend.*

class FriendAdapter(val presenter: SearchFriendContract.Presenter) : RecyclerView.Adapter<FriendAdapter.FriendsViewHolder>() {

    private var actionMode: ActionMode? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        return FriendsViewHolder(presenter, LayoutInflater.from(parent.context)
                .inflate(R.layout.item_friend, parent, false)); }

    override fun getItemCount(): Int {
        return presenter.getFriendsCount()
    }

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        presenter.onBindFriendAtPosition(position, holder)
    }

    fun leaveActionMode() {
        actionMode = null
    }

    inner class FriendsViewHolder(val presenter: SearchFriendContract.Presenter, override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer, SearchFriendContract.FriendItemView, View.OnClickListener, View.OnLongClickListener {

        init {
            containerView.setOnClickListener(this)
            containerView.setOnLongClickListener(this)
            checkboxItemFriend.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener(
                    fun(_: CompoundButton, isChecked: Boolean) {
                        if (isChecked) {
                            presenter.addFriendIdToDelete(adapterPosition)
                        } else {
                            presenter.removeFriendIdToDelete(adapterPosition)
                        }
                    }
            ))
        }

        override fun onClick(v: View?) {
            if (actionMode != null) checkboxItemFriend.isChecked = !checkboxItemFriend.isChecked
        }

        override fun onLongClick(v: View?): Boolean {
            if (actionMode == null) {
                actionMode = (containerView.context as AppCompatActivity)
                        .startSupportActionMode(DeleteActionModeToolbar(presenter))
            }
            return true
        }

        override fun setEmail(email: String) {
            textViewItemFriendName.text = email
        }

        override fun setCheckbox() {
            if (actionMode != null) {
                checkboxItemFriend.visibility = View.VISIBLE
            } else {
                checkboxItemFriend.visibility = View.GONE
            }
            checkboxItemFriend.isChecked = false
        }
    }
}
