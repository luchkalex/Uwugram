package com.uwugram.presentation.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.uwugram.R
import com.uwugram.presentation.components.SurfaceWithLoadingAnimation
import com.uwugram.presentation.viewmodel.CodeVerificationViewModel
import com.uwugram.theme.DefaultPadding
import com.uwugram.utils.getActivity
import com.uwugram.utils.hideKeyboard
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun CodeVerificationScreen(
    navController: NavHostController,
    vm: CodeVerificationViewModel = hiltViewModel(),
    userID: String
) {
    val activity = LocalContext.current.getActivity()
    val loadingAnimation = remember { mutableStateOf(false) }

    CodeVerificationContent(
        onClick = { code, onWrongCode ->
            activity?.let { hideKeyboard(it) }
            loadingAnimation.value = true

            vm.verifyCode(
                id = userID,
                verificationCode = code,
                onLoggedIn = {
                    navController.navigate(Screen.MainScreen.route) {
                        popUpTo(Screen.LoginScreen.route) { inclusive = true }
                    }
                },
                onSigningUp = {
                    navController.navigate(Screen.EditNameScreen.route)
                },
                onWrongCode = {
                    loadingAnimation.value = false
                    onWrongCode()
                }
            )
        },
        loadingAnimation = loadingAnimation.value
    )
}

@Composable
fun CodeVerificationContent(
    onClick: (String, () -> Unit) -> Unit,
    loadingAnimation: Boolean
) {
    val code = remember {
        mutableStateOf("")
    }

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val wrongCodeText = stringResource(id = R.string.code_verification_wrong_code_message)

    Scaffold(
        topBar = {
            TopAppBar(
                elevation = 0.dp,
                backgroundColor = MaterialTheme.colors.surface
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = DefaultPadding),
                    text = stringResource(id = R.string.code_verification_screen_appbar_title),
                    style = MaterialTheme.typography.h1
                )
            }
        },
        scaffoldState = scaffoldState,
    ) {
        SurfaceWithLoadingAnimation(visible = loadingAnimation) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    modifier = Modifier.padding(horizontal = DefaultPadding),
                    text = stringResource(id = R.string.code_verification_helper_text),
                    style = MaterialTheme.typography.body2.copy(textAlign = TextAlign.Center),
                )
                TextField(
                    modifier = Modifier
                        .padding(DefaultPadding)
                        .width(180.dp),
                    value = code.value,
                    singleLine = true,
                    onValueChange = { codeInput: String ->
                        if (codeInput.length <= 6) code.value = codeInput
                        if (codeInput.length == 6) onClick(code.value) {
                            scope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    wrongCodeText,
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent
                    ),
                )
            }

        }
    }
}