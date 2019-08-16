package com.github.travelplannerapp.utils

import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import android.app.Activity
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.github.travelplannerapp.R


object DrawerUtils {

    fun getDrawer(activity: Activity, toolbar: Toolbar) {

        val drawerItemSettings= PrimaryDrawerItem().withIdentifier(0)
                .withName(R.string.menu_settings).withIcon(R.drawable.ic_settings)
        val drawerItemSignOut = PrimaryDrawerItem()
                .withIdentifier(1).withName(R.string.menu_sign_out).withIcon(R.drawable.ic_sign_out)

        DrawerBuilder()
                .withActivity(activity)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .withCloseOnClick(true)
                .withSelectedItem(-1)
                .addDrawerItems(
                        drawerItemSettings,
                        drawerItemSignOut
                )
                .withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                    override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*>): Boolean {
                        // do something with the clicked item :D
                        return false
                    }
                })
                .build()
    }
}