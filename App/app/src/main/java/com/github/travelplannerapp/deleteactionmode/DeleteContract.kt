package com.github.travelplannerapp.deleteactionmode

interface DeleteContract {
    interface View {
        fun showActionMode()
        fun showNoActionMode()
        fun showConfirmationDialog()
    }

    interface ItemView {
        fun setCheckbox()
    }

    interface Presenter {
        fun onDeleteClicked()
        fun enterActionMode()
        fun leaveActionMode()
    }
}
