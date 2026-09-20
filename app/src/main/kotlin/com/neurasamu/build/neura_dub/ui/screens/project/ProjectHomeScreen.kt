package com.neurasamu.build.neura_dub.ui.screens.project

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.neurasamu.build.neura_dub.ui.screens.project.script.ScriptTab

private val TAB_TITLES = listOf("Script", "Booth", "Review", "Export")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectHomeScreen(
    projectId: String,
    onBack: () -> Unit,
    viewModel: ProjectHomeViewModel = hiltViewModel()
) {
    val project by viewModel.project.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(project?.title ?: "Project")
                        project?.let {
                            Text(
                                text = "${it.language.replaceFirstChar { c -> c.uppercase() }} · ${it.framerateMilli / 1000.0} fps · ${it.loudnessTarget}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("<", style = MaterialTheme.typography.titleLarge)
                    }
                }
            )
        }
    ) { padding ->
        val p = project
        if (p == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Loading…", style = MaterialTheme.typography.bodyLarge)
            }
            return@Scaffold
        }

        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            TabRow(selectedTabIndex = selectedTab) {
                TAB_TITLES.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                when (selectedTab) {
                    0 -> ScriptTab(framerateMilli = p.framerateMilli)
                    1 -> PlaceholderTab("Booth — recorder coming soon")
                    2 -> PlaceholderTab("Review — director QC coming soon")
                    3 -> PlaceholderTab("Export — ZIP build coming soon")
                }
            }
        }
    }
}

@Composable
private fun PlaceholderTab(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
