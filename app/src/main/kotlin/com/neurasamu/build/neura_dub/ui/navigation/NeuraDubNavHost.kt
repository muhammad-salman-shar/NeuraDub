package com.neurasamu.build.neura_dub.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.neurasamu.build.neura_dub.ui.screens.project.ProjectHomeScreen
import com.neurasamu.build.neura_dub.ui.screens.projects.ProjectsScreen

@Composable
fun NeuraDubNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.PROJECTS
    ) {
        composable(Routes.PROJECTS) {
            ProjectsScreen(
                onOpenProject = { id ->
                    navController.navigate(Routes.projectHome(id))
                }
            )
        }
        composable(
            route = Routes.PROJECT_HOME,
            arguments = listOf(navArgument(Routes.ARG_PROJECT_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString(Routes.ARG_PROJECT_ID).orEmpty()
            ProjectHomeScreen(
                projectId = projectId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
