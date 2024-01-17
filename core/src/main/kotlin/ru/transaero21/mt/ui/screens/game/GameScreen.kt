package ru.transaero21.mt.ui.screens.game

import com.badlogic.gdx.*
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.ScreenViewport
import ktx.actors.stage
import ktx.scene2d.*
import ru.transaero21.mt.models.core.GameInfo
import ru.transaero21.mt.models.core.WorldSize
import ru.transaero21.mt.network.NetworkManager
import ru.transaero21.mt.ui.screens.game.WorldMap.Companion.TILE_LENGTH
import ru.transaero21.mt.ui.screens.game.WorldMap.Companion.TILE_WIDTH
import ru.transaero21.mt.ui.screens.game.helpers.FrameHelper
import ru.transaero21.mt.ui.screens.game.helpers.InputHelper.handleDrag
import ru.transaero21.mt.ui.screens.game.helpers.InputHelper.handleResize
import ru.transaero21.mt.ui.screens.game.helpers.InputHelper.handleZoom
import ru.transaero21.mt.utils.SCREEN_HEIGHT
import ru.transaero21.mt.utils.SCREEN_WIDTH
import java.sql.Timestamp
import java.time.Instant
import kotlin.math.floor

class GameScreen(
    val startGameAt: Long, val worldSize: WorldSize
) : ScreenAdapter() {
    val mapWidth: Float = worldSize.width * TILE_WIDTH
    val mapLength: Float = worldSize.length * TILE_LENGTH

    private val batch = SpriteBatch()

    private val gameInfo: GameInfo = getNewGame(mapWidth = mapWidth, mapLength = mapLength, timestamp = startGameAt)
    private val worldMap = WorldMap(gameInfo = gameInfo, worldSize = worldSize)

    private val worldMapCamera = OrthographicCamera(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat()).apply {
        if (NetworkManager.isHost)
            position.set(0f, 0f, 0f)
        else
            position.set(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f, 0f)
    }
    private val cameraViewport = ScreenViewport(worldMapCamera)

    private val hudCamera = OrthographicCamera(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
    private val hudViewport = ScreenViewport(hudCamera)
    private val hud = stage(viewport = hudViewport, batch = batch)

    private val button: TextButton
    private val timerLabel: Label
    private val loadingLabel: Label

    init {
        initInputProcessor()

        hud.actors {
            button = textButton(text = "Move").apply {
                isVisible = false
            }
            timerLabel = label(text = getTimerText())
            loadingLabel = label(text = "Game is loading, please wait")
        }
    }

    private var gameStarted = false

    override fun render(delta: Float) {
        ScreenUtils.clear(Color.SKY)

        cameraViewport.apply()
        batch.setProjectionMatrix(worldMapCamera.combined)
        worldMapCamera.update()

        batch.begin()
        if (gameStarted) {
            renderWorld(delta = delta)
        } else {
            renderWait()
        }
        batch.end()

        hudViewport.apply()
        hud.act(delta)
        hud.draw()
    }

    override fun resize(width: Int, height: Int) {
        cameraViewport.update(width, height, false)
        worldMapCamera.handleResize(mapWidth = mapWidth, mapLength = mapLength)
        hudViewport.update(width, height, true)
        timerLabel.apply { setPosition((hud.width - this.width) / 2, hud.height - this.height) }
        loadingLabel.apply { setPosition((hud.width - this.width) / 2, (hud.height - this.height) / 2) }
    }

    override fun dispose() {
        batch.dispose()
        hud.dispose()
    }

    private fun renderWorld(delta: Float) {
        FrameHelper.update(delta = delta, gameInfo = gameInfo)
        worldMap.render(delta = delta, batch = batch)
        timerLabel.setText(getTimerText())
    }

    private fun renderWait() {
        val diff = (startGameAt - Timestamp.from(Instant.now()).time) / 1000F
        if (diff <= 0) {
            if (NetworkManager.isHost) {
                worldMapCamera.position.set(0f, 0f, 0f)
            } else {
                worldMapCamera.position.set(worldSize.width * TILE_WIDTH, worldSize.length * TILE_LENGTH, 0f)
            }
            loadingLabel.isVisible = false
            gameStarted = true
        }
    }

    private fun initInputProcessor() {
        InputMultiplexer().let { input ->
            input.addProcessor(hud)
            input.addProcessor(object : InputAdapter() {
                override fun touchDragged(screenX: Int, screenY: Int, pointer: Int) : Boolean {
                    return worldMapCamera.handleDrag(mapWidth = mapWidth, mapLength = mapLength)
                }

                override fun scrolled(amountX: Float, amountY: Float): Boolean {
                    return worldMapCamera.handleZoom(amount = amountY, mapWidth = mapWidth, mapLength = mapLength)
                }

                override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
                    hideButtons()
                    val worldPos = Vector3(screenX.toFloat(), screenY.toFloat(),0f).also { vec ->
                        worldMapCamera.unproject(vec)
                    }
                    val hudPos = Vector3(screenX.toFloat(), screenY.toFloat(),0f).also { vec ->
                        hudCamera.unproject(vec)
                    }

                    when (button) {
                        Input.Buttons.LEFT -> {
                            worldMap.sprites.forEach {
                                val fighter = it.value.fighter
                                val (dx, dy) = worldPos.x - (fighter.x - fighter.uniform.width / 2) to worldPos.y - fighter.y
                                if (dx in 0f .. fighter.uniform.width && dy in 0f .. fighter.uniform.length) {
                                    return true
                                }
                            }
                        }
                        Input.Buttons.RIGHT -> {
                            if (worldPos.x in 0f..mapWidth && worldPos.y in 0f..mapLength) {
                                this@GameScreen.button.apply {
                                    setPosition(hudPos.x, hudPos.y)
                                    isVisible = true
                                }
                                return true
                            }
                        }
                    }

                    return false
                }
            })

            Gdx.input.inputProcessor = input
        }
    }

    private fun hideButtons() {
        this@GameScreen.button.isVisible = false
    }

    private fun getTimerText(): String {
        val minutes = floor(x = gameInfo.timeLeft / 60).toInt()
        val seconds = floor(x = gameInfo.timeLeft - minutes * 60).toInt()
        return "${if (minutes > 0) "$minutes m." else "0"} $seconds s."
    }
}
