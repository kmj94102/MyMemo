package com.example.mymemo.view.write

import android.content.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mymemo.R
import com.example.mymemo.ui.theme.Black
import com.example.mymemo.ui.theme.Primary
import com.example.mymemo.ui.theme.Typography
import com.example.mymemo.ui.theme.White
import com.example.mymemo.util.ColorGroup
import com.example.mymemo.util.getMainColor
import com.example.mymemo.util.getSubColor
import com.example.mymemo.util.toast
import com.example.mymemo.view.RouteAction
import com.example.mymemo.view.list.InputBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoWriteContainer(
    routeAction: RouteAction,
    memoIndex: Long = -1,
    viewModel: MemoWriteViewModel = hiltViewModel()
) {

    val state = viewModel.memoItemState.value
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    var isModify = false

    if (memoIndex != -1L) {
        isModify = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        /** 뒤로가기 **/
        Image(
            painter = painterResource(id = R.drawable.ic_prev),
            contentDescription = "prev",
            modifier = Modifier
                .padding(top = 16.dp, start = 17.dp)
                .size(24.dp)
                .clickable {
                    routeAction.popupBackStack()
                }
        ) // 뒤로가기 버튼

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 56.dp, bottom = 64.dp)
        ) {
            /** 타이틀 입력 **/
            item {
                InputBar(
                    modifier = Modifier.padding(vertical = 10.dp),
                    field = state.title,
                    hint = stringResource(id = R.string.input_title),
                    containerColor = Color(getSubColor(state.colorGroup)),
                    borderColor = Color(getMainColor(state.colorGroup)),
                    moreInputBar = true,
                    isSingleLine = true
                ) {
                    viewModel.event(WriteEvent.WriteTitle(it))
                }
            } // 타이틀 입력

            /** 내용 입력 **/
            item {
                InputBar(
                    field = state.contents,
                    hint = stringResource(id = R.string.input_contents),
                    containerColor = Color(getSubColor(state.colorGroup)),
                    borderColor = Color(getMainColor(state.colorGroup)),
                    isSingleLine = false,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .defaultMinSize(minHeight = 200.dp)
                ) {
                    viewModel.event(WriteEvent.WriteContents(it))
                }

                Text(
                    text = "${state.contents.length}",
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))
            } // 내용 입력

            /** 색상 선택 **/
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    ColorGroup.values().forEachIndexed { index, colorGroup ->
                        SelectColor(index, colorGroup) {
                            focusManager.clearFocus()
                            viewModel.event(WriteEvent.ChangeColorGroup(it))
                        }
                    }
                }
            } // 색상 선택

            /** 비밀메모 설정 **/
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember {
                                focusManager.clearFocus()
                                MutableInteractionSource()
                            }
                        ) {
                            viewModel.event(WriteEvent.ChangeSecretMode(state.isSecret.not()))
                        }
                ) {
                    Checkbox(
                        checked = state.isSecret,
                        onCheckedChange = {
                            focusManager.clearFocus()
                            viewModel.event(WriteEvent.ChangeSecretMode(state.isSecret.not()))
                        },
                        colors = CheckboxDefaults.colors(
                            uncheckedColor = Color(0x80000000),
                            checkedColor = Primary
                        )
                    )

                    Text(
                        text = stringResource(id = R.string.secret_memo_setting),
                        style = Typography.bodySmall
                    )
                }

                if (state.isSecret) {
                    InputBar(
                        field = state.password,
                        hint = stringResource(id = R.string.input_password),
                        isSingleLine = true,
                        isPassword = true,
                        containerColor = Color(getSubColor(state.colorGroup)),
                        borderColor = Color(getMainColor(state.colorGroup)),
                        modifier = Modifier.fillParentMaxWidth()
                    ) {
                        viewModel.event(WriteEvent.WritePassword(it))
                    }
                }
            }// 비밀메모 설정

        } // LazyColumn

        /** 작성완료 버튼 **/
        Card(
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = Primary
            ),
            border = BorderStroke(1.dp, Black),
            onClick = {
                writeComplete(isModify, context, viewModel, routeAction)
            },
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .align(Alignment.BottomCenter)
        ) {
            Text(
                text = stringResource(id = R.string.write_complete),
                textAlign = TextAlign.Center,
                style = Typography.bodyLarge,
                color = White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            )
        } // 작성완료 버튼
    }// Box
}

/** 메모 색상 선택 **/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectColor(
    index: Int,
    colorGroup: ColorGroup,
    clickListener: (Int) -> Unit
) {
    Card(
        shape = CircleShape,
        border = BorderStroke(2.dp, Black),
        colors = CardDefaults.cardColors(
            containerColor = Color(colorGroup.mainColor)
        ),
        modifier = Modifier
            .size(60.dp)
            .clickable(
                indication = null,
                interactionSource = remember {
                    MutableInteractionSource()
                }
            ) {
                clickListener(index)
            }
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp)
        ) {
            Canvas(modifier = Modifier.size(58.dp)) {
                drawArc(
                    color = Color(colorGroup.subColor),
                    startAngle = 270f,
                    sweepAngle = 180f,
                    useCenter = true,
                    style = Fill
                )
            }
        }
    }
}

/** 메모 작성 완 **/
fun writeComplete(
    isModify: Boolean,
    context: Context,
    viewModel: MemoWriteViewModel,
    routeAction: RouteAction
) {
    if (isModify) {
        viewModel.event(
            WriteEvent.UpdateMemo(
                successListener = {
                    context.toast(R.string.modify_success)
                    routeAction.popupBackStack()
                },
                failureListener = {
                    context.toast(R.string.modify_failure)
                }
            )
        )
    } else {
        viewModel.event(
            WriteEvent.InsertMemo(
                successListener = {
                    context.toast(R.string.write_success)
                    routeAction.popupBackStack()
                },
                failureListener = {
                    context.toast(R.string.write_failure)
                }
            )
        )
    }
}