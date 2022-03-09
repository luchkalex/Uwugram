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
import com.uwugram.presentation.viewmodel.EditNameViewModel
import com.uwugram.theme.DefaultPadding
import com.uwugram.utils.getActivity
import com.uwugram.utils.hideKeyboard
import kotlinx.coroutines.launch

@ExperimentalPermissionsApi
@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun EditNameScreen(
    navController: NavHostController,
    vm: EditNameViewModel = hiltViewModel(),
) {
    val firstName by remember { vm.firstName }
    val lastName by remember { vm.lastName }

    val activity = LocalContext.current.getActivity()

    EditNameContent(
        onClick = { onFirstNameIsEmpty ->
            activity?.let { hideKeyboard(it) }
            if (navController.previousBackStackEntry?.destination?.route == Screen.SettingsScreen.route) {
                vm.saveName(
                    firstName,
                    lastName,
                    onSuccess = {
                        navController.popBackStack()
                    },
                    onFailed = onFirstNameIsEmpty
                )
            } else {
                vm.signUp(
                    firstName,
                    lastName,
                    onSuccess = {
                        navController.navigate(Screen.MainScreen.route) {
                            popUpTo(Screen.LoginScreen.route) { inclusive = true }
                        }
                    },
                    onFailed = onFirstNameIsEmpty
                )
            }

        },
        onFirstNameChange = {
            vm.onChangeFirstName(it)
        },
        onLastNameChange = {
            vm.onChangeLastName(it)
        },
        firstName = firstName,
        lastName = lastName
    )
}

@Composable
fun EditNameContent(
    onClick: (() -> Unit) -> Unit,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    firstName: String,
    lastName: String,
) {
    val focusManager = LocalFocusManager.current
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val firstNameIsRequiredText = stringResource(id = R.string.edit_name_name_required_message)

    Scaffold(
        topBar = {
            TopAppBar(
                elevation = 0.dp,
                backgroundColor = MaterialTheme.colors.surface
            ) {
                Text(
                    modifier = Modifier.padding(DefaultPadding),
                    text = stringResource(id = R.string.edit_name_screen_appbar_title),
                    style = MaterialTheme.typography.h1
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onClick {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            firstNameIsRequiredText,
                            duration = SnackbarDuration.Short
                        )
                    }
                }
                focusManager.clearFocus()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_edit_24),
                    contentDescription = stringResource(id = R.string.edit_name_fab_cd),
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
                value = firstName,
                singleLine = true,
                onValueChange = onFirstNameChange,
                textStyle = MaterialTheme.typography.caption,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.edit_name_first_name_hint_text),
                        style = MaterialTheme.typography.body2
                    )
                },
            )
            Spacer(modifier = Modifier.height(DefaultPadding))
            OutlinedTextField(
                modifier = Modifier
                    .padding(DefaultPadding)
                    .fillMaxWidth(),
                value = lastName,
                singleLine = true,
                onValueChange = onLastNameChange,
                textStyle = MaterialTheme.typography.caption,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.edit_name_last_name_hint_text),
                        style = MaterialTheme.typography.body2
                    )
                },
            )
        }
    }
}