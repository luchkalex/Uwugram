package com.uwugram.view.objects

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader
import com.mikepenz.materialdrawer.util.DrawerImageLoader
import com.uwugram.R
import com.uwugram.activities.MainActivity
import com.uwugram.utils.USER
import com.uwugram.utils.downloadAndSetImage
import com.uwugram.utils.replaceActivity
import com.uwugram.view.fragments.SettingsFragment

class AppDrawer(val activity: AppCompatActivity, private val toolbar: Toolbar) {

    private lateinit var drawer: Drawer
    private lateinit var header: AccountHeader
    private var primaryDrawerItemID = 0L
    private var drawerLayout: DrawerLayout
    private lateinit var currentProfile: ProfileDrawerItem

    init {
        initLoader()
        createHeader()
        createDrawer()
        drawerLayout = drawer.drawerLayout
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
                    when (position) {
                        5 -> MainActivity.fragment = SettingsFragment()
                    }
                    activity.replaceActivity(MainActivity())
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
        currentProfile = ProfileDrawerItem()
            .withName(USER.fullName)
            .withEmail(USER.phone)
            .withIcon(USER.photoURL)
            .withIdentifier(1)

        header = AccountHeaderBuilder()
            .withActivity(activity)
            .withHeaderBackground(R.drawable.header)
            .addProfiles(
                currentProfile
            ).build()
    }

    fun updateHeader() {
        currentProfile
            .withName(USER.fullName)
            .withEmail(USER.phone)
            .withIcon(USER.photoURL)

        header.updateProfile(currentProfile)
    }

    private fun initLoader() {
        DrawerImageLoader.init(object : AbstractDrawerImageLoader() {
            override fun set(imageView: ImageView, uri: Uri, placeholder: Drawable, tag: String?) {
                super.set(imageView, uri, placeholder, tag)
                imageView.downloadAndSetImage(uri.toString())
            }
        })
    }
}