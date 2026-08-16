package com.example.taskflow.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.taskflow.data.local.TaskDatabase
import com.example.taskflow.data.repository.TaskRepository
import com.example.taskflow.model.Priority
import com.example.taskflow.model.TaskStatistics
import com.example.taskflow.notifications.NotificationPreferences
import com.example.taskflow.ui.screens.AddTaskScreen
import com.example.taskflow.ui.screens.EditTaskScreen
import com.example.taskflow.ui.screens.ForgotPasswordScreen
import com.example.taskflow.ui.screens.HomeScreen
import com.example.taskflow.ui.screens.LoginScreen
import com.example.taskflow.ui.screens.OnboardingScreen
import com.example.taskflow.ui.screens.SignUpScreen
import com.example.taskflow.ui.screens.SplashScreen
import com.example.taskflow.ui.screens.StatisticsScreen
import com.example.taskflow.ui.screens.TaskDetailsScreen
import com.example.taskflow.utils.SessionManager
import com.example.taskflow.viewmodel.TaskViewModel
import com.example.taskflow.viewmodel.TaskViewModelFactory
import com.example.taskflow.ui.screens.CalendarScreen
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.taskflow.ui.screens.SettingsScreen
import com.example.taskflow.ui.theme.ThemeMode
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.taskflow.notifications.ReminderScheduler
import com.example.taskflow.ui.screens.settings.AboutScreen
import com.example.taskflow.ui.screens.settings.HelpScreen
import com.example.taskflow.ui.screens.settings.PrivacyPolicyScreen
import androidx.compose.ui.platform.LocalContext
import android.media.MediaPlayer
import com.example.taskflow.R
import com.example.taskflow.notifications.NotificationHelper
private object Routes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val LOGIN = "login"
    const val SIGN_UP = "sign_up"
    const val FORGOT_PASSWORD = "forgot_password"
    const val HOME = "home"
    const val ADD_TASK = "add_task"
    const val TASK_DETAILS = "task_details"
    const val EDIT_TASK = "edit_task"
    const val STATISTICS = "statistics"
    const val CALENDAR = "calendar"
    const val SETTINGS = "settings"
    const val ABOUT = "about"
    const val HELP = "help"
    const val PRIVACY = "privacy"
}

