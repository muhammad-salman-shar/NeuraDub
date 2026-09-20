package com.neurasamu.build.neura_dub.ui.screens.project.script

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.neurasamu.build.neura_dub.data.local.CharacterEntity
import com.neurasamu.build.neura_dub.data.local.LineEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLineDialog(
    characters: List<CharacterEntity>,
    onDismiss: () -> Unit,
    onConfirm: (
        timeInMs: Int,
        timeOutMs: Int,
        markerType: String,
        characterId: String?,
        originalText: String,
        literalText: String,
        adaptedText: String
    ) -> Unit
) {
    var tcIn by remember { mutableStateOf("0") }
    var tcOut by remember { mutableStateOf("2000") }
    var marker by remember { mutableStateOf(LineEntity.MARKER_SYNC) }
    var characterId by remember { mutableStateOf<String?>(characters.firstOrNull()?.id) }
    var original by remember { mutableStateOf("") }
    var literal by remember { mutableStateOf("") }
    var adapted by remember { mutableStateOf("") }

    var markerExpanded by remember { mutableStateOf(false) }
    var charExpanded by remember { mutableStateOf(false) }

    val tcInVal = tcIn.toIntOrNull()
    val tcOutVal = tcOut.toIntOrNull()
    val canSave = tcInVal != null && tcOutVal != null && tcOutVal > tcInVal &&
        (original.isNotBlank() || adapted.isNotBlank())

    val charLabel = characters.firstOrNull { it.id == characterId }?.name ?: "none"

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Line") },
        text = {
            Column(modifier = Modifier.fillMaxWidth().padding(top = 4.dp)) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = tcIn,
                        onValueChange = { tcIn = it.filter { c -> c.isDigit() } },
                        label = { Text("TC In (ms)") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(8.dp))
                    OutlinedTextField(
                        value = tcOut,
                        onValueChange = { tcOut = it.filter { c -> c.isDigit() } },
                        label = { Text("TC Out (ms)") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(Modifier.height(12.dp))

                ExposedDropdownMenuBox(
                    expanded = markerExpanded,
                    onExpandedChange = { markerExpanded = !markerExpanded }
                ) {
                    OutlinedTextField(
                        value = marker,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Marker") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = markerExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = markerExpanded,
                        onDismissRequest = { markerExpanded = false }
                    ) {
                        LineEntity.MARKERS.forEach { m ->
                            DropdownMenuItem(
                                text = { Text(m) },
                                onClick = { marker = m; markerExpanded = false }
                            )
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))

                ExposedDropdownMenuBox(
                    expanded = charExpanded,
                    onExpandedChange = { charExpanded = !charExpanded }
                ) {
                    OutlinedTextField(
                        value = charLabel,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Character") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = charExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = charExpanded,
                        onDismissRequest = { charExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("none") },
                            onClick = { characterId = null; charExpanded = false }
                        )
                        characters.forEach { c ->
                            DropdownMenuItem(
                                text = { Text(c.name) },
                                onClick = { characterId = c.id; charExpanded = false }
                            )
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = original,
                    onValueChange = { original = it },
                    label = { Text("Original text") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = literal,
                    onValueChange = { literal = it },
                    label = { Text("Literal translation") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = adapted,
                    onValueChange = { adapted = it },
                    label = { Text("Adapted (dub) text") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = canSave,
                onClick = {
                    onConfirm(
                        tcInVal!!,
                        tcOutVal!!,
                        marker,
                        characterId,
                        original,
                        literal,
                        adapted
                    )
                }
            ) { Text("Add") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
