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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mymemo.R
import com.example.mymemo.ui.theme.Black
import com.example.mymemo.ui.theme.Primary
import com.example.mymemo.ui.theme.Typography
import com.example.mymemo.ui.theme.White
import com.example.mymemo.view.RouteAction
import com.example.mymemo.view.list.InputBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoWriteContainer(routeAction: RouteAction) {
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
                var field by remember { mutableStateOf("") }

                InputBar(
                    modifier = Modifier.padding(vertical = 10.dp),
                    field,
                    "타이틀을 입력해주세요."
                ) {
                    field = it
                }
            } // 타이틀 입력

            /** 내용 입력 **/
            item {
                var field by remember { mutableStateOf("") }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "${field.length}",
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                InputBar(
                    field = field,
                    hint = "내용을 입력해주세요.",
                    isSingleLine = false,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .defaultMinSize(minHeight = 200.dp)
                ) {
                    field = it
                }
            } // 내용 입력

            /** 색상 선택 **/
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    SelectColor()
                    SelectColor()
                    SelectColor()
                    SelectColor()
                }
            }

            /** 비밀메모 설정 **/
            item {
                var isChecked by remember { mutableStateOf(false) }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .clickable { isChecked = !isChecked }
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = {
                            isChecked = it
                        }
                    )

                    Text(
                        text = "비밀메모 설정",
                        style = Typography.bodySmall
                    )
                }

                if (isChecked) {
                    var field by remember {
                        mutableStateOf("")
                    }
                    InputBar(
                        field = field,
                        hint = "비밀번호를 입력해주세요",
                        modifier = Modifier.fillParentMaxWidth()
                    ) {
                        field = it
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
fun SelectColor() {
    Card(
        shape = CircleShape,
        border = BorderStroke(2.dp, Black),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF37878)
        ),
        modifier = Modifier
            .size(60.dp)
    ) {

        Box(modifier = Modifier.fillMaxSize().padding(2.dp)) {
            Canvas(modifier = Modifier.size(58.dp)) {
                drawArc(
                    color = Color(0xFFFFBEBE),
                    startAngle = 270f,
                    sweepAngle = 180f,
                    useCenter = true,
                    style = Fill
                )
            }
        }
    }
}