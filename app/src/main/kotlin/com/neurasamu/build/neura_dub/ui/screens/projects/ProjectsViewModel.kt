package com.neurasamu.build.neura_dub.ui.screens.projects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neurasamu.build.neura_dub.data.local.ProjectEntity
import com.neurasamu.build.neura_dub.data.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CreateProjectInput(
    val title: String,
    val language: String,
    val framerateMilli: Int,
    val loudnessTarget: String,
    val licenseConfirmed: Boolean
)

@HiltViewModel
class ProjectsViewModel @Inject constructor(
    private val repository: ProjectRepository
) : ViewModel() {

    val projects: StateFlow<List<ProjectEntity>> = repository.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun create(input: CreateProjectInput, onCreated: (String) -> Unit) {
        viewModelScope.launch {
            val id = repository.create(
                title = input.title,
                language = input.language,
                framerateMilli = input.framerateMilli,
                loudnessTarget = input.loudnessTarget,
                licenseConfirmed = input.licenseConfirmed
            )
            onCreated(id)
        }
    }

    fun delete(id: String) {
        viewModelScope.launch { repository.delete(id) }
    }
}
