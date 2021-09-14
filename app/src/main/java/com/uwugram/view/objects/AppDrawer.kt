package com.uwugram.view.objects

import android.view.View
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.uwugram.R
import com.uwugram.view.fragments.SettingsFragment

class AppDrawer(val activity: AppCompatActivity, val toolbar: Toolbar) {

    private lateinit var drawer: Drawer
    private var primaryDrawerItemID = 0L
    private lateinit var header: AccountHeader

    init {
        println("WARNINGINIT")
        createHeader()
        createDrawer()
    }

    private fun createDrawer() {
        drawer = DrawerBuilder()
            .withActivity(activity)
            .withToolbar(toolbar)
            .withActionBarDrawerToggle(true)
            .withSelectedItem(-1)
            .withAccountHeader(header)
            .addDrawerItems(
                createPrimaryDrawerItem("New Group", R.drawable.ic_menu_create_groups),
                createPrimaryDrawerItem("Contacts", R.drawable.ic_menu_contacts),
                createPrimaryDrawerItem("Calls", R.drawable.ic_menu_phone),
                createPrimaryDrawerItem("Saved messages", R.drawable.ic_menu_favorites),
                createPrimaryDrawerItem("Settings", R.drawable.ic_menu_settings),
                DividerDrawerItem(),
                createPrimaryDrawerItem("Invite friends", R.drawable.ic_menu_invate),
                createPrimaryDrawerItem("FAQ", R.drawable.ic_menu_help),
            ).withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                override fun onItemClick(
                    view: View?,
                    position: Int,
                    drawerItem: IDrawerItem<*>
                ): Boolean {
                    when (position){
                        5 -> activity.supportFragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.fragmentContainer, SettingsFragment()).commit()
                    }
                    return false
                }
            })
            .build()
    }

    private fun createPrimaryDrawerItem(name: String, @DrawableRes icon: Int): PrimaryDrawerItem {
        return PrimaryDrawerItem()
            .withIdentifier(primaryDrawerItemID++)
            .withIconTintingEnabled(true)
            .withName(name)
            .withIcon(icon)
            .withSelectable(false)
    }

    private fun createHeader() {
        header = AccountHeaderBuilder()
            .withActivity(activity)
            .withHeaderBackground(R.drawable.header)
            .addProfiles(
                ProfileDrawerItem()
                    .withName("Name")
                    .withEmail("Email")
            ).build()
    }
}