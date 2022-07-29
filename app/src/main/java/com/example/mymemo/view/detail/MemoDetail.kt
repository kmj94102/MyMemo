package com.example.mymemo.view.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.mymemo.R
import com.example.mymemo.ui.theme.Black
import com.example.mymemo.ui.theme.Typography
import com.example.mymemo.ui.theme.White
import com.example.mymemo.view.RouteAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoDetailContainer(routeAction: RouteAction) {

    var isImportance by remember {
        mutableStateOf(false)
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

        /** 중요메모 체크버튼 **/
        Image(
            painter = painterResource(id = if (isImportance) R.drawable.ic_star_fill else R.drawable.ic_star),
            contentDescription = "star",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 20.dp)
                .size(24.dp)
                .clickable {
                    isImportance = isImportance.not()
                }
        ) // 중요메모 체크버튼

        LazyColumn(
            modifier = Modifier
                .padding(top = 56.dp, bottom = 64.dp, start = 24.dp, end = 24.dp)
        ) {

            /** 타이틀 **/
            item {
                Text(
                    text = "한줄 제목이 들어가는 곳입니다.한줄 제목이 들어가는 곳입니다.한줄 제목이 들어가는 곳입니다.한줄 제목이 들어가는 곳입니다.한줄 제목이 들어가는 곳입니다.",
                    style = Typography.titleMedium,
                )
            }  // 타이틀

            /** 구분선 **/
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .height(1.dp)
                        .background(Black)
                )
            } // 구분선

            /** 내용 **/
            item {
                Card(
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(2.dp, Color(0xFFF37878)),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFBEBE)
                    ),
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(top = 15.dp)
                        .defaultMinSize(minHeight = 200.dp)
                ) {
                    Text(
                        text = "내용이들어갑니다.내용이들어갑니다.내용이들어갑니다.내용이들어갑니다.내용이들어갑니다.내용이들어갑니다.내용이들어갑니다.내용이들어갑니다.내용이들어갑니다.내용이들어갑니다.내용이들어갑니다.\n내용이들어갑니다.내용이들어갑니다.내용이들어갑니다.내용이들어갑니다.내용이들어갑니다.내용이들어갑니다.내용이들어갑니다.내용이들어갑니다.내용이들어갑니다.내용이들어갑니다.내용이들어갑니다.\n내용이들어갑니다.내용이들어갑니다.내용이들어갑니다.내용이들어갑니다.내용이들어갑니다.내용이들어갑니다.내용이들어갑니다.내용이들어갑니다.내용이들어갑니다.내용이들어갑니다.내용이들어갑니다.",
                        modifier = Modifier
                            .padding(vertical = 6.dp, horizontal = 12.dp)
                            .fillParentMaxWidth()
                    )
                }
            } // 내용
        } // LazyColumn

        /** 수정/삭제 버튼 **/
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .align(Alignment.BottomCenter)
        ) {
            /** 수정하기 **/
            Card(
                onClick = {  },
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFAD9A1)
                ),
                border = BorderStroke(1.dp, Black),
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = "수정하기",
                    color = White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(vertical = 6.dp)
                        .fillMaxWidth()
                )
            }

            /** 삭제하기 **/
            Card(
                onClick = {  },
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF37878)
                ),
                border = BorderStroke(1.dp, Black),
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = "삭제하기",
                    color = White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(vertical = 6.dp)
                        .fillMaxWidth()
                )
            }
        } // Row
    } // Box
}