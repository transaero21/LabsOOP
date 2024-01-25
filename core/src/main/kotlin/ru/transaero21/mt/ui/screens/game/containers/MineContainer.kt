package ru.transaero21.mt.ui.screens.game.containers

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import ru.transaero21.mt.models.ammo.Ammunition
import ru.transaero21.mt.models.ammo.bullet.BigBullet
import ru.transaero21.mt.models.ammo.bullet.SmallBullet
import ru.transaero21.mt.models.ammo.mine.Mine
import ru.transaero21.mt.models.ammo.mine.MineState
import ru.transaero21.mt.utils.toTexture
import java.util.*
import kotlin.math.min

class MineContainer(private val mine: Mine): Sprite() {
    private val atlas = TextureAtlas(MINE_ATLAS_FILE)

    fun update(batch: SpriteBatch) {
        setRegion(atlas.findRegion(getRegionName()).toTexture())
        setPosition(mine.x - texture.width / 2, mine.y - texture.height / 2)
        when (mine.state) {
            MineState.Transit -> rotate(mine.angle)
            else -> rotate(0f)
        }

        draw(batch)
    }

    private fun getRegionName(): String = when(mine.state) {
        MineState.Transit, MineState.Exploded -> "transit"
        MineState.Defused -> "defused"
        MineState.Ready -> "ready"
    }

    companion object {
        private const val MINE_ATLAS_FILE = "img/ammo/mine/mine.atlas"
    }
}