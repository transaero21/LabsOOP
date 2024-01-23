package ru.transaero21.mt.ui.screens.game

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import ru.transaero21.map.OrderedMap
import ru.transaero21.mt.models.core.*
import ru.transaero21.mt.models.core.iterators.FighterIterator
import ru.transaero21.mt.ui.screens.game.containers.FighterSprite

class WorldMap(
    private val gameInfo: GameInfo,
    private val worldSize: WorldSize,
    private val selfTeam: Team
) {
    private val mapAtlas = TextureAtlas(MAP_ATLAS_FILE)

    val selfSprites: MutableMap<Int, FighterSprite> = createSpritesMap(isSelf = true)
    val enemySprites: MutableMap<Int, FighterSprite> = createSpritesMap(isSelf = false)

    private fun createSpritesMap(isSelf: Boolean): OrderedMap<Int, FighterSprite> {
        val headquarter = getHeadquarter(isSelf = isSelf)
        return OrderedMap<Int, FighterSprite>().also { map ->
            for (entry in FighterIterator(headquarter = headquarter)) {
                map[entry.key] = FighterSprite(fighter = entry.value, team = getTeam(isSelf = isSelf))
            }
        }
    }

    fun render(delta: Float, batch: SpriteBatch) {
        renderMap(batch = batch)
        renderFighters(map = selfSprites, isSelf = true, delta = delta, batch = batch)
        renderFighters(map = enemySprites, isSelf = false, delta = delta, batch = batch)
    }

    private fun renderFighters(map: MutableMap<Int, FighterSprite>, isSelf: Boolean, delta: Float, batch: SpriteBatch) {
        val ids: MutableList<Int> = mutableListOf()
        val headquarter = getHeadquarter(isSelf = isSelf)
        val team = getTeam(isSelf = isSelf)

        for (fighter in FighterIterator(headquarter = headquarter)) {
            (map[fighter.key] ?: FighterSprite(fighter = fighter.value, team = team).also { map[fighter.key] = it })
                .update(delta = delta, batch = batch)
            ids.add(fighter.key)
        }

        map.forEach { sprite ->
            if (!ids.contains(sprite.key))
                map.remove(sprite.key)
        }
    }

    private fun renderMap(batch: SpriteBatch) {
        for (row in 0 until worldSize.length) {
            for (col in 0 until worldSize.width) {
                val regionName = when (col) {
                    0 -> when (row) {
                        0 -> MAP_BOTTOM_LEFT
                        worldSize.length - 1 -> MAP_TOP_LEFT
                        else -> MAP_LEFT
                    }
                    worldSize.length - 1 -> when (row) {
                        0 -> MAP_BOTTOM_RIGHT
                        worldSize.length - 1 -> MAP_TOP_RIGHT
                        else -> MAP_RIGHT
                    }
                    else -> when (row) {
                        0 -> MAP_BOTTOM
                        worldSize.length - 1 -> MAP_TOP
                        else -> MAP_ANY
                    }
                }

                val atlasRegion = mapAtlas.findRegion(regionName)
                val (width, height) = atlasRegion.originalWidth.toFloat() to atlasRegion.originalHeight.toFloat()
                val (x, y) = when (regionName) {
                    MAP_BOTTOM_LEFT -> {
                        TILE_WIDTH - width to TILE_LENGTH - height
                    }
                    MAP_BOTTOM -> {
                        (col + 1) * TILE_WIDTH - width to TILE_LENGTH - height
                    }
                    MAP_BOTTOM_RIGHT -> {
                        col * TILE_WIDTH to TILE_LENGTH - height
                    }
                    MAP_LEFT, MAP_TOP_LEFT -> {
                        TILE_WIDTH - width to row * TILE_LENGTH
                    }
                    else -> col * TILE_WIDTH to row * TILE_LENGTH
                }

                batch.draw(atlasRegion, x, y, width, height)
            }
        }
    }

    private fun getTeam(isSelf: Boolean): Team = if (isSelf) selfTeam else selfTeam.getAnother()

    private fun getHeadquarter(isSelf: Boolean): Headquarter {
        return if (isSelf) {
            when (selfTeam) {
                Team.Left -> gameInfo.headquarters.first
                Team.Right -> gameInfo.headquarters.second
            }
        } else {
            when (selfTeam) {
                Team.Left -> gameInfo.headquarters.second
                Team.Right -> gameInfo.headquarters.first
            }
        }
    }

    companion object {
        private const val MAP_ATLAS_FILE = "img/map/map.atlas"
        private const val MAP_BOTTOM_LEFT = "bottom_left"
        private const val MAP_LEFT = "left"
        private const val MAP_TOP_LEFT = "top_left"
        private const val MAP_BOTTOM_RIGHT = "bottom_right"
        private const val MAP_RIGHT = "right"
        private const val MAP_TOP_RIGHT = "top_right"
        private const val MAP_TOP = "top"
        private const val MAP_BOTTOM = "bottom"
        private const val MAP_ANY = "any"
        const val TILE_WIDTH = 64f
        const val TILE_LENGTH = 64f
    }
}
