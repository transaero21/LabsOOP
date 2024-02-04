package ru.transaero21.mt.ui.screens.menu

sealed class MenuWindow(val title: String) {
    data object MainMenu : MenuWindow(title = "Main menu")
    data object CreateGame : MenuWindow(title = "Create game")
    data object JoinGame : MenuWindow(title = "Join game")
    data object Game : MenuWindow(title = "Game")
}