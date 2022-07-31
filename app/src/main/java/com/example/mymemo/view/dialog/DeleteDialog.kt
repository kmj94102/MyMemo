package com.example.mymemo.view.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.mymemo.R
import com.example.mymemo.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteDialog(
    isShow: MutableState<Boolean>,
    okClickListener: () -> Unit
) {

    if (isShow.value) {
        Dialog(onDismissRequest = { }) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (isSystemInDarkTheme()) {
                            darkDialogBackground
                        } else {
                            White
                        }
                    )
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Image(
                    painter = painterResource(id = R.drawable.ic_trash),
                    contentDescription = "trash",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(id = R.string.delete_memo),
                    style = Typography.bodyLarge,
                    color = if (isSystemInDarkTheme()) White else Black
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    /** 취소 **/
                    Card(
                        onClick = {
                            isShow.value = false
                        },
                        colors = CardDefaults.cardColors(
                            containerColor = Basic
                        ),
                        border = BorderStroke(1.dp, if (isSystemInDarkTheme()) White else Black),
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text(
                            text = stringResource(id = R.string.cancel),
                            color = White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(vertical = 6.dp)
                                .fillMaxWidth()
                        )
                    } // 취소

                    /** 확인 **/
                    Card(
                        onClick = {
                            okClickListener()
                        },
                        colors = CardDefaults.cardColors(
                            containerColor = Primary
                        ),
                        border = BorderStroke(1.dp, if (isSystemInDarkTheme()) White else Black),
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text(
                            text = stringResource(id = R.string.ok),
                            color = White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(vertical = 6.dp)
                                .fillMaxWidth()
                        )
                    } // 확인
                } // Row
            } // Column
        } // Dialog
    } // if
}