package ru.transaero21.mt.ui.screens

sealed class Screen {
    data object MainMenu : Screen()
    data object Gameplay : Screen()
}