package com.dreamrecall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dreamrecall.ui.DreamViewModel
import com.dreamrecall.ui.screens.AddDreamScreen
import com.dreamrecall.ui.screens.DreamListScreen
import com.dreamrecall.ui.theme.DreamRecallTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DreamRecallTheme {
                val viewModel: DreamViewModel = viewModel()
                val navController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = "dream_list") {
                        composable("dream_list") {
                            DreamListScreen(
                                viewModel = viewModel,
                                onAddDreamClick = { navController.navigate("add_dream") }
                            )
                        }
                        composable("add_dream") {
                            AddDreamScreen(
                                viewModel = viewModel,
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
