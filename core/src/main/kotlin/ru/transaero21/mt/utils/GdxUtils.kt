package ru.transaero21.mt.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener

fun Actor.onClick(onClick: () -> Unit) {
    this.addListener(object : ClickListener() {
        override fun clicked(event: InputEvent?, x: Float, y: Float) {
            onClick()
        }
    })
}

fun Any.logError(msg: String) {
    Gdx.app.error(this::class.simpleName, msg)
}