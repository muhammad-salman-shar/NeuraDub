package com.neurasamu.build.neura_dub.ui.screens.project

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neurasamu.build.neura_dub.data.local.ProjectEntity
import com.neurasamu.build.neura_dub.data.repository.ProjectRepository
import com.neurasamu.build.neura_dub.ui.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProjectHomeViewModel @Inject constructor(
    repository: ProjectRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val projectId: String = savedStateHandle.get<String>(Routes.ARG_PROJECT_ID).orEmpty()

    val project: StateFlow<ProjectEntity?> = repository.observeById(projectId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
}
