package com.example.takeoutfixer.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.takeoutfixer.presentation.home.HomeViewModel
import com.example.takeoutfixer.presentation.home.HomeScreen
import com.example.takeoutfixer.presentation.scan.ScanScreen
import com.example.takeoutfixer.presentation.analysis.AnalysisScreen
import com.example.takeoutfixer.presentation.preview.PreviewScreen
import com.example.takeoutfixer.presentation.processing.ProcessingScreen
import com.example.takeoutfixer.presentation.results.ResultsScreen

@Composable
fun NavGraph(navController: NavHostController) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        enterTransition = { fadeIn(animationSpec = tween(300)) + slideInHorizontally(initialOffsetX = { 300 }) },
        exitTransition = { fadeOut(animationSpec = tween(300)) + slideOutHorizontally(targetOffsetX = { -300 }) },
        popEnterTransition = { fadeIn(animationSpec = tween(300)) + slideInHorizontally(initialOffsetX = { -300 }) },
        popExitTransition = { fadeOut(animationSpec = tween(300)) + slideOutHorizontally(targetOffsetX = { 300 }) }
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToScan = { navController.navigate(Screen.Scan.route) },
                viewModel = homeViewModel
            )
        }
        composable(Screen.Scan.route) {
            ScanScreen(
                onNavigateToAnalysis = { 
                    navController.navigate(Screen.Analysis.route) {
                        popUpTo(Screen.Scan.route) { inclusive = true }
                    }
                },
                homeViewModel = homeViewModel
            )
        }
        composable(Screen.Analysis.route) {
            AnalysisScreen(
                onNavigateToPreview = { navController.navigate(Screen.Preview.route) },
                onNavigateToProcessing = { 
                    navController.navigate(Screen.Processing.route) {
                        popUpTo(Screen.Analysis.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Preview.route) {
            PreviewScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Processing.route) {
            ProcessingScreen(
                onNavigateToResults = { 
                    navController.navigate(Screen.Results.route) {
                        popUpTo(Screen.Processing.route) { inclusive = true }
                    }
                },
                outputUri = homeViewModel.outputFolderUri.collectAsState().value
            )
        }
        composable(Screen.Results.route) {
            ResultsScreen(onNavigateToHome = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Home.route) { inclusive = true }
                }
            })
        }
    }
}
