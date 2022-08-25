package com.uwugram.presentation.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.uwugram.R
import com.uwugram.presentation.components.SurfaceWithLoadingAnimation
import com.uwugram.presentation.viewmodel.LoginViewModel
import com.uwugram.theme.DefaultPadding
import com.uwugram.utils.getActivity
import com.uwugram.utils.hideKeyboard
import kotlinx.coroutines.launch

@ExperimentalPermissionsApi
@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun LoginScreen(
    navController: NavHostController,
    vm: LoginViewModel = hiltViewModel()
) {

    val activity = LocalContext.current.getActivity()
    val loadingAnimation = remember { mutableStateOf(false) }

    LoginContent(
        onClick = { phone, onFailed ->
            loadingAnimation.value = true
            activity?.let {
                hideKeyboard(it)
                vm.sendCode(
                    phone = phone,
                    onCodeSend = { id ->
                        navController.navigate(Screen.CodeVerificationScreen.route + "/$id")
                    },
                    onFailed = {
                        loadingAnimation.value = false
                        onFailed()
                    },
                    activity = activity
                )
            }
        },
        loadingAnimation = loadingAnimation.value
    )
}

@ExperimentalCoilApi
@Composable
fun LoginContent(
    onClick: (String, () -> Unit) -> Unit,
    loadingAnimation: Boolean
) {
    val phone = remember {
        mutableStateOf("")
    }
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val numberTooShortMessage = stringResource(id = R.string.login_number_too_short_message)

    Scaffold(
        topBar = {
            TopAppBar(
                elevation = 0.dp,
                backgroundColor = MaterialTheme.colors.surface
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = DefaultPadding),
                    text = stringResource(id = R.string.login_screen_appbar_title),
                    style = MaterialTheme.typography.h1
                )
            }
        },
        floatingActionButton = {
            if (!loadingAnimation) {
                FloatingActionButton(onClick = {
                    onClick(phone.value) {
                        scope.launch {
                            scaffoldState.snackbarHostState.showSnackbar(
                                numberTooShortMessage,
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_arrow_forward_24),
                        contentDescription = stringResource(id = R.string.login_fab_cd)
                    )
                }
            }
        },
        scaffoldState = scaffoldState,
    ) {
        SurfaceWithLoadingAnimation(visible = loadingAnimation) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    modifier = Modifier.padding(horizontal = DefaultPadding),
                    text = stringResource(id = R.string.login_helper_text),
                    style = MaterialTheme.typography.body2.copy(textAlign = TextAlign.Center)
                )
                TextField(
                    modifier = Modifier
                        .padding(DefaultPadding)
                        .fillMaxWidth(),
                    value = phone.value,
                    singleLine = true,
                    onValueChange = { phoneInput: String ->
                        if (phoneInput.length <= 9) phone.value = phoneInput
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
                    textStyle = MaterialTheme.typography.caption,
                    label = {
                        Text(
                            text = stringResource(id = R.string.login_phone_hint_text),
                            style = MaterialTheme.typography.body2
                        )
                    },
                    leadingIcon = {
                        Text(
                            modifier = Modifier.padding(horizontal = DefaultPadding),
                            text = stringResource(id = R.string.login_default_ukraine_country_code),
                            style = MaterialTheme.typography.body2
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent
                    ),
                )
            }

        }
    }
}