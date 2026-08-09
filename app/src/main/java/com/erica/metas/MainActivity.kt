package com.erica.metas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.erica.metas.data.SalesViewModel
import com.erica.metas.ui.components.BottomNavigationBar
import com.erica.metas.ui.components.NavTab
import com.erica.metas.ui.screens.ChartScreen
import com.erica.metas.ui.screens.HomeScreen
import com.erica.metas.ui.screens.SetGoalsScreen
import com.erica.metas.ui.screens.SettingsScreen
import com.erica.metas.ui.theme.AppEricaTheme

class MainActivity : ComponentActivity() {

    private val viewModel: SalesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initPreferences(applicationContext)

        setContent {
            val themeMode by viewModel.themeMode.collectAsState()

            AppEricaTheme(themeMode = themeMode) {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route ?: NavTab.Home.route

                Scaffold(
                    bottomBar = {
                        BottomNavigationBar(
                            currentRoute = currentRoute,
                            onTabSelected = { tab ->
                                navController.navigate(tab.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = NavTab.Home.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(NavTab.Home.route) {
                            HomeScreen(viewModel = viewModel)
                        }
                        composable(NavTab.Goals.route) {
                            SetGoalsScreen(viewModel = viewModel)
                        }
                        composable(NavTab.Chart.route) {
                            ChartScreen(viewModel = viewModel)
                        }
                        composable(NavTab.Settings.route) {
                            SettingsScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}
