package com.uwugram.presentation.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.uwugram.R
import com.uwugram.presentation.viewmodel.EditUsernameViewModel
import com.uwugram.theme.DefaultPadding
import com.uwugram.utils.getActivity
import com.uwugram.utils.hideKeyboard
import kotlinx.coroutines.launch

@ExperimentalPermissionsApi
@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun EditUsernameScreen(
    navController: NavHostController,
    vm: EditUsernameViewModel = hiltViewModel()
) {
    val username by remember {
        vm.username
    }

    val activity = LocalContext.current.getActivity()
    val emptyUsernameText = stringResource(id = R.string.edit_username_name_required_message)
    val notAllowedSymbolsText = stringResource(id = R.string.edit_username_na_symbols_message)
    val usernameAlreadyTakenText =
        stringResource(id = R.string.edit_username_username_taken_message)

    EditUsernameContent(
        onClick = { usernameText, onFailure ->
            activity?.let { hideKeyboard(it) }
            vm.saveUsername(
                usernameText,
                onSuccess = { navController.popBackStack() },
                onEmpty = { onFailure(emptyUsernameText) },
                onNotAllowedSymbols = { onFailure(notAllowedSymbolsText) },
                onUsernameAlreadyTaken = { onFailure(usernameAlreadyTakenText) }
            )
        },
        onValueChange = {
            vm.onChangeUsername(it)
        },
        username = username
    )
}

@Composable
fun EditUsernameContent(
    onClick: (String, (String) -> Unit) -> Unit,
    onValueChange: (String) -> Unit,
    username: String
) {
    val focusManager = LocalFocusManager.current
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                elevation = 0.dp,
                backgroundColor = MaterialTheme.colors.surface
            ) {
                Text(
                    modifier = Modifier.padding(DefaultPadding),
                    text = stringResource(id = R.string.edit_username_screen_appbar_title),
                    style = MaterialTheme.typography.h1
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onClick(username) { message ->
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message,
                            duration = SnackbarDuration.Short
                        )
                    }
                }
                focusManager.clearFocus()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_edit_24),
                    contentDescription = stringResource(id = R.string.edit_username_fab_cd),
                )
            }
        },
        scaffoldState = scaffoldState,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .padding(DefaultPadding)
                    .fillMaxWidth(),
                value = username,
                singleLine = true,
                onValueChange = onValueChange,
                textStyle = MaterialTheme.typography.caption,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.edit_username_hint_text),
                        style = MaterialTheme.typography.body2
                    )
                },
            )
            Text(
                modifier = Modifier.padding(DefaultPadding),
                text = stringResource(id = R.string.edit_username_helper_text),
                style = MaterialTheme.typography.body2
            )
        }
    }
}