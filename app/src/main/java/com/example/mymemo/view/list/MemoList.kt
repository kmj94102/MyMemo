package com.example.mymemo.view.list

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.*
import com.example.mymemo.R
import com.example.mymemo.data.MemoItem
import com.example.mymemo.ui.theme.*
import com.example.mymemo.util.dateFromTimestamp
import com.example.mymemo.util.getMainColor
import com.example.mymemo.util.getSubColor
import com.example.mymemo.util.toast
import com.example.mymemo.view.RouteAction
import com.example.mymemo.view.dialog.DeleteDialog
import com.example.mymemo.view.dialog.PasswordDialog

@Composable
fun MemoListContainer(
    routeAction: RouteAction,
    viewModel: MemoListViewModel = hiltViewModel()
) {

    val list = viewModel.list.value
    val secretDialogState = remember { mutableStateOf(false) }
    var secretIndex by remember { mutableStateOf(-1) }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty_memo))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = true,
        iterations = LottieConstants.IterateForever,
        speed = 0.5f
    )

    Box(modifier = Modifier
        .fillMaxSize()
        .clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        ) {
            focusManager.clearFocus()
        }
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {

            /** 타이틀 **/
            item {
                Text(
                    text = stringResource(id = R.string.memo),
                    style = Typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(top = 28.dp)
                )
            }

            /** 검색창 **/
            item {
                val field = viewModel.searchState.value
                InputBar(
                    modifier = Modifier.padding(vertical = 10.dp),
                    field = field,
                    hint = stringResource(id = R.string.guide_input_search),
                    hintColor = if (isSystemInDarkTheme()) White80 else Black80
                ) {
                    viewModel.event(ListEvent.WriteSearch(it))
                }
            }

            /** 검색 조건 **/
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val queryState = viewModel.queryState.value
                    CustomChip(
                        str = stringResource(id = R.string.order_by_date),
                        isSelected = queryState == 0
                    ) {
                        focusManager.clearFocus()
                        viewModel.event(ListEvent.ChangeQuery(0))
                    }
                    CustomChip(
                        str = stringResource(id = R.string.order_by_title),
                        isSelected = queryState == 1
                    ) {
                        focusManager.clearFocus()
                        viewModel.event(ListEvent.ChangeQuery(1))
                    }
                    CustomChip(
                        str = stringResource(id = R.string.only_importance),
                        isSelected = queryState == 2
                    ) {
                        focusManager.clearFocus()
                        viewModel.event(ListEvent.ChangeQuery(2))
                    }
                    CustomChip(
                        str = stringResource(id = R.string.only_secret),
                        isSelected = queryState == 3
                    ) {
                        focusManager.clearFocus() 
                        viewModel.event(ListEvent.ChangeQuery(3))
                    }
                }
            } // 검색 조건

            /** 메모 리스트 **/
            if (list.isEmpty()) {
                /** 빈 리스트 **/
                item {
                    LottieAnimation(
                        composition = composition,
                        progress = { progress },
                        modifier = Modifier.padding(horizontal = 25.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.empty_memo),
                        style = Typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                } // 빈 리스트
            } else {
                /** 리스트 **/
                item {
                    Spacer(modifier = Modifier.height(10.dp))

                    list.forEachIndexed { index, item ->
                        if (item.isSecret) {
                            /** 비밀메모 **/
                            SecretMemoItem(
                                secretClickListener = {
                                    secretDialogState.value = true
                                    secretIndex = index
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            /** 일반 메모 **/
                            MemoItem(
                                memoItem = item,
                                viewModel = viewModel,
                                clickListener = {
                                    routeAction.navToDetail(it)
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                } // 리스트
            } // 메모 리스트
        }// LazyColumn

        /** 메모 작성 버튼 **/
        MemoWriteButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp, end = 24.dp)
        ) {
            routeAction.navToWrite()
        }

        /** 비밀번호 다이얼로그 **/
        PasswordDialog(
            isShow = secretDialogState,
            index = secretIndex
        ) { password, index ->
            viewModel.event(
                ListEvent.PasswordCheck(
                    inputPassword = password,
                    index = index,
                    successListener = {
                        routeAction.navToDetail(it)
                        secretDialogState.value = false
                    },
                    failureListener = {
                        context.toast(R.string.confirm_password)
                    }
                )
            )
        } // 비밀번호 다이얼로그
    }// Box
}

/** 입력창 **/
@Composable
fun InputBar(
    modifier: Modifier = Modifier,
    field: String,
    hint: String,
    hintColor: Color = Black80,
    borderColor: Color = MaterialTheme.colorScheme.secondary,
    containerColor: Color = MaterialTheme.colorScheme.tertiary,
    textColor: Color = MaterialTheme.colorScheme.secondary,
    isSingleLine: Boolean = true,
    isPassword: Boolean = false,
    moreInputBar: Boolean = false,
    listener: (String) -> Unit
) {
    OutlinedTextField(
        value = field,
        onValueChange = {
            listener(it)
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            cursorColor = if (borderColor == MaterialTheme.colorScheme.secondary) {
                Primary
            } else {
                borderColor
            },
            unfocusedBorderColor = borderColor,
            focusedBorderColor = if (borderColor == MaterialTheme.colorScheme.secondary) {
                Primary
            } else {
                borderColor
            },
            containerColor = containerColor,
            textColor = textColor
        ),
        singleLine = isSingleLine,
        visualTransformation = if (isPassword) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text,
            imeAction = if (moreInputBar) ImeAction.Next else ImeAction.Default
        ),
        placeholder = {
            Text(
                text = hint,
                color = hintColor,
                style = Typography.bodyMedium
            )
        },
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
            .fillMaxWidth()
    )
}

/** 검색 조건에 사용할 칩 **/
@Composable
fun CustomChip(
    str: String,
    isSelected: Boolean,
    clickListener: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) Primary else Gray)
            .clickable {
                clickListener()
            }
    ) {
        Text(
            text = str,
            style = Typography.labelSmall,
            color = if (isSelected) White else Black,
            modifier = Modifier.padding(vertical = 3.dp, horizontal = 11.dp)
        )
    }
}

