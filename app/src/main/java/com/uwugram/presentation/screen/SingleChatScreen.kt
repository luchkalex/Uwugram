package com.uwugram.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.uwugram.R
import com.uwugram.domain.models.Message
import com.uwugram.domain.models.States
import com.uwugram.domain.models.User
import com.uwugram.presentation.components.AppTopBar
import com.uwugram.presentation.viewmodel.SingleChatViewModel
import com.uwugram.theme.*
import com.uwugram.utils.asTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@ExperimentalPermissionsApi
@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun SingleChatScreen(
    navController: NavController,
    userID: String,
    vm: SingleChatViewModel = hiltViewModel(),
) {

    val contact by vm.contact.observeAsState(User())

    val showNoMessagesMessage by remember { vm.showNoMessagesMessage }

    vm.getContent(userID)

    SingleChatContent(
        contact = contact,
        messageList = vm.messageList,
        userID = userID,
        onSendButtonClick = { text: String, onSuccess: () -> Unit ->
            vm.send(text, onSuccess)
        },
        showNoMessagesMessage = showNoMessagesMessage
    )
}

@ExperimentalPermissionsApi
@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun SingleChatContent(
    contact: User,
    messageList: List<Message>,
    userID: String,
    onSendButtonClick: (String, () -> Unit) -> Unit,
    showNoMessagesMessage: Boolean
) {
    val scrollState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { SingleChatTopBar(contact) },
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (showNoMessagesMessage) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(id = R.string.no_messages_message),
                        style = MaterialTheme.typography.body2.copy(textAlign = TextAlign.Center)
                    )
                }
            } else {
                Messages(
                    messageList,
                    userID,
                    Modifier.weight(1f),
                    scrollState,
                )
            }
            BottomInputBar(
                onSendButtonClick = onSendButtonClick,
                resetScroll = { resetScroll(scope, scrollState) }
            )
        }
    }
}

fun resetScroll(scope: CoroutineScope, scrollState: LazyListState): Job {
    return scope.launch {
        scrollState.animateScrollToItem(0)
    }
}

@Composable
fun Messages(
    messageList: List<Message>,
    userID: String,
    modifier: Modifier,
    scrollState: LazyListState
) {
    Box(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.padding(horizontal = SmallPadding),
            state = scrollState,
            reverseLayout = true
        ) {
            items(messageList) { message ->
                val prevMessage = try {
                    messageList[messageList.indexOf(message) + 1]
                } catch (e: IndexOutOfBoundsException) {
                    null
                }

                val nextMessage = try {
                    messageList[messageList.indexOf(message) - 1]
                } catch (e: IndexOutOfBoundsException) {
                    null
                }

                val messageStyle: MessageStyle =
                    getMessageStyle(userID, message, prevMessage, nextMessage)

                nextMessage ?: Spacer(Modifier.height(10.dp))

                MessageItem(message, messageStyle)

                prevMessage ?: Spacer(Modifier.height(10.dp))

                if (messageStyle == MessageStyle.OTHERS_SINGLE ||
                    messageStyle == MessageStyle.OWN_SINGLE ||
                    messageStyle == MessageStyle.OWN_FIRST ||
                    messageStyle == MessageStyle.OTHERS_FIRST
                )
                    Spacer(Modifier.height(10.dp))
                else
                    Spacer(Modifier.height(4.dp))
            }
        }
    }
}

@Composable
fun BottomInputBar(onSendButtonClick: (String, () -> Unit) -> Unit, resetScroll: () -> Job) {
    val textFieldValue = remember {
        mutableStateOf("")
    }
    Surface(elevation = 2.dp) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = textFieldValue.value,
            onValueChange = { textFieldValue.value = it },
            maxLines = 5,
            textStyle = MaterialTheme.typography.caption,
            trailingIcon = {
                AnimatedVisibility(visible = textFieldValue.value.isNotEmpty()) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_send_24),
                        contentDescription = stringResource(id = R.string.send_message_button_cd),
                        modifier = Modifier
                            .size(30.dp)
                            .clickable {
                                onSendButtonClick(textFieldValue.value) {
                                    textFieldValue.value = ""
                                    resetScroll()
                                }

                            },
                    )
                }
            },
            placeholder = {
                Text(
                    text = stringResource(id = R.string.input_message_hint),
                    style = MaterialTheme.typography.body2
                )
            },
            colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.surface)
        )
    }
}

