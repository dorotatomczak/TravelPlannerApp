package com.github.travelplannerapp.utils

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.github.travelplannerapp.R
import com.github.travelplannerapp.scans.ScansActivity
import com.github.travelplannerapp.searchfriend.SearchFriendActivity
import com.github.travelplannerapp.signin.SignInActivity
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem

object DrawerUtils {

    fun getDrawer(activity: Activity, toolbar: Toolbar, travelId: Int? = null) {

        val headerResult = AccountHeaderBuilder()
                .withActivity(activity)
                .addProfiles(
                        ProfileDrawerItem()
                                .withEmail(SharedPreferencesUtils.getEmail())
                                .withIcon(activity.getDrawable(R.drawable.profile_picture))
                )
                .withSelectionListEnabled(false)
                .build()

        val drawerItemScans = PrimaryDrawerItem().withIdentifier(Menu.SCANS.ordinal.toLong())
                .withName(Menu.SCANS.textId).withIcon(Menu.SCANS.icon)
        val drawerItemSearchFriend = PrimaryDrawerItem().withIdentifier(Menu.SEARCH_FRIEND.ordinal.toLong())
                .withName(Menu.SEARCH_FRIEND.textId).withIcon(Menu.SEARCH_FRIEND.icon)
        val drawerItemSettings = PrimaryDrawerItem().withIdentifier(Menu.SETTINGS.ordinal.toLong())
                .withName(Menu.SETTINGS.textId).withIcon(Menu.SETTINGS.icon)
        val drawerItemSignOut = PrimaryDrawerItem().withIdentifier(Menu.SIGN_OUT.ordinal.toLong())
                .withName(Menu.SIGN_OUT.textId).withIcon(Menu.SIGN_OUT.icon)

        if (travelId == null) {
            drawerItemScans.withEnabled(false)
        }

        DrawerBuilder()
                .withAccountHeader(headerResult)
                .withActivity(activity)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .withCloseOnClick(true)
                .withSelectedItem(-1)
                .addDrawerItems(
                        drawerItemScans,
                        drawerItemSearchFriend,
                        drawerItemSettings,
                        drawerItemSignOut
                )
                .withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                    override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*>): Boolean {
                        view?.let {
                            val item = Menu.getItem(drawerItem.identifier.toInt())
                            if (item == Menu.SCANS && travelId != null) {
                                item.onClick(it, travelId)
                            } else {
                                item.onClick(it)
                            }
                        }
                        return true
                    }
                })
                .build()
    }

    enum class Menu(val textId: Int, val icon: Int) : OnMenuItemClickListener {
        SCANS(R.string.scans, R.drawable.ic_scan) {
            override fun onClick(view: View, extra: Any?) {
                val intent = Intent(view.context, ScansActivity::class.java)
                intent.putExtra(ScansActivity.EXTRA_TRAVEL_ID, extra as Int)
                view.context.startActivity(intent)
            }
        },
        SEARCH_FRIEND(R.string.my_friends, R.drawable.ic_user) {
            override fun onClick(view: View, extra: Any?) {
                val intent = Intent(view.context, SearchFriendActivity::class.java)
                view.context.startActivity(intent)
            }
        },
        SETTINGS(R.string.menu_settings, R.drawable.ic_settings) {
            override fun onClick(view: View, extra: Any?) {
                //TODO Add SettingsActivity
            }
        },
        SIGN_OUT(R.string.menu_sign_out, R.drawable.ic_sign_out) {
            override fun onClick(view: View, extra: Any?) {
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
        fun onClick(view: View, extra: Any? = null)
    }
}
