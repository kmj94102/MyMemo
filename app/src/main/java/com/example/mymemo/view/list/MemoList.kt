package com.example.mymemo.view.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mymemo.R
import com.example.mymemo.data.MemoEntity
import com.example.mymemo.data.MemoItem
import com.example.mymemo.ui.theme.*
import com.example.mymemo.util.getMainColor
import com.example.mymemo.util.getSubColor
import com.example.mymemo.view.RouteAction
import kotlin.random.Random

@Composable
fun MemoListContainer(
    routeAction: RouteAction,
    viewModel: MemoListViewModel = hiltViewModel()
) {

    val list = viewModel.list.value

    Box(modifier = Modifier.fillMaxSize()) {

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
                    modifier = Modifier.padding(top = 28.dp)
                )
            }

            /** 검색창 **/
            item {
                val field = viewModel.searchState.value
                InputBar(
                    modifier = Modifier.padding(vertical = 10.dp),
                    field = field,
                    hint = stringResource(id = R.string.guide_input_search)
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
                    CustomChip(str = "날짜순", isSelected = queryState == 0) {
                        viewModel.event(ListEvent.ChangeQuery(0))
                    }
                    CustomChip(str = "타이틀순", isSelected = queryState == 1) {
                        viewModel.event(ListEvent.ChangeQuery(1))
                    }
                    CustomChip(str = "중요글만", isSelected = queryState == 2) {
                        viewModel.event(ListEvent.ChangeQuery(2))
                    }
                    CustomChip(str = "비밀글만", isSelected = queryState == 3) {
                        viewModel.event(ListEvent.ChangeQuery(3))
                    }
                }
            }

            /** 리스트 **/
            item {
                Spacer(modifier = Modifier.height(10.dp))

                list.forEach { entity ->
                    MemoItem(
                        memoEntity = entity,
                        viewModel = viewModel,
                        modifier = Modifier.fillMaxWidth(),
                    ){
                        routeAction.navToDetail(it)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

        }// LazyColumn

        /** 메모 작성 버튼 **/
        MemoWriteButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp, end = 24.dp)
        ) {
            routeAction.navToWrite()
        }

        /** 다이얼로그 **/

    }// Box

}

/** 검색창 **/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputBar(
    modifier: Modifier = Modifier,
    field: String,
    hint: String,
    borderColor: Color = Black,
    containerColor: Color = Gray,
    isSingleLine: Boolean = true,
    listener: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, borderColor),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        modifier = modifier
            .fillMaxWidth()
    ) {
        TextField(
            value = field,
            onValueChange = {
                listener(it)
            },
            singleLine = isSingleLine,
            placeholder = {
                Text(text = hint, color = Color(0x80000000))
            },
            colors = TextFieldDefaults.textFieldColors(
                // TextField UnderLine 제거
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                containerColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
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
    memoEntity: MemoItem,
    modifier: Modifier = Modifier,
    viewModel: MemoListViewModel,
    clickListener: (Long) -> Unit
) {

    if (memoEntity.isSecret) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .height(65.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Gray)
                .clickable {
                    // todo 다이얼로그
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_lock),
                contentDescription = "lock",
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "비밀메모 입니다.",
                style = Typography.bodyLarge
            )
        }
        return
    }

    Box(
        modifier = modifier
            .height(65.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(getSubColor(memoEntity.colorGroup)))
            .clickable { clickListener(memoEntity.index) }
    ) {
        Box(
            modifier = Modifier
                .width(20.dp)
                .fillMaxHeight()
                .background(Color(getMainColor(memoEntity.colorGroup)))
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, top = 10.dp, end = 10.dp, bottom = 10.dp)
        ) {
            Text(
                text = memoEntity.title,
                maxLines = 1,
                style = Typography.bodyLarge,
            )

            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "2022/07/27",
                    maxLines = 1,
                    style = Typography.labelSmall,
                    color = Color(0x80000000),
                    modifier = Modifier
                )

                Spacer(modifier = Modifier.weight(1f))

                Image(
                    painter = painterResource(id = R.drawable.ic_trash),
                    contentDescription = "trash",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            viewModel.event(ListEvent.DeleteMemo(index = memoEntity.index))
                        }
                )

                Image(
                    painter = painterResource(id = if (memoEntity.isImportance) R.drawable.ic_star_fill else R.drawable.ic_star),
                    contentDescription = "star",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            viewModel.event(ListEvent.UpdateImportance(memoEntity.index, memoEntity.isImportance.not()))
                        }
                )
            }
        }
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
            .clickable { clickListener() }
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_write),
            contentDescription = "write",
            modifier = Modifier
                .padding(10.dp)
        )
    }

}