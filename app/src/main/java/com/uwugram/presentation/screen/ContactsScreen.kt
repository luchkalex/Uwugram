package com.uwugram.presentation.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.uwugram.R
import com.uwugram.domain.models.States
import com.uwugram.domain.models.User
import com.uwugram.presentation.components.ContactItem
import com.uwugram.presentation.components.ListDivider
import com.uwugram.presentation.viewmodel.ContactsViewModel
import com.uwugram.theme.DefaultPadding

@ExperimentalPermissionsApi
@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun ContactsScreen(
    navController: NavHostController,
    vm: ContactsViewModel = hiltViewModel(),
) {
    val showNoContactsMessage by remember { vm.showNoContactsMessage }

    ContactsContent(
        contactsList = vm.contactsList,
        navController = navController,
        showNoContactsMessage,
    )
}

@ExperimentalPermissionsApi
@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
private fun ContactsContent(
    contactsList: List<User>,
    navController: NavHostController,
    showNoContactsMessage: Boolean
) {
    Scaffold(
        topBar = {
            TopAppBar(
                contentPadding = PaddingValues(horizontal = DefaultPadding),
                backgroundColor = MaterialTheme.colors.surface
            ) {
                Text(
                    stringResource(id = R.string.contacts_screen_appbar_title),
                    style = MaterialTheme.typography.h1
                )
            }
        }
    ) {
        if (showNoContactsMessage) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(id = R.string.no_contacts_message),
                    style = MaterialTheme.typography.body2.copy(textAlign = TextAlign.Center)
                )
            }
        } else {
            LazyColumn {
                items(contactsList) {
                    ContactItem(
                        image = it.photoURL,
                        title = it.fullName,
                        subtitle = when (it.state) {
                            States.getStringFromState(States.OFFLINE) -> stringResource(id = R.string.offline_status)
                            States.getStringFromState(States.ONLINE) -> stringResource(id = R.string.online_status)
                            else -> ""
                        },
                    ) {
                        navController.navigate(Screen.SingleChatScreen.route + "/" + it.id) {
                            popUpTo(Screen.ContactsScreen.route) {
                                inclusive = true
                            }
                        }
                    }
                    Box(modifier = Modifier.padding(start = DefaultPadding)) { ListDivider() }
                }
            }
        }
    }
}