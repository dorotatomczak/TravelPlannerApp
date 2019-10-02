package com.github.travelplannerapp.utils

import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import android.app.Activity
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.github.travelplannerapp.R
import android.content.Intent
import com.github.travelplannerapp.signin.SignInActivity

object DrawerUtils {

    fun getDrawer(activity: Activity, toolbar: Toolbar) {

        val drawerItemSettings = PrimaryDrawerItem().withIdentifier(Menu.SETTINGS.ordinal.toLong())
                .withName(Menu.SETTINGS.color).withIcon(Menu.SETTINGS.icon)
        val drawerItemSignOut = PrimaryDrawerItem().withIdentifier(Menu.SIGN_OUT.ordinal.toLong())
                .withName(Menu.SIGN_OUT.color).withIcon(Menu.SIGN_OUT.icon)

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
                        view?.let { Menu.getItem(position).onClick(it) }
                        return true
                    }
                })
                .build()
    }

    enum class Menu(val color: Int, val icon: Int) : OnMenuItemClickListener {
        SETTINGS(R.string.menu_settings, R.drawable.ic_settings) {
            override fun onClick(view: View) {
                //TODO Add SettingsActivity
            }
        },
        SIGN_OUT(R.string.menu_sign_out, R.drawable.ic_sign_out) {
            override fun onClick(view: View) {
                SharedPreferencesUtils.clear()

                val intent = Intent(view.context, SignInActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                view.context.startActivity(intent)
            }
        };

        companion object {
            fun getItem(index: Int): Menu {
                return values()[index]
            }
        }
    }

    interface OnMenuItemClickListener {
        fun onClick(view: View)
    }
}