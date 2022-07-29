package com.example.mymemo.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mymemo.view.detail.MemoDetailContainer
import com.example.mymemo.view.list.MemoListContainer
import com.example.mymemo.view.write.MemoWriteContainer

@Composable
fun NavGraph() {

    val navController = rememberNavController()
    val routeAction = remember(navController) {
        RouteAction(navController)
    }

    NavHost(navController = navController, startDestination = RouteAction.Home) {
        /** 홈(메모 목록) 화면 **/
        composable(RouteAction.Home) {
            MemoListContainer(routeAction)
        }
        /** 메모 상세 화면 **/
        composable(
            route = "${RouteAction.Detail}/{index}",
            arguments = listOf(
                navArgument("index") { type = NavType.LongType }
            )
        ) { entry ->
            val index = entry.arguments?.getLong("index")
            MemoDetailContainer(routeAction, index)
        }
        /** 메모 작성 화면 **/
        composable(RouteAction.Write) {
            MemoWriteContainer(routeAction)
        }
    }

}

class RouteAction(private val navController: NavHostController) {

    fun navToDetail(index: Long) {
        navController.navigate("$Detail/$index")
    }

    fun navToWrite() {
        navController.navigate(Write)
    }

    fun popupBackStack() {
        navController.popBackStack()
    }

    companion object {
        const val Home = "home"
        const val Detail = "detail"
        const val Write = "write"
    }

}