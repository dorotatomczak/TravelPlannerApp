package com.github.travelplannerapp.actionmodewithdelete

interface DeletableElementsContract {
    interface View {
        fun showActionMode()
        fun showNoActionMode()
        fun showConfirmationDialog()
    }

    interface ItemView {
        fun setCheckbox(checked: Boolean)
    }

    interface Presenter {
        fun setCheck(position: Int, checked: Boolean)
        fun onDeleteClicked()
        fun enterActionMode()
        fun leaveActionMode()
    }
}
