package com.neurasamu.build.neura_dub.ui.screens.projects

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.window.DialogProperties

private val LANGUAGES = listOf("urdu", "sindhi", "hindi", "english")
private val FRAMERATES = listOf(
    "23.976" to 23976,
    "24.000" to 24000,
    "25.000" to 25000,
    "29.970" to 29970
)
private val LOUDNESS = listOf("netflix", "ebur", "youtube", "custom")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProjectDialog(
    onDismiss: () -> Unit,
    onCreate: (CreateProjectInput) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var language by remember { mutableStateOf(LANGUAGES.first()) }
    var framerate by remember { mutableStateOf(FRAMERATES.first()) }
    var loudness by remember { mutableStateOf(LOUDNESS.first()) }
    var license by remember { mutableStateOf(false) }

    var langExpanded by remember { mutableStateOf(false) }
    var fpsExpanded by remember { mutableStateOf(false) }
    var loudExpanded by remember { mutableStateOf(false) }

    val canCreate = title.isNotBlank() && license

    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        title = { Text("New Project") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Project title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))

                ExposedDropdownMenuBox(
                    expanded = langExpanded,
                    onExpandedChange = { langExpanded = !langExpanded }
                ) {
                    OutlinedTextField(
                        value = language,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Language") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = langExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = langExpanded,
                        onDismissRequest = { langExpanded = false }
                    ) {
                        LANGUAGES.forEach { opt ->
                            DropdownMenuItem(
                                text = { Text(opt) },
                                onClick = { language = opt; langExpanded = false }
                            )
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))

                ExposedDropdownMenuBox(
                    expanded = fpsExpanded,
                    onExpandedChange = { fpsExpanded = !fpsExpanded }
                ) {
                    OutlinedTextField(
                        value = framerate.first,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Framerate") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = fpsExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = fpsExpanded,
                        onDismissRequest = { fpsExpanded = false }
                    ) {
                        FRAMERATES.forEach { opt ->
                            DropdownMenuItem(
                                text = { Text(opt.first) },
                                onClick = { framerate = opt; fpsExpanded = false }
                            )
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))

                ExposedDropdownMenuBox(
                    expanded = loudExpanded,
                    onExpandedChange = { loudExpanded = !loudExpanded }
                ) {
                    OutlinedTextField(
                        value = loudness,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Loudness target") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = loudExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = loudExpanded,
                        onDismissRequest = { loudExpanded = false }
                    ) {
                        LOUDNESS.forEach { opt ->
                            DropdownMenuItem(
                                text = { Text(opt) },
                                onClick = { loudness = opt; loudExpanded = false }
                            )
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = license,
                        onCheckedChange = { license = it }
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "I confirm I have dubbing rights / license for this content (Copyright Ordinance 1962).",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                enabled = canCreate,
                onClick = {
                    onCreate(
                        CreateProjectInput(
                            title = title,
                            language = language,
                            framerateMilli = framerate.second,
                            loudnessTarget = loudness,
                            licenseConfirmed = license
                        )
                    )
                }
            ) { Text("Create") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
