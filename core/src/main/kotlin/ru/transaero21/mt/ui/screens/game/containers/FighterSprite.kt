package ru.transaero21.mt.ui.screens.game.containers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import ru.transaero21.mt.models.core.Team
import ru.transaero21.mt.models.core.instructions.Move
import ru.transaero21.mt.models.units.fighters.Fighter
import java.util.*
import kotlin.math.PI

class FighterSprite(val fighter: Fighter, team: Team) : Sprite() {
//    private val atlas = TextureAtlas(FIGHTER_ATLAS_FILE)
//    private val fighterTexture = atlas.findRegion(team.alias.lowercase(Locale.getDefault())).texture
    private val fighterTexture = Texture(Gdx.files.internal("img/${team.alias.lowercase(Locale.getDefault())}/fighter_move.png"))
    private var fighterFrames = split(
        fighterTexture,
        fighterTexture.width / FRAMES,
        fighterTexture.height / DIRECTIONS_COUNT
    )

    private var currentFrame = 0
    private var timePassed = 0f

    init {
        fighterFrames[0][0].let { region ->
            setRegion(fighterFrames[0][0])
            setSize(region.regionWidth.toFloat(), region.regionHeight.toFloat())
            setOrigin(width / 2, height / 2)
        }
    }

    fun update(delta: Float, batch: SpriteBatch) {
        val direction = getDirection()

        if (!fighter.hasUnfulfilledInstructions()) {
            setRegion(fighterFrames[direction][0])
            resetAnimation()
        } else {
            val current = fighter.getCurrentInstructions()
            when (current) {
                is Move -> {
                    setRegion(fighterFrames[direction][getCurrentFrame(delta = delta)])
                }

                else -> TODO()
            }
        }

        setPosition(fighter.x - fighter.uniform.width / 2, fighter.y)
        draw(batch)
    }

    private fun getDirection(): Int {
        if (fighter.angle <= PI / 8)
            return 6
        if (fighter.angle <= 3 * PI / 8)
            return 5
        if (fighter.angle <= 5 * PI / 8)
            return 4
        if (fighter.angle <= 7 * PI / 8)
            return 3
        if (fighter.angle <= 9 * PI / 8)
            return 2
        if (fighter.angle <= 11 * PI / 8)
            return 1
        if (fighter.angle <= 13 * PI / 8)
            return 0
        if (fighter.angle <= 15 * PI / 8)
            return 7
        return 6
    }

    private fun resetAnimation() {
        currentFrame = 0
        timePassed = 0f
    }

    private fun getCurrentFrame(delta: Float): Int {
        timePassed += delta
        while (timePassed >= SINGLE_FRAME_TIME) {
            timePassed -= SINGLE_FRAME_TIME
            currentFrame++
            if (currentFrame >= FRAMES) {
                currentFrame = 0
            }
        }
        return currentFrame
    }

    companion object {
        private const val FRAMES = 4
        private const val DIRECTIONS_COUNT = 8
        private const val FRAMES_PER_SECOND = 8
        private const val SINGLE_FRAME_TIME = 1f / FRAMES_PER_SECOND
        private const val FIGHTER_ATLAS_FILE = "img/fighter/running.atlas"
    }
}
