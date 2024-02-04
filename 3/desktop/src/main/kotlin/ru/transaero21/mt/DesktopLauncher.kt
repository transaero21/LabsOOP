package ru.transaero21.mt

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import ru.transaero21.mt.utils.SCREEN_HEIGHT
import ru.transaero21.mt.utils.SCREEN_WIDTH

fun main() {
    val config = Lwjgl3ApplicationConfiguration().apply {
        setForegroundFPS(30)
        setWindowedMode(SCREEN_WIDTH, SCREEN_HEIGHT)
        setTitle("Military Training")
    }
    Lwjgl3Application(MainGame, config)
}
