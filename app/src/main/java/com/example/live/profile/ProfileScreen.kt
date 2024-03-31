package com.example.live.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.live.R
import com.example.live.ui.theme.LiveTheme

@Composable
internal fun ProfileRoute(
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    ProfileScreen(
        onUpdateTheme = profileViewModel::updateIsDarkTheme,
        darkTheme = profileViewModel.userPreferences.isDarkTheme
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(onUpdateTheme: (Boolean) -> Unit, darkTheme: Boolean) {
    var isDarkTheme by rememberSaveable { mutableStateOf(darkTheme) }
    LiveTheme(isDarkTheme) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.settings),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .wrapContentHeight()
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.dark_theme),
                            modifier = Modifier.weight(1f)
                        )
                        Switch(
                            checked = isDarkTheme,
                            onCheckedChange = {
                                isDarkTheme = !isDarkTheme
                                onUpdateTheme(isDarkTheme)
                            },
                            thumbContent = if (isDarkTheme) {
                                {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(12.dp),
                                    )
                                }
                            } else {
                                null
                            }

                        )
                    }
                    HorizontalDivider(thickness = 1.dp)
                    Text(
                        text = stringResource(id = R.string.clear_cache),
                        modifier = Modifier
                            .clickable {
                            }
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(20.dp)
                    )
                    HorizontalDivider(thickness = 1.dp)
                }
            }
        }
    }
}