/** 메모 아이템 **/
@Composable
fun MemoItem(
    memoItem: MemoItem,
    modifier: Modifier = Modifier,
    viewModel: MemoListViewModel,
    clickListener: (Long) -> Unit,
) {
    val deleteDialogState = remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .height(65.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(getSubColor(memoItem.colorGroup)))
            .clickable { clickListener(memoItem.index) }
    ) {
        Box(
            modifier = Modifier
                .width(20.dp)
                .fillMaxHeight()
                .background(Color(getMainColor(memoItem.colorGroup)))
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, top = 10.dp, end = 10.dp, bottom = 10.dp)
        ) {
            Text(
                text = memoItem.title,
                maxLines = 1,
                style = Typography.bodyLarge,
                color = Black
            )

            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                /** 작성시간 **/
                Text(
                    text = dateFromTimestamp(memoItem.timestamp),
                    maxLines = 1,
                    style = Typography.labelSmall,
                    color = Black80,
                    modifier = Modifier
                )

                Spacer(modifier = Modifier.weight(1f))

                Image(
                    painter = painterResource(id = R.drawable.ic_trash),
                    contentDescription = "trash",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            deleteDialogState.value = true
                        }
                )

                Image(
                    painter = painterResource(
                        id = if (memoItem.isImportance) R.drawable.ic_star_fill
                        else R.drawable.ic_star
                    ),
                    contentDescription = "star",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            viewModel.event(
                                ListEvent.UpdateImportance(
                                    memoItem.index,
                                    memoItem.isImportance.not()
                                )
                            )
                        }
                )

                /** 삭제 다이얼로그 **/
                DeleteDialog(isShow = deleteDialogState) {
                    viewModel.event(ListEvent.DeleteMemo(index = memoItem.index))
                }
            }
        }
    }
}

/** 비밀 메모 아이템 **/
@Composable
fun SecretMemoItem(
    modifier: Modifier = Modifier,
    secretClickListener: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .height(65.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Gray)
            .clickable {
                secretClickListener()
            }
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_lock),
            contentDescription = "lock",
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = stringResource(id = R.string.secret_memo),
            style = Typography.bodyLarge,
            color = Black
        )
    }
}

/** 메모 작성 버튼 **/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoWriteButton(
    modifier: Modifier = Modifier,
    clickListener: () -> Unit
) {

    Card(
        shape = CircleShape,
        border = BorderStroke(1.dp, Black),
        elevation = CardDefaults.cardElevation(),
        colors = CardDefaults.cardColors(
            containerColor = Primary
        ),
        modifier = modifier
            .size(45.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                clickListener()
            }
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_write),
            contentDescription = "write",
            modifier = Modifier
                .padding(10.dp)
        )
    }

}