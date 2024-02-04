package ru.transaero21.mt.models.core

enum class WorldSize(val width: Int, val length: Int) {
    Small(width = 12, length = 12),
    Medium(width = 16, length = 16),
    Big(width = 20, length = 20)
}