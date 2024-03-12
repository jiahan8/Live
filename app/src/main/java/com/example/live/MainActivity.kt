package com.example.live

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import com.example.live.home.navigation.HOME_ROUTE
import com.example.live.navigation.LiveNavHost
import com.example.live.ui.LiveBottomBar
import com.example.live.ui.rememberLiveAppState
import com.example.live.ui.theme.LiveTheme
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LiveTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val appState = rememberLiveAppState(
                        windowSizeClass = calculateWindowSizeClass(this),
                    )

                    Scaffold(
                        bottomBar = {
                            LiveBottomBar(
                                destinations = appState.topLevelDestinations,
                                onNavigateToDestination = appState::navigateToTopLevelDestination,
                                currentDestination = appState.currentDestination,
                            )
                        }
                    ) { innerPadding ->
                        LiveNavHost(
                            appState = appState,
                            modifier = Modifier.padding(innerPadding),
                            startDestination = HOME_ROUTE
                        )
                    }
                }
            }
        }
    }
}