package ru.transaero21.mt.utils

import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener

fun Actor.onClick(onClick: () -> Unit) {
    this.addListener(clickListener(onClick = onClick))
}

fun clickListener(onClick: () -> Unit) = object : ClickListener() {
    override fun clicked(event: InputEvent?, x: Float, y: Float) {
        onClick()
    }
}

fun TextureRegion.toTexture(): Texture {
    val pixmap = Pixmap(regionWidth, regionHeight, Pixmap.Format.RGBA8888)

    if (texture.textureData.isPrepared.not()) texture.textureData.prepare()
    val texturePixmap = texture.textureData.consumePixmap()

    pixmap.drawPixmap(texturePixmap, 0, 0, regionX, regionY, regionWidth, regionHeight)

    val newTexture = Texture(pixmap)

    texturePixmap.dispose()
    pixmap.dispose()

    return newTexture
}
