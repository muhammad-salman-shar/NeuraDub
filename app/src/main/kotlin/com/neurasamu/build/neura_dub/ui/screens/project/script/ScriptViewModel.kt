package com.neurasamu.build.neura_dub.ui.screens.project.script

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neurasamu.build.neura_dub.data.local.CharacterEntity
import com.neurasamu.build.neura_dub.data.local.LineEntity
import com.neurasamu.build.neura_dub.data.local.SceneEntity
import com.neurasamu.build.neura_dub.data.repository.ScriptRepository
import com.neurasamu.build.neura_dub.ui.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ScriptViewModel @Inject constructor(
    private val repository: ScriptRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val projectId: String = savedStateHandle.get<String>(Routes.ARG_PROJECT_ID).orEmpty()

    val characters: StateFlow<List<CharacterEntity>> =
        repository.observeCharacters(projectId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val scenes: StateFlow<List<SceneEntity>> =
        repository.observeScenes(projectId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _selectedSceneId = MutableStateFlow<String?>(null)
    val selectedSceneId: StateFlow<String?> = _selectedSceneId.asStateFlow()

    val lines: StateFlow<List<LineEntity>> = _selectedSceneId
        .flatMapLatest { id ->
            if (id == null) flowOf(emptyList()) else repository.observeLines(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        viewModelScope.launch {
            scenes.collect { list ->
                if (_selectedSceneId.value == null && list.isNotEmpty()) {
                    _selectedSceneId.value = list.first().id
                } else if (_selectedSceneId.value != null && list.none { it.id == _selectedSceneId.value }) {
                    _selectedSceneId.value = list.firstOrNull()?.id
                }
            }
        }
    }

    fun selectScene(id: String) { _selectedSceneId.value = id }

    fun addScene(title: String, onDone: () -> Unit) {
        viewModelScope.launch {
            val id = repository.addScene(projectId, title)
            _selectedSceneId.value = id
            onDone()
        }
    }

    fun deleteScene(id: String) {
        viewModelScope.launch { repository.deleteScene(id) }
    }

    fun addCharacter(name: String, colorHex: String, voiceType: String, onDone: () -> Unit) {
        viewModelScope.launch {
            repository.addCharacter(projectId, name, colorHex, voiceType)
            onDone()
        }
    }

    fun addLine(
        timeInMs: Int,
        timeOutMs: Int,
        markerType: String,
        characterId: String?,
        originalText: String,
        literalText: String,
        adaptedText: String,
        onDone: () -> Unit
    ) {
        val sceneId = _selectedSceneId.value ?: return
        viewModelScope.launch {
            repository.addLine(
                sceneId = sceneId,
                timeInMs = timeInMs,
                timeOutMs = timeOutMs,
                markerType = markerType,
                characterId = characterId,
                originalText = originalText,
                literalText = literalText,
                adaptedText = adaptedText,
                status = LineEntity.STATUS_PENDING
            )
            onDone()
        }
    }

    fun deleteLine(id: String) {
        viewModelScope.launch { repository.deleteLine(id) }
    }

    fun updateLine(line: LineEntity) {
        viewModelScope.launch { repository.updateLine(line) }
    }
}
