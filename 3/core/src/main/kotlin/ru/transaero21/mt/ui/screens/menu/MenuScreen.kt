package ru.transaero21.mt.ui.screens.menu

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.ScreenViewport
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ktx.actors.stage
import ktx.async.KtxAsync
import ktx.scene2d.StageWidget
import ktx.scene2d.actors
import ru.transaero21.mt.models.core.WorldSize
import ru.transaero21.mt.network.NetworkManager
import ru.transaero21.mt.ui.dialogs.errorDialog
import ru.transaero21.mt.ui.screens.menu.dialogs.connectingDialog
import ru.transaero21.mt.ui.screens.menu.windows.createGameWindow
import ru.transaero21.mt.ui.screens.menu.windows.gameWindow
import ru.transaero21.mt.ui.screens.menu.windows.joinGameWindow
import ru.transaero21.mt.ui.screens.menu.windows.mainMenuWindow
import kotlin.math.round

class MenuScreen(errorMsg: String = "") : ScreenAdapter() {
    private val batch = SpriteBatch()
    private val screenViewport = ScreenViewport()
    private val stage = stage(viewport = screenViewport, batch = batch).also { stage ->
        Gdx.input.inputProcessor = stage
    }

    private val windows: List<Window>

    private val mainMenuWindow: Window
    private val createRoomWindow: Window
    private val joinRoomWindow: Window
    private val roomWindow: Window

    init {
        stage.actors {
            mainMenuWindow = mainMenuWindow(
                onCreateRoomClicked = { setActiveWindow(window = MenuWindow.CreateGame) },
                onJoinRoomClicked = { setActiveWindow(window = MenuWindow.JoinGame) }
            )

            createRoomWindow = createGameWindow(
                onBackClicked = { setActiveWindow(window = MenuWindow.MainMenu) },
                onCreateClicked = { port, size -> onCreateRoomClicked(port = port, size = size) }
            )

            joinRoomWindow = joinGameWindow(
                onBackClick = { setActiveWindow(window = MenuWindow.MainMenu) },
                onJoinClick = { ipAddress, port ->
                    onJoinRoomClicked(ipAddress = ipAddress, port = port)
                }
            )

            roomWindow = gameWindow {
                NetworkManager.killAll()
                setActiveWindow(window = MenuWindow.MainMenu)
            }

            if (errorMsg.isNotEmpty()) {
                errorDialog(errorMsg = errorMsg).show(stage)
            }
        }

        windows = listOf(mainMenuWindow, createRoomWindow, joinRoomWindow, roomWindow)
        setActiveWindow(window = MenuWindow.MainMenu)
    }

    override fun render(delta: Float) {
        ScreenUtils.clear(Color.DARK_GRAY)
        screenViewport.apply()
        stage.act(delta)
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        screenViewport.update(width, height, true)
        windows.forEach { window ->
            window.setPosition(round((stage.width - window.width) / 2), round((stage.height - window.height) / 2))
        }
    }

    override fun dispose() {
        batch.dispose()
        stage.dispose()
    }

    private fun hideAllWindows() {
        windows.forEach { window ->
            window.isVisible = false
        }
    }

    private fun setActiveWindow(window: MenuWindow) {
        hideAllWindows()
        when (window) {
            MenuWindow.CreateGame -> createRoomWindow.isVisible = true
            MenuWindow.JoinGame -> joinRoomWindow.isVisible = true
            MenuWindow.MainMenu -> mainMenuWindow.isVisible = true
            MenuWindow.Game -> roomWindow.isVisible = true
        }
    }

    private fun StageWidget.onCreateRoomClicked(port: String, size: WorldSize) {
        connectingDialog { NetworkManager.isConnecting }.show(stage)
        try {
            NetworkManager.initializeAsHost(port, size)
            waitUnlessConnecting()
        } catch (e: Exception) {
            errorDialog(errorMsg = "Failed to initialize as host: ${e.message}").show(stage)
        }
    }

    private fun StageWidget.onJoinRoomClicked(ipAddress: String, port: String) {
        connectingDialog { NetworkManager.isConnecting }.show(stage)
        try {
            NetworkManager.initializeAsGuest(ipAddress = ipAddress, port = port)
            waitUnlessConnecting()
        } catch (e: Exception) {
            errorDialog(errorMsg = "Failed to initialize as guest: ${e.message}").show(stage)
        }
    }

    private fun StageWidget.waitUnlessConnecting() {
        KtxAsync.launch {
            while (NetworkManager.isConnecting) { delay(100L) }
            delay(timeMillis = 350L)
            if (NetworkManager.isConnected) {
                setActiveWindow(window = MenuWindow.Game)
            } else {
                errorDialog(errorMsg = "Failed to create or join room").show(stage)
            }
        }
    }
}