@ExperimentalPermissionsApi
@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
private fun SingleChatTopBar(
    contact: User
) {
    val painter = rememberImagePainter(
        data = contact.photoURL,
        builder = {
            placeholder(R.drawable.default_avatar)
            error(R.drawable.default_avatar)
            crossfade(1000)
            transformations(
                CircleCropTransformation()
            )
        }
    )

    AppTopBar {
        Image(
            modifier = Modifier
                .size(TopBarImageSize)
                .clip(CircleShape),
            painter = painter,
            contentDescription = stringResource(id = R.string.single_chant_photo_cd)
        )
        Spacer(modifier = Modifier.width(DefaultPadding))
        Column(verticalArrangement = Arrangement.SpaceEvenly) {
            Text(contact.fullName, style = MaterialTheme.typography.h2)
            Text(
                when (contact.state) {
                    States.getStringFromState(States.OFFLINE) -> stringResource(id = R.string.offline_status)
                    States.getStringFromState(States.ONLINE) -> stringResource(id = R.string.online_status)
                    else -> ""
                }, style = MaterialTheme.typography.body2
            )
        }
    }
}

@Composable
fun MessageItem(message: Message, messageStyle: MessageStyle) {
    val Timestamp = @Composable {
        Text(
            text = message.timestamp.toString().asTime(),
            style = MaterialTheme.typography.overline
        )
    }
    val MessageBuble = @Composable { MessageBubble(message = message, messageStyle = messageStyle) }

    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = if (messageStyle.own) Arrangement.End else Arrangement.Start,
        modifier = Modifier.fillMaxWidth(),
    ) {
        if (messageStyle.own) {
            Timestamp()
            MessageBuble()
        } else {
            MessageBuble()
            Timestamp()
        }
    }
}

@Composable
fun MessageBubble(message: Message, messageStyle: MessageStyle) {
    BoxWithConstraints {
        Box(
            Modifier
                .clip(
                    when (messageStyle) {
                        MessageStyle.OWN_FIRST -> OWN_FIRST_MESSAGE
                        MessageStyle.OWN_MIDDLE -> OWN_MIDDLE_MESSAGE
                        MessageStyle.OWN_LAST -> OWN_LAST_MESSAGE
                        MessageStyle.OWN_SINGLE -> SINGLE_MESSAGE
                        MessageStyle.OTHERS_FIRST -> OTHERS_FIRST_MESSAGE
                        MessageStyle.OTHERS_MIDDLE -> OTHERS_MIDDLE_MESSAGE
                        MessageStyle.OTHERS_LAST -> OTHERS_LAST_MESSAGE
                        MessageStyle.OTHERS_SINGLE -> SINGLE_MESSAGE
                    }
                )
                .background(if (messageStyle.own) OwnMessageBackground else OthersMessageBackground)
                .requiredWidthIn(max = maxWidth.times(0.8f)),
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(DefaultPadding),
                style = MaterialTheme.typography.subtitle1
            )
        }
    }
}

fun getMessageStyle(
    userID: String,
    message: Message,
    prevMessage: Message?,
    nextMessage: Message?
): MessageStyle {

    return if (prevMessage == null || prevMessage.sender != message.sender)
    //or single or first
        return if (nextMessage == null || nextMessage.sender != message.sender)
            if (userID == message.sender) MessageStyle.OTHERS_SINGLE
            else MessageStyle.OWN_SINGLE
        //SINGLE
        else
        //FIRST
            if (userID == message.sender) MessageStyle.OTHERS_FIRST
            else MessageStyle.OWN_FIRST
    else
    //or middle or last
        if (nextMessage == null || nextMessage.sender != message.sender)
        //LAST
            if (userID == message.sender) MessageStyle.OTHERS_LAST
            else MessageStyle.OWN_LAST
        else
        //MIDDLE
            if (userID == message.sender) MessageStyle.OTHERS_MIDDLE
            else MessageStyle.OWN_MIDDLE
}

enum class MessageStyle(val own: Boolean) {
    OWN_FIRST(true),
    OWN_MIDDLE(true),
    OWN_LAST(true),
    OWN_SINGLE(true),
    OTHERS_FIRST(false),
    OTHERS_MIDDLE(false),
    OTHERS_LAST(false),
    OTHERS_SINGLE(false);
}
