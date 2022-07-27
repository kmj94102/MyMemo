package com.example.mymemo.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mymemo.view.list.MemoListContainer

@Composable
fun NavGraph() {

    val navController = rememberNavController()
    val routeAction = remember(navController) {
        RouteAction(navController)
    }

    NavHost(navController = navController, startDestination = RouteAction.Home) {
        composable(RouteAction.Home) {
            MemoListContainer()
        }
    }

}

class RouteAction(navController: NavHostController) {

    companion object {
        const val Home = "home"
    }

}