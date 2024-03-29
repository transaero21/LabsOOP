package ru.transaero21.mt.ui.screens.game

import com.badlogic.gdx.*
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.ScreenViewport
import ktx.actors.stage
import ktx.scene2d.*
import ktx.scene2d.label
import ktx.scene2d.table
import ktx.scene2d.textButton
import ru.transaero21.mt.models.core.GameInfo
import ru.transaero21.mt.models.core.Headquarter
import ru.transaero21.mt.models.core.Team
import ru.transaero21.mt.models.core.WorldSize
import ru.transaero21.mt.models.core.orders.Order
import ru.transaero21.mt.models.units.fighters.Fighter
import ru.transaero21.mt.network.Command
import ru.transaero21.mt.network.NetworkManager
import ru.transaero21.mt.ui.screens.game.WorldMap.Companion.TILE_LENGTH
import ru.transaero21.mt.ui.screens.game.WorldMap.Companion.TILE_WIDTH
import ru.transaero21.mt.ui.screens.game.helpers.FrameHelper
import ru.transaero21.mt.ui.screens.game.helpers.GameHelper
import ru.transaero21.mt.ui.screens.game.helpers.InputHelper.handleDrag
import ru.transaero21.mt.ui.screens.game.helpers.InputHelper.handleResize
import ru.transaero21.mt.ui.screens.game.helpers.InputHelper.handleZoom
import ru.transaero21.mt.ui.screens.game.windows.FighterWindow
import ru.transaero21.mt.ui.screens.game.windows.FormationWindow
import ru.transaero21.mt.ui.screens.game.windows.StaffWindow
import ru.transaero21.mt.utils.SCREEN_HEIGHT
import ru.transaero21.mt.utils.SCREEN_WIDTH
import ru.transaero21.mt.utils.onClick
import java.sql.Timestamp
import java.time.Instant
import kotlin.math.floor

class GameScreen(
    val startGameAt: Long, val worldSize: WorldSize
) : ScreenAdapter() {
    val mapWidth: Float = worldSize.width * TILE_WIDTH
    val mapLength: Float = worldSize.length * TILE_LENGTH

    private val batch = SpriteBatch()

    private val gameInfo: GameInfo = GameHelper.getNewGame(mapWidth = mapWidth, mapLength = mapLength, timestamp = startGameAt)
    private val team: Team = if (NetworkManager.isHost) Team.Left else Team.Right
    private val worldMap = WorldMap(gameInfo = gameInfo, worldSize = worldSize, selfTeam = team)

    private val worldMapCamera = OrthographicCamera(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
    private val cameraViewport = ScreenViewport(worldMapCamera)

    private val hudCamera = OrthographicCamera(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
    private val hudViewport = ScreenViewport(hudCamera)
    private val hud = stage(viewport = hudViewport, batch = batch)

    private val teamLabel: Label
    private val timerLabel: Label
    private val loadingLabel: Label
    private val staffWindow = StaffWindow(staffMap = getSelfHeadquarter().commander.staff)
    private val fighterWindow = FighterWindow()
    private val formationWindows = FormationWindow(
        fieldCommanders = getSelfHeadquarter().commander.fieldCommanders,
        commanderSelectedListener = {
            NetworkManager.sendCommandHost(
                command = Command.SuggestOrder(
                    order = preOrder?.apply { fcId = it }!!,
                    isLeft = NetworkManager.isHost
                )
            )
        }
    )

    private var selected: Pair<Float, Float>? = null
    private var preOrder: Order? = null

    private val withoutBuildingBlocks: Table

    init {
        initInputProcessor()

        hud.actors {
            withoutBuildingBlocks = table {
                Order::class.sealedSubclasses.forEach { clazz ->
                    textButton(text = clazz.simpleName!!) { cell ->
                        cell.colspan(1).row()
                        onClick {
                            this@table.isVisible = false
                            formationWindows.isVisible = true
                            preOrder = clazz.constructors.first { it.parameters.isNotEmpty() }
                                .call(selected!!.first, selected!!.second, 0)
                        }
                    }
                }
                isVisible = false
                pack()
            }
            teamLabel = label(text = "Your team: ${team.alias}")
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

    override fun show() {
        hud.addActor(staffWindow)
        hud.addActor(fighterWindow)
        hud.addActor(formationWindows)
    }

    override fun resize(width: Int, height: Int) {
        cameraViewport.update(width, height, false)
        worldMapCamera.handleResize(mapWidth = mapWidth, mapLength = mapLength)
        hudViewport.update(width, height, true)
        timerLabel.apply { setPosition((hud.width - this.width) / 2, hud.height - this.height) }
        teamLabel.apply { setPosition(4f, hud.height - this.height) }
        loadingLabel.apply { setPosition((hud.width - this.width) / 2, (hud.height - this.height) / 2) }
        staffWindow.setPosition(0f, 0f)
        fighterWindow.apply { setPosition(hud.width - this.width, hud.height - this.height) }
        formationWindows.apply { setPosition((hud.width - this.width) / 2, (hud.height - this.height) / 2) }
    }

    override fun dispose() {
        batch.dispose()
        hud.dispose()
    }

    private fun renderWorld(delta: Float) {
        FrameHelper.update(delta = delta, gameInfo = gameInfo)
        batch.setProjectionMatrix(worldMapCamera.combined)
        worldMap.render(delta = delta, batch = batch)
        timerLabel.setText(getTimerText())
        staffWindow.render()
        fighterWindow.render()
        formationWindows.render()
    }

    private fun renderWait() {
        val diff = (startGameAt - Timestamp.from(Instant.now()).time) / 1000F
        if (diff <= 0) {
            FrameHelper.resetGame()
            if (NetworkManager.isHost) {
                worldMapCamera.position.set(0f, 0f, 0f)
            } else {
                worldMapCamera.position.set(worldSize.width * TILE_WIDTH, worldSize.length * TILE_LENGTH, 0f)
            }
            loadingLabel.isVisible = false
            staffWindow.isVisible = true
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
                            worldMap.selfSprites.forEach { (_, fc) ->
                                if (isFighterClicked(click = worldPos, fighter = fc.first.fighter)) {
                                    return true
                                }
                                fc.second.forEach { (_, f) ->
                                    if (isFighterClicked(click = worldPos, fighter = f.fighter))  {
                                        return true
                                    }
                                }
                            }
                        }
                        Input.Buttons.RIGHT -> {
                            if (worldPos.x in 0f..mapWidth && worldPos.y in 0f..mapLength) {
                                this@GameScreen.withoutBuildingBlocks.apply {
                                    setPosition(hudPos.x, hudPos.y)
                                    selected = worldPos.x to worldPos.y
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

    private fun isFighterClicked(click: Vector3, fighter: Fighter): Boolean {
        val (dx, dy) = click.x - (fighter.x - fighter.uniform.width / 2) to click.y - (fighter.y - fighter.uniform.length / 2)
        if (dx in 0f .. fighter.uniform.width && dy in 0f .. fighter.uniform.length) {
            fighterWindow.fighter = fighter
            return true
        }
        return false
    }

    private fun hideButtons() {
        this@GameScreen.withoutBuildingBlocks.isVisible = false
    }

    private fun getTimerText(): String {
        val minutes = floor(x = gameInfo.timeLeft / 60).toInt()
        val seconds = floor(x = gameInfo.timeLeft - minutes * 60).toInt()
        return "${if (minutes > 0) "$minutes m." else ""} $seconds s."
    }

    private fun getSelfHeadquarter(): Headquarter {
        return gameInfo.headquarters.let { if (NetworkManager.isHost) it.first else it.second }
    }
}
