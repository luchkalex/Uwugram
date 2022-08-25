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
import com.uwugram.presentation.viewmodel.EditBioViewModel
import com.uwugram.theme.DefaultPadding
import com.uwugram.utils.getActivity
import com.uwugram.utils.hideKeyboard
import kotlinx.coroutines.launch

@ExperimentalPermissionsApi
@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun EditBioScreen(
    navController: NavHostController,
    vm: EditBioViewModel = hiltViewModel()
) {
    val bio by remember { vm.bio }

    val activity = LocalContext.current.getActivity()

    EditBioContent(
        onClick = { bioText, onBioIsEmpty ->
            activity?.let { hideKeyboard(it) }
            vm.saveBio(
                bioText,
                onSuccess = { navController.popBackStack() },
                onFailure = onBioIsEmpty
            )
        },
        onValueChange = {
            vm.onChangeBio(it)
        },
        bio = bio
    )
}

@Composable
fun EditBioContent(
    onClick: (String, () -> Unit) -> Unit,
    onValueChange: (String) -> Unit,
    bio: String
) {
    val focusManager = LocalFocusManager.current

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val bioIsRequiredText = stringResource(id = R.string.edit_bio_required_message)

    Scaffold(
        topBar = {
            TopAppBar(
                elevation = 0.dp,
                backgroundColor = MaterialTheme.colors.surface
            ) {
                Text(
                    modifier = Modifier.padding(DefaultPadding),
                    text = stringResource(id = R.string.edit_bio_screen_appbar_title),
                    style = MaterialTheme.typography.h1
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onClick(bio) {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            bioIsRequiredText,
                            duration = SnackbarDuration.Short
                        )
                    }
                }
                focusManager.clearFocus()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_edit_24),
                    contentDescription = stringResource(id = R.string.edit_bio_fab_cd)
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
                value = bio,
                singleLine = true,
                onValueChange = onValueChange,
                textStyle = MaterialTheme.typography.caption,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.edit_bio_hint_text),
                        style = MaterialTheme.typography.body2
                    )
                },
            )
            Text(
                modifier = Modifier.padding(DefaultPadding),
                text = stringResource(id = R.string.edit_bio_helper_text),
                style = MaterialTheme.typography.body2
            )
        }
    }
}