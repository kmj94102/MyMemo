package com.example.mymemo.view.write

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mymemo.R
import com.example.mymemo.ui.theme.Black
import com.example.mymemo.ui.theme.Primary
import com.example.mymemo.ui.theme.Typography
import com.example.mymemo.ui.theme.White
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
    val context = LocalContext.current
    var isModify = false

    if (memoIndex != -1L) {
        isModify = true
    }

    Box(
        modifier = Modifier.fillMaxSize()
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
                    hint = "타이틀을 입력해주세요.",
                    containerColor = Color(getSubColor(state.colorGroup)),
                    borderColor = Color(getMainColor(state.colorGroup))
                ) {
                    viewModel.event(WriteEvent.WriteTitle(it))
                }
            } // 타이틀 입력

            /** 내용 입력 **/
            item {
                InputBar(
                    field = state.contents,
                    hint = "내용을 입력해주세요.",
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
                    SelectColor(0) {
                        viewModel.event(WriteEvent.ChangeColorGroup(it))
                    }
                    SelectColor(1) {
                        viewModel.event(WriteEvent.ChangeColorGroup(it))
                    }
                    SelectColor(2) {
                        viewModel.event(WriteEvent.ChangeColorGroup(it))
                    }
                    SelectColor(3) {
                        viewModel.event(WriteEvent.ChangeColorGroup(it))
                    }
                }
            }

            /** 비밀메모 설정 **/
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .clickable {
                            viewModel.event(WriteEvent.ChangeSecretMode(state.isSecret.not()))
                        }
                ) {
                    Checkbox(
                        checked = state.isSecret,
                        onCheckedChange = {
                            viewModel.event(WriteEvent.ChangeSecretMode(state.isSecret.not()))
                        }
                    )

                    Text(
                        text = "비밀메모 설정",
                        style = Typography.bodySmall
                    )
                }

                if (state.isSecret) {
                    InputBar(
                        field = state.password,
                        hint = "비밀번호를 입력해주세요",
                        isSingleLine = true,
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
                if (isModify) {
                    viewModel.event(
                        WriteEvent.UpdateMemo(
                            successListener = {
                                context.toast("수정 완료")
                                routeAction.popupBackStack()
                            },
                            failureListener = {
                                context.toast("수정 실패")
                            }
                        )
                    )
                    return@Card
                }
                viewModel.event(
                    WriteEvent.InsertMemo(
                        successListener = {
                            context.toast("등록 완료")
                            routeAction.popupBackStack()
                        },
                        failureListener = {
                            context.toast("등록 실패")
                        }
                    )
                )
            },
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .align(Alignment.BottomCenter)
        ) {
            Text(
                text = "작성완료",
                textAlign = TextAlign.Center,
                style = Typography.bodyLarge,
                color = White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            )
        }
    }// Box


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectColor(index: Int, clickListener: (Int) -> Unit) {
    Card(
        shape = CircleShape,
        border = BorderStroke(2.dp, Black),
        colors = CardDefaults.cardColors(
            containerColor = Color(getMainColor(index = index))
        ),
        modifier = Modifier
            .size(60.dp)
            .clickable {
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
                    color = Color(getSubColor(index = index)),
                    startAngle = 270f,
                    sweepAngle = 180f,
                    useCenter = true,
                    style = Fill
                )
            }
        }
    }
}