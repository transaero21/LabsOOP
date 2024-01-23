package ru.transaero21.mt.models.core

enum class Team(val alias: String) {
    Left(alias = "Blue"),
    Right(alias = "Green")
}

fun Team.getAnother() = when (this) {
    Team.Left -> Team.Right
    Team.Right -> Team.Left
}
