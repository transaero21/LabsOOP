package ru.transaero21.mt.ui.screens.game.helpers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import ru.transaero21.mt.utils.SCREEN_HEIGHT
import ru.transaero21.mt.utils.SCREEN_WIDTH

object InputHelper {
    private const val HORIZONTAL_MARGINS = 128f
    private const val VERTICAL_MARGINS = 64f

    private fun getWidth() = Gdx.graphics.width

    private fun getHeight() = Gdx.graphics.height

    private fun OrthographicCamera.getHorizontalBorders(mapWidth: Float): ClosedFloatingPointRange<Float> {
        return (-getWidth() / 2f + HORIZONTAL_MARGINS) * zoom..mapWidth + (getWidth() / 2f - HORIZONTAL_MARGINS) * zoom
    }

    private fun OrthographicCamera.getVerticalBorders(mapLength: Float): ClosedFloatingPointRange<Float> {
        return (-getHeight() / 2f + VERTICAL_MARGINS) * zoom..mapLength + (getHeight() / 2f - VERTICAL_MARGINS) * zoom
    }

    fun OrthographicCamera.handleDrag(mapWidth: Float, mapLength: Float): Boolean {
        val (x, y) = -Gdx.input.deltaX.toFloat() * zoom to Gdx.input.deltaY.toFloat() * zoom

        getHorizontalBorders(mapWidth = mapWidth).let { borders ->
            if (position.x + x < borders.start) {
                position.x = borders.start
            } else if (position.x + x > borders.endInclusive) {
                position.x = borders.endInclusive
            } else position.x += x
        }

        getVerticalBorders(mapLength = mapLength).let { borders ->
            if (position.y + y < borders.start) {
                position.y = borders.start
            } else if (position.y + y > borders.endInclusive) {
                position.y = borders.endInclusive
            } else position.y += y
        }

        return true
    }

    fun OrthographicCamera.handleZoom(amount: Float, mapWidth: Float, mapLength: Float): Boolean {
        val newZoom = zoom + amount * 0.3f
        zoom = when {
            newZoom < 0.2f -> 0.2f
            newZoom > 3f -> 3f
            else -> newZoom
        }

        this.handleResize(mapWidth = mapWidth, mapLength = mapLength)

        return true
    }

    fun OrthographicCamera.handleResize(mapWidth: Float, mapLength: Float) {
        getHorizontalBorders(mapWidth = mapWidth).let { borders ->
            if (position.x < borders.start) {
                position.x = borders.start
            } else if (position.x > borders.endInclusive) {
                position.x = borders.endInclusive
            }
        }

        getVerticalBorders(mapLength = mapLength).let { borders ->
            if (position.y < borders.start) {
                position.y = borders.start
            } else if (position.y > borders.endInclusive) {
                position.y = borders.endInclusive
            }
        }
    }
}
