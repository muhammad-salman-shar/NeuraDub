package com.neurasamu.build.neura_dub.ui.screens.project.script

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScriptTab(
    framerateMilli: Int,
    viewModel: ScriptViewModel = hiltViewModel()
) {
    val scenes by viewModel.scenes.collectAsStateWithLifecycle()
    val characters by viewModel.characters.collectAsStateWithLifecycle()
    val lines by viewModel.lines.collectAsStateWithLifecycle()
    val selectedSceneId by viewModel.selectedSceneId.collectAsStateWithLifecycle()

    var showAddScene by remember { mutableStateOf(false) }
    var showAddCharacter by remember { mutableStateOf(false) }
    var showAddLine by remember { mutableStateOf(false) }
    var sceneExpanded by remember { mutableStateOf(false) }

    val selectedScene = scenes.firstOrNull { it.id == selectedSceneId }

    Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
        // Scene selector row
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            ExposedDropdownMenuBox(
                expanded = sceneExpanded,
                onExpandedChange = { sceneExpanded = !sceneExpanded },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = selectedScene?.title ?: "No scenes yet",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Scene") },
                    trailingIcon = {
                        if (scenes.isNotEmpty()) {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = sceneExpanded)
                        }
                    },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = sceneExpanded,
                    onDismissRequest = { sceneExpanded = false }
                ) {
                    scenes.forEach { s ->
                        DropdownMenuItem(
                            text = { Text(s.title) },
                            onClick = { viewModel.selectScene(s.id); sceneExpanded = false }
                        )
                    }
                }
            }
            Spacer(Modifier.width(8.dp))
            OutlinedButton(onClick = { showAddScene = true }) { Text("+ Scene") }
        }

        Spacer(Modifier.height(8.dp))

        // Characters strip
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Characters: ${characters.size}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.weight(1f))
            OutlinedButton(onClick = { showAddCharacter = true }) { Text("+ Character") }
        }
        if (characters.isNotEmpty()) {
            Spacer(Modifier.height(6.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                items(characters, key = { it.id }) { c ->
                    FilterChip(
                        selected = false,
                        onClick = { },
                        label = { Text(c.name) }
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        if (scenes.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("No scenes yet", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Create a scene to start adding dialogue lines.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "${lines.size} lines",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.weight(1f))
                Button(
                    onClick = { showAddLine = true },
                    enabled = selectedScene != null
                ) { Text("+ Line") }
            }
            Spacer(Modifier.height(8.dp))

            if (lines.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "No lines in this scene yet.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(lines, key = { it.id }) { line ->
                        LineCard(
                            line = line,
                            character = characters.firstOrNull { it.id == line.characterId },
                            framerateMilli = framerateMilli,
                            onDelete = { viewModel.deleteLine(line.id) }
                        )
                    }
                }
            }
        }
    }

    if (showAddScene) {
        AddSceneDialog(
            onDismiss = { showAddScene = false },
            onConfirm = { title ->
                showAddScene = false
                viewModel.addScene(title) { }
            }
        )
    }
    if (showAddCharacter) {
        AddCharacterDialog(
            onDismiss = { showAddCharacter = false },
            onConfirm = { name, colorHex, voiceType ->
                showAddCharacter = false
                viewModel.addCharacter(name, colorHex, voiceType) { }
            }
        )
    }
    if (showAddLine) {
        AddLineDialog(
            characters = characters,
            onDismiss = { showAddLine = false },
            onConfirm = { tcIn, tcOut, marker, charId, orig, lit, adap ->
                showAddLine = false
                viewModel.addLine(tcIn, tcOut, marker, charId, orig, lit, adap) { }
            }
        )
    }
}
