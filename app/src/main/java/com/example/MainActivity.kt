package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.MainNavigationTab
import com.example.viewmodel.MathGrade3ViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MathGrade3ViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val snackbarHostState = remember { SnackbarHostState() }

                LaunchedEffect(uiState.snackbarMessage) {
                    uiState.snackbarMessage?.let { msg ->
                        snackbarHostState.showSnackbar(msg)
                        viewModel.clearSnackbar()
                    }
                }

                // Handle back navigation to Home if not already in Home
                BackHandler(enabled = uiState.currentTab != MainNavigationTab.HOME) {
                    viewModel.navigateToTab(MainNavigationTab.HOME)
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    bottomBar = {
                        NavigationBar(
                            modifier = Modifier.testTag("main_bottom_nav"),
                            containerColor = MaterialTheme.colorScheme.surface,
                            tonalElevation = 6.dp
                        ) {
                            NavigationBarItem(
                                selected = uiState.currentTab == MainNavigationTab.HOME,
                                onClick = { viewModel.navigateToTab(MainNavigationTab.HOME) },
                                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                                label = { Text("ទំព័រដើម", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                                modifier = Modifier.testTag("nav_item_home")
                            )

                            NavigationBarItem(
                                selected = uiState.currentTab == MainNavigationTab.GENERATE_HOMEWORK,
                                onClick = { viewModel.navigateToTab(MainNavigationTab.GENERATE_HOMEWORK) },
                                icon = { Icon(Icons.Default.Assignment, contentDescription = "Homework") },
                                label = { Text("កិច្ចការផ្ទះ", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                                modifier = Modifier.testTag("nav_item_homework")
                            )

                            NavigationBarItem(
                                selected = uiState.currentTab == MainNavigationTab.GENERATE_EXAM,
                                onClick = { viewModel.navigateToTab(MainNavigationTab.GENERATE_EXAM) },
                                icon = { Icon(Icons.Default.Description, contentDescription = "Exams") },
                                label = { Text("វិញ្ញាសា", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                                modifier = Modifier.testTag("nav_item_exam")
                            )

                            NavigationBarItem(
                                selected = uiState.currentTab == MainNavigationTab.TEACHING_GUIDE,
                                onClick = { viewModel.navigateToTab(MainNavigationTab.TEACHING_GUIDE) },
                                icon = { Icon(Icons.Default.MenuBook, contentDescription = "Teaching Guide") },
                                label = { Text("មគ្គុទ្ទេសក៍", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                                modifier = Modifier.testTag("nav_item_guide")
                            )

                            NavigationBarItem(
                                selected = uiState.currentTab == MainNavigationTab.SAVED_WORKSHEETS,
                                onClick = { viewModel.navigateToTab(MainNavigationTab.SAVED_WORKSHEETS) },
                                icon = { Icon(Icons.Default.FolderSpecial, contentDescription = "Saved") },
                                label = { Text("បានរក្សាទុក", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                                modifier = Modifier.testTag("nav_item_saved")
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        when (uiState.currentTab) {
                            MainNavigationTab.HOME -> {
                                HomeScreen(viewModel = viewModel, uiState = uiState)
                            }
                            MainNavigationTab.GENERATE_HOMEWORK -> {
                                WorksheetGeneratorScreen(viewModel = viewModel, uiState = uiState)
                            }
                            MainNavigationTab.GENERATE_EXAM -> {
                                ExamGeneratorScreen(viewModel = viewModel, uiState = uiState)
                            }
                            MainNavigationTab.TEACHING_GUIDE -> {
                                TeachingGuideScreen()
                            }
                            MainNavigationTab.SAVED_WORKSHEETS -> {
                                SavedWorksheetsScreen(viewModel = viewModel, uiState = uiState)
                            }
                        }
                    }
                }
            }
        }
    }
}
