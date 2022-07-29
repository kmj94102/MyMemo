package com.example.mymemo.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mymemo.ui.theme.MyMemoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyMemoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph()
                }
            }
        }
    }
}
//    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
//    val progress by animateLottieCompositionAsState(
//        composition = composition,
//        isPlaying = true,
//        iterations = LottieConstants.IterateForever,
//        speed = 0.5f
//    )
//
//    if (isShow) {
//        LottieAnimation(
//            composition = composition,
//            progress = { progress },
//            modifier = Modifier.padding(horizontal = 25.dp)
//        )
//    }