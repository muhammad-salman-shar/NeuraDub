package com.neurasamu.build.neura_dub.ui.screens.project.script

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp

private val VOICE_TYPES = listOf("chest", "pharyngeal", "falsetto")
private val PALETTE = listOf(
    "#7C4DFF", "#03DAC6", "#FF4D8B", "#FFB300",
    "#4CAF50", "#FF7043", "#29B6F6", "#AB47BC"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCharacterDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, colorHex: String, voiceType: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var voiceType by remember { mutableStateOf(VOICE_TYPES.first()) }
    var colorHex by remember { mutableStateOf(PALETTE.first()) }
    var voiceExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Character") },
        text = {
            Column(modifier = Modifier.fillMaxWidth().padding(top = 4.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Character name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                ExposedDropdownMenuBox(
                    expanded = voiceExpanded,
                    onExpandedChange = { voiceExpanded = !voiceExpanded }
                ) {
                    OutlinedTextField(
                        value = voiceType,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Voice type") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = voiceExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = voiceExpanded,
                        onDismissRequest = { voiceExpanded = false }
                    ) {
                        VOICE_TYPES.forEach { vt ->
                            DropdownMenuItem(
                                text = { Text(vt) },
                                onClick = { voiceType = vt; voiceExpanded = false }
                            )
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
                Text("Color", modifier = Modifier.padding(bottom = 4.dp))
                androidx.compose.foundation.layout.Row(
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    PALETTE.forEach { hex ->
                        val selected = hex == colorHex
                        androidx.compose.foundation.Canvas(
                            modifier = Modifier
                                .height(if (selected) 40.dp else 32.dp)
                                .padding(2.dp)
                                .androidx.compose.foundation.layout.weight(1f)
                                .androidx.compose.foundation.clickable {
                                    colorHex = hex
                                }
                        ) {
                            drawCircle(
                                color = androidx.compose.ui.graphics.Color(
                                    android.graphics.Color.parseColor(hex)
                                )
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                enabled = name.isNotBlank(),
                onClick = { onConfirm(name.trim(), colorHex, voiceType) }
            ) { Text("Create") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
