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
import com.uwugram.utils.MAIN_ACTIVITY
import com.uwugram.utils.USER
import com.uwugram.utils.downloadAndSetImage


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

    fun disableDrawer() {
        drawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = false
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        toolbar.setNavigationOnClickListener {
            MAIN_ACTIVITY.navController.popBackStack()
        }
    }

    fun enableDrawer() {
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        drawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = true
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        toolbar.setNavigationOnClickListener {
            drawer.openDrawer()
        }
    }

    private fun createDrawer() {
        drawer = DrawerBuilder()
            .withActivity(activity)
            .withToolbar(toolbar)
            .withActionBarDrawerToggle(true)
            .withSelectedItem(-1)
            .withAccountHeader(header)
            .addDrawerItems(
                createPrimaryDrawerItem(
                    activity.getString(R.string.drawer_item_new_group),
                    R.drawable.ic_menu_create_groups
                ),
                createPrimaryDrawerItem(
                    activity.getString(R.string.drawer_item_contacts),
                    R.drawable.ic_menu_contacts
                ),
                createPrimaryDrawerItem(
                    activity.getString(R.string.drawer_item_calls),
                    R.drawable.ic_menu_phone
                ),
                createPrimaryDrawerItem(
                    activity.getString(R.string.drawer_item_saved_messages),
                    R.drawable.ic_menu_favorites
                ),
                createPrimaryDrawerItem(
                    activity.getString(R.string.drawer_item_settings),
                    R.drawable.ic_menu_settings
                ),
                DividerDrawerItem(),
                createPrimaryDrawerItem(
                    activity.getString(R.string.drawer_item_invite_friends),
                    R.drawable.ic_menu_invate
                ),
                createPrimaryDrawerItem(
                    activity.getString(R.string.drawer_item_faq),
                    R.drawable.ic_menu_help
                ),
            ).withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                override fun onItemClick(
                    view: View?,
                    position: Int,
                    drawerItem: IDrawerItem<*>
                ): Boolean {
                    when (position) {
                        5 -> MAIN_ACTIVITY.navController
                            .navigate(R.id.action_chatFragment_to_settingsFragment)
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