private fun NavHostController.navigateBottomBar(route: String) {
    navigate(route) {
        popUpTo(graph.startDestinationId) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
fun AppNavigation(
    themeMode: ThemeMode,
    onThemeChanged: (ThemeMode) -> Unit,
    navController: NavHostController = rememberNavController()
) {

    val context = LocalContext.current

    val notificationPreferences = remember(context) {
        NotificationPreferences(context.applicationContext)
    }

    val sessionManager = remember(context) {
        SessionManager(
            context.applicationContext
        )
    }

    val taskViewModelFactory = remember(context) {
        val database = TaskDatabase.getInstance(
            context.applicationContext
        )

        val repository = TaskRepository(
            database.taskDao()
        )

        TaskViewModelFactory(
            repository
        )
    }

    val taskViewModel: TaskViewModel = viewModel(
        factory = taskViewModelFactory
    )

    val reminderScheduler = remember(context) {
        ReminderScheduler(
            context.applicationContext
        )
    }

    taskViewModel.attachReminderScheduler(
        reminderScheduler
    )
    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {
        composable(
            route = Routes.SPLASH
        ) {
            SplashScreen(
                onTimeout = {
                    val destination = when {
                        sessionManager.isLoggedIn() -> {
                            Routes.HOME
                        }
                        sessionManager.isOnboardingCompleted() -> {
                            Routes.LOGIN
                        }
                        else -> {
                            Routes.ONBOARDING
                        }
                    }

                    navController.navigate(destination) {
                        popUpTo(Routes.SPLASH) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                }
            )
        }
        composable(
            route = Routes.ONBOARDING
        ) {
            OnboardingScreen(
                onSkip = {
                    sessionManager.setOnboardingCompleted(true)

                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.ONBOARDING) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                },
                onGetStarted = {
                    sessionManager.setOnboardingCompleted(true)

                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.ONBOARDING) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                }
            )
        }
        composable(
            route = Routes.LOGIN
        ) {
            LoginScreen(
                onLoginSuccess = {
                    sessionManager.setLoggedIn(true)

                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                },
                onCreateAccount = {
                    navController.navigate(Routes.SIGN_UP) {
                        launchSingleTop = true
                    }
                },
                onForgotPassword = {
                    navController.navigate(
                        Routes.FORGOT_PASSWORD
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(
            route = Routes.SIGN_UP
        ) {
            SignUpScreen(
                onAccountCreated = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.SIGN_UP) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                },
                onSignInClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Routes.FORGOT_PASSWORD
        ) {
            ForgotPasswordScreen(
                onPasswordUpdated = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.FORGOT_PASSWORD) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                },
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Routes.HOME
        ) {
            HomeScreen(
                taskViewModel = taskViewModel,
                onAddTaskClick = {
                    navController.navigate(Routes.ADD_TASK) {
                        launchSingleTop = true
                    }
                },
                onTaskClick = { taskId ->
                    navController.navigate(
                        "${Routes.TASK_DETAILS}/$taskId"
                    )
                },
                onCalendarClick = {
                    navController.navigateBottomBar(Routes.CALENDAR)
                },
                onStatisticsClick = {
                    navController.navigateBottomBar(Routes.STATISTICS)
                },
                onSettingsClick = {
                    navController.navigateBottomBar(Routes.SETTINGS)
                },
                onNotificationsClick = {
                    // Notifications route will be added later.
                }
            )
        }

        composable(
            route = Routes.ADD_TASK
        ) {
            AddTaskScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onSaveTask = { newTask ->
                    taskViewModel.addTask(newTask)
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "${Routes.TASK_DETAILS}/{taskId}",
            arguments = listOf(
                navArgument("taskId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val taskId = backStackEntry.arguments
                ?.getInt("taskId")
                ?: return@composable

            val task by taskViewModel
                .observeTaskById(taskId)
                .collectAsStateWithLifecycle(
                    initialValue = null
                )

            TaskDetailsScreen(
                task = task,
                onBackClick = {
                    navController.popBackStack()
                },
                onEditClick = { id ->
                    navController.navigate(
                        "${Routes.EDIT_TASK}/$id"
                    )
                },
                onDeleteClick = { taskToDelete ->
                    taskViewModel.deleteTask(
                        taskToDelete
                    )

                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) {
                            inclusive = false
                        }

                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = "${Routes.EDIT_TASK}/{taskId}",
            arguments = listOf(
                navArgument("taskId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val taskId = backStackEntry.arguments
                ?.getInt("taskId")
                ?: return@composable

            val task by taskViewModel
                .observeTaskById(taskId)
                .collectAsStateWithLifecycle(
                    initialValue = null
                )

            EditTaskScreen(
                task = task,
                onBackClick = {
                    navController.popBackStack()
                },
                onUpdateTask = { updatedTask ->
                    taskViewModel.updateTask(
                        updatedTask
                    )

                    navController.popBackStack()
                }
            )
        }
        composable(
            route = Routes.CALENDAR
        ) {
            val uiState by taskViewModel
                .uiState
                .collectAsStateWithLifecycle()

            CalendarScreen(
                tasks = uiState.allTasks,
                onTaskClick = { taskId ->
                    navController.navigate(
                        "${Routes.TASK_DETAILS}/$taskId"
                    )
                },
                onTasksClick = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) {
                            inclusive = false
                        }

                        launchSingleTop = true
                    }
                },
                onStatsClick = {
                    navController.navigateBottomBar(Routes.STATISTICS)
                },
                onSettingsClick = {
                    navController.navigateBottomBar(Routes.SETTINGS)
                }
            )
        }
        composable(
            route = Routes.SETTINGS
        ) {
            val notificationPreferences = remember(context) {
                NotificationPreferences(
                    context.applicationContext
                )
            }

            var notificationsEnabled by rememberSaveable {
                mutableStateOf(
                    notificationPreferences.notificationsEnabled
                )
            }

            var vibrationEnabled by rememberSaveable {
                mutableStateOf(
                    notificationPreferences.vibrationEnabled
                )
            }

            var notificationSound by rememberSaveable {
                mutableStateOf(
                    notificationPreferences.notificationSound
                )
            }

            var defaultReminder by rememberSaveable {
                mutableStateOf(
                    notificationPreferences.defaultReminder
                )
            }

            var snoozeDuration by rememberSaveable {
                mutableStateOf(
                    notificationPreferences.snoozeDuration
                )
            }

            SettingsScreen(
                selectedTheme = themeMode,

                notificationsEnabled = notificationsEnabled,
                vibrationEnabled = vibrationEnabled,
                notificationSound = notificationSound,
                defaultReminder = "$defaultReminder min",
                snoozeDuration = "$snoozeDuration min",

                onThemeChanged = onThemeChanged,

                onNotificationsChanged = { enabled ->
                    notificationsEnabled = enabled
                    notificationPreferences.notificationsEnabled = enabled

                    if (!enabled) {
                        ReminderScheduler(
                            context.applicationContext
                        ).cancelAllReminders()
                    }
                },

                onNotificationsClick = {
                    val newValue = !notificationsEnabled

                    notificationsEnabled = newValue
                    notificationPreferences.notificationsEnabled = newValue

                    if (!newValue) {
                        ReminderScheduler(
                            context.applicationContext
                        ).cancelAllReminders()
                    }
                },
                onNotificationSoundClick = {
                    // SettingsScreen opens the sound selection dialog.
                },
                onNotificationSoundChanged = { sound ->
                    notificationSound = sound
                    notificationPreferences.notificationSound = sound

                    NotificationHelper.previewSound(
                        context = context,
                        selectedSound = sound
                    )
                },
                onVibrationChanged = { enabled ->
                    vibrationEnabled = enabled
                    notificationPreferences.vibrationEnabled = enabled
                },

                onVibrationClick = {
                    if (notificationsEnabled) {
                        vibrationEnabled = !vibrationEnabled
                        notificationPreferences.vibrationEnabled =
                            vibrationEnabled
                    }
                },

                onDefaultReminderClick = {
                    defaultReminder =
                        when (defaultReminder) {
                            10 -> 30
                            30 -> 60
                            60 -> 1440
                            else -> 10
                        }

                    notificationPreferences.defaultReminder =
                        defaultReminder
                },
                onSnoozeDurationClick = {
                    snoozeDuration =
                        when (snoozeDuration) {
                            5 -> 10
                            10 -> 15
                            15 -> 30
                            else -> 5
                        }

                    notificationPreferences.snoozeDuration =
                        snoozeDuration
                },
                onAboutClick = {
                    navController.navigate(Routes.ABOUT)
                },

                onPrivacyPolicyClick = {
                    navController.navigate(Routes.PRIVACY)
                },

                onHelpClick = {
                    navController.navigate(Routes.HELP)
                },

                onTasksClick = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) {
                            inclusive = false
                        }

                        launchSingleTop = true
                    }
                },
                onCalendarClick = {
                    navController.navigateBottomBar(
                        Routes.CALENDAR
                    )
                },

                onStatisticsClick = {
                    navController.navigateBottomBar(
                        Routes.STATISTICS
                    )
                }
            )
        }
        composable(
            route = Routes.ABOUT
        ) {
            AboutScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Routes.HELP
        ) {
            HelpScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Routes.PRIVACY
        ) {
            PrivacyPolicyScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = Routes.STATISTICS
        ) {
            val uiState by taskViewModel
                .uiState
                .collectAsStateWithLifecycle()

            val allTasks = uiState.allTasks

            val currentTime =
                System.currentTimeMillis()

            val totalTasks =
                allTasks.size

            val completedTasks =
                allTasks.count { task ->
                    task.completed
                }

            val pendingTasks =
                allTasks.count { task ->
                    !task.completed
                }

            val overdueTasks =
                allTasks.count { task ->
                    !task.completed &&
                            task.dueDateMillis < currentTime
                }

            val highPriorityTasks =
                allTasks.count { task ->
                    task.priority == Priority.HIGH
                }

            val mediumPriorityTasks =
                allTasks.count { task ->
                    task.priority == Priority.MEDIUM
                }

            val lowPriorityTasks =
                allTasks.count { task ->
                    task.priority == Priority.LOW
                }

            val completionPercentage =
                if (totalTasks == 0) {
                    0f
                } else {
                    completedTasks * 100f /
                            totalTasks
                }
            StatisticsScreen(
                statistics = TaskStatistics(
                    totalTasks = totalTasks,
                    completedTasks = completedTasks,
                    pendingTasks = pendingTasks,
                    highPriorityTasks = highPriorityTasks,
                    mediumPriorityTasks = mediumPriorityTasks,
                    lowPriorityTasks = lowPriorityTasks,
                    completionPercentage =
                        completionPercentage
                ),
                overdueTasks = overdueTasks,
                tasksForGraph = allTasks,
                onTasksClick = {
                    navController.navigate(
                        Routes.HOME
                    ) {
                        popUpTo(Routes.HOME) {
                            inclusive = false
                        }

                        launchSingleTop = true
                    }
                },
                onCalendarClick = {
                    navController.navigateBottomBar(Routes.CALENDAR)
                },
                onSettingsClick = {
                    navController.navigateBottomBar(Routes.SETTINGS)
                }

            )
        }
    }
}