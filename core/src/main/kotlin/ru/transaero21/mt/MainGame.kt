package ru.transaero21.mt

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import ktx.async.KtxAsync
import ktx.scene2d.Scene2DSkin
import ru.transaero21.mt.models.core.WorldSize
import ru.transaero21.mt.ui.screens.Screen
import ru.transaero21.mt.ui.screens.game.GameScreen
import ru.transaero21.mt.ui.screens.menu.MenuScreen
import java.awt.Menu

object MainGame : Game() {
    private var futureScreen: Screen? = null
    private var futureArguments: Map<String, Any>? = null

    override fun create() {
        loadSkin()
        KtxAsync.initiate()
        setScreen(MenuScreen())
    }

    override fun render() {
        super.render()
        updateScreen()
    }

    private fun updateScreen() {
        val screen = when (futureScreen) {
            is Screen.Gameplay -> GameScreen(
                startGameAt = futureArguments?.get("startGameAt") as Long,
                worldSize = futureArguments?.get("worldSize") as WorldSize,
            )
            is Screen.MainMenu -> MenuScreen(errorMsg =  futureArguments?.get("errorMsg") as String)
            else -> null
        } ?: return
        setScreen(screen)
        futureScreen = null
        futureArguments = null
    }

    fun setScreen(screen: Screen, arguments: Map<String, Any> = mapOf()) {
        futureScreen = screen
        futureArguments = arguments.toMap()
    }

    private fun loadSkin() {
        Scene2DSkin.defaultSkin = Skin(Gdx.files.internal("ui/metal-ui.json"))
    }
}
