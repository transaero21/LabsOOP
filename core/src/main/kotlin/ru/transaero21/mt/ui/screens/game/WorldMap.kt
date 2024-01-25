package ru.transaero21.mt.ui.screens.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import ru.transaero21.map.OrderedMap
import ru.transaero21.mt.models.ammo.bullet.BigBullet
import ru.transaero21.mt.models.ammo.bullet.Bullet
import ru.transaero21.mt.models.ammo.bullet.SmallBullet
import ru.transaero21.mt.models.ammo.mine.Mine
import ru.transaero21.mt.models.core.*
import ru.transaero21.mt.models.units.fighters.FieldCommander
import ru.transaero21.mt.ui.screens.game.containers.FighterSprite
import kotlin.math.PI

typealias IdsMap = OrderedMap<Int, List<Int>>
typealias FightersMap = OrderedMap<Int, FighterSprite>
typealias FieldCommanderSlot = Pair<FighterSprite, FightersMap>
typealias FieldCommanderMap = OrderedMap<Int, FieldCommanderSlot>
typealias DeadList = MutableList<Pair<Float, Float>>

class WorldMap(
    private val gameInfo: GameInfo,
    private val worldSize: WorldSize,
    private val selfTeam: Team
) {
    private val mapAtlas = TextureAtlas(MAP_ATLAS_FILE)

    val selfSprites: FieldCommanderMap = createSpritesMapNew(isSelf = true)
    private val enemySprites: FieldCommanderMap = createSpritesMapNew(isSelf = false)

    private val deadList: DeadList = mutableListOf()

    private fun createSpritesMapNew(isSelf: Boolean): FieldCommanderMap {
        val headquarter = getHeadquarter(isSelf = isSelf)
        val team = getTeam(isSelf = isSelf)
        return FieldCommanderMap().also { fcMap ->
            headquarter.commander.fieldCommanders.forEach { (k, v) ->
                fcMap[k] = FighterSprite(fighter = v, team = team) to FightersMap().also { fMap ->
                    v.formation.fighters.forEach { (k, v) ->
                        fMap[k] = FighterSprite(fighter = v, team = team)
                    }
                }
            }
        }
    }

    fun render(delta: Float, batch: SpriteBatch) {
        renderMap(batch = batch)
        renderDead(batch = batch)
        renderFighters(map = selfSprites, isSelf = true, delta = delta, batch = batch)
        renderFighters(map = enemySprites, isSelf = false, delta = delta, batch = batch)
        renderBullets(hq = getHeadquarter(isSelf = true), team = selfTeam, batch = batch)
        renderBullets(hq = getHeadquarter(isSelf = false), team = selfTeam.getAnother(), batch = batch)
    }

    private val atlas = TextureAtlas(AMMO_ATLAS_FILE)

    private val tomb = Texture(Gdx.files.internal("img/fighter/tomb.png"))

    private fun renderDead(batch: SpriteBatch) {
        deadList.forEach {
            batch.draw(tomb, it.first - tomb.width.toFloat() / 2, it.second - tomb.height.toFloat() / 2)
        }
    }

    private fun getRegionNameBullet(bullet: Bullet): String = "bullet-" + when(bullet) {
        is BigBullet -> "big"
        is SmallBullet -> "small"
        else -> "medium"
    }

    private fun renderBullets(hq: Headquarter, team: Team, batch: SpriteBatch) {
        hq.ammunition.forEach { (_, v) ->
            val name = when (v) {
                is Bullet -> getRegionNameBullet(v)
                is Mine -> "mine-${team.alias.lowercase()}"
                else -> ""
            }
            val region = atlas.findRegion(name)
            val (width, height) = region.originalWidth.toFloat() to region.originalHeight.toFloat()
            val (x, y) = v.x - width / 2f to v.y - height / 2f
            batch.draw(region, x, y, 0f, 0f, width, height, 1f, 1f, v.angle * (180f / PI).toFloat())
        }
    }

    private fun renderFighters(map: FieldCommanderMap, isSelf: Boolean, delta: Float, batch: SpriteBatch) {
        val idsMap = IdsMap()
        val headquarter = getHeadquarter(isSelf = isSelf)
        val team = getTeam(isSelf = isSelf)

        headquarter.commander.fieldCommanders.forEach { (kFc, v) ->
            idsMap[kFc] = v.formation.fighters.keys.toList()
            if (kFc !in map) {
                map[kFc] = FighterSprite(fighter = v, team = team) to FightersMap()
            }
            map[kFc]?.first?.update(delta = delta, batch = batch)
            v.formation.fighters.forEach { (k, v) ->
                if (map[kFc]?.second?.contains(k) != true) {
                    map[kFc]?.second?.set(k, FighterSprite(fighter = v, team = team))
                }
                map[kFc]?.second?.get(k)?.update(delta = delta, batch = batch)
            }
        }

        map.keys.forEach { kFc ->
            if (kFc !in idsMap) {
                (map[kFc]?.first?.fighter as? FieldCommander)?.let { fc ->
                    deadList.add(fc.x to fc.y)
                    fc.formation.fighters.values.forEach { f -> deadList.add(f.x to f.y) }
                }
                map.remove(kFc)
            } else {
                map[kFc]?.second?.keys?.forEach { k ->
                    if (idsMap[kFc]?.contains(k) != true) {
                        map[kFc]?.second?.get(k)?.fighter?.let { f -> deadList.add(f.x to f.y) }
                        map[kFc]?.second?.remove(k)
                    }
                }
            }
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
        private const val AMMO_ATLAS_FILE = "img/ammo/ammo.atlas"
    }
}
