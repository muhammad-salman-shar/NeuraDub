package com.neurasamu.build.neura_dub.ui.navigation

object Routes {
    const val PROJECTS = "projects"
    const val PROJECT_HOME = "project/{projectId}"
    const val ARG_PROJECT_ID = "projectId"

    fun projectHome(projectId: String): String = "project/$projectId"
}
